package gateway;

import gateway.exceptions.PrinterException;

import java.awt.*;
import java.io.*;
import java.util.Scanner;

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

        String htmlHeader = (new Scanner(new FileReader("header.html"))).useDelimiter("\\Z").next();
        String htmlFooter = (new Scanner(new FileReader("footer.html"))).useDelimiter("\\Z").next();

        fileName = fileName.concat(".html");

        BufferedWriter table = new BufferedWriter(new FileWriter(fileName));
        table.write(htmlHeader);
        table.write(document);
        table.write(htmlFooter);
        table.flush();
        table.close();

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new File(fileName).toURI());
        } else {
            throw new PrinterException();
        }
    }
}
