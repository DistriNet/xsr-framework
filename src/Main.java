import Factories.BrowserFactory;
import Factories.Browsers.BrowserName;
import Factories.Browsers.BrowserSetting;
import Factories.Browsers.BrowserVersion;
import Factories.Browsers.Selenium.Local.LocalSeleniumBrowser;
import Factories.Browsers.Selenium.Remote.RemoteSeleniumBrowser;
import Factories.Browsers.Terminal.TerminalBrowser;
import Parser.Parser;
import TestFunctionality.Tests.TestName;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static Factories.Browsers.BrowserName.*;
import static Factories.Browsers.BrowserSetting.DEFAULT;

public class Main {

    private static BrowserFactory browserFactory;

    private static final String GRID_IP = "172.19.53.168";
    //private static final String GRID_IP = "localhost";

    public static void main(String[] args) throws IOException, InterruptedException {

        init();
        browserFactory = new BrowserFactory("adition.com", "https://leak.test/set_cookies.html", GRID_IP);

        String platform = "ubuntu";

        //runChrome();
        //runFirefox();
        //runOpera();
        runSafari();
        //runEdge();
    }

    private static void init() throws IOException {
        Parser.run("init/static/1_tags.csv", "init/static/2_types.csv");
        Parser.run("init/interactive/1_tags.csv", "init/interactive/2_types.csv");
    }

    private static void runChrome() throws IOException {
        LocalSeleniumBrowser chrome = browserFactory.createLocalSelenium(CHROME, DEFAULT, null, BrowserVersion.latest());
        Runner.run(chrome, 0, 3, TestName.getAllTests());
    }

    private static void runFirefox() throws IOException {
        LocalSeleniumBrowser firefox = browserFactory.createLocalSelenium(FIREFOX, DEFAULT, null, BrowserVersion.latest());
        Runner.run(firefox, 0, 3, TestName.getAllTests());
    }

    private static void runOpera() throws IOException {
        LocalSeleniumBrowser opera = browserFactory.createLocalSelenium(OPERA, DEFAULT, null, BrowserVersion.latest());
        Runner.run(opera, 0, 3, TestName.getAllTests());
    }

    private static void runEdge() throws IOException, InterruptedException {
        TerminalBrowser edge = browserFactory.createTerminalBrowser(EDGE, DEFAULT);
        Runner.run(edge, 0, 3, TestName.getAllTests());
    }

    private static void runChromeRemote(String platform) throws IOException, InterruptedException {
        BrowserVersion c59 = BrowserVersion.version("59.0.3071.86");
        BrowserVersion c60 = BrowserVersion.version("60.0.3112.90");
        BrowserVersion c61 = BrowserVersion.version("61.0.3163.79");
        BrowserVersion c62 = BrowserVersion.version("62.0.3202.75");
        BrowserVersion c63 = BrowserVersion.version("63.0.3239.108");
        BrowserVersion c64 = BrowserVersion.version("64.0.3282.140");
        BrowserVersion c65 = BrowserVersion.version("65.0.3325.181");
        BrowserVersion c66 = BrowserVersion.version("66.0.3359.181");
        List<RemoteSeleniumBrowser> chrome = browserFactory.createRemoteBrowsers(CHROME, DEFAULT, null, c66, platform);
        Runner.run(chrome, 0, TestName.getAllTests(), 1);
    }

    private static void runFirefoxRemote(String platform) throws IOException, InterruptedException {
        BrowserVersion f53 = BrowserVersion.version("53.0.3");
        BrowserVersion f54 = BrowserVersion.version("54.0.1");
        BrowserVersion f55 = BrowserVersion.version("55.0.3");
        BrowserVersion f56 = BrowserVersion.version("56.0.2");
        BrowserVersion f57 = BrowserVersion.version("57.0.4");
        BrowserVersion f58 = BrowserVersion.version("58.0.2");
        BrowserVersion f59 = BrowserVersion.version("59. s0.3");
        List<RemoteSeleniumBrowser> firefox = browserFactory.createRemoteBrowsers(FIREFOX, DEFAULT, null, f59, platform);
        Runner.run(firefox, 0, TestName.getAllTests(), 1);
    }

    private static void runSafari() throws IOException {
        LocalSeleniumBrowser safari = browserFactory.createLocalSelenium(SAFARI, DEFAULT, null, BrowserVersion.latest());
        Runner.run(safari, 0, 3, TestName.getAllTests());
    }
}