package avancecurricular.model;

import java.util.Objects;

public abstract class Persona {
    private final String rut;
    private String nombre;

    /**
     * Crea una nueva persona validando la longitud de su identificador.
     *
     * @param rut    El identificador único (ej. formato con o sin puntos/guion).
     * @param nombre El nombre completo de la persona.
     * @throws IllegalArgumentException si la longitud del RUT excede los 10 caracteres.
     */
    public Persona(String rut, String nombre) {
        this.rut = Objects.requireNonNull(rut, "El RUT no puede ser Nulo");

        if (this.rut.length() > 10) {
            throw new IllegalArgumentException("El RUT no puede exceder los 10 caracteres.");
        }
        this.nombre = nombre;
    }
    public String getRut() {
        return this.rut;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        Persona persona = (Persona) o;
        return Objects.equals(rut, persona.rut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rut);
    }

    @Override
    public String toString() {
        return "Persona{rut='" + rut + "', nombre='" + nombre + "'}";
    }
}
