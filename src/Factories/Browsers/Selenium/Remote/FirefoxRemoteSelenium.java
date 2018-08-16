package Factories.Browsers.Selenium.Remote;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.logging.Level;

public class FirefoxRemoteSelenium extends RemoteSeleniumBrowser {

    private static final String NAME = "firefox";

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
    public FirefoxRemoteSelenium(String cookieDomain, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version, URL hubAddress, String platform) {
        super(cookieDomain, backupCookieUrl, setting, extension, version, hubAddress, platform);
    }

    @Override
    protected void init() {
        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.TRACE);

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        if (version.isLatest())
            capabilities.setVersion("58.0.1");
        else
            capabilities.setVersion(version.getVersion());

        FirefoxProfile profile = new FirefoxProfile();

        // Configuring proxy settings through Selenium is only supported for Firefox 57 and higher
        if (version.getShortVersion() < 57) {
            LOGGER.log(Level.SEVERE, "Proxy settings have not been set for " + this);
        } else {
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(this.proxy);
            setSeleniumProxy(seleniumProxy);
            options.setProxy(seleniumProxy);

        }

        if (extension != null)
            profile.addExtension(extension.getFile());

        options.setProfile(profile);
        options.merge(capabilities);

        this.driver = new RemoteWebDriver(hubAddress, options);
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
