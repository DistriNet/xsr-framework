package TestFunctionality.Tests;

import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.Pair;

import java.util.HashMap;

public class HeaderCSPTest extends LeakTest {

    private static final String PARAM = "type";
    private static HashMap<String, Pair<String, String>> VALUES = LeakTest.createHashMap("init/header-csp/mapping.csv");

    // Singleton pattern
    private static HeaderCSPTest ourInstance = new HeaderCSPTest();
    public static HeaderCSPTest getInstance() {
        return ourInstance;
    }
    private HeaderCSPTest() {}

    @Override
    public boolean run(SeleniumBrowser browser, int scenarioId) {
        String url = getUrl(scenarioId, getName());
        for (String value : VALUES.keySet())
            loadUrl(browser, url, PARAM, value, ExpectedConditions.visibilityOfElementLocated(By.id("message")));
        return true;
    }

    @Override
    public boolean run(TerminalBrowser browser, int scenarioId) {
        String url = getUrl(scenarioId, getName());
        for (String value : VALUES.keySet())
            browser.get(url + '?' + PARAM + '=' + value);
        return true;
    }

    private static TestName getName() {
        return TestName.HEADERCSP;
    }

}
