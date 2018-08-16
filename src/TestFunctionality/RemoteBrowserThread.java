package TestFunctionality;

import Factories.Browsers.Selenium.Remote.RemoteSeleniumBrowser;
import Factories.ThreadManager;
import TestFunctionality.Registers.Register;
import TestFunctionality.Tests.TestName;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.HarEntry;
import util.ProgressReporter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteBrowserThread implements Callable<Void> {

    private static final Logger LOGGER = Logger.getLogger(RemoteBrowserThread.class.getName());
    private static final int N = 3;

    private RemoteSeleniumBrowser browser;
    private int scenarioId;
    private Collection<TestName> tests;
    private Register register;
    private ProgressReporter reporter;

    public RemoteBrowserThread(RemoteSeleniumBrowser browser, int scenarioId, Collection<TestName> tests, Register register) {
        this.browser = browser;
        this.scenarioId = scenarioId;
        this.register = register;
        this.tests = tests;
    }

    public Void call() throws IOException {
        reportStarted();
        BrowserMobProxyServer proxy = null;
        try {
            String browserName = browser.toString();
            // TODO geckodriver bug (reported)
            if ((this.scenarioId == 3 || this.scenarioId == 8) && (browserName.contains("ublock_origin") || browserName.contains("ghostery")) && browserName.contains("firefox"))
                return null;
            // TODO: operadriver bug
            //if ((this.scenarioId > 0) && browser.toString().contains("contentblockhelper"))
            //    return;

            proxy = ThreadManager.claimProxy();
            browser.setProxy(proxy);

            //this.register.addBrowser(browserName); //STRING
            browser.start();
            for (TestName test : this.tests) {
                proxy.newHar();
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
                    this.register.register(test, harEntry.getRequest().getUrl(), browserName, harEntry.getRequest().getCookies());
                }
                this.register.close();

                System.out.println(browserName + ": " + register.getNumberOfLeaksFoundFor(test, browserName) + " leaks found.");
                System.out.println("Elapsed time: " + testTimeInMs / 1000 + " s");
                //totalTestTimeInMs += testTimeInMs;
                reportTestDone(test);
            }
            this.reporter.finished(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ThreadManager.freeProxy(proxy);
            browser.stop();
        }
        return null;
    }

    public void setProgressReporter(ProgressReporter reporter) {
        this.reporter = reporter;
    }

    private void reportStarted() {
        if (reporter != null)
            this.reporter.started(this);
    }

    private void reportTestDone(TestName test) {
        if (reporter != null) {
            int numberOfLeaks = register.getNumberOfLeaksFoundFor(test, browser.toString());
            reporter.testDone(this, test, numberOfLeaks);
        }
    }

    public int getNumberOfTests() {
        return this.tests.size();
    }

    public Register getRegister() {
        return register;
    }

    public String toString() {
        return browser.toString();
    }
}
