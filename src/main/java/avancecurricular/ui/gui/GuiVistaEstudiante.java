package avancecurricular.ui.gui;

import avancecurricular.model.Estudiante;
import avancecurricular.model.RegistroAcademico;
import avancecurricular.ui.controller.ControladorEstudiante;
import avancecurricular.ui.view.VistaEstudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

/**
 * Panel gráfico (Swing) para el módulo de Estudiante.
 * Utiliza un {@link DefaultTableModel} sobreescrito para renderizar una tabla de sólo lectura,
 * delegando la reactividad (actualizaciones, registros, eliminaciones) mediante listeners hacia el controlador.
 */
public class GuiVistaEstudiante extends JPanel implements VistaEstudiante {
    private ControladorEstudiante controlador;

    private JTextField txtRut;
    private JTextField txtNombre;
    private JTextField txtIdCarrera;
    private DefaultTableModel tableModel;
    private JTable tablaEstudiantes;

    public GuiVistaEstudiante() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 20));
        panelIzquierdo.setPreferredSize(new Dimension(350, 0));
        
        JPanel panelFormulario = new JPanel(new BorderLayout(0, 15));
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Registrar Estudiante"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 15));
        
        panelCampos.add(new JLabel("RUT:"));
        txtRut = new JTextField();
        panelCampos.add(txtRut);
        
        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);
        
        panelCampos.add(new JLabel("ID Carrera:"));
        txtIdCarrera = new JTextField();
        panelCampos.add(txtIdCarrera);

        JButton btnAgregar = new JButton("<html><p style='text-align:center;'>Registrar</p></html>");
        btnAgregar.setPreferredSize(new Dimension(0, 45));

        panelFormulario.add(panelCampos, BorderLayout.CENTER);
        panelFormulario.add(btnAgregar, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 0, 10));
        panelBotones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Acciones"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JButton btnVerRegistros = new JButton("<html><p style='text-align:center;'>Ver Expediente Académico</p></html>");
        JButton btnInscribirCurso = new JButton("<html><p style='text-align:center;'>Inscribir Curso</p></html>");
        JButton btnCalificar = new JButton("<html><p style='text-align:center;'>Actualizar Registro Curso</p></html>");
        JButton btnRetirarCurso = new JButton("<html><p style='text-align:center;'>Desinscribir Curso</p></html>");
        JButton btnEliminar = new JButton("<html><p style='text-align:center;'>Eliminar Estudiante</p></html>");

        panelBotones.add(btnVerRegistros);
        panelBotones.add(btnInscribirCurso);
        panelBotones.add(btnCalificar);
        panelBotones.add(btnRetirarCurso);
        panelBotones.add(btnEliminar);

        panelIzquierdo.add(panelFormulario, BorderLayout.NORTH);
        panelIzquierdo.add(panelBotones, BorderLayout.CENTER);

        add(panelIzquierdo, BorderLayout.WEST);

        String[] columnas = {"RUT", "Nombre", "Carrera", "Avance (%)"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        tablaEstudiantes = new JTable(tableModel);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEstudiantes.getTableHeader().setReorderingAllowed(false);
        
        tablaEstudiantes.setRowHeight(35); 
        tablaEstudiantes.getTableHeader().setPreferredSize(new Dimension(0, 40)); 
        tablaEstudiantes.setShowVerticalLines(false); 
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); 
                return this;
            }
        };
        
        for (int i = 0; i < tablaEstudiantes.getColumnCount(); i++) {
            tablaEstudiantes.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(tablaEstudiantes);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Matrícula de Estudiantes"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        add(scrollPane, BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> {
            if (controlador != null) {
                String rut = txtRut.getText().trim();
                String nombre = txtNombre.getText().trim();
                String idCarrera = txtIdCarrera.getText().trim();
                
                if (rut.isEmpty() || nombre.isEmpty() || idCarrera.isEmpty()) {
                    mostrarError("Todos los campos del formulario son obligatorios.");
                    return;
                }
                
                controlador.onAgregarEstudiante(rut, nombre, idCarrera);
                limpiarFormulario();
            }
        });

        btnEliminar.addActionListener(e -> {
            String rut = obtenerRutSeleccionado();
            if (rut != null && controlador != null) {
                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro que desea eliminar al estudiante con RUT " + rut + "?", 
                    "Confirmar Eliminación", 
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirmacion == JOptionPane.YES_OPTION) {
                    controlador.onEliminarEstudiante(rut);
                }
            }
        });

        btnVerRegistros.addActionListener(e -> {
            String rut = obtenerRutSeleccionado();
            if (rut != null && controlador != null) {
                controlador.onSolicitarRegistros(rut);
            }
        });

        btnInscribirCurso.addActionListener(e -> {
            String rut = obtenerRutSeleccionado();
            if (rut != null && controlador != null) {
                String idCurso = JOptionPane.showInputDialog(this, 
                    "Ingrese el ID del curso a inscribir:", 
                    "Inscribir Curso", 
                    JOptionPane.QUESTION_MESSAGE);
                    
                if (idCurso != null && !idCurso.trim().isEmpty()) {
                    controlador.onInscribirCurso(rut, idCurso.trim());
                }
            }
        });

        btnRetirarCurso.addActionListener(e -> {
            String rut = obtenerRutSeleccionado();
            if (rut != null && controlador != null) {
                String idCurso = JOptionPane.showInputDialog(this, 
                    "Ingrese el ID del curso a desinscribir:", 
                    "Retirar Curso", 
                    JOptionPane.WARNING_MESSAGE);
                    
                if (idCurso != null && !idCurso.trim().isEmpty()) {
                    controlador.onDesinscribirCurso(rut, idCurso.trim());
                }
            }
        });

        btnCalificar.addActionListener(e -> {
            String rut = obtenerRutSeleccionado();
            if (rut != null && controlador != null) {
                JTextField txtIdCurso = new JTextField();
                JTextField txtNota = new JTextField("0.0");
                JComboBox<String> cmbEstado = new JComboBox<>(new String[]{
                    RegistroAcademico.ESTADO_CURSANDO, 
                    RegistroAcademico.ESTADO_APROBADO, 
                    RegistroAcademico.ESTADO_REPROBADO
                });

                Object[] inputs = {
                    "ID del Curso:", txtIdCurso,
                    "Nota obtenida (ej: 5.5, 0.0 si está cursando):", txtNota,
                    "Estado final:", cmbEstado
                };
                
                int option = JOptionPane.showConfirmDialog(this, inputs, "Actualizar Registro Académico", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String idCurso = txtIdCurso.getText().trim();
                        if (idCurso.isEmpty()) {
                            mostrarError("Debe especificar el ID del curso.");
                            return;
                        }
                        
                        double nota = Double.parseDouble(txtNota.getText().trim().replace(",", "."));
                        String estado = (String) cmbEstado.getSelectedItem();
                        
                        controlador.onActualizarRegistro(rut, idCurso, nota, estado);
                    } catch (NumberFormatException ex) {
                        mostrarError("La nota debe ser un número decimal válido.");
                    }
                }
            }
        });
    }

    /**
     * Extrae de forma segura el identificador de la entidad en la fila seleccionada por el usuario.
     *
     * @return El identificador (RUT) contenido en la columna 0, 
     *         o {@code null} si no hay ninguna fila seleccionada en la tabla.
     */
    private String obtenerRutSeleccionado() {
        int fila = tablaEstudiantes.getSelectedRow();
        if (fila >= 0) {
            return (String) tableModel.getValueAt(fila, 0);
        } else {
            mostrarError("Debe seleccionar un estudiante de la tabla para realizar esta acción.");
            return null;
        }
    }

    private void limpiarFormulario() {
        txtRut.setText("");
        txtNombre.setText("");
        txtIdCarrera.setText("");
        txtRut.requestFocus();
    }

    @Override
    public void setControlador(ControladorEstudiante controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        if (controlador != null) {
            controlador.onSolicitarListaEstudiantes();
        }
    }

    @Override
    public void mostrarListaEstudiantes(Collection<Estudiante> estudiantes) {
        tableModel.setRowCount(0);
        for (Estudiante estudiante : estudiantes) {
            tableModel.addRow(new Object[]{
                estudiante.getRut(), 
                estudiante.getNombre(), 
                estudiante.getCarrera().getNombre(),
                String.format("%.1f%%", estudiante.calcularPorcentajeAvance())
            });
        }
    }

    @Override
    public void mostrarRegistrosAcademicos(Estudiante estudiante) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Expediente Académico: ").append(estudiante.getNombre()).append(" ===\n");
        sb.append("Carrera: ").append(estudiante.getCarrera().getNombre()).append("\n");
        sb.append(String.format("Avance Curricular: %.1f%%\n", estudiante.calcularPorcentajeAvance()));
        sb.append("Créditos Aprobados: ").append(estudiante.obtenerCreditosAprobados())
          .append(" / ").append(estudiante.getCarrera().getCreditosTotales()).append("\n");
        sb.append("--------------------------------------------------\n");

        if (estudiante.getRegistrosAcademicos().isEmpty()) {
            sb.append("(No posee registros académicos vigentes)\n");
        } else {
            for (RegistroAcademico registro : estudiante.getRegistrosAcademicos()) {
                sb.append(String.format("- [%s] %s | Nota: %.1f | Estado: %s\n", 
                    registro.getCurso().getId(),
                    registro.getCurso().getNombre(),
                    registro.getNota(),
                    registro.getEstado()
                ));
            }
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        
        Font defaultFont = UIManager.getFont("Label.font");
        int fontSize = (defaultFont != null) ? defaultFont.getSize() : 16;
        textArea.setFont(new Font("Monospaced", Font.PLAIN, fontSize));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Expediente: " + estudiante.getRut(), JOptionPane.INFORMATION_MESSAGE);
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