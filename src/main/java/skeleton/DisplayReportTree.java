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

    public DisplayReportTree() throws SQLException, ClassNotFoundException {
        sqLiteJDBC = new SQLiteJDBC();
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
        setSize(400, 300);
        setVisible(true);


    }// </editor-fold>


    //@SuppressWarnings("CallToThreadDumpStack")
    public final void pop_tree() {
        try {

            try {
                conn = sqLiteJDBC.getConn();
                stm = conn.createStatement();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ArrayList list = new ArrayList();
            list.add("Category List");
            String sql = "SELECT * FROM QUREPORT";

            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                Object value[] = {rs.getString(1), rs.getString(2)};
                list.add(value);
            }
            Object hierarchy[] = list.toArray();
            DefaultMutableTreeNode root = processHierarchy(hierarchy);

            DefaultTreeModel treeModel = new DefaultTreeModel(root);
            cat_tree.setModel(treeModel);
        } catch (Exception e) {
        }

    }

    //@SuppressWarnings("CallToThreadDumpStack")
    public DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
        try {
            int ctrow = 0;
            int i = 0;
            try {

                try {
                    conn = sqLiteJDBC.getConn();
                    stm = conn.createStatement();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String sql = "SELECT * from QUREPORT";
                ResultSet rs = stm.executeQuery(sql);

                while (rs.next()) {
                    ctrow = rs.getRow();
                }
                String L1Nam[] = new String[ctrow];
                String L1Id[] = new String[ctrow];
                ResultSet rs1 = stm.executeQuery(sql);
                while (rs1.next()) {
                    L1Nam[i] = rs1.getString("NAMEREP");
                    L1Id[i] = rs1.getString("IDREP");
                    i++;
                }
                DefaultMutableTreeNode child, grandchild;
                for (int childIndex = 0; childIndex < L1Nam.length; childIndex++) {
                    child = new DefaultMutableTreeNode(L1Nam[childIndex]);
                    node.add(child);//add each created child to root
                    String sql2 = "SELECT TITLES FROM SUBREPORT WHERE IDPARENTREP = '" + L1Id[childIndex] + "' ";
                    ResultSet rs3 = stm.executeQuery(sql2);
                    while (rs3.next()) {
                        grandchild = new DefaultMutableTreeNode(rs3.getString("TITLES"));
                        child.add(grandchild);//add each grandchild to each child
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception e) {
            //
        }

        return (node);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new DisplayReportTree();
    }

}
