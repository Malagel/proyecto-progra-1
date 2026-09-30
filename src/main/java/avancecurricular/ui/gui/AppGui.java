package avancecurricular.ui.gui;

import avancecurricular.config.ContextoAplicacion;
import avancecurricular.ui.controller.*;
import javax.swing.*;
import java.awt.Toolkit;
import java.util.Enumeration;

/**
 * Ensamblador del entorno Gráfico (Swing). Configura el entorno visual del sistema operativo host,
 * aplica correcciones de resolución, e instancia la jerarquía de ventanas y controladores.
 */
public class AppGui {
    public static void iniciar(ContextoAplicacion contexto) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            aplicarEscaladoDPI();
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
     * Extrae la resolución del monitor actual y aplica un factor de escala (multiplier) dinámico
     * a todas las fuentes del {@link UIManager} si detecta una pantalla de alta densidad (HiDPI/4K).
     * Esto garantiza que la interfaz mantenga proporciones legibles en monitores modernos.
     */
    private static void aplicarEscaladoDPI() {
        int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        float scale = dpi / 96.0f;

        if (scale > 1.0f) {
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof java.awt.Font) {
                    java.awt.Font font = (java.awt.Font) value;
                    UIManager.put(key, font.deriveFont(font.getSize2D() * scale));
                }
            }
        }
    }
}