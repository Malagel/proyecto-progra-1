# Gestión de Avance Curricular

Sistema de Información (SIA) para el curso **INF2236 - Programación Avanzada**, PUCV, periodo 2S26

## Descripción

Permite registrar alumnos, profesores y asignaturas de un instituto, y llevar el avance curricular de cada alumno según el estado de las asignaturas de su malla (aprobada, cursando, pendiente, reprobada).

## Tecnologías

- Java (JDK 11)
- Maven
- SQLite

## Cómo compilar y ejecutar el programa
1. Clonar el repositorio y entrar a la carpeta:
```bash
git clone https://github.com/Malagel/proyecto-progra-1]
cd proyecto-progra-1
```
2. Para compilar y ejecutar:
```bash
mvn clean compile exec:java
```
## Generar documentación con javadoc
1. Dentro de proyecto-progra-1, generar index.html
```bash
mvn javadoc:javadoc
```
2. La documentación generada se puede visualizar abriendo `target/site/apidocs/index.html` en cualquier navegador.

## Integrantes

- Gustavo Ordenes
- Nicolas Mariangel
- Alvaro Ulloa

## Organización Interna del proyecto

### Model Objects

- **`Persona`** (`abstract class`)
  - `RUT`
  - `nombre`

- **`Estudiante`** (`extends Persona`)
  - `carrera` (referencia)
  - `Set<RegistroAcademico> registrosAcademicos`

- **`Profesor`** (`extends Persona`)
  - `Set<Curso> cursosDictados`

- **`Curso`**
  - `id`
  - `nombre`
  - `creditos`

- **`AsignaturaMalla`** (intersección de `Curso` y `Carrera`, para que cada carrera pueda tener distintas organizaciones)
  - `curso` (referencia al curso)
  - `numeroSemestre`
  - `Set<Curso> prerrequisitos`

- **`Carrera`**
  - `id`
  - `nombre`
  - `creditosTotales` (para saber el avance del estudiante)
  - `Set<AsignaturaMalla> planDeEstudio`

- **`RegistroAcademico`**
  - `curso` (referencia)
  - `nota`
  - `estado` (`"APROBADO"`, `"REPROBADO"`, `"CURSANDO"`)

---

### Maps

- **Entity Caches** (probablemente tendrá su propia clase): Catálogo Maestro con las entidades reales. `String = Identificador`.  
  *(Hacer cuenta que desde la DB se cargará la información en los mapas, luego al cerrar el programa pasará de los mapas a la DB)*
  - `Map<String, Estudiante>`
  - `Map<String, Profesor>`
  - `Map<String, Curso>`
  - `Map<String, Carrera>`

---

### Estructura General de Carpetas dentro de `src/main/java/avancecurricular`

- **`model/`**
  - Sólo los modelos primarios con setter, getter y métodos de lógica integral interna.
  - Sin lógica compleja, representa sólo la estructura.

- **`repository/`**
  - Todas las operaciones relacionadas con la base de datos. Hablan con ella **únicamente**.
  - Retornan (`read`) y actualizan datos (`update`).
  - Usa excepciones para errores.
  - Hay que crear un `.java` por cada entidad (modelos).
  - Nombrar los objetos como `*DAO` (*Data Access Objects*).
  - *Ejemplo:* `CursoDAO.java` manejaría el leer de la DB o actualizarla cuando el usuario quiera guardar.

- **`service/`**
  - Contiene la lógica, conecta la base de datos con la interfaz.
  - Se encarga de revisar y coordinar la información. Usa `try-catch` pero **nunca** imprime.
  - Las clases manejan los mapas.
  - Nombrar las clases como `*Service.java`.
  - *Ejemplo:* `EstudianteService.java` manejaría la lógica de crear instancias de estudiantes y ponerlos en sus respectivos mapas.

- **`ui/`**
  - Es la capa de interacción con el usuario.
  - No usa ningún tipo de lógica.
  - Se encarga de leer y mostrar errores invocados por `service/`.
  - Se divide en:
    - **`view/`**: Contiene las interfaces básicas que definen qué debe poder hacer cualquier pantalla del sistema, sin importar si es ventana o terminal.
    - **`controller/`**: Son los coordinadores del sistema. Reciben los comandos del usuario desde la `gui` o la `console`, se comunican con los servicios internos para procesar o guardar los datos, y finalmente le dicen a la pantalla qué resultado mostrar.
    - **`console/`**: Contiene la versión de texto del programa. Aquí están los menús de opciones numeradas y las instrucciones para leer lo que el usuario escribe.
    - **`gui/`**: Contiene todo el código para las ventanas visuales.