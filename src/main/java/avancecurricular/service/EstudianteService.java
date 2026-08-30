package avancecurricular.service;

import avancecurricular.model.Curso;
import avancecurricular.model.Estudiante;
import avancecurricular.repository.EstudianteDAO;
import avancecurricular.repository.EstudianteDAO.FilaEstudiante;
import avancecurricular.repository.EstudianteDAO.FilaNota;
import avancecurricular.repository.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class EstudianteService {
    private final Map<String, Estudiante> estudiantes;
    
    private final EstudianteDAO estudianteDAO;
    private final UnitOfWork unitOfWork;

    public EstudianteService(EstudianteDAO dao, UnitOfWork unitOfWork) {
        this.estudiantes = new HashMap<>();
        this.estudianteDAO = dao;
        this.unitOfWork = unitOfWork;
    }

    public void inicializar(Connection conn, CarreraService carreraService, CursoService cursoService) throws SQLException {
        List<FilaEstudiante> filasEst = estudianteDAO.extraerEstudiantes(conn);

        for (FilaEstudiante fila : filasEst) {
            Carrera carreraReal = carreraService.buscarPorId(fila.getIdCarrera());
            
            Estudiante estudiante = new Estudiante(fila.getRut(), fila.getNombre(), carreraReal, new HashSet<>());
            this.estudiantes.put(estudiante.getRUT(), estudiante);
        }

        List<FilaNota> filasNotas = estudianteDAO.extraerNotas(conn);

        for (FilaNota fila : filasNotas) {
            Estudiante estudiante = this.estudiantes.get(fila.getRutEstudiante());
            Curso cursoReal = cursoService.buscarPorId(fila.getIdCurso());

            if (estudiante != null && cursoReal != null) {
                RegistroAcademico registro = new RegistroAcademico(cursoReal, fila.getNota(), fila.getEstado());
                estudiante.getRegistrosAcademicos().add(registro);
            }
        }
    }

    public void registrarEstudiante(Estudiante estudiante) {
        this.estudiantes.put(estudiante.getRUT(), estudiante);

        this.unitOfWork.registrarAccion(conn -> this.estudianteDAO.insertar(estudiante, conn));
    }   

    public void eliminarEstudiante(String rut) {
        this.estudiantes.remove(rut);

        this.unitOfWork.registrarAccion(conn -> this.estudianteDAO.eliminar(rut, conn));
    }
}`