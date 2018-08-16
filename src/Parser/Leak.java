package Parser;

import java.util.*;

public class Leak implements Comparable {

    private final String id;
    private String code;
    private HashMap<String, Integer> browserCount;
    private HashMap<String, Set<String>> cookies;

    public Leak(String id) {
        this.id = id;
        this.browserCount = new HashMap<>();
        this.cookies = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getBrowserCount(String browser) {
        return this.browserCount.get(browser);
    }

    public void incrementBrowserCount(String browser) {
        int count = this.browserCount.get(browser);
        this.browserCount.put(browser, count + 1);
    }

    public void resetBrowserCount(String browser) {
        this.browserCount.put(browser, 0);
    }

    public int getNbOfRegisteredBrowsers() {
        return this.browserCount.keySet().size();
    }

    public boolean hasRegisteredBrowser(String browser) {
        return this.browserCount.keySet().contains(browser);
    }

    public void initCookies(String browser) {
        this.cookies.put(browser, new HashSet<>());
    }

    public void addCookie(String browser, String name) {
        this.cookies.get(browser).add(name);
    }

    @Override
    public String toString() {
        return this.getId();
    }


    @Override
    public int compareTo(Object other) {
        return this.getId().toLowerCase().compareTo(((Leak) other).getId().toLowerCase());
    }

    private double getPercentage() {
        double i = 0;
        for (int entry : this.browserCount.values())
            if (entry > 0) i++;
        return 100 * i / this.browserCount.size();
    }

    public String[] toHeader() {
        int nbOfBrowsers = browserCount.keySet().size();
        String header[] = new String[2 + nbOfBrowsers];
        String[] browsers = this.browserCount.keySet().toArray(new String[nbOfBrowsers]);
        Arrays.sort(browsers);
        header[0] = "Id";
        header[1] = " % ";
        for (int i = 2; i < nbOfBrowsers + 2; i++) {
            header[i] = browsers[i-2];
        }
        return header;
    }

    public String[] toRawHeader() {
        int nbOfBrowsers = browserCount.keySet().size();
        String header[] = new String[1 + nbOfBrowsers];
        String[] browsers = this.browserCount.keySet().toArray(new String[nbOfBrowsers]);
        Arrays.sort(browsers);
        header[0] = "Id";
        for (int i = 1; i < nbOfBrowsers + 1; i++) {
            header[i] = browsers[i-1];
        }
        return header;
    }

    public String[] toRecord(String[] header) {
        List<String> record = new ArrayList<>();
        record.add(this.id);
        record.add(String.format("%.2f", getPercentage()));

        for (String browser : header) {
            if (this.browserCount.get(browser) > 0)
                record.add("true (" + this.browserCount.get(browser) + "/3) " + this.cookies.get(browser));
            else
                record.add("false");
        }
        return record.toArray(new String[record.size()]);
    }

    public String[] toRawRecord(String[] header) {
        List<String> record = new ArrayList<>();
        record.add(this.id);

        for (String browser : header) {
            if (this.browserCount.get(browser) > 0)
                record.add("true (" + this.browserCount.get(browser) + "/3) " + this.cookies.get(browser));
            else
                record.add("false");
        }
        return record.toArray(new String[record.size()]);
    }

}
