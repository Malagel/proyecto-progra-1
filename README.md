# Gestión de Avance Curricular

Sistema de Información (SIA) para el curso **INF2236 - Programación Avanzada**, PUCV, periodo 2S26

## Descripción

Permite registrar alumnos, profesores y asignaturas de un instituto, y llevar el avance curricular de cada alumno según el estado de las asignaturas de su malla (aprobada, cursando, pendiente, reprobada).

## Tecnologías

- Java (JDK 11)
- Maven

## Cómo ejecutar

1. Clonar el repositorio.
2. Abrir la carpeta como proyecto en Eclipse (con JDK 11 configurado).
3. Ejecutar la clase `app.Main` como Java Application.

## Integrantes

- Gustavo Ordenes
- Nicolas Mariangel
- Alvaro Ulloa

## Organización

### Model Objects:

- Persona (abstract class)
	- RUT,
	- nombre,

- Estudiante (extends Persona)
	- idCarrera,
	- Set<RegistroAcademico> registrosAcademicos, 
		
- Profesor (extends Persona)
	- Set<Curso> cursosDictados,
		
- Curso
	- id,
	- nombre,
	- creditos,

- AsignaturaMalla (interseccion de Curso y Carrera, para que cada carrera pueda tener distintas organizaciones)
	- curso, (referencia al curso),
	- numeroSemestre,
	- Set<Curso> prerrequisitos,

- Carrera,
	- id,
	- nombre,
	- creditosTotales, (Para saber el avance del estudiante)
	- Set<AsignaturaMalla> planDeEstudio 
		
- RegistroAcademico	
	- curso, (referencia)
	- nota, 
	- estado, ("APROBADO", "REPROBADO", "CURSANDO")
		
### Maps:

- Entity Caches (probablemente tendra su propia clase): Catálogo Maestro con las entidades reales. String = Identificador 
	(Hacer cuenta que desde la DB se cargará la información en los mapas, luego al cerrar el programa pasará de los mapas a la DB)
	- Map<String, Estudiante>
	- Map<String, Profesor>
	- Map<String, Curso>
	- Map<String, Carrera>

### Estructura General de Carpetas dentro de src/main/java/avancecurricular:
- model/
	- Sólo los modelos primarios con setter, getter y métodos de lógica integral interna . 
	- Sin lógica compleja, representa sólo la estructura.
		
- repository/
	- Todas las operaciones relacionadas con la base de datos. Hablan con ella ÚNICAMENTE.
	- Retornan (read) y actualizan datos (update).
	- Usa excepciones para errores.
	- Hay que crear un .java por cada entidad (modelos).
	- Nombrar los objetos como *DAO (Data Access Objects).
	- Ej.: "CursoDAO.java" manejaría el leer de la DB o actualizarla cuando el usuario quiera guardar.

- service/
	- Contiene la lógica, conecta la base de datos con la interfaz. 
	- Se encarga de revisar y coordinar la información. Usa try-catch pero NUNCA imprime.
	- Las clases manejan los Mapas.
	- Nombrar las clases como "*Service.java".
	- Ej.: "EstudianteService.java" manejaría la lógica de crear instancias de estudiantes y ponerlos en sus respectivos mapas.

- ui/
	- Es la capa de interacción con el usuario.
	- No usa ningún tipo de lógica.
	- Se encarga de leer y mostrar errores invocados por service/

- utils/
	- Clases con utilidades, funciones de lógica que se repiten básicamente.
