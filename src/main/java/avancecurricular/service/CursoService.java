package avancecurricular.service;

import avancecurricular.model.AsignaturaMalla;
import avancecurricular.model.Carrera;
import avancecurricular.model.Curso;
import avancecurricular.model.Estudiante;
import avancecurricular.model.Profesor;
import avancecurricular.model.RegistroAcademico;
import avancecurricular.repository.CursoDAO;
import avancecurricular.repository.CursoDAO.FilaCurso;
import avancecurricular.repository.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CursoService {
    private final Map<String, Curso> cursos;
    private final CursoDAO cursoDAO;
    private final UnitOfWork unitOfWork;

    public CursoService(CursoDAO dao, UnitOfWork unitOfWork) {
        this.cursos = new HashMap<>();
        this.cursoDAO = dao;
        this.unitOfWork = unitOfWork;
    }

    public void inicializar(Connection conn) throws SQLException {
        this.cursos.clear();
        
        List<FilaCurso> filas = cursoDAO.extraerCursos(conn);
        
        for (FilaCurso fila : filas) {
            Curso curso = new Curso(fila.getId(), fila.getNombre(), fila.getCreditos());
            this.cursos.put(curso.getId(), curso);
        }
    }

    public void registrarCurso(Curso curso) {
        if (this.cursos.containsKey(curso.getId())) {
            throw new IllegalArgumentException("El curso con ID " + curso.getId() + " ya existe.");
        }

        this.cursos.put(curso.getId(), curso);
        this.unitOfWork.registrarAccion(conn -> this.cursoDAO.insertarCurso(curso, conn));
    }

    public void eliminarCurso(String id, CarreraService carreraService, EstudianteService estudianteService, ProfesorService profesorService) {
        if (!this.cursos.containsKey(id)) {
            throw new IllegalArgumentException("El curso no existe.");
        }
        
        Curso cursoObjetivo = this.cursos.get(id);

        for (Carrera carrera : carreraService.obtenerTodas()) {
            for (AsignaturaMalla am : carrera.getPlanDeEstudio()) {
                if (am.getCurso().equals(cursoObjetivo)) {
                    throw new IllegalStateException("Violación de integridad: El curso pertenece a la malla de " + carrera.getNombre());
                }
                if (am.getPrerrequisitos().contains(cursoObjetivo)) {
                    throw new IllegalStateException("Violación de integridad: El curso es prerrequisito en la carrera " + carrera.getNombre());
                }
            }
        }

        for (Estudiante estudiante : estudianteService.obtenerTodos()) {
            for (RegistroAcademico registro : estudiante.getRegistrosAcademicos()) {
                if (registro.getCurso().equals(cursoObjetivo)) {
                    throw new IllegalStateException("Violación de integridad: El estudiante " + estudiante.getRut() + " tiene registros asociados a este curso.");
                }
            }
        }

        for (Profesor profesor : profesorService.obtenerTodos()) {
            if (profesor.getCursosDictados().contains(cursoObjetivo)) {
                profesor.removerCurso(cursoObjetivo);
            }
        }

        this.cursos.remove(id);
        this.unitOfWork.registrarAccion(conn -> this.cursoDAO.eliminarCurso(id, conn));
    }

    public Curso buscarPorId(String id) {
        Curso curso = this.cursos.get(id);
        if (curso == null) {
            throw new IllegalArgumentException("No se encontró ningún curso con el ID: " + id);
        }
        return curso;
    }

    public Collection<Curso> obtenerTodos() {
        return Collections.unmodifiableCollection(this.cursos.values());
    }
}