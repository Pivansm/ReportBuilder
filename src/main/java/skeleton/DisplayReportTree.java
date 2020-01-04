package skeleton;

import swing.PopupSample;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DisplayReportTree extends JFrame {

    Connection conn = null;
    Statement stm = null;

    private JTree cat_tree;
    private JPopupMenu popupMenu;
    SQLiteJDBC sqLiteJDBC;
    ReportDAO reportDAO;
    SubReportDAO subReportDAO;

    public DisplayReportTree() throws SQLException, ClassNotFoundException {
        sqLiteJDBC = new SQLiteJDBC();
        reportDAO = new ReportDAO(sqLiteJDBC.getConn());
        subReportDAO = new SubReportDAO(sqLiteJDBC.getConn());
        initComponents();
        pop_tree();
    }


    //System generated code
    //@SuppressWarnings("unchecked")
    private void initComponents() {

        cat_tree = new JTree();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Product List");

        popupMenu = createPopupMenu();

        cat_tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        Container container = getContentPane();
        container.add(new JScrollPane(cat_tree), BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 320);
        setVisible(true);


    }// </editor-fold>

    public final void pop_tree() {
        try {

            List<Report> list =  reportDAO.findAll();

            Object hierarchy[] = list.toArray();
            DefaultMutableTreeNode root = processHierarchy(hierarchy);
            DefaultTreeModel treeModel = new DefaultTreeModel(root);
            cat_tree.setModel(treeModel);

        } catch (Exception e) {
        }

    }

    public DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Заппросы");
        try {
            DefaultMutableTreeNode child, grandchild;
            List<Report> list = reportDAO.findAll();
            for (Report rp : list) {
                child = new DefaultMutableTreeNode(rp.getNameReport());
                node.add(child);

                List<SubReport> subList = subReportDAO.findParentId(rp.getId());
                for (SubReport srp : subList) {
                    grandchild = new DefaultMutableTreeNode(srp.getNameReport());
                    child.add(grandchild);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return (node);
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu pm = new JPopupMenu();
        JMenuItem enter = new JMenuItem("Выполнить");
        enter.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DisplayReportTree.this, "You chose the letter: ");
            }
        });
        pm.add(enter);
        pm.addSeparator();
        JMenuItem nwusel = new JMenuItem("Новый узел");
        nwusel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newuzl = JOptionPane.showInputDialog(DisplayReportTree.this,"<html><h2>Новый узел");
                newUsel(newuzl);
            }
        });
        pm.add(nwusel);
        return pm;
    }

   private void newUsel(String nameUsel) {
       Report report = new Report(1, nameUsel);
       reportDAO.insert(report);
   }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new DisplayReportTree();
    }

}
