package avancecurricular.model;

public abstract class Persona {
	private String rut;
	private String nombre;
	
	public Persona (String rut, String nombre) {
		this.rut = rut;
		this.nombre  = nombre;
	}

	public void SetRut (String rut) {
		this.rut = rut; 
	}
	
	public void SetNombre (String nombre) {
		this.nombre = nombre;
	}
	
	public String GetRut () {
		return rut;
	}
	public String GetNombre () {
		return nombre;
	}

} 
