package App;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import Controlador.ControladorPelicula;
import Modelo.ListaEnlazada;
import Modelo.Pila;
import Modelo.Cola;
import Vista.VentanaPrincipal;
import Modelo.PersistenciaPeliculasJSON;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
				
				ListaEnlazada modelo = new ListaEnlazada();
				// Cargar catálogo desde archivo JSONL si existe
				try {
					PersistenciaPeliculasJSON.cargarEn(modelo);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				Pila pila = new Pila();
				Cola cola = new Cola();
				
				VentanaPrincipal vista = new VentanaPrincipal();
				
				new ControladorPelicula(vista, modelo, pila, cola);
				
				vista.setVisible(true);
			}
		});
	}
}
