# Banco de Preguntas — Taller 7: Builder

Proyecto académico de **Laboratorio de Ingeniería de Software II** de la Universidad del Cauca. El proyecto implementa el **patrón de diseño Builder** para construir diferentes tipos de preguntas de un banco de preguntas.

## Descripción

La aplicación permite construir objetos `Question` mediante builders especializados. Actualmente se implementan tres tipos de preguntas:

- Pregunta abierta.
- Selección múltiple.
- Verdadero/Falso.

El proyecto utiliza un `QuestionDirector` para coordinar los pasos de construcción y una clase abstracta `QuestionBuilder` como base común para los builders concretos.

## Patrón Builder

La estructura principal es:

```text
                         QuestionBuilder
                               │
             ┌─────────────────┼─────────────────┐
             │                 │                 │
             ▼                 ▼                 ▼
    OpenQuestionBuilder  MultipleChoice...  TrueFalse...
             │                 │                 │
             └─────────────────┼─────────────────┘
                               ▼
                            Question
```

### Componentes principales

| Componente | Responsabilidad |
|---|---|
| `QuestionBuilder` | Define los pasos generales para construir una pregunta. |
| `OpenQuestionBuilder` | Construye preguntas abiertas. |
| `MultipleChoiceQuestionBuilder` | Construye preguntas de selección múltiple. |
| `TrueFalseQuestionBuilder` | Construye preguntas verdadero/falso. |
| `QuestionDirector` | Coordina el proceso de construcción. |
| `Question` | Producto final construido. |
| `Option` | Representa una opción de respuesta. |
| `QuestionType` | Define los tipos de pregunta. |
| `Difficultylevel` | Define los niveles de dificultad. |
| `Cliente` | Demuestra el uso de los builders. |

## Estructura

```text
lisw2_t07_g02/
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── co/edu/unicauca/lisw2_t07_g02/
    │           ├── ApplicationMain.java
    │           ├── Cliente.java
    │           ├── builder/
    │           │   ├── QuestionBuilder.java
    │           │   ├── OpenQuestionBuilder.java
    │           │   ├── MultipleChoiceQuestionBuilder.java
    │           │   └── TrueFalseQuestionBuilder.java
    │           ├── director/
    │           │   └── QuestionDirector.java
    │           └── domain/
    │               ├── Question.java
    │               ├── Option.java
    │               ├── QuestionType.java
    │               └── Difficultylevel.java
    └── test/
        └── java/
            └── co/edu/unicauca/lisw2_t07_g02/
                └── QuestionBuilderConsoleTest.java
```

## Tecnologías

- Java 21
- Maven
- JUnit 5.10.2
- Maven Surefire Plugin 3.2.5
- Exec Maven Plugin 3.1.0

## Requisitos

Antes de ejecutar el proyecto se necesita:

- JDK 21 o compatible.
- Maven 3.x.
- Git, si se desea clonar el repositorio.

Verifica las instalaciones:

```bash
java -version
mvn -version
```

## Ejecutar el proyecto

Clona el repositorio:

```bash
git clone <URL_DEL_REPOSITORIO>
```

Entra al proyecto:

```bash
cd lisw2_t07_g02
```

Ejecuta la aplicación:

```bash
mvn clean compile
mvn exec:java
```

La clase principal configurada en Maven es:

```text
co.edu.unicauca.lisw2_t07_g02.ApplicationMain
```

La aplicación ejecuta el ejemplo de `Cliente` y muestra por consola las preguntas construidas.

## Ejecutar las pruebas

Para ejecutar las pruebas:

```bash
mvn clean test
```

El proyecto contiene **8 pruebas unitarias** en `QuestionBuilderConsoleTest`.

Las pruebas cubren:

- Construcción de preguntas abiertas.
- Construcción de preguntas de selección múltiple.
- Construcción de preguntas verdadero/falso.
- Construcción con parámetros personalizados.
- Validación de enunciados.
- Validación de opciones.
- Validación de respuestas.
- Reutilización del mismo `QuestionDirector` con diferentes builders.

### Resultado registrado

```text
Tests run: 8
Failures: 0
Errors: 0
Skipped: 0
```

## Flujo de construcción

Un ejemplo general del flujo es:

```text
Cliente
   │
   ▼
QuestionDirector
   │
   ▼
QuestionBuilder
   │
   ▼
Builder concreto
   │
   ├── buildType()
   ├── buildDifficultyLevel()
   ├── buildStatement()
   └── buildOptions()
   │
   ▼
Question.validate()
   │
   ▼
Question
```

## Validaciones

`Question` verifica, entre otras reglas:

- El enunciado no puede estar vacío.
- El tipo debe estar definido.
- Una pregunta de selección múltiple debe tener opciones.
- Una pregunta verdadero/falso debe tener exactamente dos opciones.
- Las preguntas cerradas deben tener al menos una opción correcta.

## Agregar un nuevo tipo de pregunta

Para agregar un nuevo tipo se puede crear un builder que extienda `QuestionBuilder`.

Ejemplo:

```java
public class MatchingQuestionBuilder extends QuestionBuilder {

    @Override
    public void buildType() {
        // configurar el tipo
    }

    @Override
    public void buildDifficultyLevel() {
        // configurar dificultad
    }

    @Override
    public void buildStatement() {
        // configurar enunciado
    }

    @Override
    public void buildOptions() {
        // configurar opciones
    }
}
```

Luego el nuevo builder puede utilizarse con `QuestionDirector`.

## Proyecto académico

**Curso:** Laboratorio de Ingeniería de Software II  
**Taller:** 7 — Patrón Builder  
**Universidad:** Universidad del Cauca  
**Lenguaje:** Java

## 📜 Licencia

Proyecto académico. Uso destinado a fines educativos.
