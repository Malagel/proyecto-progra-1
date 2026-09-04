package avancecurricular;

import avancecurricular.config.ContextoAplicacion;

import java.sql.SQLException;

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

        // INICIO DE LA APLICACIÓN (menu)
        
        /* 
         * Ejemplo de uso:
         * 
         * MenuConsola menu = new MenuConsola(contexto);
         * menu.mostrar();
         * 
         */
        
        try {
            contexto.getUnitOfWork().confirmarCambios();
        } catch (SQLException e) {
            System.err.println("Error al confirmar cambios pendientes en SQLite: " + e.getMessage());
        }
    }
}