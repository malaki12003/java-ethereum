package org.eth.common;

import java.io.File;

public class CommonPath {

    // Creates a node name following the Ethereum convention by appending the operating system name
    // and Go runtime version to the provided name and version.
    public static String makeName(String name, String version) {
        return String.format("%s/v%s/%s/%s", name, version, System.getProperty("os.name"), Runtime.getRuntime().version());
    }

    // Checks if a file exists at the specified file path.
    public static boolean fileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    // Returns the absolute path by combining the data directory and filename.
    // If the filename is already absolute, it's returned directly.
    public static String absolutePath(String dataDir, String filename) {
        File file = new File(filename);
        if (file.isAbsolute()) {
            return filename;
        }
        return new File(dataDir, filename).getAbsolutePath();
    }
}