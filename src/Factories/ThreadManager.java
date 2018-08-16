package Factories;

import TestFunctionality.RemoteBrowserThread;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.CaptureType;
import util.ProgressReporter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class ThreadManager {

    private static int nextPort = 61985;
    private static ExecutorService pool;
    private static List<Future<Void>> futures = new ArrayList<>();
    private static Set<BrowserMobProxyServer> proxies = new HashSet<>();
    private static ProgressReporter progressReporter;

    public static void init(int numberOfThreads) {
        pool = Executors.newFixedThreadPool(numberOfThreads);
        startProxies(numberOfThreads);
    }

    public static void startTests(Collection<RemoteBrowserThread> threads) throws IOException, InterruptedException {
        progressReporter = new ProgressReporter(threads);
        for (RemoteBrowserThread testGroup : threads) {
            testGroup.setProgressReporter(progressReporter);
            Future<Void> future = pool.submit(testGroup);
            futures.add(future);
            Thread.sleep(20000);
        }
    }

    private static void startProxies(int numberOfThreads) {
        for (int i = 0; i < numberOfThreads; i++) {
            BrowserMobProxyServer proxy = new BrowserMobProxyServer();
            proxy.newHar();
            // Very important: the proxy doesn't just trust every upstream server by default:
            proxy.setTrustAllServers(true);
            for (int j = 0; j < 100; j++) {
                try {
                    proxy.start(nextPort + j);
                    break;
                } catch (RuntimeException e) {
                    if (e.getCause().getMessage().contains("Address already in use")) {
                        proxy.stop();
                        // The proxy waits 5 seconds before shutting down completely, so to shorten execution time we make a new one.
                        proxy = new BrowserMobProxyServer();
                        continue;
                    } else
                        throw e;
                }
            }
            proxy.enableHarCaptureTypes(CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_HEADERS);
            proxies.add(proxy);
            nextPort++;
        }
    }

    public static void close() throws InterruptedException {
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        if (!pool.isTerminated())
            throw new IllegalStateException("Not all threads could be terminated");
        for (BrowserMobProxyServer proxy : proxies)
            proxy.stop();
    }

    public static synchronized BrowserMobProxyServer claimProxy() {
        if (proxies.isEmpty())
            throw new IllegalStateException("There is no available proxy");
        BrowserMobProxyServer proxy = proxies.iterator().next();
        proxies.remove(proxy);
        proxy.newHar();
        return proxy;
    }

    public static synchronized void freeProxy(BrowserMobProxyServer proxy) {
        proxy.newHar();
        proxies.add(proxy);
    }

    public static void waitUntilDone() {
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (ExecutionException | InterruptedException e) {
                e.getCause().printStackTrace();
            }
        }
    }

    public static ProgressReporter getProgressReporter() {
        return progressReporter;
    }
}
