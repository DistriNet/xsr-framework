/*package TestFunctionality.Tests.Selenium;

import Interactions.*;
import TestFunctionality.Tests.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;

public class InteractiveLeakTest extends SeleniumTest {

    private ArrayList<String> elementIds;

    public InteractiveLeakTest(String url) {
        super(url);
        this.createElementIds();
    }

    @Override
    public void runInner()  {
        browser.maximize();
        loadUrl(ExpectedConditions.textToBePresentInElementLocated(By.id("check"), "interactive"));
        browser.checkAndSwitchToFrame();
        for (String id : this.elementIds) {
            //browser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            WebElement e = browser.findElement(By.id(id));
            for (Interaction interaction : this.getAllInteractions()) {
                interaction.perform(e);
                //browser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                e = checkAndUpdateWebElement(e, id);
            }
        }
    }

    private WebElement checkAndUpdateWebElement(WebElement e, String id) {
        // FirefoxTerminal specific workaround:
        // getCurrentUrl gets called twice because when it is called during the time in which FirefoxTerminal is loading a new page,
        // it will return the original page, while prompting FirefoxTerminal to stop loading the new page but leaving the url
        // of the new page in the address bar.
        // So in this scenario driverUrl and driverUrl2 would differ when called while FirefoxTerminal is trying to load a page.
        // driverUrl would the url of the new page
        // driverUrl2 would be the url of the original page
        //Wait wait = new WebDriverWait(browser, 3);
        //wait.until(ExpectedConditions.);
        String driverUrl = browser.getCurrentUrl();
        // Sleep to let the browser catch up
        waitFor(1000);
        String driverUrl2 = browser.getCurrentUrl();
        if (!driverUrl2.equals(url)) {
            loadUrl(ExpectedConditions.textToBePresentInElementLocated(By.id("check"), "interactive"));
            browser.checkAndSwitchToFrame();
            return browser.findElement(By.id(id));
        } else {
            return e;
        }
    }

    // TODO: These do not work for safari
    private ArrayList<Interaction> getAllInteractions() {
        ArrayList<Interaction> interactions = new ArrayList<>();

        // Hover over element
        interactions.add(new HoverAbove(browser));

        // Left click
        interactions.add(new LeftClickOn(browser));

        // Right click
        interactions.add(new RightClickOn(browser));

        // Press enter
        interactions.add(new PressEnter(browser));

        // Press middle mouse button
        interactions.add(new MiddleMouseClick(browser));

        // TODO: middlemouse click, specific action for img-longdesc, ...
        // TODO: Result may differ for each action and HTML element combination, this is not supported

        return interactions;
    }

    private void createElementIds() {
        this.elementIds = new ArrayList<>();
        this.elementIds.add("a1");
        this.elementIds.add("i1");
        this.elementIds.add("i2");
        this.elementIds.add("b1");
        this.elementIds.add("index2");
        this.elementIds.add("index1");
        // TODO test2
        this.elementIds.add("p1");
        this.elementIds.add("s1");
        this.elementIds.add("m1");
        this.elementIds.add("m2");
    }

    @Override
    public TestName getName() {
        return TestName.INTERACTIVE;
    }
}
*/