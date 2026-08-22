package avancecurricular.model;

import java.util.Objects;

public class Curso {
    private final String id;
    private String nombre;
    private int creditos;

    public Curso(String id, String nombre, int creditos) {
        this.id = Objects.requireNonNull(id, "El ID no puede ser nulo");

        setNombre(nombre);
        setCreditos(creditos);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

	public int getCreditos() {
		return creditos;
	}

    public final void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
    }

    public final void setCreditos(int creditos) {
        if (creditos <= 0) {
            throw new IllegalArgumentException("Los créditos no pueden ser menores o igual a cero.");
        }
        this.creditos = creditos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return Objects.equals(id, curso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%d créditos)", id, nombre, creditos);
    }
}