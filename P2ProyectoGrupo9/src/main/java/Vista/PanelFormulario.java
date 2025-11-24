package Vista;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;

public class PanelFormulario extends JPanel {

	private static final long serialVersionUID = 1L;
	public JTextField txtTitulo;
	public JTextField txtDirector;
	public JTextField txtAnio;
	public JTextField txtGenero;
	public JTextArea txtSinopsis;
	public JComboBox<String> cmbPosicionInsercion;
	public JButton btnGuardar;
	public JButton btnNuevo;
	public JButton btnEliminar;
	public JButton btnMoverAlInicio;
	public JButton btnMoverAlFinal;
	public JButton btnDemoEstructuras; // opens demo dialog for Stack/Queue/List

	/**
	 * Create the panel.
	 */
	public PanelFormulario() {
		setBackground(new Color(45, 45, 45));
		setBorder(new TitledBorder(new LineBorder(new Color(90, 90, 90)), " Formulario de Película ", TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.BOLD, 14), new Color(220, 220, 220)));
		
		JLabel lblTitle = new JLabel("Titulo:");
		styleLabel(lblTitle);
		txtTitulo = new JTextField();
		styleTextField(txtTitulo);
		
		JLabel lblDirector = new JLabel("Director:");
		styleLabel(lblDirector);
		txtDirector = new JTextField();
		styleTextField(txtDirector);
		
		JLabel lblYear = new JLabel("Año:");
		styleLabel(lblYear);
		txtAnio = new JTextField();
		styleTextField(txtAnio);
		
		JLabel lblGenre = new JLabel("Género:");
		styleLabel(lblGenre);
		txtGenero = new JTextField();
		styleTextField(txtGenero);
		
		JLabel lblSynopsis = new JLabel("Sinopsis:");
		styleLabel(lblSynopsis);
		txtSinopsis = new JTextArea();
		txtSinopsis.setLineWrap(true);
		txtSinopsis.setWrapStyleWord(true);
		styleTextArea(txtSinopsis);
		JScrollPane scrollPane = new JScrollPane(txtSinopsis);
		scrollPane.setBorder(null);
		
		JLabel lblInsertPosition = new JLabel("Posición de Inserción:");
		styleLabel(lblInsertPosition);
		cmbPosicionInsercion = new JComboBox<>(new String[]{"Insertar al Final", "Insertar al Inicio"});
		styleComboBox(cmbPosicionInsercion);
		
		btnGuardar = new JButton("💾 Guardar");
		styleButton(btnGuardar, new Color(0, 123, 255));
		btnGuardar.setToolTipText("Guardar película: inserta en ListaEnlazada al inicio o al final (su elección)");
		
		btnNuevo = new JButton("✨ Nuevo");
		styleButton(btnNuevo, new Color(40, 167, 69));
		btnNuevo.setToolTipText("Limpiar el formulario para ingresar una nueva película");

		btnEliminar = new JButton("🗑️ Eliminar");
		styleButton(btnEliminar, new Color(220, 53, 69));
		btnEliminar.setToolTipText("Eliminar película seleccionada de ListaEnlazada (se apilará en Pila)");
		
		btnMoverAlInicio = new JButton("⬆️ Mover al Inicio");
		styleButton(btnMoverAlInicio, new Color(255, 193, 7));
		btnMoverAlInicio.setToolTipText("Mover película seleccionada a la cabeza de la ListaEnlazada");
		
		btnMoverAlFinal = new JButton("⬇️ Mover al Final");
		styleButton(btnMoverAlFinal, new Color(108, 117, 125));
		btnMoverAlFinal.setToolTipText("Mover película seleccionada a la cola de la ListaEnlazada");

		// Botón Demo
		btnDemoEstructuras = new JButton("🎓 Demo Estructuras");
		styleButton(btnDemoEstructuras, new Color(138, 43, 226));
		btnDemoEstructuras.setToolTipText("Abrir demo interactivo mostrando operaciones de Lista, Pila (LIFO) y Cola (FIFO)");
		
		// --- Layout ---
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnGuardar, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(btnNuevo, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(btnEliminar, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnMoverAlInicio, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(btnMoverAlFinal, GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
						.addComponent(btnDemoEstructuras, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lblDirector)
								.addComponent(lblTitle)
								.addComponent(lblYear)
								.addComponent(lblGenre)
								.addComponent(lblSynopsis)
								.addComponent(lblInsertPosition))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(txtDirector)
								.addComponent(txtTitulo)
								.addComponent(txtAnio)
								.addComponent(txtGenero)
								.addComponent(scrollPane)
								.addComponent(cmbPosicionInsercion))))
					.addGap(20))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblTitle)
						.addComponent(txtTitulo, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblDirector)
						.addComponent(txtDirector, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblYear)
						.addComponent(txtAnio, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblGenre)
						.addComponent(txtGenero, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lblSynopsis)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblInsertPosition)
						.addComponent(cmbPosicionInsercion, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(btnGuardar, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNuevo, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnEliminar, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(btnMoverAlInicio, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnMoverAlFinal, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addComponent(btnDemoEstructuras, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(20))
		);
		setLayout(groupLayout);
	}

	private void styleLabel(JLabel label) {
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        label.setForeground(new Color(200, 200, 200));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        textField.setBackground(new Color(60, 60, 60));
        textField.setForeground(new Color(220, 220, 220));
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(new LineBorder(new Color(90, 90, 90), 2));
    }
    
    private void styleTextArea(JTextArea textArea) {
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        textArea.setBackground(new Color(60, 60, 60));
        textArea.setForeground(new Color(220, 220, 220));
        textArea.setCaretColor(Color.WHITE);
        textArea.setBorder(new LineBorder(new Color(90, 90, 90), 2));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(bgColor.darker(), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        comboBox.setBackground(new Color(60, 60, 60));
        comboBox.setForeground(new Color(220, 220, 220));
        comboBox.setBorder(new LineBorder(new Color(90, 90, 90), 2));
    }
}


