package avancecurricular.ui.gui;

import avancecurricular.ui.controller.ControladorPrincipal;
import avancecurricular.ui.view.VistaPrincipal;

import javax.swing.*;
import java.awt.*;

/**
 * Contenedor principal de la interfaz gráfica. Implementa un sistema de navegación
 * basado en un {@link JTabbedPane}, donde cada pestaña representa un submódulo de la aplicación.
 */
public class GuiVistaPrincipal extends JFrame implements VistaPrincipal {
    private ControladorPrincipal controlador;
    private final JTabbedPane tabbedPane;

    public GuiVistaPrincipal() {
        setTitle("Sistema Avance Curricular");
        setSize(1000, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

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

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

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

    /**
     * Agrega un panel como una nueva pestaña en la ventana principal, inyectando código HTML 
     * en el título para forzar el padding y lograr un diseño más robusto.
     *
     * @param titulo Nombre de la pestaña.
     * @param panel  El componente (vista) a renderizar en el cuerpo de la pestaña.
     */
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