package util;

import convention.schedulePrinter.Schedule;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SchedulePrinter {
    public String objectToString(Schedule schedule) {
        ArrayList<ArrayList<String>> a = schedule.getEventStringLists();
        if (a.size() == 0) {
            return "";
        }
        ArrayList<Integer> colWidths = new ArrayList<>();
        for (String s: a.get(0)) {
            colWidths.add(Math.floorDiv(70, a.get(0).size()));}
        int width = 0;
        for (int i: colWidths) { width = width + i;}
        width = width + a.get(0).size() - 1;
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
        titleLine.append(schedule.getTitle());
        while (titleLine.length() < width+1) {
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

        for (ArrayList<String> sub : a) {
            StringBuilder row = new StringBuilder();
            row.append("║");
            for (int i = 0; i < colWidths.size(); i++) {
                StringBuilder cell = new StringBuilder();

                cell.append(sub.get(i));
                while (cell.length() <= colWidths.get(i) -1) {
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

    public void print(Schedule schedule) throws IOException {
        String s = objectToString(schedule);
        BufferedWriter table = new BufferedWriter(new FileWriter("event_schedule.txt"));
        table.write(s);
        table.flush();
        table.close();

        File scheduleTable = new File("event_schedule.txt");

        Desktop d = Desktop.getDesktop();
        d.print(scheduleTable);
    }
}
