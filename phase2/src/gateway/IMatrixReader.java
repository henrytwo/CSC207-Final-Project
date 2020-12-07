package gateway;

import java.io.IOException;
import java.util.List;

/**
 * Class to read a document that parses into a matrix of strings
 */
public interface IMatrixReader {
    List<String[]> read() throws IOException;
}
