package avancecurricular.model;

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * Representa la instanciación de un curso dentro del plan de estudios de una carrera,
 * definiendo el semestre en el que se dicta y los prerrequisitos asociados.
 */
public class AsignaturaMalla {
    private final Curso curso;
    private final Set<Curso> prerrequisitos;   
    private int numeroSemestre;
    
    public AsignaturaMalla(Curso curso, int numeroSemestre) {
        this.curso = Objects.requireNonNull(curso, "El curso no puede ser nulo");
        this.prerrequisitos = new HashSet<>();

        setNumeroSemestre(numeroSemestre);
    }

    /**
     * Agrega un curso como prerrequisito obligatorio para esta asignatura.
     *
     * @param curso El curso que debe ser aprobado previamente.
     * @throws IllegalArgumentException si el curso a agregar es la misma asignatura.
     */
    public void addPrerrequisito(Curso curso) {
        Objects.requireNonNull(curso, "El prerrequisito no puede ser nulo.");

        if (this.curso.equals(curso)) {
            throw new IllegalArgumentException("No es posible agregar a un curso como su mismo prerrequisito");
        }

        this.prerrequisitos.add(curso);
    }

    /**
     * Remueve un curso de la lista de prerrequisitos de esta asignatura.
     *
     * @param curso El curso a remover.
     * @throws IllegalArgumentException si el curso no existe en los prerrequisitos.
     */
    public void removePrerrequisito(Curso curso) {
        if (!this.prerrequisitos.remove(curso)) {
            throw new IllegalArgumentException("No se puede remover prerrequisito. No existe o es nulo.");
        }
    }
    public Curso getCurso() {
        return this.curso;
    }

    public int getNumeroSemestre() { 
        return this.numeroSemestre; 
    }

    public Set<Curso> getPrerrequisitos() {
        return Collections.unmodifiableSet(this.prerrequisitos);
    }

    public final void setNumeroSemestre(int numeroSemestre) {
        if (numeroSemestre <= 0) {
            throw new IllegalArgumentException("El número del semestre no puede ser menor o igual a cero.");
        }
        this.numeroSemestre = numeroSemestre;
    }

    /**
     * La igualdad de esta entidad se evalúa de manera estricta mediante su identificador único,
     * ignorando sus atributos mutables. Esto garantiza su consistencia y estabilidad 
     * al ser almacenada en colecciones basadas en hashes (ej. {@link java.util.HashSet}).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsignaturaMalla that = (AsignaturaMalla) o;
        return Objects.equals(curso, that.curso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curso);
    }

    @Override
    public String toString() {
        return String.format("Semestre %d: %s (Prerrequisitos: %d)", 
                this.numeroSemestre, this.curso.getNombre(), this.prerrequisitos.size());
    }
}
