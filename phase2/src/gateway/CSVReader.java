package gateway;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader implements IFileReader {
    private String path;

    public CSVReader(String path) {
        this.path = path;
    }

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
