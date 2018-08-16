package Factories.Browsers.Selenium.Remote;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import Factories.Browsers.Selenium.SeleniumBrowser;
import org.openqa.selenium.Proxy;

import java.net.URL;

public abstract class RemoteSeleniumBrowser extends SeleniumBrowser {

    private static final String MACOS_DOCKER_PROXY = "host.docker.internal";
    private static final String UBUNTU_DOCKER_PROXY = "172.17.0.1";

    protected URL hubAddress;
    private String platform;

    /**
     * SeleniumBrowser represents a browser instance controlled by Selenium API.
     *
     * @param cookieDomain    the domain to which the cookies will belong
     * @param backupCookieUrl the backup url to be visited in order to set the cookies
     * @param setting         the specific browser setting that is used
     * @param extension       the specific browser extension that is used
     * @param version         the specific browser version that is used
     * @param hubAddress      the address of the hub controlling all remote drivers
     * @param platform        the platform on which Docker is hosted
     */
    public RemoteSeleniumBrowser(String cookieDomain, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version, URL hubAddress, String platform) {
        super(cookieDomain, backupCookieUrl, setting, extension, version);
        this.hubAddress = hubAddress;//"http://localhost:4444/wd/hub"
        this.platform = platform;
    }

    protected void setSeleniumProxy(Proxy seleniumProxy) {
        String address;
        if (platform.equals("mac"))
            address = MACOS_DOCKER_PROXY + ':' + this.proxy.getPort();
        else if (platform.equals("ubuntu"))
            address = UBUNTU_DOCKER_PROXY + ':' + this.proxy.getPort();
        else
            throw new IllegalArgumentException("The given platform '" + platform + "' is not supported");
        seleniumProxy.setHttpProxy(address);
        seleniumProxy.setSslProxy(address);
        seleniumProxy.setFtpProxy(address);
    }

    @Override
    abstract protected void init();

    @Override
    abstract protected boolean supports(BrowserSetting setting);

    @Override
    abstract public String getName();
}
