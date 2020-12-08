package gateway;

import java.io.IOException;

/**
 * Interface for printing a string
 */
public interface IDocumentPrinter {
    void print(String document, String fileName) throws IOException;
}
