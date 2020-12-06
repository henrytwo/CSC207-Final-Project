package gateway;

import java.io.IOException;

public interface IDocumentPrinter {
    void print(String document, String fileName) throws IOException;
}
