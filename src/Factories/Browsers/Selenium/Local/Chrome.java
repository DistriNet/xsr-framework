package Factories.Browsers.Selenium.Local;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.LinkedHashMap;
import java.util.Map;

public class Chrome extends LocalSeleniumBrowser {

    public static final String NAME = "chrome";
    // TODO: fill in path to ChromeDriver
    private final String PATH_TO_CHROMEDRIVER = "path/to/chromedriver";

    /**
     * Chrome represents a Chrome browser instance controlled through Selenium API.
     *
     * {@inheritDoc}
     */
    public Chrome(String cookieUrl, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version) {
        super(cookieUrl, backupCookieUrl, setting, extension, version);
    }

    @Override
    protected void init() {
        System.setProperty("webdriver.chrome.driver", PATH_TO_CHROMEDRIVER);

        ChromeOptions options = new ChromeOptions();
        // The browser is not made headless because behavior concerning pdf differs.

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(this.proxy);
        seleniumProxy.setHttpProxy("leaking.via:"+ proxy.getPort());
        seleniumProxy.setSslProxy("leaking.via:"+ proxy.getPort());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        if (setting == BrowserSetting.EXPERIMENTAL_MODE)
            options.addArguments("--enable-experimental-web-platform-features");
        if (setting == BrowserSetting.BLOCK_THIRD_PARTY_COOKIES) {
            Map<String, Object> prefs = new LinkedHashMap<>();
            prefs.put("profile.block_third_party_cookies", true);
            options.setExperimentalOption("prefs", prefs);
        }
        options.merge(capabilities);

        if (extension != null)
            options.addExtensions(extension.getFile());
        this.driver = new ChromeDriver(options);
    }

    @Override
    protected boolean supports(BrowserSetting setting) {
        switch (setting) {
            case DEFAULT:
            case EXPERIMENTAL_MODE:
            case BLOCK_THIRD_PARTY_COOKIES:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() { return this.NAME; }
}