package avancecurricular.ui.console;

import avancecurricular.config.ContextoAplicacion;
import avancecurricular.ui.controller.*;
import avancecurricular.ui.view.*;

/**
 * Ensamblador del entorno de Consola. Se encarga de instanciar las vistas de terminal
 * y orquestar la inyección de dependencias uniendo cada vista con su controlador correspondiente.
 */
public class AppConsola {
    public static void iniciar(ContextoAplicacion contexto) {
        VistaPrincipal vistaPrincipal = new ConsolaVistaPrincipal();
        VistaCurso vistaCurso = new ConsolaVistaCurso();
        VistaCarrera vistaCarrera = new ConsolaVistaCarrera();
        VistaProfesor vistaProfesor = new ConsolaVistaProfesor();
        VistaEstudiante vistaEstudiante = new ConsolaVistaEstudiante();

        new ControladorCurso(
            vistaCurso,
            contexto.getCursoService(),
            contexto.getCarreraService(),
            contexto.getEstudianteService(),
            contexto.getProfesorService()
        );

        new ControladorCarrera(
            vistaCarrera,
            contexto.getCarreraService(),
            contexto.getEstudianteService(),
            contexto.getCursoService()
        );

        new ControladorProfesor(
            vistaProfesor,
            contexto.getProfesorService(),
            contexto.getCursoService()
        );
        
        new ControladorEstudiante(
        	    vistaEstudiante,
        	    contexto.getEstudianteService(),
        	    contexto.getCarreraService(),
        	    contexto.getCursoService()
        	);

        new ControladorPrincipal(
            vistaPrincipal,
            vistaCurso,
            vistaCarrera,
            vistaProfesor,
            vistaEstudiante
        );

        vistaPrincipal.iniciar();
    }
}