package Factories.Browsers.Selenium.Local;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;


public class Edge extends LocalSeleniumBrowser {

    public static final String NAME = "edge";
    // TODO: fill in path to EdgeDriver
    private final String PATH_TO_EDGEDRIVER = "path/to/edgedriver";

    public Edge(String cookieUrl, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version) {
        super(cookieUrl, backupCookieUrl, setting, extension, version);
    }

    @Override
    protected void init() {
        System.setProperty("webdriver.edge.driver", PATH_TO_EDGEDRIVER);

        EdgeOptions options = new EdgeOptions();

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(this.proxy);
        seleniumProxy.setHttpProxy("leaking.via:"+ proxy.getPort());
        seleniumProxy.setSslProxy("leaking.via:"+ proxy.getPort());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

        options.merge(capabilities);

        this.driver = new EdgeDriver(options);
    }

    @Override
    protected boolean supports(BrowserSetting setting) {
        switch(setting) {
            case DEFAULT:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String getName() {
        return this.NAME;
    }
}
