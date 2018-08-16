package TestFunctionality.Tests;

import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import com.opencsv.CSVReader;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import util.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public abstract class LeakTest {

    private static Logger LOGGER = Logger.getLogger(LeakTest.class.getName());

    private static String leakDomain = "https://adition.com";
    private static String leakingDomain = "https://leak.test";
    private static String attackingDomain = "https://attacker.test";

    // Scenario 0:
    private static String S0_INITIATOR = leakingDomain + "/s0";
    private static String S0_TARGET = leakDomain;
    // Scenario 1:
    private static String S1_INITIATOR = attackingDomain +"/s1";
    private static String S1_TARGET = leakDomain;
    // Scenario 2:
    private static String S2_INITIATOR = leakingDomain + "/s2";
    private static String S2_TARGET = leakDomain;
    // Scenario 3:
    private static String S3_INITIATOR = leakDomain + "/s3";
    private static String S3_TARGET = leakDomain;
    // Scenario 4:
    private static String S4_INITIATOR = leakingDomain + "/s4";
    private static String S4_TARGET = leakDomain;
    // Scenario 5:
    private static String S5_INITIATOR = "C:\\xampp\\htdocs\\framework\\leaktest\\s0";
    private static String S5_TARGET = leakDomain;
    // Scenario 7:
    private static String S7_INITIATOR = "https://attacker.test/s7";
    private static String S7_TARGET = leakDomain;
    // Scenario 8:
    private static String S8_INITIATOR = leakDomain + "/s8";
    private static String S8_TARGET = leakDomain;

    public abstract boolean run(SeleniumBrowser browser, int scenarioId);

    public abstract boolean run(TerminalBrowser browser, int scenarioId);

    protected static String getUrl(int scenarioId, TestName testName) {
        if (testName.equals(TestName.MALICIOUS_PDF_UPLOAD))
            return "https://leaking.via/ind/pdf/pdf-iframe.html";
        switch(scenarioId) {
            case 0:
                return S0_INITIATOR + '/' + testName.name;
            case 1:
                return S1_INITIATOR + '/' + testName.name;
            case 2:
                return S2_INITIATOR + '/' + testName.name;
            case 3:
                return S3_INITIATOR + '/' + testName.name;
            case 4:
                return S4_INITIATOR + '/' + testName.name;
            case 5:
                return S5_INITIATOR + '/' + testName.name;
            case 7:
                return S7_INITIATOR + '/' + testName.name;
            case 8:
                return S8_INITIATOR + '/' + testName.name;
            default:
                throw new IllegalArgumentException("The given scenario id does not exist");
        }
    }

    public static String getLeakDomain(int scenarioId) {
        switch(scenarioId) {
            case 0:
                return S0_TARGET;
            case 1:
                return S1_TARGET;
            case 2:
                return S2_TARGET;
            case 3:
                return S3_TARGET;
            case 4:
                return S4_TARGET;
            case 5:
                return S5_TARGET;
            case 7:
                return S7_TARGET;
            case 8:
                return S8_TARGET;
            default:
                throw new IllegalArgumentException("The given scenario id does not exist");
        }
    }

    protected static HashMap<String, Pair<String, String>> createHashMap(String filePath) {
        HashMap<String, Pair<String, String>> hash = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath), ',');
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                hash.put(record[0], new Pair(record[1], record[2]));
            }
            reader.close();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read initial HeaderLinkTest data", e);
        }
        return hash;
    }

    /*
     * HELPER METHODS FOR SELENIUM TESTS
     */

    protected static boolean loadUrl(SeleniumBrowser browser, String url) {
        return loadUrl(browser, url, null, null);
    }

    protected static boolean loadUrl(SeleniumBrowser browser, String url, ExpectedCondition condition) {
        return loadUrl(browser, url, condition, null);
    }

    // Returns true: page loaded correctly
    // Returns false: page loaded incorrectly, but due to active countermeasures against tracking, ads, ...
    protected static boolean loadUrl(SeleniumBrowser browser, String url, ExpectedCondition condition, ExpectedCondition backupCondition) {
        int i = 0;
        boolean ready = false;
        while (!ready && i < 5) {
            browser.get(url);

            if (!browser.checkAndSwitchToFrame())
                return false;
            // Some browser extensions will prevent access to blacklisted sites.
            // In this case, we don't have to wait or check the condition.
            if (browser.pageIsBlockedByExtension()) {
                return false;
            }
            if (condition != null)
                ready = browser.wait(condition, 3);
            else {
                browser.wait(500);
                ready = true;
            }
            i++;
        }
        if (!ready) {
            if (backupCondition != null && browser.wait(backupCondition, 3))
                return false;
            else
                throw new TimeoutException("Timeout after " + i + " tries");
        }
        // We always switch back to the default frame because Safari will wait indefinitely for some reason if this is not done.
        browser.switchToTopFrame();
        return true;
    }

    protected static boolean loadUrl(SeleniumBrowser browser, String url, String param, String value, ExpectedCondition condition) {
        String completeUrl = url + '?' + param + '=' + value;
        return loadUrl(browser, completeUrl, condition);
    }

    protected static void waitFor(SeleniumBrowser browser, int timeInMs) {
        browser.wait(timeInMs);
    }
}
