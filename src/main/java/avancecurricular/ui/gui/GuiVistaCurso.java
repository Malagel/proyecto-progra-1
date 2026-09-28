package avancecurricular.ui.gui;

import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.model.Profesor;
import avancecurricular.ui.controller.ControladorCurso;
import avancecurricular.ui.view.VistaCurso;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;

public class GuiVistaCurso extends JPanel implements VistaCurso {
    private ControladorCurso controlador;

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtCreditos;
    private DefaultTableModel tableModel;
    private JTable tablaCursos;

    public GuiVistaCurso() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 20)); 
        panelIzquierdo.setPreferredSize(new Dimension(350, 0)); 
        
        JPanel panelFormulario = new JPanel(new BorderLayout(0, 15));
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Registrar Curso"),
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

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 10)); 
        panelBotones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Acciones"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JButton btnDetalles = new JButton("<html><p style='text-align:center;'>Ver Detalles</p></html>");
        JButton btnEliminar = new JButton("<html><p style='text-align:center;'>Eliminar Curso</p></html>");
        JButton btnModificar = new JButton("<html><p style='text-align:center;'>Modificar Curso</p></html>");

        panelBotones.add(btnDetalles);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnModificar);

        panelIzquierdo.add(panelFormulario, BorderLayout.NORTH);
        panelIzquierdo.add(panelBotones, BorderLayout.CENTER);

        add(panelIzquierdo, BorderLayout.WEST);

        String[] columnas = {"ID", "Nombre", "Créditos"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        tablaCursos = new JTable(tableModel);
        tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCursos.getTableHeader().setReorderingAllowed(false);
        
        tablaCursos.setRowHeight(35); 
        tablaCursos.getTableHeader().setPreferredSize(new Dimension(0, 40)); 
        tablaCursos.setShowVerticalLines(false); 
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); 
                return this;
            }
        };
        
        for (int i = 0; i < tablaCursos.getColumnCount(); i++) {
            tablaCursos.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(tablaCursos);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Catálogo de Cursos"),
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
                    
                    controlador.onAgregarCurso(id, nombre, creditos);
                    limpiarFormulario();
                } catch (NumberFormatException ex) {
                    mostrarError("Los créditos deben ser un número entero válido.");
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaCursos.getSelectedRow();
            if (filaSeleccionada >= 0 && controlador != null) {
                String id = (String) tableModel.getValueAt(filaSeleccionada, 0);
                
                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro que desea eliminar el curso " + id + "?", 
                    "Confirmar Eliminación", 
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirmacion == JOptionPane.YES_OPTION) {
                    controlador.onEliminarCurso(id);
                }
            } else {
                mostrarError("Debe seleccionar un curso de la tabla para eliminarlo.");
            }
        });

        btnDetalles.addActionListener(e -> {
            int filaSeleccionada = tablaCursos.getSelectedRow();
            if (filaSeleccionada >= 0 && controlador != null) {
                String id = (String) tableModel.getValueAt(filaSeleccionada, 0);
                controlador.onConsultarDetalleCurso(id);
            } else {
                mostrarError("Debe seleccionar un curso de la tabla para ver sus detalles.");
            }
        });

        btnModificar.addActionListener(e -> {
            int filaSeleccionada = tablaCursos.getSelectedRow();
            if (filaSeleccionada >= 0 && controlador != null) {
                String id = (String) tableModel.getValueAt(filaSeleccionada, 0);
                
                JTextField txtNombre = new JTextField();
                JTextField txtCreditos = new JTextField();
                Object[] inputs = {
                    "Nuevo Nombre:", txtNombre,
                    "Nuevos Créditos:", txtCreditos
                };
                
                int option = JOptionPane.showConfirmDialog(this, inputs, "Modificar Curso: " + id, JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String nombre = txtNombre.getText().trim();
                        int creditos = Integer.parseInt(txtCreditos.getText().trim());
                        
                        if (nombre.isEmpty()) {
                            mostrarError("El nombre no puede estar vacío.");
                            return;
                        }
                        
                        controlador.onModificarCurso(id, nombre, creditos);
                        controlador.onSolicitarListaCursos(); 
                    } catch (NumberFormatException ex) {
                        mostrarError("Los créditos deben ser un número entero válido.");
                    }
                }
            } else {
                mostrarError("Debe seleccionar un curso de la tabla para modificarlo.");
            }
        });
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtCreditos.setText("");
        txtId.requestFocus();
    }

    @Override
    public void setControlador(ControladorCurso controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        if (controlador != null) {
            controlador.onSolicitarListaCursos();
        }
    }

    @Override
    public void mostrarListaCursos(Collection<Curso> cursos) {
        tableModel.setRowCount(0);
        for (Curso curso : cursos) {
            tableModel.addRow(new Object[]{
                curso.getId(), 
                curso.getNombre(), 
                curso.getCreditos()
            });
        }
    }

    @Override
    public void mostrarDetalleCurso(Curso curso, Collection<Carrera> carreras, Collection<Profesor> profesores) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FICHA DEL CURSO ===\n");
        sb.append("ID: ").append(curso.getId()).append("\n");
        sb.append("Nombre: ").append(curso.getNombre()).append("\n");
        sb.append("Créditos: ").append(curso.getCreditos()).append("\n");
        sb.append("---------------------------------\n");
        
        sb.append("Carreras que lo incluyen en su malla:\n");
        if (carreras.isEmpty()) {
            sb.append("  (Ninguna carrera incluye este curso aún)\n");
        } else {
            for (Carrera c : carreras) {
                sb.append("  - ").append(c.getNombre()).append("\n");
            }
        }
        
        sb.append("---------------------------------\n");
        sb.append("Profesores asignados para dictarlo:\n");
        if (profesores.isEmpty()) {
            sb.append("  (Sin profesores asignados)\n");
        } else {
            for (Profesor p : profesores) {
                sb.append("  - ").append(p.getNombre()).append(" (RUT: ").append(p.getRut()).append(")\n");
            }
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Detalles del Curso: " + curso.getId(), JOptionPane.INFORMATION_MESSAGE);
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