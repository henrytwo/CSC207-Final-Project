package gateway;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class to trigger the system print dialog on a string
 */
public class DocumentPrinter implements IDocumentPrinter {

    /**
     * Performs the print operation
     *
     * @param document document to print as a string
     * @param fileName path to save the document temporarily
     * @throws IOException
     */
    @Override
    public void print(String document, String fileName) throws IOException {
        BufferedWriter table = new BufferedWriter(new FileWriter(fileName.concat(".txt")));
        table.write(document);
        table.flush();
        table.close();

        File scheduleTable = new File(fileName.concat(".txt"));

        Desktop d = Desktop.getDesktop();
        d.print(scheduleTable);
    }
}
