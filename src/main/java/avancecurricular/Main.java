package avancecurricular;

import avancecurricular.config.ContextoAplicacion;
import avancecurricular.ui.console.AppConsola;
// import avancecurricular.ui.gui.AppGui;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ContextoAplicacion contexto = new ContextoAplicacion();

        try {
            contexto.inicializarDatos();
        } catch (RuntimeException e) {
            System.err.println("El sistema no pudo iniciar debido a un error en la base de datos.");
            e.printStackTrace();
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("==========================================");
        System.out.println("   SELECCIONE MODO DE VISUALIZACIÓN       ");
        System.out.println("==========================================");
        System.out.println("1. Consola de comandos (Terminal)");
        System.out.println("2. Interfaz Gráfica (Ventanas)");
        System.out.print("> ");

        String opcion = scanner.nextLine().trim();

        switch (opcion) {
            case "1":
                AppConsola.iniciar(contexto);
                break;
            case "2":
                // AppGui.iniciar(contexto);
                break;
            default:
                System.out.println("Opción no reconocida. Iniciando modo consola por defecto.");
                AppConsola.iniciar(contexto);
                break;
        }

        try {
            contexto.getUnitOfWork().confirmarCambios();
            System.out.println("Datos sincronizados en base de datos correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al confirmar cambios pendientes: " + e.getMessage());
        }
    }
}