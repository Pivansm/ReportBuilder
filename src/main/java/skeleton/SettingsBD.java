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
    private boolean settingUpdate;

    public SettingsBD(JFrame owner, Connection connection) {

        super(owner, "Настройка", true);
        setting = new Setting();
        this.connection = connection;
        settingsDAO = new SettingsDAO(connection);
        settingUpdate = true;
        if(!settingsDAO.isTable("SETTING"))
            createSettingsDB();
        else {
            setting = settingsDAO.findEntityByCurrent();
        }

        JPanel jp_setting = new JPanel();
        jp_setting.setLayout(new GridLayout(4, 2));

        JLabel label = new JLabel("Сервер");
        JTextField textField = new JTextField(setting.getServerName());
        label.setDisplayedMnemonic(KeyEvent.VK_U);
        label.setLabelFor(textField);

        JLabel labelBase = new JLabel("База");
        JTextField textFieldBase = new JTextField(setting.getBaseName());
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

        jp_setting.add(label);
        jp_setting.add(textField);
        jp_setting.add(labelBase);
        jp_setting.add(textFieldBase);
        jp_setting.add(labelUser);
        jp_setting.add(textFieldUser);
        jp_setting.add(labelPass);
        jp_setting.add(textFieldPass);

        //Create the radio buttons.
        JRadioButton postgresButton = new JRadioButton("Postgres");
        postgresButton.setMnemonic(KeyEvent.VK_G);

        JRadioButton oracleButton = new JRadioButton("Oracle");
        oracleButton.setMnemonic(KeyEvent.VK_O);
        JRadioButton mssqlButton = new JRadioButton("MsSql");
        mssqlButton.setMnemonic(KeyEvent.VK_M);
        JRadioButton ownerButton = new JRadioButton("Owner");
        ownerButton.setMnemonic(KeyEvent.VK_W);
        ownerButton.setSelected(true);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(postgresButton);
        group.add(oracleButton);
        group.add(mssqlButton);
        group.add(ownerButton);

        postgresButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setting.setTypeJDBC("jdbc:postgresql");
                Setting settingPostgres = settingsDAO.findEntityByName(setting.getTypeJDBC());
                settingUpdate = false;
                if(settingPostgres != null) {
                    textField.setText(settingPostgres.getServerName());
                    textFieldBase.setText(settingPostgres.getBaseName());
                    settingUpdate = true;
                }
            }
        });

        oracleButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setting.setTypeJDBC("jdbc:oracle");
                Setting settingOracle = settingsDAO.findEntityByName(setting.getTypeJDBC());
                settingUpdate = false;
                if(settingOracle != null) {
                    textField.setText(settingOracle.getServerName());
                    textFieldBase.setText(settingOracle.getBaseName());
                    settingUpdate = true;
                }
            }
        });

        mssqlButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setting.setTypeJDBC("jdbc:sqlserver");
                Setting settingMssql = settingsDAO.findEntityByName(setting.getTypeJDBC());
                settingUpdate = false;
                if(settingMssql != null) {
                    textField.setText(settingMssql.getServerName());
                    textFieldBase.setText(settingMssql.getBaseName());
                    settingUpdate = true;
                }
            }
        });

        ownerButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setting.setTypeJDBC("jdbc:sqlite");
                settingUpdate = false;
                textField.setText(null);
                textFieldBase.setText(null);
                Setting settingOwner = settingsDAO.findEntityByName(setting.getTypeJDBC());
                connectJDBC ="" + setting.getTypeJDBC() + ":" + settingOwner.getBaseName();
                if(settingOwner != null) {
                    textField.setText(settingOwner.getServerName());
                    textFieldBase.setText(settingOwner.getBaseName());
                    settingUpdate = true;
                }
            }
        });


        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(postgresButton);
        radioPanel.add(oracleButton);
        radioPanel.add(mssqlButton);
        radioPanel.add(ownerButton);


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
                Setting settingSave = new Setting();
                settingSave.setTypeJDBC(setting.getTypeJDBC());
                settingSave.setServerName(textField.getText());
                settingSave.setBaseName(textFieldBase.getText());
                if(settingUpdate) {
                    settingsDAO.update(settingSave);
                }
                else
                    settingsDAO.insert(settingSave);
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

    public String getConnectJDBC() {
        return connectJDBC;
    }

    public Setting getSetting() {
        return setting;
    }

}
