package Controlador;
import Modelo.ListaEnlazada;
import Modelo.Pelicula;
import Modelo.Pila;
import Modelo.Cola;
import Vista.VentanaPrincipal;
import Modelo.PersistenciaPeliculasJSON;
import Modelo.PersistenciaPeliculasJSON.Campo;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class ControladorPelicula implements ActionListener, ListSelectionListener, DocumentListener {

    private VentanaPrincipal vista;
    private ListaEnlazada modelo;
    private Pila pila;
    private Cola cola;

    public ControladorPelicula(VentanaPrincipal vista, ListaEnlazada modelo, Pila pila, Cola cola) {
        this.vista = vista;
        this.modelo = modelo;
        this.pila = pila;
        this.cola = cola;
        
        this.vista.panelFormulario.btnGuardar.addActionListener(this);
        this.vista.panelFormulario.btnNuevo.addActionListener(this);
        this.vista.panelFormulario.btnEliminar.addActionListener(this);
        this.vista.panelFormulario.btnMoverAlInicio.addActionListener(this);
        this.vista.panelFormulario.btnMoverAlFinal.addActionListener(this);
        this.vista.panelLista.tablaPeliculas.getSelectionModel().addListSelectionListener(this);
        this.vista.panelLista.txtBuscar.getDocument().addDocumentListener(this);
        this.vista.panelLista.cmbBuscarPor.addActionListener(this);
        this.vista.panelFormulario.btnDemoEstructuras.addActionListener(this);
        // Botones de ordenamiento (Mezcla Natural sobre archivo)
        this.vista.panelLista.btnOrdenarTitulo.addActionListener(this);
        this.vista.panelLista.btnOrdenarDirector.addActionListener(this);
        this.vista.panelLista.btnOrdenarAnio.addActionListener(this);
        this.vista.panelLista.btnOrdenarGenero.addActionListener(this);

        actualizarTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();
        if (fuente == vista.panelFormulario.btnGuardar) {
            guardarPelicula();
        } else if (fuente == vista.panelFormulario.btnNuevo) {
            limpiarFormulario();
        } else if (fuente == vista.panelFormulario.btnEliminar) {
            eliminarPelicula();
        } else if (fuente == vista.panelFormulario.btnMoverAlInicio) {
            moverAlInicio();
        } else if (fuente == vista.panelFormulario.btnMoverAlFinal) {
            moverAlFinal();
        } else if (fuente == vista.panelLista.cmbBuscarPor) {
            filtrar();
        } else if (fuente == vista.panelFormulario.btnDemoEstructuras) {
            abrirDialogoDemo();
        } else if (fuente == vista.panelLista.btnOrdenarTitulo) {
            ordenarPor(Campo.TITULO, true);
        } else if (fuente == vista.panelLista.btnOrdenarDirector) {
            ordenarPor(Campo.DIRECTOR, true);
        } else if (fuente == vista.panelLista.btnOrdenarAnio) {
            ordenarPor(Campo.ANIO, true);
        } else if (fuente == vista.panelLista.btnOrdenarGenero) {
            ordenarPor(Campo.GENERO, true);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int filaSeleccionada = vista.panelLista.tablaPeliculas.getSelectedRow();
            if (filaSeleccionada != -1) {
                int filaModelo = vista.panelLista.tablaPeliculas.convertRowIndexToModel(filaSeleccionada);
                String titulo = (String) vista.panelLista.modeloTabla.getValueAt(filaModelo, 0);
                
                Pelicula pelicula = modelo.buscarPorTitulo(titulo);
                if (pelicula != null) {
                    llenarFormulario(pelicula);
                }
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        filtrar();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        filtrar();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        filtrar();
    }

    // --- Business Logic Methods ---

    private void guardarPelicula() {
        try {
            String titulo = vista.panelFormulario.txtTitulo.getText().trim();
            String director = vista.panelFormulario.txtDirector.getText().trim();
            String anioStr = vista.panelFormulario.txtAnio.getText().trim();
            String genero = vista.panelFormulario.txtGenero.getText().trim();
            String sinopsis = vista.panelFormulario.txtSinopsis.getText();

            if (titulo.isEmpty() || director.isEmpty() || anioStr.isEmpty() || genero.isEmpty()) {
                JOptionPane.showMessageDialog(vista, 
                    "Complete todos los campos requeridos:\n- Título\n- director\n- Año\n- Género", 
                    "Campos Incompletos", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int anio;
            try {
                if (!Pattern.matches("\\d{4}", anioStr)) {
                    JOptionPane.showMessageDialog(vista, 
                        "El año debe ser un número de 4 dígitos (ej: 2024).", 
                        "Formato de Año Inválido", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                anio = Integer.parseInt(anioStr);
                
                final int ANIO_MIN = 1888;
                final int ANIO_MAX = java.time.Year.now().getValue() + 1;
                if (anio < ANIO_MIN || anio > ANIO_MAX) {
                    JOptionPane.showMessageDialog(vista, 
                        String.format("El año debe estar entre %d y %d.", ANIO_MIN, ANIO_MAX), 
                        "Año Inválido", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, 
                    "El año debe ser un número válido.", 
                    "Formato de Año Inválido", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Pelicula peliculaExistente = modelo.buscarPorTitulo(titulo);
            boolean modoEdicion = !vista.panelFormulario.txtTitulo.isEditable();
            
            if (peliculaExistente != null && !modoEdicion) {
                JOptionPane.showMessageDialog(vista, 
                    "Ya existe una película con el título '" + titulo + "'.\nElija un título diferente.", 
                    "Título Duplicado", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (modoEdicion && peliculaExistente != null) {
                peliculaExistente.setDirector(director);
                peliculaExistente.setAnio(anio);
                peliculaExistente.setGenero(genero);
                peliculaExistente.setSinopsis(sinopsis);
                JOptionPane.showMessageDialog(vista, 
                    "Película '" + titulo + "' actualizada exitosamente.", 
                    "Actualización Exitosa", 
                    JOptionPane.INFORMATION_MESSAGE);
                actualizarEstado("Actualizada: " + titulo);
            } else {
                Pelicula nuevaPelicula = new Pelicula(titulo, director, anio, genero, sinopsis);
                
                String posicionInsercion = (String) vista.panelFormulario.cmbPosicionInsercion.getSelectedItem();
                if ("Insertar al Inicio".equals(posicionInsercion)) {
                    modelo.insertarAlInicio(nuevaPelicula);
                } else {
                    modelo.insertarAlFinal(nuevaPelicula);
                }
                
                JOptionPane.showMessageDialog(vista, 
                    "Película '" + titulo + "' guardada exitosamente.", 
                    "Guardado Exitoso", 
                    JOptionPane.INFORMATION_MESSAGE);
                actualizarEstado("Agregada: " + titulo);
            }

            actualizarTabla();
            limpiarFormulario();
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(vista, 
                "Error: " + ex.getMessage(), 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, 
                "Ocurrió un error inesperado: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarPelicula() {
        try {
            int filaSeleccionada = vista.panelLista.tablaPeliculas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, 
                    "Seleccione una película de la lista para eliminar.", 
                    "Ninguna Película Seleccionada", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int filaModelo = vista.panelLista.tablaPeliculas.convertRowIndexToModel(filaSeleccionada);
            String titulo = (String) vista.panelLista.modeloTabla.getValueAt(filaModelo, 0);
            
            int respuesta = JOptionPane.showConfirmDialog(vista, 
                "¿Está seguro de que desea eliminar '" + titulo + "'?\n\nNota: La película se agregará a la Pila de Vistas Recientes.", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                // Almacenar película removida en pila antes de eliminar
                Pelicula aEliminar = modelo.buscarPorTitulo(titulo);
                if (aEliminar == null) {
                    JOptionPane.showMessageDialog(vista, 
                        "Película no encontrada en el catálogo.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean eliminada = modelo.eliminarPorTitulo(titulo);
                if (eliminada) {
                    pila.apilar(aEliminar);
                    JOptionPane.showMessageDialog(vista, 
                        "Película '" + titulo + "' eliminada exitosamente.\nAgregada a la Pila de Vistas Recientes.", 
                        "Eliminación Exitosa", 
                        JOptionPane.INFORMATION_MESSAGE);
                    actualizarEstado("eliminada: " + titulo);
                    actualizarTabla();
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(vista, 
                        "No se pudo eliminar la película. Por favor intente de nuevo.", 
                        "Eliminación Fallida", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, 
                "Ocurrió un error al eliminar la película: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarTabla() {
        try {
            // Persistir el estado actual al archivo JSONL
            try { PersistenciaPeliculasJSON.guardar(modelo); } catch (Exception ig) { /* no bloquear UI */ }
            // Save current filtro
            javax.swing.RowFilter<? super DefaultTableModel, ? super Integer> currentFilter = 
                vista.panelLista.sorter.getRowFilter();
            
            // Temporarily disable sorter to avoid warnings during updates
            vista.panelLista.tablaPeliculas.setRowSorter(null);
            
            // Clear existing rows
            vista.panelLista.modeloTabla.setRowCount(0);
            
            // Populate table with current movies
            Pelicula[] movies = modelo.obtenerTodasLasPeliculas();
            for (Pelicula pelicula : movies) {
                if (pelicula != null) { // Defensive check
                    Object[] rowData = {
                        pelicula.getTitulo(),
                        pelicula.getDirector(),
                        pelicula.getAnio(),
                        pelicula.getGenero()
                    };
                    vista.panelLista.modeloTabla.addRow(rowData);
                }
            }
            
            // Reactivate sorter and restore filtro
            vista.panelLista.tablaPeliculas.setRowSorter(vista.panelLista.sorter);
            if (currentFilter != null) {
                vista.panelLista.sorter.setRowFilter(currentFilter);
            }
            
            // Actualizar contadores en panel de estado
            actualizarSoloContadores();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, 
                "Error al actualizar la tabla: " + ex.getMessage(), 
                "Error de Actualización", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        vista.panelFormulario.txtTitulo.setText("");
        vista.panelFormulario.txtDirector.setText("");
        vista.panelFormulario.txtAnio.setText("");
        vista.panelFormulario.txtGenero.setText("");
        vista.panelFormulario.txtSinopsis.setText("");
        vista.panelLista.tablaPeliculas.clearSelection();
        vista.panelFormulario.txtTitulo.setEditable(true);
        // Limpiar campo de búsqueda, lo cual también resetea el filtro de tabla
        vista.panelLista.txtBuscar.setText("");
        actualizarEstado("Formulario limpiado");
    }

    private void llenarFormulario(Pelicula pelicula) {
        vista.panelFormulario.txtTitulo.setText(pelicula.getTitulo());
        vista.panelFormulario.txtDirector.setText(pelicula.getDirector());
        vista.panelFormulario.txtAnio.setText(String.valueOf(pelicula.getAnio()));
        vista.panelFormulario.txtGenero.setText(pelicula.getGenero());
        vista.panelFormulario.txtSinopsis.setText(pelicula.getSinopsis());
        vista.panelFormulario.txtTitulo.setEditable(false);
    }
    
    private void filtrar() {
        try {
            String query = vista.panelLista.txtBuscar.getText();
            if (query == null) {
                query = ""; // Defensive null check
            }
            
            String buscarPor = (String) vista.panelLista.cmbBuscarPor.getSelectedItem();
            if (buscarPor == null) buscarPor = "Título";

            if ("Título".equalsIgnoreCase(buscarPor)) {
                // Búsqueda aproximada por título (no genérica)
                Pelicula[] resultados = modelo.buscarPorTituloAproximado(query, 2);
                // Desactivar sorter durante la recarga
                vista.panelLista.tablaPeliculas.setRowSorter(null);
                vista.panelLista.modeloTabla.setRowCount(0);
                for (Pelicula p : resultados) {
                    vista.panelLista.modeloTabla.addRow(new Object[]{p.getTitulo(), p.getDirector(), p.getAnio(), p.getGenero()});
                }
                vista.panelLista.tablaPeliculas.setRowSorter(vista.panelLista.sorter);
            } else {
                int searchColumn;
                switch (buscarPor) {
                    case "Género": searchColumn = 3; break;
                    case "Año": searchColumn = 2; break;
                    case "Director": searchColumn = 1; break;
                    default: searchColumn = 0;
                }
                vista.panelLista.filterTable(query, searchColumn);
            }
        } catch (Exception ex) {
            // Don't show dialog for filtro errors, just log
            System.err.println("filtro error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Ordenamiento en memoria (preserva instancias y evita archivos temporales)
    private void ordenarPor(Campo campo, boolean asc) {
        try {
            if (modelo.estaVacia()) {
                JOptionPane.showMessageDialog(vista,
                    "No hay películas para ordenar.",
                    "Lista Vacía",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Ordenar en memoria usando Timsort (O(n log n))
            modelo.ordenar(PersistenciaPeliculasJSON.obtenerComparador(campo, asc));
            
            // Persistir resultado al archivo
            PersistenciaPeliculasJSON.guardar(modelo);
            
            // Limpiar archivos temporales si quedaron de ejecuciones previas
            PersistenciaPeliculasJSON.limpiarTemporales();
            
            actualizarTabla();
            actualizarEstado("Ordenado por " + campo.name().toLowerCase() + " (" + (asc ? "ascendente" : "descendente") + ")");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                "No se pudo ordenar: " + ex.getMessage(),
                "Error de Ordenamiento",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void moverAlInicio() {
        try {
            int filaSeleccionada = vista.panelLista.tablaPeliculas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, 
                    "Por favor seleccione una película de la lista para mover.", 
                    "Ninguna Película Seleccionada", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int filaModelo = vista.panelLista.tablaPeliculas.convertRowIndexToModel(filaSeleccionada);
            String titulo = (String) vista.panelLista.modeloTabla.getValueAt(filaModelo, 0);
            
            Pelicula pelicula = modelo.buscarPorTitulo(titulo);
            if (pelicula == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Película no encontrada en el catálogo.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Remover de posición actual e insertar al inicio
            modelo.eliminarPorTitulo(titulo);
            modelo.insertarAlInicio(pelicula);
            
            JOptionPane.showMessageDialog(vista, 
                "Película '" + titulo + "' movida al inicio exitosamente.", 
                "Movimiento Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            actualizarEstado("Movida al inicio: " + titulo);
            actualizarTabla();
            limpiarFormulario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, 
                "Ocurrió un error al mover la película: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void moverAlFinal() {
        try {
            int filaSeleccionada = vista.panelLista.tablaPeliculas.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, 
                    "Por favor seleccione una película de la lista para mover.", 
                    "Ninguna Película Seleccionada", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int filaModelo = vista.panelLista.tablaPeliculas.convertRowIndexToModel(filaSeleccionada);
            String titulo = (String) vista.panelLista.modeloTabla.getValueAt(filaModelo, 0);
            
            Pelicula pelicula = modelo.buscarPorTitulo(titulo);
            if (pelicula == null) {
                JOptionPane.showMessageDialog(vista, 
                    "Película no encontrada en el catálogo.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Remover de posición actual e insertar al final
            modelo.eliminarPorTitulo(titulo);
            modelo.insertarAlFinal(pelicula);
            
            JOptionPane.showMessageDialog(vista, 
                "Película '" + titulo + "' movida al final exitosamente.", 
                "Movimiento Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            actualizarEstado("Movida al final: " + titulo);
            actualizarTabla();
            limpiarFormulario();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, 
                "Ocurrió un error al mover la película: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // --- Demo Dialog ---

    /**
     * Opens the structure demonstration dialog.
     * After closing, refreshes the main table in case structures were modified.
     */
    private void abrirDialogoDemo() {
        try {
            Vista.DialogoDemoEstructuras dialog = new Vista.DialogoDemoEstructuras(vista, modelo, pila, cola);
            dialog.setVisible(true);
            // Después de cerrar el diálogo, refrescar la tabla por si se modificaron las estructuras
            actualizarTabla();
            actualizarEstado("Diálogo demo cerrado");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, 
                "Error al abrir diálogo demo: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Updates only the size counters in the status panel.
     */
    private void actualizarSoloContadores() {
        try {
            vista.panelEstado.lblTamanioLista.setText("Tamaño Lista: " + modelo.getTamanio());
            vista.panelEstado.lblTamanioPila.setText("Tamaño Pila: " + pila.tamanio());
            vista.panelEstado.lblTamanioCola.setText("Tamaño Cola: " + cola.tamanio());
        } catch (Exception ex) {
            System.err.println("Error actualizando contadores: " + ex.getMessage());
        }
    }

    /**
     * Updates the status panel with operation message and current counts.
     * 
     * @param message the operation message to log
     */
    private void actualizarEstado(String message) {
        try {
            actualizarSoloContadores();
            
            if (message != null && !message.trim().isEmpty()) {
                vista.panelEstado.lblUltimaOperacion.setText("Última: " + message);
                
                // Agregar líneas de log al principio (más reciente primero)
                String prev = vista.panelEstado.txtLog.getText();
                if (prev == null) {
                    prev = "";
                }
                String timestamp = java.time.LocalTime.now().withNano(0).toString();
                String newLine = timestamp + " - " + message + "\n";
                vista.panelEstado.txtLog.setText(newLine + prev);
            }
        } catch (Exception ex) {
            System.err.println("Error actualizando estado: " + ex.getMessage());
        }
    }
}











