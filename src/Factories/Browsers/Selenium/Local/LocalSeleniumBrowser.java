package Factories.Browsers.Selenium.Local;

import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import Factories.Browsers.Selenium.SeleniumBrowser;

public abstract class LocalSeleniumBrowser extends SeleniumBrowser {

    /**
     * SeleniumBrowser represents a browser instance controlled by Selenium API.
     *
     * @param cookieDomain    the domain to which the cookies will belong
     * @param backupCookieUrl the backup url to be visited in order to set the cookies
     * @param setting         the specific browser setting that is used
     * @param extension       the specific browser extension that is used
     * @param version         the specific browser version that is used
     */
    public LocalSeleniumBrowser(String cookieDomain, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version) {
        super(cookieDomain, backupCookieUrl, setting, extension, version);
    }

    @Override
    abstract protected void init();

    @Override
    abstract protected boolean supports(BrowserSetting setting);

    @Override
    abstract public String getName();
}
