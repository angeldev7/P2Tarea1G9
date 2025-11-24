package Vista;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.RowFilter;
import javax.swing.JButton;

public class PanelLista extends JPanel {

	private static final long serialVersionUID = 1L;
	public JTable tablaPeliculas;
	public DefaultTableModel modeloTabla;
	public JTextField txtBuscar;
	public JComboBox<String> cmbBuscarPor;
	public TableRowSorter<DefaultTableModel> sorter;

	// Botones de ordenamiento
	public JButton btnOrdenarTitulo;
	public JButton btnOrdenarDirector;
	public JButton btnOrdenarAnio;
	public JButton btnOrdenarGenero;

	public PanelLista() {
		setBackground(new Color(45, 45, 45));
		setBorder(new TitledBorder(new LineBorder(new Color(90, 90, 90)), " Catálogo de Películas ", TitledBorder.LEADING, TitledBorder.TOP, new Font(Font.SANS_SERIF, Font.BOLD, 14), new Color(220, 220, 220)));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setBackground(new Color(55, 55, 55));
		scrollPane.setBorder(new LineBorder(new Color(90, 90, 90)));

		JLabel lblSearch = new JLabel("\uD83D\uDD0D Buscar por:");
		lblSearch.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		lblSearch.setForeground(new Color(200, 200, 200));

		cmbBuscarPor = new JComboBox<>();
		cmbBuscarPor.setModel(new DefaultComboBoxModel<>(new String[] {"Título", "Género", "Año", "Director"}));
		cmbBuscarPor.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));

		txtBuscar = new JTextField();
		txtBuscar.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		txtBuscar.setBackground(new Color(60, 60, 60));
		txtBuscar.setForeground(new Color(220, 220, 220));
		txtBuscar.setCaretColor(Color.WHITE);
		txtBuscar.setBorder(new LineBorder(new Color(90, 90, 90), 2));
		txtBuscar.setColumns(10);

		String[] columnNames = {"Título", "Director", "Año", "Género"};
		modeloTabla = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		tablaPeliculas = new JTable(modeloTabla) {
			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(super.getPreferredScrollableViewportSize().width, 400);
			}
		};

		tablaPeliculas.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		tablaPeliculas.setToolTipText("Orden lógico de ListaEnlazada: fila superior es CABEZA, fila inferior es COLA. Mover e insertar actualiza este orden.");
		tablaPeliculas.setRowHeight(32);
		tablaPeliculas.setShowGrid(false);
		tablaPeliculas.setIntercellSpacing(new Dimension(0, 0));

		JTableHeader header = tablaPeliculas.getTableHeader();
		header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		header.setBackground(new Color(30, 30, 30));
		header.setForeground(new Color(0, 123, 255));
		header.setReorderingAllowed(false);
		header.setBorder(new LineBorder(new Color(90, 90, 90)));

		scrollPane.setViewportView(tablaPeliculas);

		sorter = new TableRowSorter<>(modeloTabla);
		tablaPeliculas.setRowSorter(sorter);

        // Botones ordenar con colores distintivos
        btnOrdenarTitulo = new JButton("📚 Ordenar por Título");
        styleButton(btnOrdenarTitulo, new Color(52, 152, 219)); // Azul brillante
        
        btnOrdenarDirector = new JButton("🎬 Ordenar por Director");
        styleButton(btnOrdenarDirector, new Color(155, 89, 182)); // Púrpura
        
        btnOrdenarAnio = new JButton("📅 Ordenar por Año");
        styleButton(btnOrdenarAnio, new Color(46, 204, 113)); // Verde
        
        btnOrdenarGenero = new JButton("🎭 Ordenar por Género");
        styleButton(btnOrdenarGenero, new Color(230, 126, 34)); // Naranja

        GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(lblSearch)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(cmbBuscarPor, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(txtBuscar))
				.addGroup(layout.createSequentialGroup()
					.addComponent(btnOrdenarTitulo)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(btnOrdenarDirector)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(btnOrdenarAnio)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(btnOrdenarGenero))
				.addComponent(scrollPane)
		);

		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(lblSearch)
					.addComponent(cmbBuscarPor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(txtBuscar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(btnOrdenarTitulo)
					.addComponent(btnOrdenarDirector)
					.addComponent(btnOrdenarAnio)
					.addComponent(btnOrdenarGenero))
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(scrollPane)
		);
	}

	private void styleButton(JButton button, Color bgColor) {
		button.setBackground(bgColor);
		button.setForeground(Color.WHITE);
		button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		button.setBorder(new LineBorder(new Color(90, 90, 90), 2));
		button.setFocusPainted(false);
		button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
	}

	public void filterTable(String query, int searchColumn) {
		if (query == null || query.trim().isEmpty()) {
			sorter.setRowFilter(null);
			return;
		}
		try {
			RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(query), searchColumn);
			sorter.setRowFilter(rf);
		} catch (Exception e) {
			sorter.setRowFilter(null);
		}
	}
}

