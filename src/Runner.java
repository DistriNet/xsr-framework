import Factories.Browsers.Browser;
import Factories.Browsers.Selenium.Local.LocalSeleniumBrowser;
import Factories.Browsers.Selenium.Remote.RemoteSeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import Factories.ThreadManager;
import TestFunctionality.Registers.Register;
import TestFunctionality.RemoteBrowserThread;
import TestFunctionality.Tests.TestName;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Runner {

    private static final Logger LOGGER = Logger.getLogger(Runner.class.getName());

    public static void run(TerminalBrowser browser, int scenarioId, int N, Collection<TestName> tests) throws IOException {
        Register register = createRegister(browser, scenarioId, tests);
        BrowserMobProxyServer proxy = startProxy();
        // TODO set system proxy
        runLocalBrowser(browser, scenarioId, N, tests, proxy, register);
    }

    public static void run(LocalSeleniumBrowser browser, int scenarioId, int N, Collection<TestName> tests) throws IOException {
        Register register = createRegister(browser, scenarioId, tests);
        BrowserMobProxyServer proxy = startProxy();
        browser.setProxy(proxy);
        runLocalBrowser(browser, scenarioId, N, tests, proxy, register);
    }

    public static void run(Collection<RemoteSeleniumBrowser> browsers, int scenarioId, Collection<TestName> tests, int numberOfThreads) throws IOException, InterruptedException {
        Set<RemoteBrowserThread> threads = new HashSet<>();
        ThreadManager.init(numberOfThreads);
        for (RemoteSeleniumBrowser browser : browsers) {
            Register register = createRegister(browser, scenarioId, tests);
            RemoteBrowserThread browserThread = new RemoteBrowserThread(browser, scenarioId, tests, register);
            threads.add(browserThread);
        }
        ThreadManager.startTests(threads);
        ThreadManager.waitUntilDone();
        ThreadManager.close();
    }

    private static Register createRegister(Browser browser, int scenarioId, Collection<TestName> tests) throws IOException {
        return new Register(browser, scenarioId, tests, true);
    }

    private static BrowserMobProxyServer startProxy() {
        BrowserMobProxyServer proxy = new BrowserMobProxyServer();
        // Very important: the proxy doesn't just trust every upstream server by default:
        proxy.setTrustAllServers(true);
        // CHANGE SAFARI SETTINGS IF PORT CHANGES!!!
        proxy.start(61985);
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_HEADERS);
        return proxy;
    }

    private static void runLocalBrowser(Browser browser, int scenarioId, int N, Collection<TestName> tests, BrowserMobProxyServer proxy, Register register) {
        try {
            if (scenarioId > 0)
                throw new NotImplementedException("Scenarios above 0 still have to be modified to adition.com/report/?leak=...");

            String browserName = browser.toString();
            // TODO geckodriver bug (reported)
            if ((scenarioId == 3 || scenarioId == 8) && (browserName.contains("ublock_origin") || browserName.contains("ghostery")) && browserName.contains("firefox"))
                return;
            // TODO: operadriver bug
            //if ((this.scenarioId > 0) && browser.toString().contains("contentblockhelper"))
            //    return;

            //register.addBrowser(browserName); //STRING
            browser.start();
            for (TestName test : tests) {
                proxy.newHar();
                //register.addTest(test);
                long testTimeInMs = 0;
                for (int i = 0; i < N; i++) {
                    System.out.println("STARTING: " + test + " (" + (i + 1) + ")");
                    long startTime = System.currentTimeMillis();
                    browser.runTest(test, scenarioId);
                    testTimeInMs += System.currentTimeMillis() - startTime;
                }
                List<HarEntry> harEntries = proxy.getHar().getLog().getEntries();

                if (harEntries.isEmpty())
                    //throw new IllegalStateException("No HAR entries found: " + test.toString());
                    LOGGER.log(Level.SEVERE, "No HAR entries found: " + test.toString());
                for (HarEntry harEntry : harEntries) {
                    register.register(test, harEntry.getRequest().getUrl(), browserName, harEntry.getRequest().getCookies());
                }
                register.close();

                System.out.println(browserName + ": " + register.getNumberOfLeaksFoundFor(test, browserName) + " leaks found.");
                System.out.println("Elapsed time: " + testTimeInMs / 1000 + " s");
                //totalTestTimeInMs += testTimeInMs;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            browser.stop();
            proxy.stop();
        }
    }


}
