package avancecurricular.ui.view;

import avancecurricular.model.Carrera;
import avancecurricular.ui.controller.ControladorCarrera;
import java.util.Collection;

/**
 * Contrato específico para la presentación de los datos y acciones del módulo de Carreras.
 */
public interface VistaCarrera extends VistaBase<ControladorCarrera> {
    /**
     * Muestra el listado de carreras registradas.
     *
     * @param carreras Carreras a mostrar.
     */
	void mostrarListaCarreras(Collection<Carrera> carreras);
    /**
     * Muestra la malla de una carrera: sus asignaturas y el semestre de cada una.
     *
     * @param carrera Carrera cuya malla se muestra.
     */
	void mostrarDetalleMalla(Carrera carrera);
}