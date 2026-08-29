package avancecurricular.model;

import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

public class Estudiante extends Persona{
    private Carrera carrera;
    private final Set<RegistroAcademico> registrosAcademicos;

    public Estudiante(String rut, String nombre, String idCarrera) {
        super(rut, nombre);

        this.registrosAcademicos = new HashSet<>();
        this.idCarrera = Objects.requireNonNull(idCarrera, "El ID de la Carrera no puede ser Nulo.");
    }

    // faltan getters, setters y metodos relacionados a esta clase
}