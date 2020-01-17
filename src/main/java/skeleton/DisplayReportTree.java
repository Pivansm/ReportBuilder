package skeleton;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.Style;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.List;
import java.util.Vector;

public class DisplayReportTree extends JInternalFrame {

    private final String STYLE_heading = "heading", STYLE_normal = "normal", FONT_style = "Times New Roman";

    Connection connReport = null;
    Statement stm = null;
    private Setting setting;

    PerformQuery performQuery;

    private JToolBar toolbar;
    private JTree cat_tree;
    private JTable per_table;
    private JPanel jp_status;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JPopupMenu popupMenu;
    private JLabel lb_status;
    private int idParent;
    private int idCurrChildren;
    private String query;

    SQLiteJDBC sqLiteJDBC;
    ReportDAO reportDAO;
    SubReportDAO subReportDAO;

    public DisplayReportTree(Setting setting) throws SQLException, ClassNotFoundException {
        super("Builder", true, true);
        sqLiteJDBC = new SQLiteJDBC();
        reportDAO = new ReportDAO(sqLiteJDBC.getConn());
        subReportDAO = new SubReportDAO(sqLiteJDBC.getConn());
        this.setting = setting;
        idParent = 0;
        idCurrChildren = 0;
        query = "SELECT * FROM QUREPORT";
        initComponents();
        pop_tree();
        connectionReport();
    }


