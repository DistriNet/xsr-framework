package Factories.Browsers.Selenium;

import Factories.Browsers.Browser;
import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import TestFunctionality.Tests.TestName;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.HarCookie;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

public abstract class SeleniumBrowser extends Browser {

    protected BrowserMobProxyServer proxy;
    private final String cookieDomain;
    private final String backupCookieUrl;
    protected WebDriver driver;
    protected final Extension extension;
    protected final BrowserVersion version;

    /**
     * SeleniumBrowser represents a browser instance controlled by Selenium API.
     *
     * @param cookieDomain the domain to which the cookies will belong
     * @param backupCookieUrl the backup url to be visited in order to set the cookies
     * @param setting the specific browser setting that is used
     * @param extension the specific browser extension that is used
     * @param version the specific browser version that is used
     */
    public SeleniumBrowser(String cookieDomain, String backupCookieUrl, BrowserSetting setting, Extension extension, BrowserVersion version) {
        super(setting);
        this.cookieDomain = cookieDomain;
        this.backupCookieUrl = backupCookieUrl;
        this.driver = null;
        this.extension = extension;
        this.version = version;
    }

    /**
     * Initializes the SeleniumBrowser.
     */
    protected abstract void init();

    /**
     * Checks if this SeleniumBrowser's browser instance supports Service Workers.
     *
     * @return true if this SeleniumBrowser's browser instance supports Service Workers, otherwise false
     */
    public boolean supportsSW() {
        return true;
    }

    /**
     * Sets the proxy for this SeleniumBrowser.
     *
     * @param proxy the proxy for this SeleniumBrowser.
     * @return true if the proxy has been set, otherwise false
     */
    @Override
    public boolean setProxy(BrowserMobProxyServer proxy) {
        this.proxy = proxy;
        return true;
    }

    /**
     * Starts this SeleniumBrowser's browser instance.
     */
    @Override
    public void start() {
        if (isActive())
            throw new IllegalStateException("This browser is already active");
        if (!proxyIsSet())
            throw new IllegalStateException("There is no proxy set");
        this.init();
        String actualVersion = ((RemoteWebDriver) driver).getCapabilities().getVersion();
        if (!this.version.getVersion().contains(actualVersion) && !this.version.isLatest())
            throw new IllegalStateException("The requested browser version does not match the actual browser version");
        // Set all cookies
        this.setCookies();
        // Waiting for extension to be ready and active.
        if (hasExtension())
            wait(5000);
    }

    /**
     * Stops this SeleniumBrowser's browser instance.
     */
    @Override
    public void stop() {
        if (!isActive())
            throw new IllegalStateException("This browser is not active");
        driver.quit();
        this.driver = null;
    }

    /**
     * Sets all cookies, except same-site cookies, with the Cookie API.
     *
     * @param domain the domain to which the cookies belong@
     */
    private void setCookiesWithAPI(String domain) {
        driver.get("https://" + domain + "/init.php");
        Calendar expiry = new GregorianCalendar();
        expiry.set(2025, Calendar.MAY, 30, 7, 28);
        driver.manage().addCookie(new Cookie("generic", "1", domain, "/", expiry.getTime(), false, false));
        driver.manage().addCookie(new Cookie("secure", "1", domain, "/", expiry.getTime(), true, false));
        driver.manage().addCookie(new Cookie("httpOnly", "1", domain, "/", expiry.getTime(), false, true));
    }

    /**
     * Sets all cookies for the SeleniumBrowser's browser instance.
     * As for now, cookies are set by visiting a certain web page (or the backup web page).
     * Selenium's Cookie API cannot be used since it does not support same-site cookies (yet) and a web page still has to be visited when using this API.
     * See https://github.com/w3c/webdriver/issues/1134
     */
    // TODO geckodriver bug: webdriver hangs indefinitely when visiting a website blocked by certain extensions (e.g. uBlock)
    protected void setCookies() {
        proxy.newHar();
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_COOKIES);

        driver.get("https://" + cookieDomain + "/init.php");
        if (!cookiesAreSet()) {
            proxy.newHar();
            driver.get(backupCookieUrl);
            if (!cookiesAreSet())
                throw new IllegalStateException("No cookies have been set");
        }

