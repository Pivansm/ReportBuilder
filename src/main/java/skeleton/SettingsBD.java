package skeleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SettingsBD extends JDialog {

    private boolean modalOk;

    public SettingsBD(JFrame owner) {

        super(owner, "Настройка", true);

        JPanel jp_setting = new JPanel();
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

        JButton jbtCancel = new JButton("Cancel");
        jbtCancel.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }); // назначаем обработчик события

        Container box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(jbtOk);
        box.add(Box.createHorizontalGlue());
        box.add(jbtCancel);
        jp_button.add(box, BorderLayout.EAST);

        // добавление и настройка компонент
        Container c = getContentPane(); // клиентская область окна
        c.setLayout(new BorderLayout()); // выбираем компоновщик

        c.add(jp_setting, BorderLayout.NORTH);
        c.add(jp_button, BorderLayout.SOUTH);

        setModal(true);
        setSize(480, 320);

    }

    public boolean isModalOk() {
        return modalOk;
    }
}
