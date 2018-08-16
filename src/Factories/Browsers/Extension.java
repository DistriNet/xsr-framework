package Factories.Browsers;

import java.io.File;

public class Extension {

    private File file;
    private String name;
    private String version;

    public Extension(File file) {
        this.file = file;
        File parent = file.getParentFile();
        if (parent.getName().equals("modified"))
            parent = parent.getParentFile();
        this.name = parent.getName();
        this.version = file.getName().substring(0, file.getName().length() - 4);
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return name + "-v" + version;
    }
}
