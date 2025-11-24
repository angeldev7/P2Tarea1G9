package Vista;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import Modelo.ListaEnlazada;
import Modelo.Pila;
import Modelo.Cola;
import Modelo.Pelicula;
import java.awt.*;

public class DialogoDemoEstructuras extends JDialog {
    private static final long serialVersionUID = 1L;

    private ListaEnlazada list;
    private Pila Pila;
    private Cola Cola;

    private DefaultListModel<String> listModel;
    private DefaultListModel<String> stackModel;
    private DefaultListModel<String> queueModel;

    private JList<String> listView;
    private JList<String> stackView;
    private JList<String> queueView;

    private JLabel lblListSize;
    private JLabel lblStackSize;
    private JLabel lblQueueSize;
    private JTextArea txtLog;

    public DialogoDemoEstructuras(Frame owner, ListaEnlazada list, Pila Pila, Cola Cola) {
        super(owner, "🎓 Demo de Estructuras de Datos - Trabajando con Datos Reales", true);
        this.list = list;
        this.Pila = Pila;
        this.Cola = Cola;

        setSize(900, 650);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(15, 15));

        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        mainPanel.setBackground(new Color(30, 30, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainPanel.add(createListPanel());
        mainPanel.add(createStackPanel());
        mainPanel.add(createQueuePanel());

        add(mainPanel, BorderLayout.CENTER);

        // Status bar with log
        JPanel statusBar = new JPanel(new BorderLayout(10, 10));
        statusBar.setBackground(new Color(35, 35, 35));
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel counters = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        counters.setBackground(new Color(35, 35, 35));
        lblListSize = styledLabel("Lista: " + list.getTamanio());
        lblStackSize = styledLabel("Pila: " + Pila.tamanio());
        lblQueueSize = styledLabel("Cola: " + Cola.tamanio());
        counters.add(lblListSize);
        counters.add(lblStackSize);
        counters.add(lblQueueSize);

        txtLog = new JTextArea(3, 80);
        txtLog.setEditable(false);
        txtLog.setBackground(new Color(45, 45, 45));
        txtLog.setForeground(new Color(200, 200, 200));
        txtLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        txtLog.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(70, 70, 70)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JScrollPane logScroll = new JScrollPane(txtLog);
        logScroll.setBorder(new LineBorder(new Color(70, 70, 70)));

        statusBar.add(counters, BorderLayout.NORTH);
        statusBar.add(logScroll, BorderLayout.CENTER);

        add(statusBar, BorderLayout.SOUTH);

        updateViews();
        log("Demo abierto. Todas las estructuras comparten los mismos datos del catálogo principal.");
    }

    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(new TitledBorder(new LineBorder(new Color(90, 90, 90)), " 📚 Catálogo (ListaEnlazada) ", TitledBorder.CENTER, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.BOLD, 14), new Color(220, 220, 220)));

        listModel = new DefaultListModel<>();
        listView = createStyledList(listModel, "Seleccione una película para agregar a Vistas Recientemente (Pila) o Por Ver (Cola FIFO)");

        JButton btnMarkAsWatched = styledButton("👁️ Marcar como Vista", new Color(102, 16, 242));
        JButton btnAddToWatchlist = styledButton("⏱️ Agregar a Por Ver", new Color(23, 162, 184));
        JButton btnRemove = styledButton("🗑️ Eliminar del Catálogo", new Color(220, 53, 69));

        btnMarkAsWatched.setToolTipText("Agregar película seleccionada a Vistas Recientemente (permanece en catálogo)");
        btnAddToWatchlist.setToolTipText("Agregar película seleccionada a Por Ver (permanece en catálogo)");
        btnRemove.setToolTipText("Eliminar permanentemente película seleccionada del catálogo");

