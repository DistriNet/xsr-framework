package Factories.Browsers.Selenium.Remote;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import Factories.Browsers.Selenium.SeleniumBrowser;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class OperaRemote extends SeleniumBrowser {

    private static final String NAME = "opera";

    /**
     * SeleniumBrowser represents a browser instance controlled by Selenium API.
     *
     * @param cookieDomain the domain to which the cookies will belong
     * @param backupCookieUrl the backup url to be visited in order to set the cookies
     * @param setting the specific browser setting that is used
     * @param extension the specific browser extension that is used
     * @param version the specific browser version that is used
     */
    public OperaRemote(String cookieDomain, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version) {
        super(cookieDomain, backupCookieUrl, setting, extension, version);
    }

    @Override
    protected void init() {
        OperaOptions options = new OperaOptions();

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(this.proxy);
        seleniumProxy.setHttpProxy("leaking.via:"+ proxy.getPort());
        seleniumProxy.setSslProxy("leaking.via:"+ proxy.getPort());

        options.setCapability(CapabilityType.PROXY, seleniumProxy);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        if (!version.isLatest())
            options.setCapability(CapabilityType.BROWSER_VERSION, version.getVersion());


        try {
            this.driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("URL is malformed");
        }
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
