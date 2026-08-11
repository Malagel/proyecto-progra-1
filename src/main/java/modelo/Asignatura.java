package modelo;

public class Asignatura {
	private String clave; 
	private String nombre;
	private int nivel; 
	
	public Asignatura(String clave,String nombre,int nivel){
		this.clave = clave; 
		this.nombre = nombre; 
		this.nivel = nivel; 
	}
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getNombre () {
		return nombre;
	}
	public void setNombre (String nombre) {
		this.nombre = nombre; 
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel (int nivel) {
		this.nivel = nivel;
	}
}


