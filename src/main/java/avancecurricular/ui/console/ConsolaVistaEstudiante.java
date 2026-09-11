package avancecurricular.ui.console;

import avancecurricular.model.Estudiante;
import avancecurricular.model.RegistroAcademico;
import avancecurricular.ui.controller.ControladorEstudiante;
import avancecurricular.ui.view.VistaEstudiante;

import java.util.Collection;

public class ConsolaVistaEstudiante implements VistaEstudiante {

    private ControladorEstudiante controlador;

    @Override
    public void setControlador(ControladorEstudiante controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        boolean enSubmenu = true;
        while (enSubmenu) {
            System.out.println("\n--- MÓDULO DE ESTUDIANTES ---");
            System.out.println("1. Listar estudiantes");
            System.out.println("2. Registrar estudiante");
            System.out.println("3. Eliminar estudiante");
            System.out.println("4. Inscribir curso a estudiante");
            System.out.println("5. Actualizar registro (calificar)");
            System.out.println("6. Ver registros académicos");
            System.out.println("7. Retirar/Desinscribir curso");
            System.out.println("0. Volver al menú principal");

            int opcion = LectorConsola.leerEntero("> ");
            switch (opcion) {
                case 1: controlador.onSolicitarListaEstudiantes(); break;
                case 2: formularioAgregarEstudiante(); break;
                case 3: formularioEliminarEstudiante(); break;
                case 4: formularioInscribirCurso(); break;
                case 5: formularioActualizarRegistro(); break;
                case 6: formularioVerRegistros(); break;
                case 7: formularioDesinscribirCurso(); break;
                case 0: enSubmenu = false; break;
                default: System.out.println("[!] Opción inválida.");
            }
        }
    }

    private void formularioAgregarEstudiante() {
        System.out.println("\n-- Ingreso de Estudiante --");
        String rut = LectorConsola.leerTexto("RUT: ");
        String nombre = LectorConsola.leerTexto("Nombre completo: ");
        String idCarrera = LectorConsola.leerTexto("ID de la carrera asociada: ");
        controlador.onAgregarEstudiante(rut, nombre, idCarrera);
    }

    private void formularioEliminarEstudiante() {
        String rut = LectorConsola.leerTexto("RUT del estudiante a eliminar: ");
        controlador.onEliminarEstudiante(rut);
    }

    private void formularioInscribirCurso() {
        String rut = LectorConsola.leerTexto("RUT del estudiante: ");
        String idCurso = LectorConsola.leerTexto("ID del curso a inscribir: ");
        controlador.onInscribirCurso(rut, idCurso);
    }

    private void formularioActualizarRegistro() {
        String rut = LectorConsola.leerTexto("RUT del estudiante: ");
        String idCurso = LectorConsola.leerTexto("ID del curso a actualizar: ");
        double nota = LectorConsola.leerDecimal("Nota (ej: 5.5, 0.0 si está cursando): ");
        String estado = LectorConsola.leerTexto("Estado (APROBADO, REPROBADO, CURSANDO): ");
        controlador.onActualizarRegistro(rut, idCurso, nota, estado);
    }

    private void formularioVerRegistros() {
        String rut = LectorConsola.leerTexto("RUT del estudiante: ");
        controlador.onSolicitarRegistros(rut);
    }

    private void formularioDesinscribirCurso() {
        String rut = LectorConsola.leerTexto("RUT del estudiante: ");
        String idCurso = LectorConsola.leerTexto("ID del curso a desinscribir: ");
        controlador.onDesinscribirCurso(rut, idCurso);
    }
    
    @Override
    public void mostrarListaEstudiantes(Collection<Estudiante> estudiantes) {
        System.out.println("\n=== Matrícula de Estudiantes ===");
        if (estudiantes.isEmpty()) {
            System.out.println("(No hay estudiantes registrados)");
            return;
        }
        for (Estudiante estudiante : estudiantes) {
            System.out.println(estudiante);
        }
    }

@Override
    public void mostrarRegistrosAcademicos(Estudiante estudiante) {
        System.out.println("\n=== Expediente Académico: " + estudiante.getNombre() + " ===");
        System.out.println("Carrera: " + estudiante.getCarrera().getNombre());
        
        System.out.printf("Avance Curricular: %.1f%%\n", estudiante.calcularPorcentajeAvance());
        System.out.println("Créditos Aprobados: " + estudiante.obtenerCreditosAprobados() + " / " + estudiante.getCarrera().getCreditosTotales());
        System.out.println("--------------------------------------------------");

        if (estudiante.getRegistrosAcademicos().isEmpty()) {
            System.out.println("(No posee registros académicos)");
            return;
        }
        
        for (RegistroAcademico registro : estudiante.getRegistrosAcademicos()) {
            System.out.printf("- [%s] %s | Nota: %.1f | Estado: %s\n", 
                registro.getCurso().getId(),
                registro.getCurso().getNombre(),
                registro.getNota(),
                registro.getEstado()
            );
        }
        System.out.println("==================================================");
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