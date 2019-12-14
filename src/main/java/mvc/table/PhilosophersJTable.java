package mvc.table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PhilosophersJTable extends JFrame {

    private DefaultTableModel philosophers;
    private JTable table;

    public PhilosophersJTable() {
        super("Favorite Philosophers");
        philosophers = new DefaultTableModel();
        philosophers.addColumn("First Name");
        philosophers.addColumn("Last Name");
        philosophers.addColumn("Years");

        String[] socrates = {"Socrates", "", "428-399 B.C."};

    }

}
