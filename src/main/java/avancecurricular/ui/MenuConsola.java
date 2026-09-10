package avancecurricular.ui;

import avancecurricular.config.ContextoAplicacion;
import avancecurricular.model.Curso;
import avancecurricular.model.Estudiante;
import avancecurricular.model.Carrera;

import java.util.Scanner;

public class MenuConsola {
	private final ContextoAplicacion contexto;
	private final Scanner scanner;
	
	public MenuConsola(ContextoAplicacion contexto) {
		this.contexto = contexto;
		this.scanner = new Scanner(System.in);
	}
	
	public void mostrar() {
		boolean salir = false;
		
		while(!salir) {
			System.out.println("\n MENU PRINCIPAL ");
	        System.out.println("1) Gestion de Estudiantes");
	        System.out.println("2) Gestion de Cursos");
	        System.out.println("0) Salir");
	        System.out.print("Seleccione una opcion: ");
	        
	        String opcion = scanner.nextLine().trim();
	        		
	        switch (opcion) {
	        case "1": 
	        	menuEstudiantes();
	        	break;
	        case "2": 
	        	menuCursos();
	        	break;
	        case "0":
	        	salir = true;
	        	break;
	        default:
	            System.out.println("Opcion invalida");
	        }
	        
		}
	    System.out.println("Saliendo del sistema...");

	}
	
	private void menuEstudiantes() {
	    boolean volver = false;

	    while (!volver) {
	        System.out.println("\n Gestion de Estudiantes ");
	        System.out.println("1) Listar estudiantes");
	        System.out.println("2) Insertar estudiante");
	        System.out.println("3) Editar estudiante (pendiente)");
	        System.out.println("4) Eliminar estudiante");
	        System.out.println("5) Buscar estudiante");
	        System.out.println("0) Volver");
	        System.out.print("Seleccione una opcion: ");

	        String opcion = scanner.nextLine().trim();

	        switch (opcion) {
	        case "1":
	            listarEstudiantes();
	            break;
	        case "2":
	            insertarEstudiante();
	            break;
	        case "3":
	            System.out.println("Funcionalidad pendiente.");
	            break;
	        case "4":
	            eliminarEstudiante();
	            break;
	        case "5":
	            buscarEstudiante();
	            break;
	        case "0":
	            volver = true;
	            break;
	        default:
	            System.out.println("Opcion invalida");
	        }
	    }
	}

	private void listarEstudiantes() {
	    for (Estudiante est : contexto.getEstudianteService().obtenerTodos()) {
	        System.out.println(est);
	    }
	}
	
	private void insertarEstudiante() {
	    System.out.print("RUT: ");
	    String rut = scanner.nextLine().trim();

	    System.out.print("Nombre: ");
	    String nombre = scanner.nextLine().trim();

	    System.out.println("Carreras disponibles:");
	    for (Carrera carrera : contexto.getCarreraService().obtenerTodas()) {
	        System.out.println(carrera);
	    }

	    System.out.print("ID de la carrera: ");
	    String idCarrera = scanner.nextLine().trim();
	    Carrera carrera = contexto.getCarreraService().buscarPorId(idCarrera);

	    if (carrera == null) {
	        System.out.println("No existe una carrera con ese ID. Operacion cancelada.");
	        return;
	    }

	    try {
	        Estudiante nuevo = new Estudiante(rut, nombre, carrera);
	        contexto.getEstudianteService().registrarEstudiante(nuevo);
	        System.out.println("Estudiante registrado correctamente.");
	    } catch (IllegalArgumentException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}
	
	private void buscarEstudiante() {
	    System.out.print("RUT del estudiante a buscar: ");
	    String rut = scanner.nextLine().trim();

	    try {
	        Estudiante est = contexto.getEstudianteService().buscarPorRut(rut);
	        System.out.println(est);
	    } catch (IllegalArgumentException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}
	
	private void eliminarEstudiante() {
	    System.out.print("RUT del estudiante a eliminar: ");
	    String rut = scanner.nextLine().trim();

	    try {
	        contexto.getEstudianteService().eliminarEstudiante(rut);
	        System.out.println("Estudiante eliminado.");
	    } catch (IllegalArgumentException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}
	

}
