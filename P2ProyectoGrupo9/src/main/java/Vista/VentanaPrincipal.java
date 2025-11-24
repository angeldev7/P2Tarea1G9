package Vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import java.awt.Color;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panelContenido;
	public PanelFormulario panelFormulario;
	public PanelLista panelLista;
	public PanelEstado panelEstado;

	public VentanaPrincipal() {
		setTitle("🎬 Catálogo de Películas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 860);
		setMinimumSize(new java.awt.Dimension(1150, 840));
		panelContenido = new JPanel(new BorderLayout(20, 20));
		panelContenido.setBackground(new Color(30, 30, 30));
		panelContenido.setBorder(new EmptyBorder(15, 15, 15, 15));
		setContentPane(panelContenido);

		panelFormulario = new PanelFormulario();
		panelLista = new PanelLista();

		JSplitPane panelDividido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelFormulario, panelLista);
		panelDividido.setResizeWeight(0.45);
		panelDividido.setDividerSize(8);
		panelDividido.setContinuousLayout(true);

		panelContenido.add(panelDividido, BorderLayout.CENTER);
		panelEstado = new PanelEstado();
		panelContenido.add(panelEstado, BorderLayout.SOUTH);
	}
}
