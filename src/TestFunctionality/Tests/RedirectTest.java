package TestFunctionality.Tests;


import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RedirectTest extends LeakTest {

    private static final String PARAM = "type";
    private static final String[] VALUES = {"meta-refresh", "header-300", "header-301", "header-302", "header-303",
            "header-304", "header-305", "header-306", "header-307", "header-308", "window-location", "form-GET", "form-POST"};

    // Singleton pattern
    private static RedirectTest ourInstance = new RedirectTest();
    public static RedirectTest getInstance() {
        return ourInstance;
    }
    private RedirectTest() {}

    @Override
    public boolean run(SeleniumBrowser browser, int scenarioId) {
        // TODO: geckodriver bug (reported)
        //if (browser.toString().contains("ghostery") || browser.toString().contains("ublock_origin") || browser.toString().contains("contentblockhelper"));

        String baseUrl = LeakTest.getUrl(scenarioId, getName());

        if (scenarioId == 5) {
            String url = baseUrl + "/file.html";
            LeakTest.loadUrl(browser, url, ExpectedConditions.not(ExpectedConditions.urlContains(url)));
            url = baseUrl + "/file2.html";
            LeakTest.loadUrl(browser, url, ExpectedConditions.not(ExpectedConditions.urlContains(url)));
        } else if (scenarioId == 7) {
            LeakTest.loadUrl(browser, baseUrl);
        } else {
            for (String value : VALUES) {
                // The header-304 test causes an infinite loop in the checkAndSwitchToFrame() method.
                // (it returns a blank page)
                // TODO: fix this
                if (value.equals("header-304")) continue;
                LeakTest.loadUrl(browser, baseUrl, PARAM, value, null);
            }
        }
        return true;
    }

    @Override
    public boolean run(TerminalBrowser browser, int scenarioId) {
        String baseUrl = LeakTest.getUrl(scenarioId, getName());
        for (String value : VALUES)
            browser.get(baseUrl + '?' + PARAM + '=' + value);
        return true;
    }

    private static TestName getName() {
        return TestName.REDIRECT;
    }
}