        btnMarkAsWatched.addActionListener(e -> {
            int selectedIndex = listView.getSelectedIndex();
            if (selectedIndex >= 0) {
                Pelicula[] movies = list.obtenerTodasLasPeliculas();
                if (selectedIndex < movies.length) {
                    Pelicula m = movies[selectedIndex];
                    Pila.apilar(m);
                    updateViews();
                    log("CATÁLOGO → Pila: '" + m.getTitulo() + "' marcada como vista (agregada a vistas recientemente)");
                }
            } else {
                JOptionPane.showMessageDialog(this, "¡Por favor seleccione una película del catálogo primero!", "Sin Selección", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnAddToWatchlist.addActionListener(e -> {
            int selectedIndex = listView.getSelectedIndex();
            if (selectedIndex >= 0) {
                Pelicula[] movies = list.obtenerTodasLasPeliculas();
                if (selectedIndex < movies.length) {
                    Pelicula m = movies[selectedIndex];
                    Cola.encolar(m);
                    updateViews();
                    log("CATÁLOGO → Cola: '" + m.getTitulo() + "' agregada a por ver (se verá después)");
                }
            } else {
                JOptionPane.showMessageDialog(this, "¡Por favor seleccione una película del catálogo primero!", "Sin Selección", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnRemove.addActionListener(e -> {
            int selectedIndex = listView.getSelectedIndex();
            if (selectedIndex >= 0) {
                Pelicula[] movies = list.obtenerTodasLasPeliculas();
                if (selectedIndex < movies.length) {
                    Pelicula m = movies[selectedIndex];
                    list.eliminarPorTitulo(m.getTitulo());
                    updateViews();
                    log("CATÁLOGO: Eliminada '" + m.getTitulo() + "' permanentemente del catálogo");
                }
            } else {
                JOptionPane.showMessageDialog(this, "¡Por favor seleccione una película del catálogo primero!", "Sin Selección", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        btnPanel.setBackground(new Color(45, 45, 45));
        btnPanel.add(btnMarkAsWatched);
        btnPanel.add(btnAddToWatchlist);
        btnPanel.add(btnRemove);

        panel.add(new JScrollPane(listView), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStackPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(new TitledBorder(new LineBorder(new Color(90, 90, 90)), " 👁️ Vistas Recientemente (Pila LIFO) ", TitledBorder.CENTER, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.BOLD, 14), new Color(220, 220, 220)));

        stackModel = new DefaultListModel<>();
        stackView = createStyledList(stackModel, "Más reciente arriba (Último en Entrar, Primero en Salir). Historial de películas vistas.");

        JButton btnViewAgain = styledButton("🔄 Ver Más Reciente Otra Vez", new Color(0, 123, 255));
        JButton btnClearHistory = styledButton("🗑️ Limpiar Historial", new Color(220, 53, 69));

        btnViewAgain.setToolTipText("Remover película más reciente del historial (LIFO - operación pop)");
        btnClearHistory.setToolTipText("Limpiar todo el historial de vistas");

        btnViewAgain.addActionListener(e -> {
            Pelicula m = Pila.desapilar();
            if (m != null) {
                updateViews();
                log("Pila: Removida '" + m.getTitulo() + "' del historial (operación LIFO pop)");
                JOptionPane.showMessageDialog(this, 
                    "Removida del historial: " + m.getTitulo() + "\n(La película sigue en el catálogo)", 
                    "Removida del Historial", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡El historial de vistas está vacío!", "Vacío", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnClearHistory.addActionListener(e -> {
            if (!Pila.estaVacia()) {
                int count = 0;
                while (!Pila.estaVacia()) {
                    Pila.desapilar();
                    count++;
                }
                updateViews();
                log("Pila: Limpiado todo el historial (" + count + " películas removidas)");
                JOptionPane.showMessageDialog(this, 
                    "¡Historial limpiado! (" + count + " entradas removidas)", 
                    "Historial Limpiado", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡El historial ya está vacío!", "Vacío", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        btnPanel.setBackground(new Color(45, 45, 45));
        btnPanel.add(btnViewAgain);
        btnPanel.add(btnClearHistory);

        panel.add(new JScrollPane(stackView), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createQueuePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(45, 45, 45));
        panel.setBorder(new TitledBorder(new LineBorder(new Color(90, 90, 90)), " ⏱️ Cola Por Ver (Cola FIFO) ", TitledBorder.CENTER, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.BOLD, 14), new Color(220, 220, 220)));

        queueModel = new DefaultListModel<>();
        queueView = createStyledList(queueModel, "Primera agregada arriba (Primero en Entrar, Primero en Salir). Películas por ver en orden.");

        JButton btnWatchNext = styledButton("▶️ Ver Siguiente Película", new Color(40, 167, 69));
        JButton btnRemoveFromQueue = styledButton("❌ Remover Siguiente de Cola", new Color(255, 193, 7));
        JButton btnClearQueue = styledButton("🗑️ Limpiar Toda la Cola", new Color(220, 53, 69));

        btnWatchNext.setToolTipText("Ver siguiente película en Cola (FIFO - desencolar) y agregar al historial");
        btnRemoveFromQueue.setToolTipText("Remover siguiente película de Cola sin verla");
        btnClearQueue.setToolTipText("Limpiar toda la cola por ver");

        btnWatchNext.addActionListener(e -> {
            Pelicula m = Cola.desencolar();
            if (m != null) {
                Pila.apilar(m);
                updateViews();
                log("Cola → Pila: Vista '" + m.getTitulo() + "' (FIFO desencolar → agregada al historial)");
                JOptionPane.showMessageDialog(this, 
                    "Viendo ahora: " + m.getTitulo() + "\n¡Agregada al historial!\n(La película sigue en el catálogo)", 
                    "Viendo Película", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡La cola por ver está vacía!", "Vacío", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnRemoveFromQueue.addActionListener(e -> {
            Pelicula m = Cola.desencolar();
            if (m != null) {
                updateViews();
                log("Cola: Removida '" + m.getTitulo() + "' de cola por ver (FIFO desencolar)");
                JOptionPane.showMessageDialog(this, 
                    "Removida de Cola: " + m.getTitulo() + "\n(La película sigue en el catálogo)", 
                    "Removida de Cola", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡La cola por ver está vacía!", "Vacío", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnClearQueue.addActionListener(e -> {
            if (!Cola.estaVacia()) {
                int count = 0;
                while (!Cola.estaVacia()) {
                    Cola.desencolar();
                    count++;
                }
                updateViews();
                log("Cola: Limpiada toda la cola por ver (" + count + " películas removidas)");
                JOptionPane.showMessageDialog(this, 
                    "¡Cola limpiada! (" + count + " películas removidas)", 
                    "Cola Limpiada", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "¡La cola ya está vacía!", "Vacío", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        btnPanel.setBackground(new Color(45, 45, 45));
        btnPanel.add(btnWatchNext);
        btnPanel.add(btnRemoveFromQueue);
        btnPanel.add(btnClearQueue);

        panel.add(new JScrollPane(queueView), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JList<String> createStyledList(DefaultListModel<String> model, String tooltip) {
        JList<String> list = new JList<>(model);
        list.setBackground(new Color(55, 55, 55));
        list.setForeground(new Color(220, 220, 220));
        list.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        list.setToolTipText(tooltip);
        return list;
    }

    private JButton styledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new LineBorder(bgColor.darker(), 2));
        return btn;
    }

    private JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(220, 220, 220));
        l.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        return l;
    }

    private void updateViews() {
        listModel.clear();
        for (Pelicula m : list.obtenerTodasLasPeliculas()) {
            listModel.addElement(m.toString());
        }

        stackModel.clear();
        for (Pelicula m : Pila.aArray()) {
            stackModel.addElement(m.toString());
        }

        queueModel.clear();
        for (Pelicula m : Cola.aArray()) {
            queueModel.addElement(m.toString());
        }

        lblListSize.setText("Lista: " + list.getTamanio());
        lblStackSize.setText("Pila: " + Pila.tamanio());
        lblQueueSize.setText("Cola: " + Cola.tamanio());
    }

    private void log(String message) {
        String timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        txtLog.append("[" + timestamp + "] " + message + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }
}




