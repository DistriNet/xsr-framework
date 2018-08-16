package Factories.Browsers.Terminal;

import Factories.Browsers.Browser;
import Factories.Browsers.BrowserSetting;
import TestFunctionality.Tests.TestName;

import java.io.IOException;


/**
 * When using this class, profiles have to be prepared in advance:
 * 1) Set the cookies by visiting the init.php pages
 * 2) Set proxy for HTTP, SSL and SOCKS to localhost:61985
 * 3) Import the BrowserMob Proxy certificate as authority
 * 4) Configure settings according to test
 * 5) (Configure system proxy settings for Chrome)
 */
public abstract class TerminalBrowser extends Browser {

    protected final int STANDARD_WAIT_MS = 5000;

    protected Process proc;

    public TerminalBrowser(BrowserSetting setting) {
        super(setting);
    }

    abstract protected boolean supports(BrowserSetting setting);

    abstract protected String getProgramName();

    abstract protected String[] getArguments();

    abstract public String getName();

    @Override
    public String getVersion() {
        return "???";
    }

    @Override
    public String getExtensionName() {
        return "_base";
    }

    @Override
    public String getExtensionVersion() { return "???";}

    @Override
    public void get(String url) {
        try {
            String[] arguments = getArguments();
            String[] command = new String[5 + arguments.length];
            command[0] = "open";
            command[1] = "-na";
            command[2] = getProgramName();
            command[3] = "--args";
            for (int i = 0; i < arguments.length; i ++)
                command[4 + i] = arguments[i];
            command[5 + arguments.length - 1] = url;
            start(command);
            wait(STANDARD_WAIT_MS);
            //proc.destroy();
            //proc.waitFor();
            stop2();
            wait(500);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("FirefoxTerminal getter private mode");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IllegalStateException("FirefoxTerminal getter private mode");
        }
    }

    protected void start(String[] command) throws IOException, InterruptedException {
        proc = new ProcessBuilder(command).start();
        //proc.waitFor();
    }

    private void stop2() throws IOException {
        String[] command = new String[]{"osascript", "-e", "quit app \"" + getProgramName() + "\""};
        Process proc = new ProcessBuilder(command).start();
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}

    @Override
    public String toString() {
        return this.getName() + setting.getTestSuffix();
    }

    // Visitor pattern
    @Override
    public void runTest(TestName test, int scenarioId) {
        test.run(this, scenarioId);
    }

}
