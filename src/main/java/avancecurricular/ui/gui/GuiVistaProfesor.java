package avancecurricular.ui.gui;

import avancecurricular.model.Curso;
import avancecurricular.model.Profesor;
import avancecurricular.ui.controller.ControladorProfesor;
import avancecurricular.ui.view.VistaProfesor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

/**
 * Panel gráfico (Swing) para el módulo de Profesor.
 * Utiliza un {@link DefaultTableModel} sobreescrito para renderizar una tabla de sólo lectura,
 * delegando la reactividad (actualizaciones, registros, eliminaciones) mediante listeners hacia el controlador.
 */
public class GuiVistaProfesor extends JPanel implements VistaProfesor {
    private ControladorProfesor controlador;

    private JTextField txtRut;
    private JTextField txtNombre;
    private DefaultTableModel tableModel;
    private JTable tablaProfesores;

    public GuiVistaProfesor() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 20));
        panelIzquierdo.setPreferredSize(new Dimension(350, 0));
        
        JPanel panelFormulario = new JPanel(new BorderLayout(0, 15));
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Registrar Profesor"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JPanel panelCampos = new JPanel(new GridLayout(2, 2, 10, 15));
        
        panelCampos.add(new JLabel("RUT:"));
        txtRut = new JTextField();
        panelCampos.add(txtRut);
        
        panelCampos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelCampos.add(txtNombre);

        JButton btnAgregar = new JButton("<html><p style='text-align:center;'>Registrar</p></html>");
        btnAgregar.setPreferredSize(new Dimension(0, 45));

        panelFormulario.add(panelCampos, BorderLayout.CENTER);
        panelFormulario.add(btnAgregar, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 0, 10));
        panelBotones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Acciones"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JButton btnVerCursos = new JButton("<html><p style='text-align:center;'>Ver Cursos Dictados</p></html>");
        JButton btnAsignarCurso = new JButton("<html><p style='text-align:center;'>Asignar Curso a Profesor</p></html>");
        JButton btnRemoverCurso = new JButton("<html><p style='text-align:center;'>Remover Curso de Profesor</p></html>");
        JButton btnEliminar = new JButton("<html><p style='text-align:center;'>Eliminar Profesor</p></html>");

        panelBotones.add(btnVerCursos);
        panelBotones.add(btnAsignarCurso);
        panelBotones.add(btnRemoverCurso);
        panelBotones.add(btnEliminar);

        panelIzquierdo.add(panelFormulario, BorderLayout.NORTH);
        panelIzquierdo.add(panelBotones, BorderLayout.CENTER);

        add(panelIzquierdo, BorderLayout.WEST);

        String[] columnas = {"RUT", "Nombre", "Cursos Asignados"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        tablaProfesores = new JTable(tableModel);
        tablaProfesores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProfesores.getTableHeader().setReorderingAllowed(false);
        
        tablaProfesores.setRowHeight(35); 
        tablaProfesores.getTableHeader().setPreferredSize(new Dimension(0, 40)); 
        tablaProfesores.setShowVerticalLines(false); 
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); 
                return this;
            }
        };
        
        for (int i = 0; i < tablaProfesores.getColumnCount(); i++) {
            tablaProfesores.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(tablaProfesores);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Nómina de Profesores"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        add(scrollPane, BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> {
            if (controlador != null) {
                String rut = txtRut.getText().trim();
                String nombre = txtNombre.getText().trim();
                
                if (rut.isEmpty() || nombre.isEmpty()) {
                    mostrarError("El RUT y el Nombre no pueden estar vacíos.");
                    return;
                }
                
                controlador.onAgregarProfesor(rut, nombre);
                limpiarFormulario();
            }
        });

        btnEliminar.addActionListener(e -> {
            String rut = obtenerRutSeleccionado();
            if (rut != null && controlador != null) {
                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro que desea eliminar al profesor con RUT " + rut + "?", 
                    "Confirmar Eliminación", 
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirmacion == JOptionPane.YES_OPTION) {
                    controlador.onEliminarProfesor(rut);
                }
            }
        });

        btnVerCursos.addActionListener(e -> {
            String rut = obtenerRutSeleccionado();
            if (rut != null && controlador != null) {
                controlador.onSolicitarCursosProfesor(rut);
            }
        });

        btnAsignarCurso.addActionListener(e -> {
            String rut = obtenerRutSeleccionado();
            if (rut != null && controlador != null) {
                String idCurso = JOptionPane.showInputDialog(this, 
                    "Ingrese el ID del curso a asignar:", 
                    "Asignar Curso", 
                    JOptionPane.QUESTION_MESSAGE);
                    
                if (idCurso != null && !idCurso.trim().isEmpty()) {
                    controlador.onAsignarCurso(rut, idCurso.trim());
                }
            }
        });

        btnRemoverCurso.addActionListener(e -> {
            String rut = obtenerRutSeleccionado();
            if (rut != null && controlador != null) {
                String idCurso = JOptionPane.showInputDialog(this, 
                    "Ingrese el ID del curso a remover:", 
                    "Remover Curso", 
                    JOptionPane.WARNING_MESSAGE);
                    
                if (idCurso != null && !idCurso.trim().isEmpty()) {
                    controlador.onRemoverCurso(rut, idCurso.trim());
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
        int fila = tablaProfesores.getSelectedRow();
        if (fila >= 0) {
            return (String) tableModel.getValueAt(fila, 0);
        } else {
            mostrarError("Debe seleccionar un profesor de la tabla para realizar esta acción.");
            return null;
        }
    }

    private void limpiarFormulario() {
        txtRut.setText("");
        txtNombre.setText("");
        txtRut.requestFocus();
    }

    @Override
    public void setControlador(ControladorProfesor controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        if (controlador != null) {
            controlador.onSolicitarListaProfesores();
        }
    }

    @Override
    public void mostrarListaProfesores(Collection<Profesor> profesores) {
        tableModel.setRowCount(0);
        for (Profesor profesor : profesores) {
            tableModel.addRow(new Object[]{
                profesor.getRut(), 
                profesor.getNombre(), 
                profesor.getCursosDictados().size()
            });
        }
    }

    @Override
    public void mostrarCursosDelProfesor(Profesor profesor) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Cursos dictados por ").append(profesor.getNombre()).append(" ===\n\n");
        
        if (profesor.getCursosDictados().isEmpty()) {
            sb.append("(No dicta ningún curso actualmente)\n");
        } else {
            for (Curso curso : profesor.getCursosDictados()) {
                sb.append(" - [").append(curso.getId()).append("] ")
                  .append(curso.getNombre()).append(" (")
                  .append(curso.getCreditos()).append(" créditos)\n");
            }
        }
        
        JOptionPane.showMessageDialog(this, sb.toString(), "Carga Académica: " + profesor.getRut(), JOptionPane.INFORMATION_MESSAGE);
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