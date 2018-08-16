package Factories.Browsers;

public enum BrowserSetting {
    DEFAULT ("default", ""),
    BLOCK_THIRD_PARTY_COOKIES("block_third_party_cookies", "_btpc"),
    ENABLE_TRACKING_PROTECTION("tracking_protection", "_tp"),
    ENABLE_AD_BLOCKING("ad_blocking", "_ab"),
    EXPERIMENTAL_MODE("experimental_mode", "_exp");

    private final String name;
    private final String testSuffix;

    BrowserSetting(String name, String testSuffix) {
        this.name = name;
        this.testSuffix = testSuffix;
    }

    public String getTestSuffix() {
        return this.testSuffix;
    }

    public String toString() {
        return this.name;
    }
}
