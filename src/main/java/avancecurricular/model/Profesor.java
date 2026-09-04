package avancecurricular.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Profesor extends Persona {
    private final Set<Curso> cursosDictados;

    public Profesor(String rut, String nombre) {
        super(rut, nombre);
        this.cursosDictados = new HashSet<>();
    }

    public Profesor(String rut, String nombre, Set<Curso> cursosDictados) {
        super(rut, nombre);
        this.cursosDictados = (cursosDictados != null) ? new HashSet<>(cursosDictados) : new HashSet<>();
    }
    
    public void asignarCurso(Curso curso) {
        Objects.requireNonNull(curso, "El curso no puede ser nulo.");
        this.cursosDictados.add(curso);
    }

    public void removerCurso(Curso curso) {
        this.cursosDictados.remove(curso);
    }

    public Set<Curso> getCursosDictados() {
        return Collections.unmodifiableSet(this.cursosDictados);
    }

    @Override
    public String toString() {
        return "Profesor{" +
                "rut='" + getRut() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", totalCursosDictados=" + cursosDictados.size() +
                '}';
    }
}