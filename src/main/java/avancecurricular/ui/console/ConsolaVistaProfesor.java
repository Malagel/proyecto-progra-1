package avancecurricular.ui.console;

import avancecurricular.model.Curso;
import avancecurricular.model.Profesor;
import avancecurricular.ui.controller.ControladorProfesor;
import avancecurricular.ui.view.VistaProfesor;

import java.util.Collection;

public class ConsolaVistaProfesor implements VistaProfesor {

    private ControladorProfesor controlador;

    @Override
    public void setControlador(ControladorProfesor controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        boolean enSubmenu = true;
        while (enSubmenu) {
            System.out.println("\n--- MÓDULO DE PROFESORES ---");
            System.out.println("1. Listar profesores");
            System.out.println("2. Registrar profesor");
            System.out.println("3. Eliminar profesor");
            System.out.println("4. Asignar curso a profesor");
            System.out.println("5. Remover curso de profesor");
            System.out.println("6. Ver cursos dictados por profesor");
            System.out.println("0. Volver al menú principal");

            int opcion = LectorConsola.leerEntero("> ");
            switch (opcion) {
                case 1:
                    controlador.onSolicitarListaProfesores();
                    break;
                case 2:
                    formularioAgregarProfesor();
                    break;
                case 3:
                    formularioEliminarProfesor();
                    break;
                case 4:
                    formularioAsignarCurso();
                    break;
                case 5:
                    formularioRemoverCurso();
                    break;
                case 6:
                    formularioVerCursos();
                    break;
                case 0:
                    enSubmenu = false;
                    break;
                default:
                    System.out.println("[!] Opción inválida.");
            }
        }
    }

    private void formularioAgregarProfesor() {
        System.out.println("\n-- Ingreso de Profesor --");
        String rut = LectorConsola.leerTexto("RUT: ");
        String nombre = LectorConsola.leerTexto("Nombre completo: ");
        controlador.onAgregarProfesor(rut, nombre);
    }

    private void formularioEliminarProfesor() {
        String rut = LectorConsola.leerTexto("RUT del profesor a eliminar: ");
        controlador.onEliminarProfesor(rut);
    }

    private void formularioAsignarCurso() {
        String rut = LectorConsola.leerTexto("RUT del profesor: ");
        String idCurso = LectorConsola.leerTexto("ID del curso a asignar: ");
        controlador.onAsignarCurso(rut, idCurso);
    }

    private void formularioRemoverCurso() {
        String rut = LectorConsola.leerTexto("RUT del profesor: ");
        String idCurso = LectorConsola.leerTexto("ID del curso a remover: ");
        controlador.onRemoverCurso(rut, idCurso);
    }

    private void formularioVerCursos() {
        String rut = LectorConsola.leerTexto("RUT del profesor: ");
        controlador.onSolicitarCursosProfesor(rut);
    }

    @Override
    public void mostrarListaProfesores(Collection<Profesor> profesores) {
        System.out.println("\n=== Nómina de Profesores ===");
        if (profesores.isEmpty()) {
            System.out.println("(No hay profesores registrados)");
            return;
        }
        for (Profesor profesor : profesores) {
            System.out.println(profesor);
        }
    }

    @Override
    public void mostrarCursosDelProfesor(Profesor profesor) {
        System.out.println("\n=== Cursos dictados por " + profesor.getNombre() + " ===");
        if (profesor.getCursosDictados().isEmpty()) {
            System.out.println("(No dicta ningún curso actualmente)");
            return;
        }
        for (Curso curso : profesor.getCursosDictados()) {
            System.out.println(curso);
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