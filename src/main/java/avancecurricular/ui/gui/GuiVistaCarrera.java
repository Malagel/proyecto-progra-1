package avancecurricular.ui.gui;

import avancecurricular.model.AsignaturaMalla;
import avancecurricular.model.Carrera;
import avancecurricular.ui.controller.ControladorCarrera;
import avancecurricular.ui.view.VistaCarrera;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Panel gráfico (Swing) para el módulo de Carrera.
 * Utiliza un {@link DefaultTableModel} sobreescrito para renderizar una tabla de sólo lectura,
 * delegando la reactividad (actualizaciones, registros, eliminaciones) mediante listeners hacia el controlador.
 */
public class GuiVistaCarrera extends JPanel implements VistaCarrera {
    private ControladorCarrera controlador;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtCreditos;
    private DefaultTableModel tableModel;
    private JTable tablaCarreras;

    public GuiVistaCarrera() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 20));
        panelIzquierdo.setPreferredSize(new Dimension(350, 0));
        
        JPanel panelFormulario = new JPanel(new BorderLayout(0, 15));
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Registrar Carrera"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 15));
        
        panelCampos.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelCampos.add(txtId);
        
        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);
        
        panelCampos.add(new JLabel("Créditos:"));
        txtCreditos = new JTextField();
        panelCampos.add(txtCreditos);

        JButton btnAgregar = new JButton("<html><p style='text-align:center;'>Registrar</p></html>");
        btnAgregar.setPreferredSize(new Dimension(0, 45));

        panelFormulario.add(panelCampos, BorderLayout.CENTER);
        panelFormulario.add(btnAgregar, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 0, 10));
        panelBotones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Acciones"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JButton btnVerMalla = new JButton("<html><p style='text-align:center;'>Ver Malla Curricular</p></html>");
        JButton btnAddAsignatura = new JButton("<html><p style='text-align:center;'>Agregar Curso a la Malla</p></html>");
        JButton btnAddPrerrequisito = new JButton("<html><p style='text-align:center;'>Agregar Prerrequisito a Curso</p></html>");
        JButton btnEliminar = new JButton("<html><p style='text-align:center;'>Eliminar Carrera</p></html>");

        panelBotones.add(btnVerMalla);
        panelBotones.add(btnAddAsignatura);
        panelBotones.add(btnAddPrerrequisito);
        panelBotones.add(btnEliminar);

        panelIzquierdo.add(panelFormulario, BorderLayout.NORTH);
        panelIzquierdo.add(panelBotones, BorderLayout.CENTER);

        add(panelIzquierdo, BorderLayout.WEST);

        String[] columnas = {"ID", "Nombre", "Créditos Totales", "Cant. Asignaturas"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        tablaCarreras = new JTable(tableModel);
        tablaCarreras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCarreras.getTableHeader().setReorderingAllowed(false);
        
        tablaCarreras.setRowHeight(35); 
        tablaCarreras.getTableHeader().setPreferredSize(new Dimension(0, 40)); 
        tablaCarreras.setShowVerticalLines(false); 
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); 
                return this;
            }
        };
        
        for (int i = 0; i < tablaCarreras.getColumnCount(); i++) {
            tablaCarreras.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(tablaCarreras);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Catálogo de Carreras"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        add(scrollPane, BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> {
            if (controlador != null) {
                try {
                    String id = txtId.getText().trim();
                    String nombre = txtNombre.getText().trim();
                    int creditos = Integer.parseInt(txtCreditos.getText().trim());
                    
                    if (id.isEmpty() || nombre.isEmpty()) {
                        mostrarError("El ID y el Nombre no pueden estar vacíos.");
                        return;
                    }
                    
                    controlador.onAgregarCarrera(id, nombre, creditos);
                    limpiarFormulario();
                } catch (NumberFormatException ex) {
                    mostrarError("Los créditos totales deben ser un número entero válido.");
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            String idCarrera = obtenerIdSeleccionado();
            if (idCarrera != null && controlador != null) {
                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro que desea eliminar la carrera " + idCarrera + "?", 
                    "Confirmar Eliminación", 
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirmacion == JOptionPane.YES_OPTION) {
                    controlador.onEliminarCarrera(idCarrera);
                }
            }
        });

        btnVerMalla.addActionListener(e -> {
            String idCarrera = obtenerIdSeleccionado();
            if (idCarrera != null && controlador != null) {
                controlador.onVerDetalleMalla(idCarrera);
            }
        });

        btnAddAsignatura.addActionListener(e -> {
            String idCarrera = obtenerIdSeleccionado();
            if (idCarrera != null && controlador != null) {
                JTextField txtIdCurso = new JTextField();
                JTextField txtSemestre = new JTextField();
                Object[] inputs = {
                    "ID del Curso a integrar:", txtIdCurso,
                    "Semestre en el que se dictará:", txtSemestre
                };
                
                int option = JOptionPane.showConfirmDialog(this, inputs, "Agregar Asignatura", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String idCurso = txtIdCurso.getText().trim();
                        int semestre = Integer.parseInt(txtSemestre.getText().trim());
                        controlador.onAgregarAsignaturaMalla(idCarrera, idCurso, semestre);
                    } catch (NumberFormatException ex) {
                        mostrarError("El semestre debe ser un número entero.");
                    }
                }
            }
        });

        btnAddPrerrequisito.addActionListener(e -> {
            String idCarrera = obtenerIdSeleccionado();
            if (idCarrera != null && controlador != null) {
                JTextField txtDestino = new JTextField();
                JTextField txtPre = new JTextField();
                Object[] inputs = {
                    "ID del Curso que requiere prerrequisito:", txtDestino,
                    "ID del Curso que DEBE SER APROBADO PREVIAMENTE:", txtPre
                };
                
                int option = JOptionPane.showConfirmDialog(this, inputs, "Agregar Prerrequisito", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String idDestino = txtDestino.getText().trim();
                    String idPre = txtPre.getText().trim();
                    controlador.onAgregarPrerrequisito(idCarrera, idDestino, idPre);
                }
            }
        });
    }

    /**
     * Extrae de forma segura el identificador de la entidad en la fila seleccionada por el usuario.
     *
     * @return El identificador (ej. ID o RUT) contenido en la columna 0, 
     *         o {@code null} si no hay ninguna fila seleccionada en la tabla.
     */
    private String obtenerIdSeleccionado() {
        int fila = tablaCarreras.getSelectedRow();
        if (fila >= 0) {
            return (String) tableModel.getValueAt(fila, 0);
        } else {
            mostrarError("Debe seleccionar una carrera de la tabla para realizar esta acción.");
            return null;
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtCreditos.setText("");
        txtId.requestFocus();
    }

    @Override
    public void setControlador(ControladorCarrera controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        if (controlador != null) {
            controlador.onSolicitarListaCarreras();
        }
    }

    @Override
    public void mostrarListaCarreras(Collection<Carrera> carreras) {
        tableModel.setRowCount(0);
        for (Carrera carrera : carreras) {
            tableModel.addRow(new Object[]{
                carrera.getId(), 
                carrera.getNombre(), 
                carrera.getCreditosTotales(), 
                carrera.getPlanDeEstudio().size()
            });
        }
    }

    @Override
    public void mostrarDetalleMalla(Carrera carrera) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Malla Curricular: ").append(carrera.getNombre()).append(" ===\n\n");
        
        if (carrera.getPlanDeEstudio().isEmpty()) {
            sb.append("(No hay asignaturas registradas en esta malla)\n");
        } else {
            List<AsignaturaMalla> mallaOrdenada = new ArrayList<>(carrera.getPlanDeEstudio());
            mallaOrdenada.sort(Comparator.comparingInt(AsignaturaMalla::getNumeroSemestre));
            
            int semestreActual = -1;
            for (AsignaturaMalla am : mallaOrdenada) {
                if (am.getNumeroSemestre() != semestreActual) {
                    semestreActual = am.getNumeroSemestre();
                    sb.append("\n[ Semestre ").append(semestreActual).append(" ]\n");
                }
                sb.append("  - ").append(am.getCurso().getId()).append(": ").append(am.getCurso().getNombre());
                
                if (!am.getPrerrequisitos().isEmpty()) {
                    sb.append("\n      (Prerrequisitos: ");
                    List<String> pres = new ArrayList<>();
                    am.getPrerrequisitos().forEach(p -> pres.add(p.getId()));
                    sb.append(String.join(", ", pres)).append(")");
                }
                sb.append("\n");
            }
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        
        Font defaultFont = UIManager.getFont("Label.font");
        int fontSize = (defaultFont != null) ? defaultFont.getSize() : 16;
        textArea.setFont(new Font("Monospaced", Font.PLAIN, fontSize));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detalle de Malla: " + carrera.getId(), JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}