    //System generated code
    //@SuppressWarnings("unchecked")
    private void initComponents() {

        //Строка состояния
        jp_status = new JPanel();
        jp_status.setLayout(new BorderLayout());
        lb_status = new JLabel("...");
        jp_status.add(lb_status, BorderLayout.WEST);
        jp_status.setBorder(new BevelBorder(BevelBorder.LOWERED));

        cat_tree = new JTree();
        per_table = new JTable();
        toolbar = new JToolBar();

        //JButton enterButton = new JButton("Выполнить");
        performQuery = new PerformQuery();
        toolbar.add(performQuery);
        toolbar.addSeparator();
        String[] export = new String[] {"Excel(xlsx)", "Excel(xml)", "File(csv)"};
        JComboBox comboBox = new JComboBox(export);

        comboBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JComboBox box = (JComboBox)e.getSource();
                String item = (String)box.getSelectedItem();
                if(item.equals("File(csv)")) {
                    exportFileCSV("export.csv");
                }

            }
        });

        toolbar.add(comboBox);
        // Блокируем возможность перетаскивания панели
        toolbar.setFloatable(false);

        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //setTitle("Report Builder");

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
        container.add(jp_status, BorderLayout.SOUTH);
        //container.add(new JScrollPane(cat_tree), BorderLayout.CENTER);

        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 480);
        setVisible(true);

    }// </editor-fold>

    private boolean connectionReport() {
        try {

            System.out.println("" + setReportConnect(setting) + " user:" + setting.getUserName());
            if(setting.getUserName() == null)
                connReport = DriverManager.getConnection(setReportConnect(setting));
            else
                connReport = DriverManager.getConnection(setReportConnect(setting), setting.getUserName(), setting.getPassword());
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Запросы");
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
        pm.add(performQuery);
        pm.addSeparator();
        JMenuItem nwusel = new JMenuItem("Новый узел");
        nwusel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newuzl = JOptionPane.showInputDialog(DisplayReportTree.this,"<html><h2>Новый узел");
                Report report = newUsel(newuzl);
                treeModel.insertNodeInto(new DefaultMutableTreeNode(report), root, 0);
            }
        });
        pm.add(nwusel);

        JMenuItem nwreport = new JMenuItem("Новый отчет");
        nwreport.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newRep = JOptionPane.showInputDialog(DisplayReportTree.this,"<html><h2>Новый отчет", "Отчет", JOptionPane.QUESTION_MESSAGE);
                DefaultMutableTreeNode parent = getSelectedNode();
                if(parent == null) {
                    JOptionPane.showMessageDialog(DisplayReportTree.this, "Select ar era.","Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    if(newRep != null) {
                        SubReport subReport = newReport(newRep);
                        treeModel.insertNodeInto(new DefaultMutableTreeNode(subReport), parent, parent.getChildCount());
                    }
                }
            }
        });
        pm.add(nwreport);
        //Удаление узлов и отчетов
        JMenuItem delreport = new JMenuItem("Удалить");
        delreport.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int result = JOptionPane.showConfirmDialog(DisplayReportTree.this, "Удалить", "Окно подтверждения", JOptionPane.YES_NO_CANCEL_OPTION);
                DefaultMutableTreeNode parent = getSelectedNode();
                if (result == JOptionPane.YES_OPTION) {
                    if (parent == null) {
                        System.out.println("Нет дерева!");
                    } else {
                        int level =  parent.getLevel();
                        if(level > 1) {
                            delReport();
                            treeModel.removeNodeFromParent(parent);
                        }
                        if(level == 1) {
                            delParent();
                            treeModel.removeNodeFromParent(parent);
                        }
                    }
                }
            }
        });
        pm.add(delreport);

        pm.addSeparator();
        JMenuItem nwQuery = new JMenuItem("SQL запрос");
        nwQuery.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode parent = getSelectedNode();
               QueryEdit dialog = new QueryEdit(null);
               dialog.setQuery(query);
                //dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
                if(dialog.isModalOk()) {
                    System.out.println("" + dialog.getQuery());
                    query = dialog.getQuery();
                    updateReport(query);

                    Object nodeInfo = parent.getUserObject();
                    if (nodeInfo instanceof SubReport) {
                        SubReport subReport = (SubReport) nodeInfo;
                        subReport.setQuery(query);
                        parent.setUserObject(subReport);
                    }
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
                    setTitle("SQL отчет (" + report.getNameReport() + ")");
                }
                if (nodeInfo instanceof SubReport) {
                    SubReport subReport = (SubReport) nodeInfo;
                    System.out.println("Report:"+ subReport.getNameReport() + " id " + subReport.getId() + " query:" + subReport.getQuery());
                    setTitle("SQL отчет (" + subReport.getNameReport() + ")");
                    idCurrChildren = subReport.getId();
                    query = subReport.getQuery();
                    Object parentInfo = node.getParent();
                    if (parentInfo instanceof Report) {
                        Report parentReport = (Report) parentInfo;
                        idParent = parentReport.getId();
                    }
                }
            }
        };
        return objTreeListener;
    }

    private Report newUsel(String nameUsel) {
       Report report = new Report(1, nameUsel);
       return reportDAO.insert(report);
   }

    private SubReport newReport(String nameReport) {

        SubReport subReport = new SubReport();
        subReport.setParentId(idParent);
        subReport.setNameReport(nameReport);
        subReport.setQuery("SELECT * FROM SUBREPORT");

        return subReportDAO.insert(subReport);
    }

    private void updateReport(String query) {
        SubReport subReport = new SubReport();
        subReport.setId(idCurrChildren);
        subReport.setQuery(query);
        subReportDAO.update(subReport);
    }

    private void delReport() {
        SubReport subReport = new SubReport();
        subReport.setId(idCurrChildren);
        subReportDAO.delete(idCurrChildren);
    }

    private void delParent() {
        reportDAO.delete(idParent);
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

    private class PerformQuery extends AbstractAction {

        public PerformQuery() {
            putValue(NAME, "Выполнить");
            putValue(SHORT_DESCRIPTION, "Выполнить");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //Connection conn = sqLiteJDBC.getConn();

                Statement statement = connReport.createStatement();
                ResultSet rs = statement.executeQuery(query);
                if(rs.next()) {
                    TableModel model = buildTableModel(rs);
                    RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
                    per_table.setModel(model);
                    per_table.setRowSorter(sorter);
                    lb_status.setText("Количество строк: " + per_table.getRowCount());
                }
                else
                {
                    lb_status.setText("Количество строк: 0");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }


    private String setReportConnect(Setting setting) {
        String result = "";
        result = "" + setting.getTypeJDBC() + ":";
        if(!setting.getServerName().equals(""))
            result += "" + setting.getServerName();
        if(setting.getBaseName() != null)
            result += "" + setting.getBaseName();

        return result;
    }


    private void exportFileCSV(String pathToExportTo) {

        if (per_table.getRowCount() > 0) {

            try {

                TableModel model = per_table.getModel();
                FileWriter csv = new FileWriter(new File(pathToExportTo));

                for (int i = 0; i < model.getColumnCount(); i++) {
                    csv.write(model.getColumnName(i) + ";");
                }

                csv.write("\n");

                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        csv.write(model.getValueAt(i, j).toString() + ";");
                    }
                    csv.write("\n");
                }
                csv.close();

                System.out.println("Файл выгружен");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
        {
            System.out.println("Таблица пустая");
        }
    }


}
