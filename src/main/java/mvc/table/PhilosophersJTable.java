package mvc.table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

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
        philosophers.addRow(socrates);

        table = new JTable(philosophers);

        JButton addButton = new JButton("Add Philosopher");
        addButton.addActionListener(
                new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        String[] philosopher = {"", "", ""};
                        philosophers.addRow(philosopher);
                    }
                }
        );

        JPanel inputPanel = new JPanel();
        inputPanel.add(addButton);

        Container container = getContentPane();
        container.add(new JScrollPane(table), BorderLayout.CENTER);
        container.add(inputPanel, BorderLayout.NORTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    public static void main(String[] args) {
        new PhilosophersJTable();
    }

}
