package avancecurricular.model;

public abstract class Persona {
	private final String rut;
	private String nombre;
	// constructor
	public Persona (String rut, String nombre) {
		this.rut = rut;
		this.nombre  = nombre;
	}
	 public persona (){
		rut = "No definido";
		nombre = "No definido";
	 }
    // getters y setters
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

	// metodos de  identidad 
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(rut, persona.rut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rut);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "rut='" + rut + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }

} 
