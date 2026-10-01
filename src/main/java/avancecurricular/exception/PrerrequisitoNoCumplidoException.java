package avancecurricular.exception;

/**
 * Excepción lanzada cuando un estudiante intenta inscribir una asignatura sin haber
 * aprobado las materias requeridas previamente en su malla.
 */
public class PrerrequisitoNoCumplidoException extends RuntimeException {
    /**
     * Crea la excepción con un mensaje que indica qué curso no se pudo inscribir.
     *
     * @param mensaje Descripción del error, que se muestra al usuario.
     */
    public PrerrequisitoNoCumplidoException(String mensaje) {
        super(mensaje);
    }
}