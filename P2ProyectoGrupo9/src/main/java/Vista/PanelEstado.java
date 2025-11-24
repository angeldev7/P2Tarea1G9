package Vista;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelEstado extends JPanel {
    private static final long serialVersionUID = 1L;

    public JLabel lblTamanioLista;
    public JLabel lblTamanioPila;
    public JLabel lblTamanioCola;
    public JLabel lblUltimaOperacion;
    public JTextArea txtLog;

    public PanelEstado() {
        setBackground(new Color(35, 35, 35));
        setBorder(new TitledBorder(new LineBorder(new Color(90, 90, 90)), " Estado de Estructuras de Datos ", TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.BOLD, 14), new Color(220, 220, 220)));

        lblTamanioLista = styledLabel("Tamaño Lista: 0");
        lblTamanioPila = styledLabel("Tamaño Pila: 0");
        lblTamanioCola = styledLabel("Tamaño Cola: 0");
        lblUltimaOperacion = styledLabel("Última: (ninguna)");

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        txtLog.setBackground(new Color(45, 45, 45));
        txtLog.setForeground(new Color(210, 210, 210));
        txtLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        txtLog.setBorder(new LineBorder(new Color(70, 70, 70)));
        JScrollPane scroll = new JScrollPane(txtLog);
        scroll.setBorder(new LineBorder(new Color(70, 70, 70)));

        JLabel lblLogTitle = styledLabel("Registro de Operaciones (más reciente primero):");
        lblLogTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(15)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblTamanioLista)
                            .addGap(30)
                            .addComponent(lblTamanioPila)
                            .addGap(30)
                            .addComponent(lblTamanioCola)
                            .addGap(30)
                            .addComponent(lblUltimaOperacion))
                        .addComponent(lblLogTitle)
                        .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE))
                    .addGap(15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTamanioLista)
                        .addComponent(lblTamanioPila)
                        .addComponent(lblTamanioCola)
                        .addComponent(lblUltimaOperacion))
                    .addGap(10)
                    .addComponent(lblLogTitle)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addGap(12))
        );
        setLayout(layout);
    }

    private JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(220, 220, 220));
        l.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        return l;
    }
}

