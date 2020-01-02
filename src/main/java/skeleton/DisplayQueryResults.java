package skeleton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Vector;

public class DisplayQueryResults extends JFrame {

    private JTextArea queryArea;
    private JTable table;

    private Connection conn;
    private Statement statement;
    private ResultSet rs;
    private int numberOfRows;

    public DisplayQueryResults() {
        super("Display");

        String connUrl = "jdbc:sqlite:dbsqlrb.sqlite";
        String strSQL = "Select * from UNOTHOPR ";

        try
        {
            conn = DriverManager.getConnection(connUrl);
            statement = conn.createStatement();
            rs = statement.executeQuery(strSQL);
            table = new JTable(buildTableModel(rs));

            queryArea = new JTextArea(strSQL, 3, 100);
            queryArea.setWrapStyleWord(true);
            queryArea.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(queryArea,
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
                    );
            JButton submmitButton = new JButton("Выполнить запрос");
            JButton exitButton = new JButton("Выход");

            submmitButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        rs = statement.executeQuery(queryArea.getText());
                        table.setModel(buildTableModel(rs));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }
            });

            exitButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            Box box = Box.createHorizontalBox();
            box.add(scrollPane);
            box.add(submmitButton);
            box.add(exitButton);

            Container container = getContentPane();
            container.add(box, BorderLayout.NORTH);
            container.add(new JScrollPane(table), BorderLayout.CENTER);

            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(500, 400);
            setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for(int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()){
            Vector<Object> vector = new Vector<>();
            for(int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        new DisplayQueryResults();
    }
}
