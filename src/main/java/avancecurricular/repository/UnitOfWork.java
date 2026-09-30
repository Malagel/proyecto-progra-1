package avancecurricular.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementa el patrón Unit of Work para agrupar múltiples operaciones de base de datos
 * en una única transacción atómica, asegurando la consistencia de los datos.
 * <p>
 * <b>Advertencia de concurrencia:</b> Esta clase <b>no es thread-safe</b> ya que mantiene un 
 * estado interno mutable (la cola de operaciones pendientes). Su ciclo de vida debe estar 
 * estrictamente limitado al hilo de la transacción actual y nunca debe compartirse en entornos concurrentes.
 */
public class UnitOfWork {
    private final List<DBAction> colaOperaciones = new ArrayList<>();

    /**
     * Añade una nueva operación a la cola de acciones pendientes de confirmación.
     *
     * @param accion Operación a ejecutar en la base de datos.
     */
    public void registrarAccion(DBAction accion) {
        this.colaOperaciones.add(accion);
    }

    /**
     * Ejecuta todas las operaciones encoladas dentro de una transacción.
     * En caso de error, realiza un rollback automático para revertir los cambios.
     *
     * @throws SQLException si ocurre un error durante el volcado a la base de datos.
     */
    public void confirmarCambios() throws SQLException {
        if (colaOperaciones.isEmpty()) {
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            for (DBAction accion : colaOperaciones) {
                accion.ejecutar(conn);
            }

            conn.commit();
            colaOperaciones.clear();

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}   