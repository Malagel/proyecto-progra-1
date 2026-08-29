package avancecurricular.repository;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface DBAction {
    void ejecutar(Connection conn) throws SQLException;
}