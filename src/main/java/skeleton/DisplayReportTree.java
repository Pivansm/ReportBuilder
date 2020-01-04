package skeleton;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
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

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new DisplayReportTree();
    }

}
