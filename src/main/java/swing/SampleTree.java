package swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.TreePath;

public class SampleTree extends JFrame {
    private JPopupMenu menu = new JPopupMenu("Popup");

    public SampleTree() throws HeadlessException {
        super("Tree");
        final JTree tree = new JTree();

        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    TreePath tp = tree.getClosestPathForLocation(e.getX(),e.getY());
                    if (tp != null) {
                        System.out.println(tp);
                        tree.setSelectionPath(tp);
                    }
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        String letters = "ABCDEF";

        for (final char letter : letters.toCharArray()) {
            JMenuItem item = new JMenuItem(String.valueOf(letter));
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(SampleTree.this, "You chose the letter: " + letter);
                }
            });
            menu.add(item);
        }

        add(new JScrollPane(tree));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SampleTree st = new SampleTree();
                st.setSize(200, 200);
                st.setLocationRelativeTo(null);
                st.setVisible(true);
            }
        });
    }
}
