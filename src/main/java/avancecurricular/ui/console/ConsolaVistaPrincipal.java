package avancecurricular.ui.console;

import avancecurricular.ui.controller.ControladorPrincipal;
import avancecurricular.ui.view.VistaPrincipal;

public class ConsolaVistaPrincipal implements VistaPrincipal {
    private ControladorPrincipal controlador;

    @Override
    public void setControlador(ControladorPrincipal controlador) {
        this.controlador = controlador;
    }

    @Override
    public void iniciar() {
        boolean ejecutando = true;
        while (ejecutando) {
            System.out.println("\n=================================");
            System.out.println("   SISTEMA AVANCE CURRICULAR    ");
            System.out.println("=================================");
            System.out.println("1. Gestionar Cursos");
            System.out.println("2. Gestionar Carreras");
            System.out.println("3. Gestionar Profesores");
            System.out.println("4. Gestionar Estudiantes");
            System.out.println("0. Salir y Guardar Cambios");
            
            int opcion = LectorConsola.leerEntero("> ");

            switch (opcion) {
                case 1:
                    controlador.onNavegarACursos();
                    break;
                case 2:
                    controlador.onNavegarACarreras();
                    break;
                case 3:
                    controlador.onNavegarAProfesores();
                    break;
                case 4:
                    controlador.onNavegarAEstudiantes();
                    break;
                case 0:
                    ejecutando = false;
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("[!] Opción no válida.");
            }
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    @Override
    public void mostrarError(String error) {
        System.err.println("[ERROR] " + error);
    }
}