package Factories.Browsers.Terminal;

import Factories.Browsers.BrowserSetting;

import java.io.IOException;

public class CliqzTerminal extends TerminalBrowser {

    public CliqzTerminal(BrowserSetting setting) {
        super(setting);
    }

    // The get method is overridden because osascript and Cliqz do not work well together.
    @Override
    public void get(String url) {
        String[] command = new String[]{"/Applications/Cliqz.app/Contents/MacOS/cliqz", url};
        try {
            proc = new ProcessBuilder(command).start();
            wait(STANDARD_WAIT_MS);
            proc.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean supports(BrowserSetting setting) {
        switch (setting) {
            case DEFAULT:
            case BLOCK_THIRD_PARTY_COOKIES:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String getProgramName() {
        return "Cliqz";
    }

    @Override
    protected String[] getArguments() {
        switch(setting) {
            case BLOCK_THIRD_PARTY_COOKIES:
                return new String[]{"-P", "Block Third Party Cookies", "-new-instance"};
            case DEFAULT:
                return new String[]{"-P", "Terminal", "-new-instance"};
            default:
                throw new IllegalStateException("Setting is not supported here");
        }
    }

    @Override
    public String getName() {
        return "cliqz_terminal";
    }
}
