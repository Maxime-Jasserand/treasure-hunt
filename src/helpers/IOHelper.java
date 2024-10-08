package helpers;

import java.io.*;

/**
 * This class helps us with basic IO operation
 */
public class IOHelper {
    /**
     * @param path location of the file
     * @return a BufferedReader that will allow us to read the file line by line easily
     * @throws IOException in case the path leads us to a bad file
     */
    public static BufferedReader getInputReader(String path) throws IOException {
        File file = new File(path);

        if (!file.exists() || !file.canRead() || !file.isFile()) {
            throw new IOException();
        }

        Reader inputReader = new InputStreamReader(
                new FileInputStream(file)
        );

        return new BufferedReader(inputReader);
    }

    /**
     * @param path destination
     * @return a PrintWriter to write data line by line easily
     * @throws IOException in case something went wrong
     */
    public static PrintWriter getOutputWriter(String path) throws IOException {
        File file = new File(path);
        FileWriter fileWriter = new FileWriter(file);
        return new PrintWriter(fileWriter);
    }
}
