// main/java/avancecurricular/ui/gui/AppGui.java
package avancecurricular.ui.gui;

import avancecurricular.config.ContextoAplicacion;
import avancecurricular.ui.controller.*;
import javax.swing.*;
import java.awt.Toolkit;
import java.util.Enumeration;

public class AppGui {
    public static void iniciar(ContextoAplicacion contexto) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            aplicarEscaladoDPI(); // Calcula y aplica el tamaño correcto según tu monitor
        } catch (Exception e) {
            System.err.println("No se pudo cargar el diseño del sistema.");
        }

        GuiVistaPrincipal vistaPrincipal = new GuiVistaPrincipal();
        GuiVistaCurso vistaCurso = new GuiVistaCurso();
        GuiVistaCarrera vistaCarrera = new GuiVistaCarrera();
        GuiVistaProfesor vistaProfesor = new GuiVistaProfesor();
        GuiVistaEstudiante vistaEstudiante = new GuiVistaEstudiante();

        vistaPrincipal.agregarPestana("Cursos", vistaCurso);
        vistaPrincipal.agregarPestana("Carreras", vistaCarrera);
        vistaPrincipal.agregarPestana("Profesores", vistaProfesor);
        vistaPrincipal.agregarPestana("Estudiantes", vistaEstudiante);

        new ControladorCurso(vistaCurso, contexto.getCursoService(), contexto.getCarreraService(), contexto.getEstudianteService(), contexto.getProfesorService());
        new ControladorCarrera(vistaCarrera, contexto.getCarreraService(), contexto.getEstudianteService(), contexto.getCursoService());
        new ControladorProfesor(vistaProfesor, contexto.getProfesorService(), contexto.getCursoService());
        new ControladorEstudiante(vistaEstudiante, contexto.getEstudianteService(), contexto.getCarreraService(), contexto.getCursoService());
        new ControladorPrincipal(vistaPrincipal, vistaCurso, vistaCarrera, vistaProfesor, vistaEstudiante);

        vistaPrincipal.iniciar();
    }

    /**
     * Calcula la resolución del monitor actual y aumenta dinámicamente 
     * el tamaño de todas las fuentes de la interfaz si es una pantalla 4K/HiDPI.
     */
    private static void aplicarEscaladoDPI() {
        int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        float scale = dpi / 96.0f; // 96 DPI es el estándar base de pantallas antiguas

        if (scale > 1.0f) {
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                // Si la propiedad es una fuente, le aplicamos el multiplicador
                if (value instanceof java.awt.Font) {
                    java.awt.Font font = (java.awt.Font) value;
                    UIManager.put(key, font.deriveFont(font.getSize2D() * scale));
                }
            }
        }
    }
}