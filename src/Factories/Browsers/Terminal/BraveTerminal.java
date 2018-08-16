package Factories.Browsers.Terminal;

import Factories.Browsers.BrowserSetting;

public class BraveTerminal extends TerminalBrowser {

    public BraveTerminal(BrowserSetting setting) {
        super(setting);
    }

    @Override
    protected boolean supports(BrowserSetting setting) {
        switch (setting) {
            case DEFAULT:
            case BLOCK_THIRD_PARTY_COOKIES:
            case ENABLE_TRACKING_PROTECTION:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected String getProgramName() {
        return "Brave";
    }

    @Override
    protected String[] getArguments() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "brave_terminal";
    }
}
