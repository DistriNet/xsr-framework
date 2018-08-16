package TestFunctionality.Tests;

import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ScriptTest extends LeakTest {

    // Singleton pattern
    private static ScriptTest ourInstance = new ScriptTest();
    public static ScriptTest getInstance() {
        return ourInstance;
    }
    private ScriptTest() { }

    public boolean run(SeleniumBrowser browser, int scenarioId) {
        LeakTest.loadUrl(browser, LeakTest.getUrl(scenarioId, getName()), ExpectedConditions.textToBePresentInElementLocated(By.id("check"), "Done"),
                ExpectedConditions.textToBePresentInElementLocated(By.id("check"), "ScriptTest"));
        return true;
    }

    @Override
    public boolean run(TerminalBrowser browser, int scenarioId) {
        browser.get(LeakTest.getUrl(scenarioId, getName()));
        return true;
    }

    private static TestName getName() {
        return TestName.SCRIPT;
    }
}
