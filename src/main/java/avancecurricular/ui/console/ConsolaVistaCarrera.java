package avancecurricular.ui.console;

import avancecurricular.model.AsignaturaMalla;
import avancecurricular.model.Carrera;
import avancecurricular.ui.controller.ControladorCarrera;
import avancecurricular.ui.view.VistaCarrera;

import java.util.Collection;

public class ConsolaVistaCarrera implements VistaCarrera {
    private ControladorCarrera controlador;

    @Override
    public void setControlador(ControladorCarrera controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        boolean enSubmenu = true;
        while (enSubmenu) {
            System.out.println("\n--- MÓDULO DE CARRERAS ---");
            System.out.println("1. Listar todas las carreras");
            System.out.println("2. Registrar nueva carrera");
            System.out.println("3. Eliminar una carrera");
            System.out.println("4. Ver malla curricular");
            System.out.println("5. Agregar curso a la malla");
            System.out.println("6. Agregar prerrequisito a un curso");
            System.out.println("0. Volver al menú principal");

            int opcion = LectorConsola.leerEntero("> ");
            switch (opcion) {
                case 1: controlador.onSolicitarListaCarreras(); break;
                case 2: formularioAgregarCarrera(); break;
                case 3: formularioEliminarCarrera(); break;
                case 4: formularioVerMalla(); break;
                case 5: formularioAgregarAsignatura(); break;
                case 6: formularioAgregarPrerrequisito(); break;
                case 0: enSubmenu = false; break;
                default: System.out.println("[!] Opción inválida.");
            }
        }
    }

    private void formularioAgregarCarrera() {
        System.out.println("\n-- Ingreso de Carrera --");
        String id = LectorConsola.leerTexto("ID de la carrera: ");
        String nombre = LectorConsola.leerTexto("Nombre de la carrera: ");
        int creditos = LectorConsola.leerEntero("Créditos totales: ");
        controlador.onAgregarCarrera(id, nombre, creditos);
    }

    private void formularioEliminarCarrera() {
        String id = LectorConsola.leerTexto("ID de la carrera a eliminar: ");
        controlador.onEliminarCarrera(id);
    }

    private void formularioVerMalla() {
        String id = LectorConsola.leerTexto("ID de la carrera: ");
        controlador.onVerDetalleMalla(id);
    }

    private void formularioAgregarAsignatura() {
        System.out.println("\n-- Agregar Curso a la Malla --");
        String idCarrera = LectorConsola.leerTexto("ID de la Carrera: ");
        String idCurso = LectorConsola.leerTexto("ID del Curso a integrar: ");
        int semestre = LectorConsola.leerEntero("Semestre en el que se dictará: ");
        controlador.onAgregarAsignaturaMalla(idCarrera, idCurso, semestre);
    }

    private void formularioAgregarPrerrequisito() {
        System.out.println("\n-- Agregar Prerrequisito --");
        String idCarrera = LectorConsola.leerTexto("ID de la Carrera: ");
        String idCursoDestino = LectorConsola.leerTexto("ID del Curso que requiere el prerrequisito: ");
        String idCursoPre = LectorConsola.leerTexto("ID del Curso que DEBE SER APROBADO PREVIAMENTE: ");
        controlador.onAgregarPrerrequisito(idCarrera, idCursoDestino, idCursoPre);
    }

    @Override
    public void mostrarListaCarreras(Collection<Carrera> carreras) {
        System.out.println("\n=== Catálogo de Carreras ===");
        if (carreras.isEmpty()) {
            System.out.println("(No hay carreras registradas)");
            return;
        }
        for (Carrera carrera : carreras) {
            System.out.println(carrera);
        }
    }

    @Override
    public void mostrarDetalleMalla(Carrera carrera) {
        System.out.println("\n=== Malla Curricular: " + carrera.getNombre() + " ===");
        if (carrera.getPlanDeEstudio().isEmpty()) {
            System.out.println("(No hay asignaturas en esta malla)");
            return;
        }
        for (AsignaturaMalla asignatura : carrera.getPlanDeEstudio()) {
            System.out.println(asignatura);
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println("[ÉXITO] " + mensaje);
    }

    @Override
    public void mostrarError(String error) {
        System.err.println("[ERROR] " + error);
    }
}