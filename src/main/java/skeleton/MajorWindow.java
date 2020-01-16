package skeleton;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class MajorWindow extends JFrame {

    private String reportConnect;
    private JMenuBar menuBar;
    private JPanel jp_status;
    private JLabel lb_status;
    private JDesktopPane desktopPane;

    Action newReport;

    SQLiteJDBC sqLiteJDBC;
    SettingsDAO settingsDAO;
    private Setting setting;

    public MajorWindow() throws SQLException, ClassNotFoundException {
        super("Report Builder");
        sqLiteJDBC = new SQLiteJDBC();
        settingsDAO = new SettingsDAO(sqLiteJDBC.getConn());
        setting = settingsDAO.findEntityByCurrent();
        setReportConnect(setting);

        //Рабочий стол
        desktopPane = new JDesktopPane();

        ActionListener menuListener = new MajorWindow.MenuActionListener();
        newReport = new NewReport();

        //Меню с настройками
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

        //Меню с отчетом
        JMenu reports = new JMenu("Отчеты");
        reports.setMnemonic(KeyEvent.VK_O);
        menuBar.add(reports);
        //JMenuItem newReportItem = new JMenuItem("Построитель", KeyEvent.VK_R);
        //newReportItem.addActionListener(menuListener);
        reports.add(newReport);

        /*try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.metal.MetalLookAndFeel");
        }
        catch(Exception e){e.printStackTrace();};*/

        Container container = getContentPane();
        container.add(desktopPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //super.windowClosing(e);
                shutDown();
            }
        });

        //Строка состояния
        jp_status = new JPanel();
        jp_status.setLayout(new BorderLayout());
        lb_status = new JLabel(reportConnect);
        jp_status.add(lb_status, BorderLayout.WEST);
        jp_status.setBorder(new BevelBorder(BevelBorder.LOWERED));
        container.add(jp_status, BorderLayout.SOUTH);

        Toolkit toolkit = getToolkit();
        Dimension dimension = toolkit.getScreenSize();

        setBounds(100, 100, dimension.width - 200, dimension.height - 200);

        setJMenuBar(menuBar);
        //setJMenuBar(menuReport);
        setVisible(true);

    }

    private void shutDown() {
        System.exit(0);
    }

    private class MenuActionListener implements ActionListener {
        public void actionPerformed (ActionEvent actionEvent) {
            System.out.println ("Selected: " + actionEvent.getActionCommand());
            if(actionEvent.getActionCommand() == "Exit") {

                System.exit(0);
            }

            if(actionEvent.getActionCommand() == "Настройка") {

                SettingsBD dialogSetting = new SettingsBD(MajorWindow.this, sqLiteJDBC.getConn());

                dialogSetting.setVisible(true);
                if (dialogSetting.isModalOk()) {

                    reportConnect = dialogSetting.getConnectJDBC();
                    setting = dialogSetting.getSetting();

                    System.out.println("Ok: " +  reportConnect);
                }
            }

        }
    }

    private DisplayReportTree createDisplayReport() throws SQLException, ClassNotFoundException {

        DisplayReportTree displayReportTree = new DisplayReportTree(setting);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        displayReportTree.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                //super.internalFrameActivated(e);
                System.out.println("Activate Windows!");
                newReport.setEnabled(true);
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                //super.internalFrameDeactivated(e);
                System.out.println("Deactivated Windows!");
                newReport.setEnabled(false);
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                super.internalFrameClosed(e);
                //newReport.setEnabled(false);
                //newReport.dispose();
            }
        });

        return displayReportTree;
    }

    private class NewReport extends AbstractAction {

        public NewReport() {
           putValue(NAME, "Построитель");
           putValue(SHORT_DESCRIPTION, "Построитель отчетов");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                DisplayReportTree displayReportTree = new DisplayReportTree(setting);
                //setDefaultCloseOperation(EXIT_ON_CLOSE);
                desktopPane.add(displayReportTree);

                displayReportTree.setVisible(true);

            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        }
    }

    public String getReportConnect() {
        return reportConnect;
    }

    private void setReportConnect(Setting setting) {

        reportConnect = "" + setting.getTypeJDBC() + ":";
        if(!setting.getServerName().equals(""))
            reportConnect += "" + setting.getServerName();
        if(setting.getBaseName() != null)
            reportConnect += "" + setting.getBaseName();
    }
}
