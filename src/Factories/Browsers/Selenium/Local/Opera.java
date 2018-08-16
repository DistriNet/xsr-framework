package Factories.Browsers.Selenium.Local;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Opera extends Chrome {

    public static final String NAME = "opera";
    // TODO: fill in path to OperaDriver
    private final String PATH_TO_OPERA_DRIVER = "path/to/opera/driver";

    /**
     * Opera represents an Opera browser instance controlled through Selenium API.
     *
     * {@inheritDoc}
     */
    public Opera(String cookieUrl, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version) {
        super(cookieUrl, backupCookieUrl, setting, extension, version);
    }

    @Override
    protected void init() {
        OperaOptions options = new OperaOptions();

        System.setProperty("webdriver.opera.driver", PATH_TO_OPERA_DRIVER);

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(this.proxy);
        seleniumProxy.setHttpProxy("leaking.via:"+ proxy.getPort());
        seleniumProxy.setSslProxy("leaking.via:"+ proxy.getPort());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        if (setting == BrowserSetting.EXPERIMENTAL_MODE)
            options.addArguments("--enable-experimental-web-platform-features");
        options.merge(capabilities);

        if (extension != null)
            options.addExtensions(extension.getFile());

        this.driver = new OperaDriver(options);
    }

    @Override
    protected boolean supports(BrowserSetting setting) {
        if (setting == BrowserSetting.ENABLE_AD_BLOCKING)
            return true;
        if (setting == BrowserSetting.BLOCK_THIRD_PARTY_COOKIES)
            return true;
        return super.supports(setting);
    }

    @Override
    public String getName() { return this.NAME; }
}
