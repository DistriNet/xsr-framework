package TestFunctionality.Tests;

import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class StaticTest extends LeakTest {

    // Singleton pattern
    private static StaticTest ourInstance = new StaticTest();
    public static StaticTest getInstance() {
        return ourInstance;
    }
    private StaticTest() {}

    @Override
    public boolean run(SeleniumBrowser browser, int scenarioId) {
        LeakTest.loadUrl(browser, LeakTest.getUrl(scenarioId, getName()), ExpectedConditions.textToBePresentInElementLocated(By.id("check"), "Static"));
        LeakTest.waitFor(browser,500);
        return true;
    }

    @Override
    public boolean run(TerminalBrowser browser, int scenarioId) {
        browser.get(LeakTest.getUrl(scenarioId, getName()));
        return true;
    }

    private static TestName getName() {
        return TestName.STATIC;
    }
}