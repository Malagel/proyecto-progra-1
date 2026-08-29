package avancecurricular.model;

public class Estudiante extends Persona{
	private String idCarrera;
	private Set<RegistroAcademico> registrosAcademicos;
	
	
	public Estudiante (string rut, String nombre, String idCarrera) {
		super(rut, nombre);
		this.idCarrera = idCarrera;
		this.registrosAcademicos =  new HashSet<>();
	}
	
	
	

}