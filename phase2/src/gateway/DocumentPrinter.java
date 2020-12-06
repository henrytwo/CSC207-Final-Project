package gateway;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class DocumentPrinter implements IDocumentPrinter {


    // title -> document content
    @Override
    public void print(String stringifiedTable, String fileName) throws IOException {
        BufferedWriter table = new BufferedWriter(new FileWriter(fileName.concat(".txt")));
        table.write(stringifiedTable);
        table.flush();
        table.close();

        File scheduleTable = new File(fileName.concat(".txt"));

        Desktop d = Desktop.getDesktop();
        d.print(scheduleTable);
    }
}
