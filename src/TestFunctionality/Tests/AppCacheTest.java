package TestFunctionality.Tests;

import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;

public class AppCacheTest extends LeakTest {

    // Singleton pattern
    private static AppCacheTest ourInstance = new AppCacheTest();
    public static AppCacheTest getInstance() {
        return ourInstance;
    }
    private AppCacheTest() {}

    @Override
    public boolean run(SeleniumBrowser browser, int scenarioId) {
        loadUrl(browser, getUrl(scenarioId, getName()));
        waitFor(browser,500);
        return true;
    }

    @Override
    public boolean run(TerminalBrowser browser, int scenarioId) {
        browser.get(getUrl(scenarioId, getName()));
        return true;
    }

    private static TestName getName() {
        return TestName.APPCACHE;
    }
}
