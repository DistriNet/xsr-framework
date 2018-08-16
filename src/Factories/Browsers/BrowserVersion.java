package Factories.Browsers;

public class BrowserVersion {

    private final String version;

    private BrowserVersion(String version) {
        this.version = version;
    }

    public static BrowserVersion version(String version) {
        return new BrowserVersion(version);
    }

    public static BrowserVersion latest() {
        return new BrowserVersion("latest");
    }

    public String getVersion() {
        return this.version;
    }

    public boolean isLatest() {
        return this.version.equals("latest");
    }

    public int getShortVersion() {
        int i = version.indexOf('.');
        return Integer.parseInt(version.substring(0, i));
    }

    @Override
    public String toString() {
        return this.version;
    }
}