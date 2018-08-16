# XSR-Framework

The XSR-Framework was developed for evaluating third-party cookie policies implemented by browsers and extensions.
It runs on Java and uses the browser automation tool Selenium.
The results of these evaluations are available on our website [WhoLeftOpenTheCookieJar.com](https://wholeftopenthecookiejar.com), as well as our USENIX paper for which this framework was the foundation.

## Dependencies

* [Selenium](https://www.seleniumhq.org/) 3.11.0
* [Browsermob Proxy](https://bmp.lightbody.net/) 2.1.5
* [OpenCsv](http://opencsv.sourceforge.net/) 4.0
* [Apache Commons Lang](https://mvnrepository.com/artifact/org.apache.commons/commons-lang3/3.7) 3.7

## Getting started

Note that Browsers and their associated WebDrivers have to be installed prior to running the tests.
For browsers like Firefox, the browser profile has also to be created in advance.
In addition, the paths to those WebDrivers and browser profiles have to be entered at the appropriate Browser classes.
These are indicated with TODOs.

The Main.java class is used to drive the framework.
Here, several test examples have already been implemented.

## WebDrivers

* [ChromeDriver](http://chromedriver.chromium.org/)
* [OperaDriver](https://github.com/operasoftware/operachromiumdriver/releases)
* [GeckoDriver](https://github.com/mozilla/geckodriver/releases) (used for both Firefox and TorBrowser)
* [SafariDriver](https://webkit.org/blog/6900/webdriver-support-in-safari-10/) (already integrated in Safari)
* [EdgeDriver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)