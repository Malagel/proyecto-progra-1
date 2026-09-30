package avancecurricular.model;

import avancecurricular.exception.PrerrequisitoNoCumplidoException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Representa a un estudiante matriculado en una carrera, manteniendo el historial
 * de sus registros y avance académico.
 */
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

    /**
     * Evalúa si el estudiante ha aprobado todos los prerrequisitos exigidos en su malla curricular
     * para poder cursar una asignatura específica.
     *
     * @param cursoDeseado El curso que el estudiante desea inscribir.
     * @return {@code true} si el curso está en la malla y todos sus prerrequisitos están aprobados, {@code false} en caso contrario.
     */
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

    /**
     * Inscribe formalmente un curso en el expediente del estudiante, generando un nuevo registro
     * en estado cursando.
     *
     * @param curso El curso a inscribir.
     * @return El nuevo registro académico generado.
     * @throws PrerrequisitoNoCumplidoException si no se cumplen los prerrequisitos del curso.
     */
    public RegistroAcademico inscribirCurso(Curso curso) {
        if (!cumplePrerrequisitosPara(curso)) {
            throw new PrerrequisitoNoCumplidoException("No cumple los prerrequisitos para inscribir: " + curso.getNombre());        
        }
        
        RegistroAcademico nuevoRegistro = new RegistroAcademico(curso, 0.0, RegistroAcademico.ESTADO_CURSANDO);
        this.registrosAcademicos.add(nuevoRegistro);

        return nuevoRegistro;
    }

    /**
     * Calcula la suma total de créditos obtenidos únicamente de los cursos con estado aprobado.
     *
     * @return La cantidad total de créditos aprobados.
     */
    public int obtenerCreditosAprobados() {
        return this.registrosAcademicos.stream()
            .filter(RegistroAcademico::esAprobado)
            .mapToInt(registro -> registro.getCurso().getCreditos())
            .sum();
    }

    /**
     * Calcula el porcentaje de completitud de la carrera basándose en los créditos aprobados
     * respecto a los créditos totales exigidos.
     *
     * @return El porcentaje de avance (de 0.0 a 100.0).
     */
    public double calcularPorcentajeAvance() {
        int requeridos = this.carrera.getCreditosTotales();
        if (requeridos == 0) return 0.0;
        return ((double) obtenerCreditosAprobados() / requeridos) * 100.0;
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