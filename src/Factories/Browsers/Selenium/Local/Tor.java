package Factories.Browsers.Selenium.Local;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;

import java.io.File;

public class Tor extends LocalSeleniumBrowser {

    public static String NAME = "tor";
    // TODO: fill in path to GeckoDriver
    private final String geckoDriverPath = "PATH_TO_GECKODRIVER";
    // TODO: fill in path to TorBrowser profile
    private final String profilePath = "PATH_TO_BROWSER_PROFILE";

    public Tor(String cookieUrl, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version) {
        super(cookieUrl, backupCookieUrl, setting, extension, version);
    }

    @Override
    protected void init() {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(this.proxy);

        System.setProperty("webdriver.gecko.driver", geckoDriverPath);

        FirefoxProfile profile = new FirefoxProfile(new File(profilePath));


        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("network.proxy.socks_remote_dns", false);
        options.addPreference("network.proxy.http", "localhost");
        options.addPreference("network.proxy.http_port", proxy.getPort());
        options.addPreference("network.proxy.ssl", "localhost");
        options.addPreference("network.proxy.ssl_port", proxy.getPort());
        options.addPreference("network.proxy.type", 1);
        options.addPreference("security.csp.enable", true);

        FirefoxBinary binary = new FirefoxBinary(new File("/Applications/TorBrowser.app/Contents/MacOS/firefox"));
        options.setBinary(binary);

        options.setCapability(FirefoxDriver.PROFILE, profile);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");

        FirefoxDriver driver = new FirefoxDriver(options);

        this.driver = driver;
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
    public String getName() { return this.NAME; }

}
