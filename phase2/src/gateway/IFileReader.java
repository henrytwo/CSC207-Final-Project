package gateway;

import java.io.IOException;
import java.util.List;

public interface IFileReader {
    List<String[]> read() throws IOException;
}
