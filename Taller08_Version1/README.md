# Banco de Preguntas Saber PRO — Taller 8

Proyecto académico de Laboratorio de Ingeniería de Software II de la Universidad del Cauca. Aplica los patrones State, Template Method, Decorator, Adapter y Facade sobre un modelo común de preguntas. Se conserva Builder como mecanismo de construcción del proyecto base.

## Patrones implementados

- **State:** controla el ciclo de vida de una pregunta y restringe las transiciones no permitidas.
- **Template Method:** centraliza cinco pasos de evaluación: validar pregunta, obtener/validar respuestas, comparar, determinar el resultado y generar retroalimentación. Incluye evaluadores para selección única, selección múltiple, verdadero/falso y pregunta abierta.
- **Decorator:** agrega características como dificultad alta, contextualización, retroalimentación, competencia y marca de revisión sin modificar `Question`.
- **Adapter:** recibe una pregunta de un servicio externo como JSON en un `String` y transforma `question_text` y `answer_text` al modelo interno.
- **Facade:** ofrece operaciones coordinadas para crear, consultar, cambiar estado, evaluar, decorar e importar preguntas.
- **Builder (base heredada):** permite construir preguntas abiertas, de selección múltiple y verdadero/falso.

## Tecnologías

- Java 21
- Maven
- JUnit 5.10.2

## Requisitos y ejecución

Se necesita JDK 21 y Maven 3.x. Verifica las instalaciones con `java -version` y `mvn -version`. Desde la carpeta del proyecto ejecuta:

```bash
mvn clean test
mvn exec:java
```

La clase principal es `co.unicauca.taller08.ApplicationMain`.

## Preguntas abiertas importadas

El servicio de ejemplo devuelve un JSON como:

```json
{
  "question_text": "¿Cuál es la capital de Colombia?",
  "answer_text": "Bogotá"
}
```

El Adapter transforma esos campos en una pregunta interna de tipo `OPEN`. `OpenQuestionEvaluator` compara la respuesta escrita después de normalizar mayúsculas, espacios y tildes; por ejemplo, `Bogotá`, `BOGOTA` y ` bogotá ` se consideran equivalentes. Para demostrar la evaluación a través de la Facade, la pregunta debe recorrer el ciclo de revisión y publicación antes de evaluarse.

## Estructura principal

```text
src/main/java/co/unicauca/taller08/
├── adapter/
├── builder/
├── decorator/
├── director/
├── domain/
├── facade/
├── state/
└── templatemethod/
```

## Nota de evaluación

Las preguntas solo se pueden evaluar cuando están publicadas. Las respuestas abiertas requieren una respuesta esperada no vacía.

## Codigo del uml en plant uml

```text
@startuml
skinparam classAttributeIconSize 0
skinparam linetype ortho
title Banco de Preguntas - Taller 8

package state {
  interface QuestionState
  abstract class AbstractQuestionState
  class DraftState
  class PendingReviewState
  class ReviewState
  class ApprovedState
  class PublishedState
  class ArchivedState
}
QuestionState <|.. AbstractQuestionState
AbstractQuestionState <|-- DraftState
AbstractQuestionState <|-- PendingReviewState
AbstractQuestionState <|-- ReviewState
AbstractQuestionState <|-- ApprovedState
AbstractQuestionState <|-- PublishedState
AbstractQuestionState <|-- ArchivedState

package domain {
  class Question {
    -statement: String
    -type: QuestionType
    -difficultyLevel: Difficultylevel
    -correctAnswer: String
    -explanation: String
    +submitForReview()
    +assignReviewer()
    +approve()
    +reject()
    +publish()
    +archive()
    +validate()
  }
  class Option
  enum QuestionType
  enum Difficultylevel
}
Question o--> QuestionState : state
Question *-- "0..*" Option
Question --> QuestionType
Question --> Difficultylevel

package templatemethod {
  abstract class QuestionEvaluator {
    +evaluate(): boolean
    +evaluateWithFeedback(): EvaluationResult
    #validateQuestion()
    #obtainAnswers()
    #{abstract} validateQuestionType()
    #{abstract} compareAnswers()
    #determineResult()
    #generateFeedback()
  }
  class SingleChoiceEvaluator
  class MultipleAnswerEvaluator
  class TrueFalseEvaluator
  class OpenQuestionEvaluator
  class EvaluationResult <<record>>
}
QuestionEvaluator <|-- SingleChoiceEvaluator
QuestionEvaluator <|-- MultipleAnswerEvaluator
QuestionEvaluator <|-- TrueFalseEvaluator
QuestionEvaluator <|-- OpenQuestionEvaluator
QuestionEvaluator ..> EvaluationResult
QuestionEvaluator ..> Question

package decorator {
  interface QuestionComponent {
    +getDescription()
    +getScore()
  }
  class BasicQuestion
  abstract class QuestionDecorator
  class HighDifficultyQuestion
  class ContextualizedQuestion
  class FeedbackQuestion
  class CompetencyQuestion
  class MarkedForReviewQuestion
}
QuestionComponent <|.. BasicQuestion
QuestionComponent <|.. QuestionDecorator
QuestionDecorator o--> QuestionComponent
QuestionDecorator <|-- HighDifficultyQuestion
QuestionDecorator <|-- ContextualizedQuestion
QuestionDecorator <|-- FeedbackQuestion
QuestionDecorator <|-- CompetencyQuestion
QuestionDecorator <|-- MarkedForReviewQuestion
BasicQuestion --> Question

package adapter {
  interface QuestionProvider
  interface ExternalQuestionService {
    +getQuestionJson(): String
  }
  class ExternalQuestionAdapter
  class SampleExternalQuestionService
}
QuestionProvider <|.. ExternalQuestionAdapter
ExternalQuestionAdapter --> ExternalQuestionService
ExternalQuestionService <|.. SampleExternalQuestionService
ExternalQuestionAdapter ..> Question : crea

package facade {
  class QuestionBankFacade
  class QuestionService
  class QuestionRepository
  class QuestionValidator
  class QuestionPublisher
  class QuestionImporter
}
QuestionBankFacade --> QuestionService
QuestionBankFacade --> QuestionRepository
QuestionBankFacade --> QuestionValidator
QuestionBankFacade --> QuestionPublisher
QuestionBankFacade --> QuestionImporter
QuestionService --> QuestionRepository
QuestionService --> QuestionValidator
QuestionPublisher --> QuestionValidator
QuestionImporter ..> QuestionProvider
QuestionBankFacade ..> QuestionEvaluator
QuestionBankFacade ..> QuestionComponent
QuestionRepository o--> Question
@enduml
```
