package avancecurricular.model;

import java.util.Objects;
import java.util.Set;

public class RegistroAcademico {

    public static final String ESTADO_APROBADO = "APROBADO";
    public static final String ESTADO_REPROBADO = "REPROBADO";
    public static final String ESTADO_CURSANDO = "CURSANDO";

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
        ESTADO_APROBADO, 
        ESTADO_REPROBADO, 
        ESTADO_CURSANDO
    );

    private final Curso curso;
    private double nota;
    private String estado;

    public RegistroAcademico(Curso curso, double nota, String estado) {
        this.curso = Objects.requireNonNull(curso, "El curso no puede ser nulo.");
        validarEstado(estado);
        validarNota(nota, estado);
        
        this.nota = nota;
        this.estado = estado.toUpperCase().trim();
    }

    private void validarEstado(String estado) {
        Objects.requireNonNull(estado, "El estado no puede ser nulo.");
        String estNormalizado = estado.toUpperCase().trim();
        if (!ESTADOS_VALIDOS.contains(estNormalizado)) {
            throw new IllegalArgumentException("Estado inválido: '" + estado + "'. Valores permitidos: " + ESTADOS_VALIDOS);
        }
    }

    private void validarNota(double nota, String estado) {
        if (ESTADO_CURSANDO.equalsIgnoreCase(estado)) {
            return;
        }
        if (nota < 1.0 || nota > 7.0) {
            throw new IllegalArgumentException("La nota debe estar en el rango de 1.0 a 7.0 (recibido: " + nota + ")");
        }
    }

    public boolean esAprobado() {
        return ESTADO_APROBADO.equals(this.estado);
    }

    public Curso getCurso() {
        return this.curso;
    }

    public double getNota() {
        return this.nota;
    }

    public String getEstado() {
        return this.estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroAcademico that = (RegistroAcademico) o;
        return Objects.equals(curso, that.curso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curso);
    }

    @Override
    public String toString() {
        return "RegistroAcademico{" +
                "curso=" + curso.getNombre() + " (" + curso.getId() + ")" +
                ", nota=" + nota +
                ", estado='" + estado + '\'' +
                '}';
    }
}