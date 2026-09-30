package avancecurricular.exception;

/**
 * Excepción lanzada cuando un estudiante intenta inscribir una asignatura sin haber
 * aprobado las materias requeridas previamente en su malla.
 */
public class PrerrequisitoNoCumplidoException extends RuntimeException {
    public PrerrequisitoNoCumplidoException(String mensaje) {
        super(mensaje);
    }
}