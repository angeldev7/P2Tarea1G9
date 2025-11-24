package Vista;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelPila extends JPanel {
    private static final long serialVersionUID = 1L;

    public DefaultListModel<String> listModel;
    public JList<String> list;
    public JButton btnPop;

    public PanelPila() {
        setBackground(new Color(45, 45, 45));
        setBorder(new TitledBorder(new LineBorder(new Color(90, 90, 90)), " Pila (LIFO) ", TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.BOLD, 14), new Color(220, 220, 220)));

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setBackground(new Color(55, 55, 55));
        list.setForeground(new Color(220, 220, 220));
        list.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(list);
        scroll.getViewport().setBackground(new Color(55, 55, 55));
        scroll.setBorder(new LineBorder(new Color(90, 90, 90)));

    btnPop = new JButton("⬆ Desapilar Tope");
    styleButton(btnPop, new Color(255, 102, 0));
    btnPop.setToolTipText("Remueve y retorna la película del tope (última apilada) de la Pila (LIFO)");
    list.setToolTipText("El tope de la Pila es el PRIMER elemento mostrado; orden: tope -> fondo");

        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                        .addComponent(btnPop, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(15)
                    .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addGap(12)
                    .addComponent(btnPop, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                    .addGap(15))
        );
        setLayout(layout);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new LineBorder(new Color(90, 90, 90), 2));
    }
}


