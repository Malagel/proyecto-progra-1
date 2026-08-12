# Gestión de Avance Curricular

Sistema de Información (SIA) para el curso **INF2236 - Programación Avanzada**, PUCV, periodo 2S26

## Descripción

Permite registrar alumnos, profesores y asignaturas de un instituto, y llevar el avance curricular de cada alumno según el estado de las asignaturas de su malla (aprobada, cursando, pendiente, reprobada).

## Tecnologías

- Java (JDK 11)
- Maven
- Eclipse IDE for Java Developers

## Estructura del proyecto

```
src/main/java/
├── modelo/     → Clases del dominio (Asignatura, Profesor, Alumno, EstadoAvance)
└── app/        → Clase principal (Main) con datos de prueba
```

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
	- Estudiante
		RUT,
		nombre,
		codigoCarrera,
		
	- Profesor
		RUT,
		nombre,
		
	- Curso
		codigo,
		creditos,
		titulo,
		prerrequisitos (List<Curso>),
			
	- Carrera,
		codigo,
		titulo,
		creditos, // Para saber el avance del estudiante.
		
	- RegistroAcademico // Objeto repetido por cada curso que haya cursado o curse algun estudiante.
		curso, (referencia)
		nota, 
		estado, ("APROBADO", "REPROBADO", "CURSANDO")
		
### Maps:
	- Relationship Maps: // Mapas con las relaciones necesarias para el programa. Sostiene solo referencias a las entidades.
		- Map<Estudiante, List<RegistroAcademico>>
		- Map<Profesor, List<Curso>>
		- Map<Carrera, List<Curso>>

	- Entity Caches: // Catalogo Maestro con las entidades reales. String = Identificador 
		- Map<String, Estudiante>
		- Map<String, Profesor>
		- Map<String, Curso>
		- Map<String, Carrera>

### Estructura General dentro de src/main/java/avancecurricular:
	- model/
		- Solo los modelos primarios con setter y getter. 
		- Sin logica compleja, representa solo la estructura.
		
	- repository/
		- Todas las operaciones relacionadas con la base de datos. Hablan con ella ÚNICAMENTE.
		- Retornan (read) y actualizan datos (update).
		- Usa excepciones para errores.
		- Hay que crear un .java por cada entidad (modelos).
		- Nombrar los objetos como *DAO (Data Access Objects).
		- Ej.: "CursoDAO.java" manejaría el leer de la DB o actualizarla cuando el usuario quiera guardar.

	- service/
		- Contiene la lógica, conecta la base de datos con la interfaz. 
		- Se encarga de revisar y coordinar la información. Usa try-catch pero NUNCA imprime información.
		- Clases acá manejan los Mapas.
		- Nombrar las clases como "*Service.java".
		- Ej.: "EstudiantesService.java" manejaría la lógica de crear instancias de estudiantes y ponerlos en sus respectivos mapas.

	- ui/
		- Es la capa de interacción con el usuario.
		- No usa ningún tipo de lógica.
		- Se encarga de leer y mostrar errores invocados por service/

	- utils/
		- Clases con utilidades, funciones de lógica que se repiten básicamente.
