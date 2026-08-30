package avancecurricular.service;

import avancecurricular.model.Carrera;
import avancecurricular.repository.CarreraDAO;
import avancecurricular.repository.CarreraDAO.FilaCarrera;
import avancecurricular.repository.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarreraService {
    private final Map<String, Carrera> carreras;

    private final CarreraDAO carreraDAO;
    private final UnitOfWork unitOfWork;

    public CarreraService(CarreraDAO dao, UnitOfWork unitOfWork) {
        this.carreras = new HashMap<>();
        this.carreraDAO = dao;
        this.unitOfWork = unitOfWork;
    }

    public void inicializar(Connection conn) throws SQLException {
        List<FilaCarrera> filasCarrera = carreraDAO.extraerCarreras(conn);

        for (FilaCarrera fila : filasCarrera) {
            Carrera carrera = new Carrera(fila.getId(), fila.getNombre(), fila.getCreditosTotales());
            this.carreras.put(carrera.getId(), carrera);
        }
    }

    public Carrera buscarPorId(String id) {
        return this.carreras.get(id);
    }

    public void registrarCarrera(Carrera carrera) {
        this.carreras.put(carrera.getId(), carrera);

        this.unitOfWork.registrarAccion(conn -> this.carreraDAO.insertar(carrera, conn));
    }

    public void eliminarCarrera(String id) {
        this.carreras.remove(id);

        this.unitOfWork.registrarAccion(conn -> this.carreraDAO.eliminar(id, conn));
    }
}