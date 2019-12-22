package skeleton;

import javax.swing.*;

class TreeViewTable extends JFrame {

    private JTree tree;
    private JTable table;

    public TreeViewTable() {

        tree = new JTree();
        table = new JTable();

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, true,
                new JScrollPane(tree),
                new JScrollPane(table)
        );

        getContentPane().add(splitPane);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 480);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TreeViewTable();
    }
}
