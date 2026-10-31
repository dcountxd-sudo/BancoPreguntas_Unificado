# Banco de Preguntas Saber Pro

Proyecto desarrollado para el **Taller 10 – Java Persistence API (JPA)** de la asignatura Laboratorio de Ingeniería de Software II, del programa de Ingeniería de Sistemas de la Universidad del Cauca.

## 1. Propósito

El proyecto consiste en desarrollar una aplicación Java para administrar un banco de preguntas Saber Pro, utilizando Spring Data JPA para la persistencia de la información y H2 como base de datos.

La aplicación permite:

- Registrar y consultar usuarios con diferentes roles.
- Administrar temas y competencias.
- Crear preguntas de selección múltiple y verdadero/falso.
- Clasificar preguntas por temas y competencias.
- Gestionar el proceso de revisión, aprobación, rechazo y publicación.
- Crear cuestionarios utilizando preguntas publicadas.

Uno de los objetivos principales es mantener separadas las reglas del negocio de los detalles tecnológicos de persistencia, evitando que las clases del dominio dependan directamente de JPA.

## 2. Arquitectura

El proyecto está organizado por capas, separando las responsabilidades de cada parte de la aplicación.

```text
co.unicauca.questionbank/
├── api/
├── application/
├── config/
├── domain/
│   ├── exception/
│   ├── model/
│   ├── repository/
│   └── service/
└── infrastructure/
    └── jpa/
        ├── mapper/
        ├── record/
        ├── repository/
        ├── QuestionRepositoryJpa.java
        ├── UserRepositoryJpa.java
        └── ...
```

Las responsabilidades principales son:

- **API:** contiene los controladores REST, los modelos de solicitud y respuesta y el manejo de excepciones.
- **Application:** contiene los servicios que implementan los casos de uso, como la creación de preguntas, la revisión y la creación de cuestionarios.
- **Domain:** contiene las clases del negocio, sus reglas, los estados, las excepciones y los contratos de repositorio.
- **Infrastructure/JPA:** contiene las clases `Record` con las anotaciones JPA, los repositorios de Spring Data JPA y las implementaciones de los contratos del dominio.
- **Config:** contiene la configuración del dominio y la carga de datos de demostración.

Para mantener separadas las clases del dominio y las de persistencia se utiliza `DomainMapper`, encargado de convertir los objetos entre ambos modelos. Así, las reglas del negocio no necesitan depender de las anotaciones de JPA.

## 3. Instrucciones de ejecución

### Requisitos previos

- JDK 17 o superior.
- Apache Maven 3.6 o superior.
- Un IDE como Visual Studio Code, IntelliJ IDEA o Eclipse.

Se puede comprobar la instalación con:

```bash
java -version
mvn -version
```

### Compilar y ejecutar

1. Descargar o clonar el repositorio.
2. Abrir una terminal en la carpeta raíz del proyecto, donde se encuentra `pom.xml`.
3. Ejecutar las pruebas y compilar:

```bash
mvn clean test
```

4. Iniciar la aplicación:

```bash
mvn spring-boot:run
```

También se puede generar el archivo ejecutable:

```bash
mvn clean package
```

Y ejecutarlo con:

```bash
java -jar target/lisw2_t10_g9-1.0.0.jar
```

La API estará disponible en `http://localhost:8080`.

### Base de datos H2

La aplicación utiliza H2 en memoria. Las tablas se crean al iniciar y se eliminan al detener la aplicación, debido a la configuración `create-drop`.

La consola de H2 está disponible en:

`http://localhost:8080/h2-console`

Datos de conexión:

- **JDBC URL:** `jdbc:h2:mem:questionbankdb`
- **Usuario:** `sa`
- **Contraseña:** vacía.

Al iniciar la aplicación se cargan datos de demostración. Esta opción se puede desactivar desde `application.properties`, cambiando `app.seed-demo-data=true` por `app.seed-demo-data=false`.

## 4. Ejemplos de uso

Los endpoints REST reciben y responden información en formato JSON. Se pueden probar utilizando Postman, Insomnia o `curl`.

### Consultar usuarios

```http
GET http://localhost:8080/api/users
```

### Consultar temas y competencias

```http
GET http://localhost:8080/api/topics
GET http://localhost:8080/api/competencies
```

### Crear una pregunta de selección múltiple

```http
POST http://localhost:8080/api/questions/multiple-choice
Content-Type: application/json
```

```json
{
  "authorId": 2,
  "statement": "Which pattern allows selecting an algorithm at runtime?",
  "difficulty": "MEDIUM",
  "topicId": 1,
  "competencyIds": [1, 2],
  "options": [
    { "label": "A", "text": "Singleton", "correct": false },
    { "label": "B", "text": "Strategy", "correct": true },
    { "label": "C", "text": "Facade", "correct": false },
    { "label": "D", "text": "Proxy", "correct": false },
    { "label": "E", "text": "Flyweight", "correct": false }
  ],
  "metadata": {
    "cognitiveLevel": "Apply",
    "estimatedSeconds": 90,
    "editorialNotes": "Example question"
  }
}
```

### Crear una pregunta de verdadero/falso

```http
POST http://localhost:8080/api/questions/true-false
Content-Type: application/json
```

```json
{
  "authorId": 2,
  "statement": "A facade provides a simplified interface to a subsystem.",
  "difficulty": "EASY",
  "topicId": 1,
  "competencyIds": [1],
  "expectedValue": true
}
```

### Consultar preguntas

```http
GET http://localhost:8080/api/questions
GET http://localhost:8080/api/questions/1
GET http://localhost:8080/api/questions?authorId=2
GET http://localhost:8080/api/questions?status=DRAFT
GET http://localhost:8080/api/questions?competencyId=1
```

Los filtros se realizan utilizando un criterio a la vez.

### Gestionar la revisión de una pregunta

Para una pregunta con identificador `1`, se pueden realizar las siguientes operaciones:

```http
POST http://localhost:8080/api/questions/1/submit
POST http://localhost:8080/api/questions/1/assign-reviewer
POST http://localhost:8080/api/questions/1/approve
POST http://localhost:8080/api/questions/1/publish
```

También se puede rechazar una pregunta:

```http
POST http://localhost:8080/api/questions/1/reject
```

Y archivar una pregunta publicada:

```http
POST http://localhost:8080/api/questions/1/archive
```

### Crear y consultar cuestionarios

Para crear un cuestionario:

```http
POST http://localhost:8080/api/quizzes
Content-Type: application/json
```

```json
{
  "creatorId": 4,
  "title": "Software Design Mock Exam",
  "questionIds": [1, 2]
}
```

Para consultar los cuestionarios:

```http
GET http://localhost:8080/api/quizzes
GET http://localhost:8080/api/quizzes/1
```

El creador debe tener el rol `TEACHER` y las preguntas seleccionadas deben estar publicadas.

**Nota:** Los identificadores de los ejemplos corresponden a datos existentes en la base de datos. Si se desactiva la carga de datos de demostración, primero se deben crear los usuarios, temas y competencias necesarios. Para crear cuestionarios también se deben crear las preguntas y completar su proceso de revisión y publicación.