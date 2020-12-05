package gateway;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TablePrinter {
    List<List<String>> table = new ArrayList<>();

    public String stringifyTable(String title) {
        if (this.table.isEmpty()) {
            return "";
        }
        ArrayList<Integer> colWidths = new ArrayList<>();
        for (String s : this.table.get(0)) {
            colWidths.add(Math.floorDiv(70, this.table.get(0).size()));
        }
        int width = 0;
        for (int i : colWidths) {
            width = width + i;
        }
        width = width + this.table.get(0).size() - 1;
        StringBuilder table = new StringBuilder();
        StringBuilder topLine = new StringBuilder();
        StringBuilder bottomLine = new StringBuilder();
        topLine.append("╔");
        bottomLine.append("╚");
        for (int i = 0; i < width; i++) {
            topLine.append("═");
            bottomLine.append("═");
        }
        topLine.append("╗\r\n");
        bottomLine.append("╝");
        table.append(topLine);

        StringBuilder titleLine = new StringBuilder();
        titleLine.append("║");
        titleLine.append(title);
        while (titleLine.length() < width + 1) {
            titleLine.append(" ");
        }
        titleLine.append("║\r\n");
        table.append(titleLine);

        StringBuilder hline = new StringBuilder();
        hline.append("╠");
        while (hline.length() < width + 1) {
            hline.append("-");
        }
        hline.append("╣\r\n");
        table.append(hline);

        for (List<String> sub : this.table) {
            StringBuilder row = new StringBuilder();
            row.append("║");
            for (int i = 0; i < colWidths.size(); i++) {
                StringBuilder cell = new StringBuilder();

                cell.append(sub.get(i));
                while (cell.length() <= colWidths.get(i) - 1) {
                    cell.append(" ");
                }
                cell.append("│");
                row.append(cell);
            }
            row.deleteCharAt(row.length() - 1);
            table.append(row);

            table.append("║\r\n");
        }

        table.append(bottomLine);
        return table.toString();
    }

    public void print(String title, String fileName) throws IOException {
        BufferedWriter table = new BufferedWriter(new FileWriter(fileName.concat(".txt")));
        table.write(this.stringifyTable(title));
        table.flush();
        table.close();

        File scheduleTable = new File(fileName.concat(".txt"));

        Desktop d = Desktop.getDesktop();
        d.print(scheduleTable);
    }
}
