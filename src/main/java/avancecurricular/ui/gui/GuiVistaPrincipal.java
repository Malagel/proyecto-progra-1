// main/java/avancecurricular/ui/gui/GuiVistaPrincipal.java
package avancecurricular.ui.gui;

import avancecurricular.ui.controller.ControladorPrincipal;
import avancecurricular.ui.view.VistaPrincipal;

import javax.swing.*;
import java.awt.*;

public class GuiVistaPrincipal extends JFrame implements VistaPrincipal {
    private ControladorPrincipal controlador;
    private final JTabbedPane tabbedPane;

    public GuiVistaPrincipal() {
        setTitle("Sistema Avance Curricular");
        setSize(1000, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // 1. Contenedor de pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); 

        add(tabbedPane, BorderLayout.CENTER);
        
        tabbedPane.addChangeListener(e -> {
            if (controlador == null) return;
            switch (tabbedPane.getSelectedIndex()) {
                case 0: controlador.onNavegarACursos(); break;
                case 1: controlador.onNavegarACarreras(); break;
                case 2: controlador.onNavegarAProfesores(); break;
                case 3: controlador.onNavegarAEstudiantes(); break;
            }
        });

        // 2. Barra inferior dinámica
        // Usamos BorderLayout para la barra inferior, alineando el botón al ESTE (Derecha)
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Usamos HTML para que herede la fuente escalada por DPI, permitiendo auto-wrapping y
        // añadiendo padding interno (10px arriba/abajo, 40px a los lados) para darle volumen nativo.
        JButton btnSalir = new JButton("<html><p style='text-align:center; padding: 10px 40px;'><b>Guardar cambios y Salir</b></p></html>");
        
        btnSalir.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Desea sincronizar los datos y cerrar la aplicación?", 
                "Salir del Sistema", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
                
            if (confirmacion == JOptionPane.YES_OPTION) {
                System.exit(0); 
            }
        });

        panelInferior.add(btnSalir, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);
    }

    public void agregarPestana(String titulo, JPanel panel) {
        tabbedPane.addTab(titulo, panel);
        
        JLabel tabLabel = new JLabel(titulo, SwingConstants.CENTER);
        tabLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 
        
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tabLabel);
    }

    @Override
    public void setControlador(ControladorPrincipal controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        setVisible(true);
        if (controlador != null) {
            controlador.onNavegarACursos();
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void mostrarError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}