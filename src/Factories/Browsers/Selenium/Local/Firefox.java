package Factories.Browsers.Selenium.Local;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class Firefox extends LocalSeleniumBrowser {

    public static final String NAME = "firefox";
    // TODO: fill in path to Firefox profile to which the LittleProxy certificate is added to the certificate store.
    private final String PATH_TO_FIREFOX_PROFILE = "path/to/firefox/profile";

    /**
     * Firefox represents a Firefox browser instance controlled through Selenium API.
     *
     * {@inheritDoc}
     */
    public Firefox(String cookieUrl, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version) {
        super(cookieUrl, backupCookieUrl, setting, extension, version);
    }

    @Override
    protected void init() {
        FirefoxBinary binary = new FirefoxBinary(new File("/Applications/Firefox.app/Contents/MacOS/firefox"));
        FirefoxOptions options = new FirefoxOptions();

        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        // This profile makes sure the LittleProxy certificate is present in Firefox' certificate store.
        // Otherwise, this certificate is marked as insecure, which could manifest unintended side-effects.
        FirefoxProfile profile = new FirefoxProfile(new File(PATH_TO_FIREFOX_PROFILE));
        if (extension != null)
            profile.addExtension(extension.getFile());

        profile.setPreference("network.proxy.http", "localhost");
        profile.setPreference("network.proxy.http_port", proxy.getPort());
        profile.setPreference("network.proxy.ssl", "localhost");
        profile.setPreference("network.proxy.ssl_port", proxy.getPort());
        profile.setPreference("network.proxy.type", 1);
        profile.setPreference("security.csp.enable", true);

        if (setting == BrowserSetting.BLOCK_THIRD_PARTY_COOKIES)
            profile.setPreference("network.cookie.cookieBehavior", 1);

        options.setProfile(profile);
        options.setBinary(binary);
        options.merge(capabilities);

        this.driver = new FirefoxDriver(options);
    }


    @Override
    protected boolean supports(BrowserSetting setting) {
        switch (setting) {
            case DEFAULT:
            case BLOCK_THIRD_PARTY_COOKIES:
            case ENABLE_TRACKING_PROTECTION:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() { return this.NAME; }
}
