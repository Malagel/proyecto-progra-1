package avancecurricular.model;

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class Carrera {
    private final String id;
    private String nombre;
    private int creditosTotales;
    private final Set<AsignaturaMalla> planDeEstudio;

    public Carrera(String id, String nombre, int creditosTotales) {
        this.id = Objects.requireNonNull(id, "El ID no puede ser nulo");
        this.planDeEstudio = new HashSet<>();

        setNombre(nombre);
        setCreditosTotales(creditosTotales);
    }
    
    public void addAsignatura(AsignaturaMalla asignatura) {
    	Objects.requireNonNull(asignatura, "La asignatura no puede ser nula");
    	this.planDeEstudio.add(asignatura);
    }
    
    public void removeAsignatura(AsignaturaMalla asignatura) {
    	if(!this.planDeEstudio.remove(asignatura)) {
    		throw new IllegalArgumentException("No se puede remover la asignatura. No existe");
    	}
    }

    public Set<AsignaturaMalla> getPlanDeEstudio(){
    	return Collections.unmodifiableSet(this.planDeEstudio);
    }
    
    public String getId () {
    	return id;
    }
    
    public String getNombre() {
    	return nombre;
    }
    
    public int getCreditosTotales() {
    	return creditosTotales;
    }
	
	public final void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
    }

    public final void setCreditosTotales(int creditosTotales) {
        if (creditosTotales <= 0) {
            throw new IllegalArgumentException("Los créditos totales no pueden ser menores o igual a cero.");
        }
        this.creditosTotales = creditosTotales;
    }
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrera carrera = (Carrera) o;
        return Objects.equals(id, carrera.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%d créditos totales, %d asignaturas)",id, nombre, creditosTotales, planDeEstudio.size());
    }

}
