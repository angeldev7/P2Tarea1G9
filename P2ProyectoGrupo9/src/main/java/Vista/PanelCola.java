package Vista;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelCola extends JPanel {
    private static final long serialVersionUID = 1L;

    public DefaultListModel<String> listModel;
    public JList<String> list;
    public JButton btnDequeue;

    public PanelCola() {
        setBackground(new Color(45, 45, 45));
        setBorder(new TitledBorder(new LineBorder(new Color(90, 90, 90)), " Cola (FIFO) ", TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.BOLD, 14), new Color(220, 220, 220)));

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setBackground(new Color(55, 55, 55));
        list.setForeground(new Color(220, 220, 220));
        list.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(list);
        scroll.getViewport().setBackground(new Color(55, 55, 55));
        scroll.setBorder(new LineBorder(new Color(90, 90, 90)));

    btnDequeue = new JButton("➡ Desencolar Frente");
    styleButton(btnDequeue, new Color(0, 123, 255));
    btnDequeue.setToolTipText("Remueve y retorna la película del frente (más antigua) de la Cola (FIFO)");
    list.setToolTipText("El frente de la Cola es el PRIMER elemento mostrado; orden: frente -> final");

        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(20)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                        .addComponent(btnDequeue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(15)
                    .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addGap(12)
                    .addComponent(btnDequeue, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
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


