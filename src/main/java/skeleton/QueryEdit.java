package skeleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class QueryEdit extends JDialog {
    private JTextArea jta = new JTextArea();
    private String query;
    private boolean modalOk;

    public QueryEdit(JFrame owner) {

        super(owner, "SQL edit text", true);
        modalOk = false;

        // ------------------------------------------
        // добавление и настройка компонент
        Container c = getContentPane(); // клиентская область окна
        c.setLayout(new BorderLayout()); // выбираем компоновщик
        // метку наверх
        c.add(new JLabel("Edit SQL"), BorderLayout.NORTH);
        // две кнопки в дополнительную панель
        JPanel jp_button = new JPanel();
        jp_button.setLayout(new BorderLayout());

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


        // добавляем панель вниз
        c.add(jp_button, BorderLayout.SOUTH);

        // помещаем текст. поле в область прокрутки
        // а область прокрутки в центр окна,
        // BorderLayout.CENTER значение по умолчанию
        c.add(new JScrollPane(jta));
        jta.setLineWrap(true); // автоматический перенос строк
        // всплывающая подсказка
        jta.setToolTipText("this is simple text editor");
        // -------------------------------------------
        // настройка окна

        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        //pack();
        setModal(true);
        setLocationRelativeTo(owner);
        setSize(640, 480);
        //setVisible(true);
    }

    public String getQuery() {
        query = jta.getText();
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        jta.setText(query);
    }

    @Override
    public boolean isModal() {
        return super.isModal();
    }

    public boolean isModalOk() {
        return modalOk;
    }

    /*public static void main(String[] args) {
        new QueryEdit();
    }*/

}
