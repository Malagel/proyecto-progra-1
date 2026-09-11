package avancecurricular.ui.console;

import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.model.Profesor;
import avancecurricular.ui.controller.ControladorCurso;
import avancecurricular.ui.view.VistaCurso;
import java.util.Collection;

public class ConsolaVistaCurso implements VistaCurso {
    private ControladorCurso controlador;

    @Override
    public void setControlador(ControladorCurso controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        boolean enSubmenu = true;
        while (enSubmenu) {
            System.out.println("\n--- MÓDULO DE CURSOS ---");
            System.out.println("1. Listar todos los cursos");
            System.out.println("2. Registrar nuevo curso");
            System.out.println("3. Eliminar un curso");
            System.out.println("0. Volver al menú principal");

            int opcion = LectorConsola.leerEntero("> ");

            switch (opcion) {
                case 1:
                    controlador.onSolicitarListaCursos();
                    break;
                case 2:
                    formularioAgregarCurso();
                    break;
                case 3:
                    formularioEliminarCurso();
                    break;
                case 0:
                    enSubmenu = false;
                    break;
                default:
                    System.out.println("[!] Opción inválida.");
            }
        }
    }

    private void formularioAgregarCurso() {
        System.out.println("\n-- Ingreso de Curso --");
        String id = LectorConsola.leerTexto("ID (Sigla): ");
        String nombre = LectorConsola.leerTexto("Nombre del curso: ");
        int creditos = LectorConsola.leerEntero("Cantidad de créditos: ");

        controlador.onAgregarCurso(id, nombre, creditos);
    }

    private void formularioEliminarCurso() {
        String id = LectorConsola.leerTexto("ID del curso a eliminar: ");
        controlador.onEliminarCurso(id);
    }

    @Override
    public void mostrarListaCursos(Collection<Curso> cursos) {
        System.out.println("\n=== Catálogo de Cursos ===");
        if (cursos.isEmpty()) {
            System.out.println("(No hay cursos registrados)");
            return;
        }
        for (Curso curso : cursos) {
            System.out.println(curso);
        }
    }

    @Override
    public void mostrarDetalleCurso(Curso curso, Collection<Carrera> carreras, Collection<Profesor> profesores) {
        System.out.println("\n=== FICHA DEL CURSO ===");
        System.out.println("ID: " + curso.getId());
        System.out.println("Nombre: " + curso.getNombre());
        System.out.println("Créditos: " + curso.getCreditos());
        System.out.println("---------------------------------");
        
        System.out.println("Carreras que lo incluyen en su malla:");
        if (carreras.isEmpty()) {
            System.out.println("  (Ninguna carrera incluye este curso aún)");
        } else {
            for (Carrera c : carreras) {
                System.out.println("  - " + c.getNombre());
            }
        }

        System.out.println("---------------------------------");
        System.out.println("Profesores asignados para dictarlo:");
        if (profesores.isEmpty()) {
            System.out.println("  (Sin profesores asignados)");
        } else {
            for (Profesor p : profesores) {
                System.out.println("  - " + p.getNombre() + " (RUT: " + p.getRut() + ")");
            }
        }
        System.out.println("=================================");
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