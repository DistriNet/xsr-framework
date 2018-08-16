package Factories.Browsers;

import Factories.BrowserFactory;
import TestFunctionality.Tests.TestName;
import net.lightbody.bmp.BrowserMobProxyServer;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Browser {

    protected static final Logger LOGGER = Logger.getLogger(BrowserFactory.class.getName());

    protected final BrowserSetting setting;

    public Browser(BrowserSetting setting) {
        if (supports(setting))
            this.setting = setting;
        else
            throw new IllegalArgumentException(this.getName() + " does not support the given setting " + setting);
    }

    abstract public void start();

    abstract public void stop();

    abstract public void get(String url);

    abstract public String getName();

    abstract public String getVersion();

    public String getSetting() {
        return this.setting.toString();
    }

    abstract public String getExtensionName();

    abstract public String getExtensionVersion();

    abstract public String toString();

    abstract protected boolean supports(BrowserSetting setting);

    public boolean setProxy(BrowserMobProxyServer proxy) {
        throw new IllegalArgumentException("This browser cannot set a proxy");
    }

    public void wait(int timeInMs) {
        try {
            Thread.sleep(timeInMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "Sleeping thread has been interrupted");
        }
    }

    // Visitor pattern
    abstract public void runTest(TestName test, int scenarioId);
}