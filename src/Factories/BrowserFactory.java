package Factories;

import Factories.Browsers.BrowserName;
import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Extension;
import Factories.Browsers.Selenium.Local.*;
import Factories.Browsers.Selenium.Remote.ChromeRemoteSelenium;
import Factories.Browsers.Selenium.Remote.FirefoxRemoteSelenium;
import Factories.Browsers.Selenium.Remote.RemoteSeleniumBrowser;
import Factories.Browsers.Terminal.BraveTerminal;
import Factories.Browsers.Terminal.CliqzTerminal;
import Factories.Browsers.Terminal.TerminalBrowser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BrowserFactory {

    private final String cookieDomain;
    private final String backupCookieUrl;
    private final URL hubAddress;

    public BrowserFactory(String cookieDomain, String backupCookieUrl, String gridIP) {
        this.cookieDomain = cookieDomain;
        this.backupCookieUrl = backupCookieUrl;
        String hubString = "http://" + gridIP + ":4444/wd/hub";
        try {
            this.hubAddress = new URL(hubString);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("The given hubAddress '" + hubString + "' is a malformed URL");
        }
    }

    public LocalSeleniumBrowser createLocalSelenium(BrowserName name, BrowserSetting setting, File extensionFile, BrowserVersion version) {
        Extension extension = extensionFile == null ? null : new Extension(extensionFile);
        switch (name) {
            case CHROME:
                return new Chrome(cookieDomain, backupCookieUrl, setting, extension, version);
            case FIREFOX:
                return new Firefox(cookieDomain, backupCookieUrl, setting, extension, version);
            case OPERA:
                return new Opera(cookieDomain, backupCookieUrl, setting, extension, version);
            case SAFARI:
                return new Safari(cookieDomain, backupCookieUrl, setting, version);
            case TOR:
                return new Tor(cookieDomain, backupCookieUrl, setting, extension, version);
            case EDGE:
                return new Edge(cookieDomain, backupCookieUrl, setting, extension, version);
            default:
                throw new IllegalArgumentException("Given name '" + name + "' is not a valid browser name");
        }
    }

    public TerminalBrowser createTerminalBrowser(BrowserName name, BrowserSetting setting) {
        switch (name) {
            case CLIQZ:
                return new CliqzTerminal(setting);
            case BRAVE:
                return new BraveTerminal(setting);
            default:
                throw new IllegalArgumentException("Given name '" + name + "' is not a valid browser name");
        }
    }

    public List<RemoteSeleniumBrowser> createRemoteBrowsers(BrowserName name, BrowserSetting setting, String extensionName, BrowserVersion version, String platform) {
        List<Extension> extensions = new ArrayList<>();
        List<RemoteSeleniumBrowser> browsers = new ArrayList<>();
        if (extensionName == null) {
            extensions.add(null);
        } else {
            extensions = getAllVersionsOf(name, extensionName);
        }
        switch (name) {
            case CHROME:
                for (Extension extension : extensions)
                    browsers.add(new ChromeRemoteSelenium(cookieDomain, backupCookieUrl, setting, extension, version, hubAddress, platform));
                break;
            case FIREFOX:
                for (Extension extension : extensions)
                    browsers.add(new FirefoxRemoteSelenium(cookieDomain, backupCookieUrl, setting, extension, version, hubAddress, platform));
                break;
            default:
                throw new IllegalArgumentException("Given browser '" + name + "' is not supported as remote browser");
        }
        return browsers;
    }

    public List<Extension> getAllVersionsOf(BrowserName name, String extensionName) {
        File browserFolder = new File("ext/" + name);
        File extensionFolder = searchInAllSubdirectories(extensionName, browserFolder);
        if (extensionFolder == null)
            throw new IllegalArgumentException("No extension folder found");
        File modified = new File(extensionFolder.getPath() + "/modified");
        if (modified.exists())
            extensionFolder = modified;

        List<Extension> extensions = new ArrayList<>();
        for (File extensionFile : extensionFolder.listFiles()) {
            if (!extensionFile.getName().endsWith(name.getExtensionSuffix()))
                continue;
            Extension extension = new Extension(extensionFile);
            extensions.add(extension);
        }
        return extensions;
    }

    private File searchInAllSubdirectories(String dirName, File curDir) {
        for (File newDir : curDir.listFiles()) {
            if (newDir.isDirectory()) {
                if (newDir.getName().toLowerCase().equals(dirName.toLowerCase()))
                    return newDir;
                File result = searchInAllSubdirectories(dirName, newDir);
                if (result != null)
                    return result;
            }
        }
        return null;
    }
}
