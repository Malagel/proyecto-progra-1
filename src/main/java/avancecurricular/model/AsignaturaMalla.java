package avancecurricular.model;

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class AsignaturaMalla {
    private final Curso curso;
    private final Set<Curso> prerrequisitos;   
    private int numeroSemestre;
    
    public AsignaturaMalla(Curso curso, int numeroSemestre) {
        this.curso = Objects.requireNonNull(curso, "El curso no puede ser nulo");
        prerrequisitos = new HashSet<>();

        setNumeroSemestre(numeroSemestre);
    }

    public void addPrerrequisito(Curso curso) {
        Objects.requireNonNull(curso, "El prerrequisito no puede ser nulo.");

        if (this.curso.equals(curso)) {
            throw new IllegalArgumentException("No es posible agregar a un curso como su mismo prerrequisito");
        }

        prerrequisitos.add(curso);
    }

    public void removePrerrequisito(Curso curso) {
        if (!prerrequisitos.remove(curso)) {
            throw new IllegalArgumentException("No se puede remover prerrequisito. No existe o es nulo.");
        }
    }
    public Curso getCurso() {
        return curso;
    }

    public int getNumeroSemestre() { 
        return numeroSemestre; 
    }

    public Set<Curso> getPrerrequisitos() {
        return Collections.unmodifiableSet(prerrequisitos);
    }

    public final void setNumeroSemestre(int numeroSemestre) {
        if (numeroSemestre <= 0) {
            throw new IllegalArgumentException("El número del semestre no puede ser menor o igual a cero.");
        }
        this.numeroSemestre = numeroSemestre;
    }
    
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
                numeroSemestre, curso.getNombre(), prerrequisitos.size());
    }
}
