package avancecurricular.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {
    private final List<DBAction> colaOperaciones = new ArrayList<>();

    public void registrarAccion(DBAction accion) {
        this.colaOperaciones.add(accion);
    }

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