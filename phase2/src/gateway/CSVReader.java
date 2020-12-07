package gateway;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to read and parse CSV objects into a 2D array
 */
public class CSVReader implements IMatrixReader {
    private String path;

    /**
     * Creates a CSV that reads from a path
     *
     * @param path path to read from
     */
    public CSVReader(String path) {
        this.path = path;
    }

    /**
     * Reads the CSV
     *
     * @return a list of string arrays, with each entry in the list corresponding to a row in the CSV
     * @throws IOException
     */
    @Override
    public List<String[]> read() throws IOException {
        List<String[]> out = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line;

        while ((line = br.readLine()) != null) {
            out.add(line.split(","));
        }

        return out;
    }
}
