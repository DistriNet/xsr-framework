package TestFunctionality.Tests;

import Factories.Browsers.Browser;
import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;

public class PdfTest extends LeakTest {

    // Singleton pattern
    private static PdfTest ourInstance = new PdfTest();
    public static PdfTest getInstance() {
        return ourInstance;
    }
    private PdfTest() {}

    public boolean run(SeleniumBrowser browser, int scenarioId) {
        return runInner(browser, scenarioId);
    }

    public boolean run(TerminalBrowser browser, int scenarioId) {
        return runInner(browser, scenarioId);
    }

    private boolean runInner(Browser browser, int scenarioId) {
        String url = getUrl(scenarioId, getName());
        browser.get(url + "/pdf-redirect.html");
        browser.wait(1000);
        browser.get(url + "/pdf-iframe.html");
        browser.wait(1000);
        return true;
    }

    private static TestName getName() {
        return TestName.PDF;
    }
}
