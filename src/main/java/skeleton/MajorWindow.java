package skeleton;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class MajorWindow extends JFrame {

    private JMenuBar menuBar;
    private JDesktopPane desktopPane;

    SQLiteJDBC sqLiteJDBC;

    public MajorWindow() throws SQLException, ClassNotFoundException {
        super("Report Builder");
        sqLiteJDBC = new SQLiteJDBC();

        //Рабочий стол
        desktopPane = new JDesktopPane();

        ActionListener menuListener = new MajorWindow.MenuActionListener();

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
        JMenuItem newReportItem = new JMenuItem("Построитель", KeyEvent.VK_R);
        newReportItem.addActionListener(menuListener);
        reports.add(newReportItem);

        Container container = getContentPane();
        container.add(desktopPane, BorderLayout.CENTER);

        Toolkit toolkit = getToolkit();
        Dimension dimension = toolkit.getScreenSize();

        setBounds(100, 100, dimension.width - 200, dimension.height - 200);

        setJMenuBar(menuBar);
        //setJMenuBar(menuReport);
        setVisible(true);

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
                    System.out.println("Ok");
                }
            }


            if(actionEvent.getActionCommand() == "Построитель") {
                System.out.println("Построитель");
                try {
                    DisplayReportTree displayReportTree = new DisplayReportTree();

                    desktopPane.add(displayReportTree);

                    /*displayReportTree.addInternalFrameListener(new InternalFrameAdapter() {
                        @Override
                        public void internalFrameActivated(InternalFrameEvent e) {
                            super.internalFrameActivated(e);

                        }
                    });*/

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


        }
    }


}
