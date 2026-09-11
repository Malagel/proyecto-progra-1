package avancecurricular.ui.view;

import avancecurricular.model.Carrera;
import avancecurricular.ui.controller.ControladorCarrera;
import java.util.Collection;

public interface VistaCarrera extends VistaBase<ControladorCarrera> {
    void mostrarListaCarreras(Collection<Carrera> carreras);
    void mostrarDetalleMalla(Carrera carrera);
}