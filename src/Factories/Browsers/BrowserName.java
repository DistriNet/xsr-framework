package Factories.Browsers;

public enum BrowserName {
    CHROME("chrome", "crx"),
    FIREFOX("firefox", "xpi"),
    OPERA("opera", "nex"),
    SAFARI("safari", "safariextz"),
    TOR("tor", "xpi"),
    CLIQZ("cliqz", null),
    EDGE("edge", null),
    BRAVE("brave", null);

    private final String name;
    private final String extensionSuffix;

    BrowserName(String name, String extensionSuffix) {
        this.name = name;
        this.extensionSuffix = extensionSuffix;
    }

    public String getExtensionSuffix() {
        return extensionSuffix;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
