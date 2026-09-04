package avancecurricular.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Estudiante extends Persona {
    private final Carrera carrera;
    private final Set<RegistroAcademico> registrosAcademicos;

    public Estudiante(String rut, String nombre, Carrera carrera) {
        super(rut, nombre);
        this.carrera = Objects.requireNonNull(carrera, "La Carrera no puede ser Nula.");
        this.registrosAcademicos = new HashSet<>();
    }

    public Estudiante(String rut, String nombre, Carrera carrera, Set<RegistroAcademico> registros) {
        super(rut, nombre);
        this.carrera = Objects.requireNonNull(carrera, "La Carrera no puede ser Nula.");
        this.registrosAcademicos = (registros != null) ? new HashSet<>(registros) : new HashSet<>();
    }

    // agregar registro sin verificacion
    public void addRegistroAcademico(RegistroAcademico registro) {
        Objects.requireNonNull(registro, "El registro académico no puede ser nulo.");
        this.registrosAcademicos.add(registro);
    }

    public void removeRegistroAcademico(RegistroAcademico registro) {
        this.registrosAcademicos.remove(registro);
    }

    public Set<RegistroAcademico> getRegistrosAcademicos() {
        return Collections.unmodifiableSet(this.registrosAcademicos);
    }

    public boolean cumplePrerrequisitosPara(Curso cursoDeseado) {
        AsignaturaMalla asignaturaMalla = null;
        for (AsignaturaMalla am : this.carrera.getPlanDeEstudio()) {
            if (am.getCurso().equals(cursoDeseado)) {
                asignaturaMalla = am;
                break;
            }
        }

        if (asignaturaMalla == null) {
            return false; 
        }

        for (Curso prerrequisito : asignaturaMalla.getPrerrequisitos()) {
            boolean aprobado = false;
            
            for (RegistroAcademico miRegistro : this.registrosAcademicos) {
                if (miRegistro.getCurso().equals(prerrequisito) && miRegistro.esAprobado()) {
                    aprobado = true;
                    break;
                }
            }
            
            if (!aprobado) {
                return false;
            }
        }

        return true;
    }

    public RegistroAcademico inscribirCurso(Curso curso) {
        if (!cumplePrerrequisitosPara(curso)) {
            throw new IllegalStateException("No cumple los prerrequisitos para inscribir: " + curso.getNombre());
        }
        
        RegistroAcademico nuevoRegistro = new RegistroAcademico(curso, 0.0, RegistroAcademico.ESTADO_CURSANDO);
        this.registrosAcademicos.add(nuevoRegistro);

        return nuevoRegistro;
    }

    public Carrera getCarrera() {
        return this.carrera;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "rut='" + getRut() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", carrera=" + (carrera != null ? carrera.getNombre() : "Sin Carrera") +
                ", totalRegistros=" + registrosAcademicos.size() +
                '}';
    }
}