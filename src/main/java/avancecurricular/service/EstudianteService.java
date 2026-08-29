package avancecurricular.service;

import avancecurricular.model.Curso;
import avancecurricular.model.Estudiante;
import avancecurricular.repository.EstudianteDAO;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class EstudianteService {
    private final Map<String, Estudiante> estudiantesPorRut;
    private final EstudianteDAO estudianteDAO;
    
    public EstudianteService() {
        estudiantesPorRut = new HashMap<>();
        estudianteDAO = new EstudianteDAO();
    }

    public void cargarDatosDesdeDB(Map<String, Curso> catalogoCursos) {
        List<Estudiante> desdeDB = estudianteDAO.obtenerTodos();

        for (Estudiante e : desdeDB) {
            this.estudiantes.put(e.getRUT(), e);
        }
    }   

    
}

