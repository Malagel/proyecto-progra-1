package avancecurricular.config;

import avancecurricular.repository.*;
import avancecurricular.service.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ContextoAplicacion {
    private final UnitOfWork unitOfWork;

    private final CursoDAO cursoDAO;
    private final CarreraDAO carreraDAO;
    private final ProfesorDAO profesorDAO;
    private final EstudianteDAO estudianteDAO;

    private final CursoService cursoService;
    private final CarreraService carreraService;
    private final ProfesorService profesorService;
    private final EstudianteService estudianteService;

    public ContextoAplicacion() {
        this.unitOfWork = new UnitOfWork();

        this.cursoDAO = new CursoDAO();
        this.carreraDAO = new CarreraDAO();
        this.profesorDAO = new ProfesorDAO();
        this.estudianteDAO = new EstudianteDAO();

        this.cursoService = new CursoService(this.cursoDAO, this.unitOfWork);
        this.carreraService = new CarreraService(this.carreraDAO, this.unitOfWork);
        this.profesorService = new ProfesorService(this.profesorDAO, this.unitOfWork);
        this.estudianteService = new EstudianteService(this.estudianteDAO, this.unitOfWork);
    }

    public void inicializarDatos() {        
        try {
            DatabaseInitializer.crearTablasSiNoExisten();

            try (Connection conn = DatabaseConnection.getConnection()) {
                this.cursoService.inicializar(conn);               
                this.carreraService.inicializar(conn, this.cursoService);              
                this.profesorService.inicializar(conn, this.cursoService);               
                this.estudianteService.inicializar(conn, this.carreraService, this.cursoService);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error crítico al inicializar la base de datos", e);
        }
    }

    public CursoService getCursoService() { return cursoService; }
    public CarreraService getCarreraService() { return carreraService; }
    public ProfesorService getProfesorService() { return profesorService; }
    public EstudianteService getEstudianteService() { return estudianteService; }
    public UnitOfWork getUnitOfWork() { return unitOfWork; }
}