        proxy.newHar();
        this.proxy.enableHarCaptureTypes(CaptureType.REQUEST_COOKIES, CaptureType.RESPONSE_HEADERS);
    }

    /**
     * Checks whether the cookies have been set.
     *
     * @return True if the cookies have been set, otherwise false
     */
    protected boolean cookiesAreSet() {
        List<HarEntry> entries = proxy.getHar().getLog().getEntries();
        if (entries.isEmpty())
            return false;
        for (HarEntry entry : entries) {
            if (!entry.getRequest().getUrl().contains("https://" + cookieDomain))
                continue;
            List<HarCookie> cookies = entry.getResponse().getCookies();
            if (cookies.isEmpty())
                return false;
            String cookieList = "";
            for (HarCookie cookie : cookies) {
                cookieList += cookie.getName() + ", ";
            }
            cookieList = cookieList.substring(0, cookieList.length() - 2);
            LOGGER.log(Level.INFO, cookieList + " have been set for " + this);
            return true;
        }
        return false;
    }

    /**
     * Orders this SeleniumBrowser's browser instance to visit the given url.
     *
     * @param url the url to be visited by this SeleniumBrowser's instance
     */
    public void get(String url) {
        if (!isActive())
            throw new IllegalStateException("The browser is not active yet");
        // At random moments, the time limit will be exceeded by a browser.
        // By retrying a multiple times, interruptions are reduced.
        int count = 0;
        int maxTries = 3;
        boolean success = false;
        while (!success) {
            try {
                this.driver.get(url);
                success = true;
            } catch (TimeoutException e) {
                if (++count >= maxTries) throw e;
                LOGGER.log(Level.INFO, "Try " + count + " for loading '" + url + "' failed");
            }
        }
    }

    /**
     * Checks whether a page is being blocked by an extension.
     *
     * @return true if a page is being blocked by an extension, otherwise false
     */
    public boolean pageIsBlockedByExtension() {
        return (findElements(By.id("warningSign")).size() > 0
                && checkElementText("proceedPermanent", "Permanently")
                && checkElementText("proceedTemporary", "Temporarily"));
    }

    /**
     * Lets this SeleniumBrowser wait until a given condition is met or until the given time limit has run out.
     *
     * @param condition the condition that has to be met
     * @param timeInSec the maximum amount of time that can be waited
     *
     * @return true if the given condition has been met within the given time limit, otherwise false
     */
    public boolean wait(ExpectedCondition condition, int timeInSec) {
        try {
            (new WebDriverWait(driver, timeInSec)).until(condition);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Find an element on the active web page of this SeleniumBrowser, conform with the given criteria.
     *
     * @param by the element criteria
     * @return the web element that satisfies the given criteria
     */
    public WebElement findElement(By by) {
        return driver.findElement(by);
    }

    /**
     * Find all elements on the active web page of this SeleniumBrowser, conform with the given criteria.
     * @param by the element criteria
     * @return the web elements that satisfy the given criteria
     */
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    /**
     * Switches the context of the SeleniumBrowser to the iframe defined by the given WebElement.
     *
     * @param element the WebElement to which the context has to be switched
     */
    public void switchToFrame(WebElement element) {
        driver.switchTo().frame(element);
    }

    /**
     * Switches the context of the SeleniumBrowser to the topmost frame.
     */
    public void switchToTopFrame() {
        driver.switchTo().defaultContent();
    }

    /**
     * Maximizes the SeleniumBrowser's browser instance window.
     */
    public void maximize() {
        driver.manage().window().maximize();
    }

    /**
     * Checks whether the currently active web page contains an iframe (with id='iframe1') and, if so, switches the context to that iframe until no such iframes are found.
     * This is used for nested tests.
     *
     * @return false if contents of the iframe are blocked or made invisible by an extension
     */
    public boolean checkAndSwitchToFrame() {
        // Because of nested tests of varying depths, the deepest iframe is used.
        int i = 0;
        while (findElements(By.id("iframe1")).size() > 0) {
            if (checkTagAttributeContains("iframe1", "style", "visibility: hidden !important"))
                return false;
            if (checkTagAttributeContains("iframe1", "style", "display: none !important"))
                return false;
            switchToFrame(findElement(By.id("iframe1")));
            if (checkElementText("sub-frame-error-details", ""))
                return false;
            if (noBody() || bodyContentIsEmpty())
                return false;
            if (i > 100)
                throw new IllegalStateException("Possible infinite loop");
            i++;
        }
        return true;
    }

    /**
     * Checks whether a WebElement with the given id is present on the currently active web page of this SeleniumBrowser.
     *
     * @param id the id associated with the WebElement
     * @return true if a WebElement associated with the given id is found, otherwise false
     */
    public boolean elementIsPresent(String id) {
        try {
            driver.findElement(By.id(id));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks whether a WebElement with given id and content is present on the currently active web page of this SeleniumBrowser.
     * @param id the id associated with the WebElement
     * @param content the content associated with the WebElement
     * @return true if a WebElement associated with the given id and content is found, otherwise false
     */
    public boolean checkElementText(String id, String content) {
        try {
            WebElement element = findElement(By.id(id));
            return element.getText().equals(content);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks whether the content of the HTML body currently active web page is empty.
     * This is used to detect page blockage by an extension.
     *
     * @return true if the HTML body is empty, otherwise false
     */
    private boolean bodyContentIsEmpty() {
        try {
            WebElement element = findElement(By.tagName("body"));
            return element.getText().isEmpty();
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            wait(1000);
            WebElement element = findElement(By.tagName("body"));
            return element.getText().isEmpty();
        }
    }

    /**
     * Checks whether the currently active web page has no HTML body.
     *
     * @return true if no HTML body is present, otherwise false
     */
    private boolean noBody() {
        return findElements(By.tagName("body")).isEmpty();
    }

    /**
     * Checks whether the given attribute with given value is present in an HTML tag associated with the given id in the currently active web page.
     *
     * @param id the id associated with the HTML tag
     * @param attribute the attribute of which the value has to be checked
     * @param value the value of the associated attribute
     * @return true if this tag specified by the given id, attribute and value is present in the currently active web page, otherwise false
     */
    private boolean checkTagAttributeContains(String id, String attribute, String value) {
        return findElement(By.id(id)).getAttribute(attribute).contains(value);
    }

    /**
     * Checks whether this SeleniumBrowser's browser instance is active.
     *
     * @return true if this SeleniumBrowser's browser instance is active, otherwise false
     */
    public boolean isActive() {
        return this.driver != null;
    }

    /**
     * Checks whether a proxy has been set for this SeleniumBrowser.
     *
     * @return true if a proxy has been set, otherwise false
     */

    private boolean proxyIsSet() {
        return this.proxy != null;
    }

    /**
     * Checks whether this SeleniumBrowser has an extension.
     *
     * @return true if this SeleniumBrowser has an extension, otherwise false
     */
    private boolean hasExtension() {
        return this.extension != null;
    }

    /**
     * Creates an Actions object for the SeleniumBrowser's web driver.
     *
     * @return an Actions object for the SeleniumBrowser's web driver
     */
    public Actions getActions() {
        return new Actions(driver);
    }

    /**
     * Returns the url of the currently active web page of the SeleniumBrowser.
     *
     * @return the url of the currently active web page of the SeleniumBrowser
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Checks whether this SeleniumBrowser supports the given BrowserSetting.
     *
     * @param setting the specific browser setting to be checked
     * @return true if this SeleniumBrowser supports the given BrowserSetting, otherwise false
     */
    abstract protected boolean supports(BrowserSetting setting);

    /**
     * Returns the name of the browser used by this SeleniumBrowser.
     *
     * @return the name of the browser instance used by this SeleniumBrowser
     */
    public abstract String getName();

    /**
     * Returns the version of the browser used by this SeleniumBrowser.
     *
     * @return the version of the browser used by this SeleniumBrowser
     */
    public String getVersion() {
        if (this.version == null)
            throw new IllegalArgumentException("The version is not available because the driver hasn't been initiated yet");
        return this.version.toString();
    }

    @Override
    public String getExtensionName() {
        if (extension == null)
            return "_base";
        else
            return extension.getName();
    }

    @Override
    public String getExtensionVersion() {
        if (extension == null)
            return "-1.-1";
        else
            return extension.getVersion();
    }

    /**
     * Returns a String that represents this SeleniumBrowser.
     *
     * @return a String that represents this SeleniumBrowser
     */
    @Override
    public String toString() {
        if (extension == null && version == null)
            return this.getName() + setting.getTestSuffix();
        else if (extension != null && version == null)
            return this.getName() + "_" + extension + setting.getTestSuffix();
        else if (extension == null && version != null)
            return this.getName() + "-" + version + setting.getTestSuffix();
        else
            return this.getName() + "-" + version + "_" + extension + setting.getTestSuffix();
    }

    // Visitor pattern
    @Override
    public void runTest(TestName test, int scenarioId) {
        if (!isActive())
            throw new IllegalArgumentException("The given browser '" + this + "' is not active");
        //String url = (scenarioId != 5) ? test.initiatingUrl(LeakTest.getUrl(scenarioId, test)) : test.initiatingFileUrl(LeakTest.getUrl(scenarioId, test)).get(0);
        wait(1000);
        test.run(this, scenarioId);
    }
}
