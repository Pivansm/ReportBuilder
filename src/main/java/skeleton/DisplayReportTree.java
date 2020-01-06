package skeleton;

import swing.PopupSample;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Style;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
import java.util.Vector;

public class DisplayReportTree extends JFrame {

    private final String STYLE_heading = "heading", STYLE_normal = "normal", FONT_style = "Times New Roman";

    Connection conn = null;
    Statement stm = null;

    private JMenuBar menuBar;
    private JToolBar toolbar;
    private JTree cat_tree;
    private JTable per_table;
    private JTextPane textEditor;
    private Style normal;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JPopupMenu popupMenu;

    private int idParent;
    private int idCurrChildren;
    private String query;

    SQLiteJDBC sqLiteJDBC;
    ReportDAO reportDAO;
    SubReportDAO subReportDAO;

    public DisplayReportTree() throws SQLException, ClassNotFoundException {
        sqLiteJDBC = new SQLiteJDBC();
        reportDAO = new ReportDAO(sqLiteJDBC.getConn());
        subReportDAO = new SubReportDAO(sqLiteJDBC.getConn());
        idParent = 0;
        idCurrChildren = 0;
        query = "SELECT * FROM QUREPORT";
        initComponents();
        pop_tree();
    }


    //System generated code
    //@SuppressWarnings("unchecked")
    private void initComponents() {

        //Меню
        ActionListener menuListener = new MenuActionListener();
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        JMenuItem newMenuItem = new JMenuItem("Настройка", KeyEvent.VK_N);
        newMenuItem.addActionListener(menuListener);
        fileMenu.add(newMenuItem);
        // Separator
        fileMenu.addSeparator();
        // File->Exit, X - Mnemonic
        JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitMenuItem.addActionListener(menuListener);
        fileMenu.add(exitMenuItem);


        cat_tree = new JTree();
        per_table = new JTable();
        toolbar = new JToolBar();

        JButton enterButton = new JButton("Выполнить");
        toolbar.add(enterButton);
        toolbar.addSeparator();
        String[] export = new String[] {"Excel(xlsx)", "Excel(xml)", "File(csv)"};
        toolbar.add(new JComboBox<String> (export));
        // Блокируем возможность перетаскивания панели
        toolbar.setFloatable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Report Builder");

        popupMenu = createPopupMenu();

        cat_tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
                /*if (e.getClickCount() == 1) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)cat_tree.getLastSelectedPathComponent();
                    if (node == null) return;
                    Object nodeInfo = node.getUserObject();
                    // Cast nodeInfo to your object and do whatever you want
                    System.out.println("" + nodeInfo.toString());
                }*/
            }
        });

        Container container = getContentPane();

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, true,
                new JScrollPane(cat_tree),
                new JScrollPane(per_table)
        );
        container.add(splitPane);
        container.add(toolbar, BorderLayout.NORTH);
        //container.add(new JScrollPane(cat_tree), BorderLayout.CENTER);

        setJMenuBar(menuBar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 480);
        setVisible(true);


    }// </editor-fold>

    public final void pop_tree() {
        try {

            List<Report> list =  reportDAO.findAll();
            Object hierarchy[] = list.toArray();
            root = processHierarchy(hierarchy);
            treeModel = new DefaultTreeModel(root);
            cat_tree.setModel(treeModel);
            cat_tree.addTreeSelectionListener(listener());

        } catch (Exception e) {
        }

    }

    public DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Заппросы");
        try {
            DefaultMutableTreeNode child, grandchild;
            List<Report> list = reportDAO.findAll();
            for (Report rp : list) {
                //child = new DefaultMutableTreeNode(rp.getNameReport());
                child = new DefaultMutableTreeNode(rp);
                //child.
                node.add(child);

                List<SubReport> subList = subReportDAO.findParentId(rp.getId());
                for (SubReport srp : subList) {
                    //grandchild = new DefaultMutableTreeNode(srp.getNameReport());
                    grandchild = new DefaultMutableTreeNode(srp);
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
                //JOptionPane.showMessageDialog(DisplayReportTree.this, "You chose the letter: ");
                try {
                        Connection conn = sqLiteJDBC.getConn();
                        Statement statement = conn.createStatement();
                        ResultSet rs = statement.executeQuery(query);
                        per_table.setModel(buildTableModel(rs));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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
                treeModel.insertNodeInto(new DefaultMutableTreeNode(newuzl), root, 0);
            }
        });
        pm.add(nwusel);

        JMenuItem nwreport = new JMenuItem("Новый отчет");
        nwreport.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newRep = JOptionPane.showInputDialog(DisplayReportTree.this,"<html><h2>Новый отчет");
                DefaultMutableTreeNode parent = getSelectedNode();
                if(parent == null) {
                    JOptionPane.showMessageDialog(DisplayReportTree.this, "Select ar era.","Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    newReport(newRep);
                    treeModel.insertNodeInto(new DefaultMutableTreeNode(newRep), parent, parent.getChildCount());
                }
            }
        });
        pm.add(nwreport);
        pm.addSeparator();
        JMenuItem nwQuery = new JMenuItem("SQL запрос");
        nwQuery.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

               QueryEdit dialog = new QueryEdit(DisplayReportTree.this);
               dialog.setQuery(query);
                //dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
                if(dialog.isModalOk()) {
                    System.out.println("" + dialog.getQuery());
                    updateReport(dialog.getQuery());
                }
            }
        });
        pm.add(nwQuery);


        return pm;
    }

    private DefaultMutableTreeNode getSelectedNode() {
        return (DefaultMutableTreeNode) cat_tree.getLastSelectedPathComponent();
    }

    //Отклик на Выбор Tree
    TreeSelectionListener listener() {

        TreeSelectionListener objTreeListener = new TreeSelectionListener()
        {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) cat_tree.getLastSelectedPathComponent();

                if (node == null)
                    // Nothing is selected.
                    return;

                Object nodeInfo = node.getUserObject();
                if (nodeInfo instanceof Report) {
                    Report report = (Report) nodeInfo;
                    idParent = report.getId();
                    System.out.println("Report:"+ report.getNameReport() + " id " + report.getId());
                }
                if (nodeInfo instanceof SubReport) {
                    SubReport subReport = (SubReport) nodeInfo;
                    System.out.println("Report:"+ subReport.getNameReport() + " id " + subReport.getId() + " query:" + subReport.getQuery());
                    idCurrChildren = subReport.getId();
                    query = subReport.getQuery();
                    Object parentInfo = node.getParent();
                    if (parentInfo instanceof Report) {
                        Report parentReport = (Report) parentInfo;
                        idParent = parentReport.getId();
                    }
                }

                /*if (node.isLeaf()) {
                    System.out.println("THE ROOT NODE IS: "+node.getParent().toString()+" CHILD NODE IS: "+nodeInfo.toString());
                } else {

                }*/
            }
        };
        return objTreeListener;
    }

    private void newUsel(String nameUsel) {
       Report report = new Report(1, nameUsel);
       reportDAO.insert(report);
   }

    private void newReport(String nameReport) {
        SubReport subReport = new SubReport();
        subReport.setParentId(idParent);
        subReport.setNameReport(nameReport);
        subReport.setQuery("SELECT * FROM SUBREPORT");
        subReportDAO.insert(subReport);
    }

    private void updateReport(String query) {
        SubReport subReport = new SubReport();
        subReport.setId(idCurrChildren);
        subReport.setQuery(query);
        subReportDAO.update(subReport);
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

    static class MenuActionListener implements ActionListener {
        public void actionPerformed (ActionEvent actionEvent) {
            System.out.println ("Selected: " + actionEvent.getActionCommand());
            if(actionEvent.getActionCommand() == "Exit") {
                System.exit(0);
            }
        }
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        new DisplayReportTree();
    }

}
