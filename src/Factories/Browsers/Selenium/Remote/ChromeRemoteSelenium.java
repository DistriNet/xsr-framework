package Factories.Browsers.Selenium.Remote;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class ChromeRemoteSelenium extends RemoteSeleniumBrowser {

    private static final String NAME = "chrome";

    /**
     * SeleniumBrowser represents a browser instance controlled by Selenium API.
     *
     * @param cookieDomain the domain to which the cookies will belong
     * @param backupCookieUrl the backup url to be visited in order to set the cookies
     * @param setting the specific browser setting that is used
     * @param extension the specific browser extension that is used
     * @param version the specific browser version that is used
     * @param hubAddress the address of the hub controlling all remote drivers
     * @param platform the platform on which Docker is hosted
     */
    public ChromeRemoteSelenium(String cookieDomain, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version, URL hubAddress, String platform) {
        super(cookieDomain, backupCookieUrl, setting, extension, version, hubAddress, platform);
    }

    @Override
    protected void init() {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(this.proxy);
        setSeleniumProxy(seleniumProxy);

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        if (!version.isLatest())
            capabilities.setVersion(version.getVersion());

        ChromeOptions options = new ChromeOptions();
        options.setProxy(seleniumProxy);
        options.setAcceptInsecureCerts(true);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        if (extension != null)
            options.addExtensions(extension.getFile());
        options.merge(capabilities);

        this.driver = new RemoteWebDriver(hubAddress, options);
    }

    @Override
    protected void setCookies() {
        proxy.newHar();
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_COOKIES);

        driver.get("https://leak.test/set_cookies_chromium.html");
        // Wait for pdf to be loaded
        wait(2000);
        if (!cookiesAreSet())
            throw new IllegalStateException("No cookies have been set");

        proxy.newHar();
        this.proxy.enableHarCaptureTypes(CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_HEADERS);
    }

    @Override
    protected boolean supports(BrowserSetting setting) {
        switch (setting) {
            case DEFAULT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
