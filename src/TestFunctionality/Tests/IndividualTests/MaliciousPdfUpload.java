package TestFunctionality.Tests.IndividualTests;

import Factories.Browsers.Selenium.SeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import TestFunctionality.Tests.LeakTest;
import TestFunctionality.Tests.TestName;

public class MaliciousPdfUpload extends LeakTest {

    private static final TestName testName = TestName.MALICIOUS_PDF_UPLOAD;

    private static MaliciousPdfUpload ourInstance = new MaliciousPdfUpload();
    public static MaliciousPdfUpload getInstance() {
        return ourInstance;
    }
    private MaliciousPdfUpload() {}

    @Override
    public boolean run(SeleniumBrowser browser, int scenarioId) {
        String url = getUrl(scenarioId, testName);
        browser.get(url);
        browser.wait(1000);
        return false;
    }

    @Override
    public boolean run(TerminalBrowser browser, int scenarioId) {
        return false;
    }
}
