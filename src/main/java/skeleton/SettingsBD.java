package skeleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;

public class SettingsBD extends JDialog {
    Setting setting;
    private String connectJDBC;
    private Connection connection;
    SettingsDAO settingsDAO;
    private boolean modalOk;

    public SettingsBD(JFrame owner, Connection connection) {

        super(owner, "Настройка", true);
        setting = new Setting();
        this.connection = connection;
        settingsDAO = new SettingsDAO(connection);

        if(!settingsDAO.isTable("SETTING"))
            createSettingsDB();

        JPanel jp_setting = new JPanel();
        jp_setting.setLayout(new GridLayout(4, 2));

        JLabel label = new JLabel("Сервер");
        JTextField textField = new JTextField();
        label.setDisplayedMnemonic(KeyEvent.VK_U);
        label.setLabelFor(textField);

        JLabel labelBase = new JLabel("База");
        JTextField textFieldBase = new JTextField();
        labelBase.setDisplayedMnemonic(KeyEvent.VK_U);
        labelBase.setLabelFor(textFieldBase);

        JLabel labelUser = new JLabel("Username");
        JTextField textFieldUser = new JTextField();
        labelUser.setDisplayedMnemonic(KeyEvent.VK_N);
        labelUser.setLabelFor(textFieldUser);

        JLabel labelPass = new JLabel("Password");
        JTextField textFieldPass = new JTextField();
        labelPass.setDisplayedMnemonic(KeyEvent.VK_P);
        labelPass.setLabelFor(textFieldPass);

        jp_setting.add(label, 0, 0);
        jp_setting.add(textField, 0, 1);
        jp_setting.add(labelBase, 1, 0);
        jp_setting.add(textFieldBase, 1, 1);
        jp_setting.add(labelUser, 2, 0);
        jp_setting.add(textFieldUser, 2, 1);
        jp_setting.add(labelPass, 3, 0);
        jp_setting.add(textFieldPass, 3, 1);

        //Create the radio buttons.
        JRadioButton postgresButton = new JRadioButton("Postgres");
        postgresButton.setMnemonic(KeyEvent.VK_G);
        postgresButton.setSelected(true);
        JRadioButton oracleButton = new JRadioButton("Oracle");
        oracleButton.setMnemonic(KeyEvent.VK_O);
        JRadioButton mssqlButton = new JRadioButton("MsSql");
        mssqlButton.setMnemonic(KeyEvent.VK_M);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(postgresButton);
        group.add(oracleButton);
        group.add(mssqlButton);

        postgresButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setting.setTypeJDBC("jdbc:postgresql");
            }
        });

        oracleButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setting.setTypeJDBC("jdbc:oracle");
            }
        });

        mssqlButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setting.setTypeJDBC("jdbc:sqlserver");
            }
        });


        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(postgresButton);
        radioPanel.add(oracleButton);
        radioPanel.add(mssqlButton);


        JPanel jp_button = new JPanel();
        jp_button.setLayout(new BorderLayout());
        jp_button.setSize(30, 40);

        JButton jbtOk = new JButton("Ok");
        jbtOk.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                modalOk = true;
            }
        }); // назначаем обработчик события

        JButton jbtSave = new JButton("Save");
        jbtSave.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        }); // назначаем обработчик события

        JButton jbtCancel = new JButton("Cancel");
        jbtCancel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }); // назначаем обработчик события

        Container box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(jbtSave);
        box.add(Box.createHorizontalGlue());
        box.add(jbtOk);
        box.add(Box.createHorizontalGlue());
        box.add(jbtCancel);
        jp_button.add(box, BorderLayout.EAST);

        // добавление и настройка компонент
        Container c = getContentPane(); // клиентская область окна
        c.setLayout(new BorderLayout()); // выбираем компоновщик

        c.add(jp_setting, BorderLayout.NORTH);
        c.add(radioPanel, BorderLayout.CENTER);
        c.add(jp_button, BorderLayout.SOUTH);

        setModal(true);
        setLocationRelativeTo(owner);
        setSize(480, 260);

    }

    public boolean isModalOk() {
        return modalOk;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void createSettingsDB() {
        if(settingsDAO.create()) {
            System.out.println("Ok!");
        }
    }
}
