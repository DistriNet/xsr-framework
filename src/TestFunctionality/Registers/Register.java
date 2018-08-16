package TestFunctionality.Registers;

import Factories.Browsers.Browser;
import Parser.Leak;
import TestFunctionality.Tests.LeakTest;
import TestFunctionality.Tests.TestName;
import com.opencsv.CSVWriter;
import net.lightbody.bmp.core.har.HarCookie;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register {

    private static final String INIT_FOLDER = "init/";

    private ArrayList<String> list;
    protected HashMap<TestName, HashMap<String,Leak>> toWrite;
    protected ArrayList<String> registeredBrowsers;
    protected BufferedReader reader;
    private final String targetDomain;
    private final File initFolder;
    protected final File folderToWrite;
    private final boolean ignoreFavicon;


    public Register(Browser browser, int scenarioId, Collection<TestName> tests, boolean ignoreFavicon) throws IOException {
        this.list = new ArrayList<>();
        this.toWrite = new HashMap<>();
        this.registeredBrowsers = new ArrayList<>();

        this.targetDomain = LeakTest.getLeakDomain(scenarioId);
        this.initFolder = new File(INIT_FOLDER);
        if (!this.initFolder.exists())
            throw new IllegalArgumentException("File with path '" + INIT_FOLDER + "' does not exist");
        if (browser.getExtensionName().equals("_base"))
            this.folderToWrite = new File("data/" + browser.getExtensionName() + '/' + browser.getName() + '/' + browser.getVersion() + '/' + browser.getSetting() + "/s" + scenarioId);
        else
            this.folderToWrite = new File("data/" + browser.getExtensionName() + '/' + browser.getName() + "-v" + browser.getVersion() + '/' + browser.getExtensionVersion() + "/s" + scenarioId);
        this.ignoreFavicon = ignoreFavicon;
        for (TestName test : tests)
            addTest(test);
        addBrowser(browser.toString());

        this.prepareOutputFile();
    }

    private void prepareOutputFile() throws IOException {
        if (folderToWrite.exists())
            return;
        folderToWrite.mkdirs();
    }

    private void addTest(TestName testName) throws IOException {
        if (this.toWrite.containsKey(testName))
            throw new IllegalArgumentException("The test \'" + testName + "\' has already been registered.");
        refreshReader(testName);
    }

    private void addBrowser(String browser) {
        if (this.registeredBrowsers.contains(browser))
            throw new IllegalArgumentException("The browser \'" + browser + "\' has already been registered.");
        this.registeredBrowsers.add(browser);
        for (HashMap<String, Leak> map : this.toWrite.values()) {
            for (Leak leak : map.values()) {
                leak.resetBrowserCount(browser);
                leak.initCookies(browser);
            }
        }
    }

    public void register(TestName testName, String URL, String browserName, List<HarCookie> cookies) throws IOException {
        String type = getQuery(URL);
        if (type == null) return;

        if (!this.toWrite.get(testName).containsKey(type)) {
            if (type.contains("favicon") && !ignoreFavicon)
                throw new IllegalArgumentException(type + " has not been found in " + this.initFolder);
        } else {
            this.list.add(type + " (" + URL + ")");
            Leak leak = this.toWrite.get(testName).get(type);
            leak.incrementBrowserCount(browserName);
            for (HarCookie cookie : cookies) {
                leak.addCookie(browserName, cookie.getName());
            }
        }
    }

    private void refreshReader(TestName testName) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(initFolder.getPath() + "/" + testName.name + "/2_types.csv")));
        String line;
        int i=0;
        this.toWrite.put(testName, new HashMap<>());
        for (line = this.reader.readLine(); line != null; line = reader.readLine(), i++)
            this.toWrite.get(testName).put(line, new Leak(line));
    }

    public void close() throws IOException {
        for (TestName testName : this.toWrite.keySet()) {
            HashMap<String, Leak> map = this.toWrite.get(testName);

            CSVWriter writer = new CSVWriter(new FileWriter(folderToWrite.getPath() + "/" + testName + ".csv"));

            if (map.values().isEmpty())
                return;

            ArrayList<Leak> leaks = new ArrayList(map.values());
            Collections.sort(leaks);
            String[] header = leaks.get(0).toRawHeader();
            String[] headerWithoutFirst = Arrays.copyOfRange(header, 1, header.length);

            // Write first header
            writer.writeNext(header);

            // Write second header (Total)
            String[] header2 = new String[header.length];
            header2[0] = "TOTAL:";
            int total;
            for (int i = 0; i < headerWithoutFirst.length; i++) {
                total = getTotal(testName, headerWithoutFirst[i]);
                header2[i + 1] = Integer.toString(total) + " leaks";
            }
            writer.writeNext(header2);

            for (Leak leak : leaks) {
                String[] record = leak.toRawRecord(headerWithoutFirst);
                writer.writeNext(record);
            }

            this.reader.close();
            writer.close();
        }
    }

    protected int getTotal(TestName testName, String browser) {
        int i = 0;
        for (Leak leak: this.toWrite.get(testName).values())
            if (leak.getBrowserCount(browser) > 0) i++;
        return i;
    }

    private String getQuery(String URL) {
        // TODO: AppCache Safari is over http
        Pattern p = Pattern.compile(/*".+://" + */this.targetDomain + "/report/\\?leak=(?<type>[a-zA-Z0-9-]+)(:[0-9]+)?");
        Matcher m = p.matcher(URL);
        while(m.find()) {
            String query = URL.substring(m.start("type"), m.end("type"));
            // The browser puts an '?' at the end of the requested url by GET form.
            if (query.charAt(query.length() - 1) == '?')
                query = query.substring(0, query.length() - 1);
            return query;
        }
        return null;
    }

    public File getBaseFolder() {
        return folderToWrite.getParentFile().getParentFile();
    }

    public ArrayList<String> getList() {
        return (ArrayList<String>) this.list.clone();
    }

    public int getNumberOfLeaksFoundFor(TestName testName, String browserName) {
        int i = 0;
        for (Leak leak : toWrite.get(testName).values()) {
            if (leak.getBrowserCount(browserName) > 0)
                i++;
        }
        return i;
    }

}