package TestFunctionality.Tests;

import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class HeaderLinkTest extends LeakTest {

    private static final String PARAM = "type";
    private static HashMap<String, Pair<String, String>> VALUES = LeakTest.createHashMap("init/header-link/mapping.csv");

    // Singleton pattern
    private static HeaderLinkTest ourInstance = new HeaderLinkTest();
    public static HeaderLinkTest getInstance() {
        return ourInstance;
    }
    private HeaderLinkTest() {}

    @Override
    public boolean run(SeleniumBrowser browser, int scenarioId) {
        String url = getUrl(scenarioId, getName());
        for (String value : VALUES.keySet()) {
            try {
                String encodeBase64String = Base64.encodeBase64String(VALUES.get(value).right.getBytes());
                loadUrl(browser, url, PARAM, URLEncoder.encode(encodeBase64String, "UTF-8"), ExpectedConditions.visibilityOfElementLocated(By.id("message")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean run(TerminalBrowser browser, int scenarioId) {
        String url = getUrl(scenarioId, getName());
        for (String value : VALUES.keySet()) {
            try {
                String encodeBase64String = Base64.encodeBase64String(VALUES.get(value).right.getBytes());
                browser.get(url + '?' + PARAM + '=' + URLEncoder.encode(encodeBase64String, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private static TestName getName() {
        return TestName.HEADERLINK;
    }
}
