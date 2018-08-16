package TestFunctionality.Tests;

import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SWFFTest extends LeakTest {

    // Singleton pattern
    private static SWFFTest ourInstance = new SWFFTest();
    public static SWFFTest getInstance() {
        return ourInstance;
    }
    private SWFFTest() {}

    @Override
    public boolean run(SeleniumBrowser browser, int scenarioId) {
        String url = LeakTest.getUrl(scenarioId, getName());
        LeakTest.loadUrl(browser, url, ExpectedConditions.textToBePresentInElementLocated(By.id("check"), "ForeignFetch"));
        LeakTest.waitFor(browser,2000);
        LeakTest.loadUrl(browser, url, ExpectedConditions.textToBePresentInElementLocated(By.id("check"), "ForeignFetch"));
        LeakTest.waitFor(browser,1000);
        LeakTest.loadUrl(browser, url, ExpectedConditions.textToBePresentInElementLocated(By.id("check"), "ForeignFetch"));
        return true;
    }

    @Override
    public boolean run(TerminalBrowser browser, int scenarioId) {
        return false;
    }

    private static TestName getName() {
        return TestName.SWFF;
    }
}
