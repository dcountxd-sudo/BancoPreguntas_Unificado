# Taller 08 — Patrones de diseño (continuación del Taller 07)

Proyecto Java 17 / Maven. Conserva el modelo `Question`, `Option`, `QuestionType`, `DifficultyLevel`, los builders y `QuestionDirector` del Taller 07, y los extiende con State, Template Method, Decorator, Adapter y Facade.

## Ejecución

En NetBeans: abrir como proyecto Maven la carpeta que contiene `pom.xml`; ejecutar `co.unicauca.taller08.ApplicationMain`.

En terminal, con JDK 17 y Maven instalado:

```bash
mvn clean test
mvn exec:java -Dexec.mainClass="co.unicauca.taller08.ApplicationMain"
```

Si no se tiene configurado el plugin `exec`, ejecutar `ApplicationMain` desde el IDE o compilar con `mvn package` y usar el IDE para lanzar la clase.

## Menú y requisitos funcionales

1. Crear pregunta: elegir selección múltiple, verdadero/falso o abierta; introducir opciones cuando corresponda y respuesta correcta.
2. Consultar preguntas registradas.
3. Validar campos y reglas de tipo.
4. Cambiar estados usando State, siguiendo la Figura 1: Borrador → Pendiente de revisión → En revisión → Aprobada → Publicada → Archivada, y En revisión → Rechazada → Borrador. Publicar exige aprobación previa.
5. Evaluar respuestas con Template Method; solo permite evaluar preguntas publicadas.
6. Agregar características dinámicamente (se pueden apilar) con Decorator: alta dificultad, contextualización, retroalimentación, competencia o marca de revisión.
7. Importar una pregunta de un servicio externo (JSON) mediante Adapter (`QuestionProvider` / `ExternalQuestionService` / `ExternalQuestionAdapter`).
8. Ver los subsistemas coordinados por Facade: QuestionService, QuestionRepository, QuestionValidator, QuestionEvaluator, QuestionPublisher y QuestionImporter.

Los datos se guardan en memoria durante la ejecución; al cerrar la aplicación se reinician. El DTO de Adapter simula una fuente externa, suficiente para demostrar la conversión de formato sin depender de un servicio de terceros.

## Paquetes

- `builder`, `director`, `domain`: base reutilizada del Taller 07.
- `state`: estados y transiciones.
- `templatemethod`: algoritmo general de evaluación y evaluadores concretos.
- `decorator`: características agregadas dinámicamente.
- `adapter`: DTO externo y conversión al modelo de dominio.
- `facade`: interfaz simplificada para el cliente.
- `service`, `repository`: subsistemas explícitos coordinados por Facade.
