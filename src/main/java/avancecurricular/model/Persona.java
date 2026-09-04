package avancecurricular.model;

import java.util.Objects;

public abstract class Persona {
    private final String rut;
    private String nombre;

    public Persona(String rut, String nombre) {
        this.rut = Objects.requireNonNull(rut, "El RUT no puede ser Nulo");
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
