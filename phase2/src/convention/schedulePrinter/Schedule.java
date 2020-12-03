package convention.schedulePrinter;

import java.util.ArrayList;

public class Schedule {
    private ArrayList<ArrayList<String>> eventStringLists;
    private String title;

    public Schedule() {
        this.eventStringLists = new ArrayList<>();
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public void addEventStringList(ArrayList<String> s) {
        this.eventStringLists.add(s);
    }

    /**
     * @param a List of List of Strings. Each sub List contain strings for description of an event
     * @return
     */
    public String toString(ArrayList<ArrayList<String>> a) {
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
        titleLine.append(this.title);
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

}
