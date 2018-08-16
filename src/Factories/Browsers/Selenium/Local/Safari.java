package Factories.Browsers.Selenium.Local;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

public class Safari extends LocalSeleniumBrowser {

    public static final String NAME = "safari";

    /**
     * Safari represents a Safari browser instance controlled through Selenium API.
     *
     * {@inheritDoc}
     */
    public Safari(String cookieUrl, String backupCookieUrl, BrowserSetting setting, BrowserVersion version) {
        super(cookieUrl, backupCookieUrl, setting, null, version);
    }

    @Override
    protected void init() {
        SafariOptions options = new SafariOptions();
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(this.proxy);

        seleniumProxy.setHttpProxy("localhost:"+ proxy.getPort());
        seleniumProxy.setFtpProxy("localhost:"+ proxy.getPort());
        seleniumProxy.setSslProxy("localhost:"+ proxy.getPort());

        // Safari does not respect the proxy settings passed by capabilities.
        // Solution: using a fixed port that has been set in the Safari proxy settings (see constructor of DriverFactory).
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        options.merge(capabilities);

        this.driver = new SafariDriver(options);
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
