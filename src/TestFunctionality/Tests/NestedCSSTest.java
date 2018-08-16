package TestFunctionality.Tests;

import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class NestedCSSTest extends LeakTest {

    private static NestedCSSTest ourInstance = new NestedCSSTest();
    public static NestedCSSTest getInstance() {
        return ourInstance;
    }
    private NestedCSSTest() {}

    @Override
    public boolean run(SeleniumBrowser browser, int scenarioId) {
        LeakTest.loadUrl(browser, LeakTest.getUrl(scenarioId, getName()), ExpectedConditions.textToBePresentInElementLocated(By.id("check"), "Static"));
        LeakTest.waitFor(browser,500);
        return true;
    }

    @Override
    public boolean run(TerminalBrowser browser, int scenarioId) {
        return false;
    }

    private static TestName getName() {
        return TestName.NESTED_CSS;
    }
}
