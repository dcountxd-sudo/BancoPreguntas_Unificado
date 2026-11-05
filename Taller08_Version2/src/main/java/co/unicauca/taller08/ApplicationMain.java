package co.unicauca.taller08;

import co.unicauca.taller08.builder.MultipleChoiceQuestionBuilder;
import co.unicauca.taller08.builder.OpenQuestionBuilder;
import co.unicauca.taller08.builder.QuestionBuilder;
import co.unicauca.taller08.builder.TrueFalseQuestionBuilder;
import co.unicauca.taller08.builder.SingleChoiceQuestionBuilder;
import co.unicauca.taller08.decorator.QuestionFeature;
import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.facade.QuestionBankFacade;
import co.unicauca.taller08.templatemethod.EvaluationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Aplicación de consola interactiva para demostrar los requisitos del Taller 08. */
public class ApplicationMain {

    private static final Scanner IN = new Scanner(System.in);
    private static final QuestionBankFacade BANK = new QuestionBankFacade();

    public static void main(String[] args) {
        System.out.println("=== BANCO DE PREGUNTAS SABER PRO | TALLER 07 + TALLER 08 ===");
        boolean running = true;

        while (running) {
            menu();
            String option = read("Seleccione una opción: ");

            try {
                switch (option) {
                    case "1":
                        createQuestion();
                        break;
                    case "2":
                        listQuestions();
                        break;
                    case "3":
                        validateQuestion();
                        break;
                    case "4":
                        changeState();
                        break;
                    case "5":
                        evaluate();
                        break;
                    case "6":
                        decorate();
                        break;
                    case "7":
                        importExternal();
                        break;
                    case "8":
                        System.out.println("Facade -> " + BANK.subsystemStatus());
                        break;
                    case "0":
                        running = false;
                        System.out.println("Hasta pronto.");
                        break;
                    default:
                        System.out.println("Opción no válida. Ingrese un número del menú.");
                }
            } catch (RuntimeException e) {
                System.out.println("No se pudo completar la operación: " + e.getMessage());
                System.out.println("Puede intentarlo nuevamente desde el menú.");
            }
            System.out.println();
        }
    }

    private static void menu() {
        System.out.println("\n--------------- MENÚ PRINCIPAL ---------------");
        System.out.println("1. Crear pregunta (tipo, opciones y respuesta correcta)");
        System.out.println("2. Consultar todas las preguntas");
        System.out.println("3. Validar una pregunta");
        System.out.println("4. Cambiar estado / ciclo de vida");
        System.out.println("5. Evaluar respuesta (solo publicada)");
        System.out.println("6. Añadir característica con Decorator");
        System.out.println("7. Importar pregunta externa con Adapter");
        System.out.println("8. Ver subsistemas coordinados por Facade");
        System.out.println("0. Salir");
        System.out.println("-----------------------------------------------");
    }

    private static void createQuestion() {
        System.out.println("Tipo de pregunta:");
        System.out.println("1. Selección única");
        System.out.println("2. Selección múltiple");
        System.out.println("3. Verdadero/Falso");
        System.out.println("4. Abierta");

        String type = choose("Tipo: ", "1", "2", "3", "4");
        QuestionBuilder builder;
        List<Option> options = new ArrayList<>();
        String correct;

        if (type.equals("1")) {
            builder = new SingleChoiceQuestionBuilder();
            int amount = integerAtLeast("Cantidad de opciones (mínimo 2): ", 2);

            for (int i = 0; i < amount; i++) {
                String label = required("Etiqueta de opción " + (i + 1) + " (A, B...): ");
                String text = required("Texto de opción " + (i + 1) + ": ");
                options.add(new Option(label, text));
            }
            correct = required("Etiqueta de la única respuesta correcta:  ");
        } else if (type.equals("2")) {
            builder = new MultipleChoiceQuestionBuilder();
            int amount = integerAtLeast("Cantidad de opciones (mínimo 2): ", 2);
             for (int i = 0; i < amount; i++) {
                String label = required(
                    "Etiqueta de opción " + (i + 1) + " (A, B...): ");
                String text = required(
                    "Texto de opción " + (i + 1) + ": ");
                options.add(new Option(label, text));
            }

            correct = required(
            "Etiqueta(s) correcta(s), separadas por coma si son varias: ");


        } else if (type.equals("3")) {
            builder = new TrueFalseQuestionBuilder();
            correct = choose("Respuesta correcta (V/VERDADERO o F/FALSO): ",
                    "V", "VERDADERO", "F", "FALSO");
        } else {
            builder = new OpenQuestionBuilder();
            correct = required("Respuesta esperada exacta: ");
        }

        // El Builder conserva el proceso de construcción definido en el Taller 07.
        Question question = BANK.createQuestion(
                builder,
                required("Enunciado: "),
                required("Contexto: "),
                difficulty(),
                required("Categoría: "),
                required("Subtema: "),
                required("Competencia: "),
                required("Autor: "),
                required("Justificación/retroalimentación: "),
                required("Fuente bibliográfica: "),
                options,
                correct
        );

        System.out.println("Pregunta creada y registrada en estado BORRADOR:\n" + question);
    }

    private static void listQuestions() {
        List<Question> questions = BANK.getQuestions();
        if (questions.isEmpty()) {
            System.out.println("No hay preguntas registradas.");
            return;
        }

        for (int i = 0; i < questions.size(); i++) {
            System.out.println("[" + i + "]\n" + questions.get(i));
        }
    }

    private static Question chooseQuestion() {
        if (BANK.getQuestions().isEmpty()) {
            System.out.println("Primero cree o importe una pregunta.");
            return null;
        }

        listQuestions();
        while (true) {
            int index = integer("Índice de la pregunta (o -1 para cancelar): ");
            if (index == -1) {
                System.out.println("Operación cancelada.");
                return null;
            }
            if (index >= 0 && index < BANK.getQuestions().size()) {
                return BANK.getQuestion(index).orElse(null);
            }
            System.out.println("No existe una pregunta con ese índice. Inténtelo de nuevo.");
        }
    }

    private static void validateQuestion() {
        Question question = chooseQuestion();
        if (question == null) return;

        BANK.validate(question);
        System.out.println("Validación correcta. Estado actual: " + question.getStatus());
    }

    private static void changeState() {
        Question question = chooseQuestion();
        if (question == null) return;

        System.out.println("Estado actual: " + question.getStatus());
        System.out.println("1. Enviar a revisión (queda pendiente de revisión)");
        System.out.println("2. Asignar revisor (pasa a en revisión)");
        System.out.println("3. Aprobar");
        System.out.println("4. Rechazar");
        System.out.println("5. Volver a borrador (desde rechazada)");
        System.out.println("6. Publicar");
        System.out.println("7. Archivar");
        System.out.println("8. Publicar de una vez (valida, aprueba y publica)");

        String action = choose("Acción (o 0 para cancelar): ", "0", "1", "2", "3", "4", "5", "6", "7", "8");
        if (action.equals("0")) {
            System.out.println("Operación cancelada.");
            return;
        }

        switch (action) {
            case "1": BANK.submitForReview(question); break;
            case "2": BANK.assignReviewer(question); break;
            case "3": BANK.approve(question); break;
            case "4": BANK.reject(question); break;
            case "5": BANK.returnToDraft(question); break;
            case "6": BANK.publish(question); break;
            case "7": BANK.archive(question); break;
            default: BANK.publishQuestion(question); break;
        }
        System.out.println("Nuevo estado: " + question.getStatus());
    }

    private static void evaluate() {
        Question question = chooseQuestion();
        if (question == null) return;

        String answer = required(
                "Respuesta del estudiante (etiquetas separadas por coma para selección múltiple): ");
        EvaluationResult result = BANK.evaluate(question, answer);
        System.out.println("Resultado: " + result.getMessage() + " | Correcta: " + result.isCorrect());
    }

    private static void decorate() {
        Question question = chooseQuestion();
        if (question == null) return;

        System.out.println("Característica:");
        System.out.println("1. Alta dificultad");
        System.out.println("2. Contextualizada");
        System.out.println("3. Retroalimentación");
        System.out.println("4. Competencia");
        System.out.println("5. Marcada para revisión");

        String featureOption = choose("Seleccione (o 0 para cancelar): ", "0", "1", "2", "3", "4", "5");
        if (featureOption.equals("0")) {
            System.out.println("Operación cancelada.");
            return;
        }

        String featureName = featureName(featureOption);

        QuestionFeature feature = BANK.decorate(question, featureName);
        // Los decoradores se pueden apilar: cada uno envuelve al anterior.
        while (true) {
            String more = choose("¿Agregar otra característica? (S/N): ", "S", "N");
            if (more.equals("N")) break;
            String next = choose("Seleccione (1-5): ", "1", "2", "3", "4", "5");
            feature = BANK.decorate(feature, featureName(next));
        }
        System.out.println("Pregunta decorada: " + feature.describe() + " | Puntaje: " + feature.getScore());
    }

    private static String featureName(String option) {
        switch (option) {
            case "1": return "dificultad";
            case "2": return "contexto";
            case "3": return "retroalimentacion";
            case "4": return "competencia";
            default: return "revision";
        }
    }

    private static void importExternal() {
        System.out.println("Importando desde el servicio externo (formato JSON distinto al modelo interno)...");
        Question question = BANK.importExternalQuestion();
        System.out.println("Pregunta importada y adaptada mediante Adapter:\n" + question);
    }

    private static DifficultyLevel difficulty() {
        System.out.println("Dificultad: 1) FACIL 2) MEDIO 3) DIFICIL");
        String option = choose("Seleccione: ", "1", "2", "3");
        switch (option) {
            case "1": return DifficultyLevel.FACIL;
            case "2": return DifficultyLevel.MEDIO;
            default: return DifficultyLevel.DIFICIL;
        }
    }

    // Repite la lectura hasta recibir una de las opciones permitidas.
    private static String choose(String prompt, String... validOptions) {
        while (true) {
            String value = read(prompt);
            for (String validOption : validOptions) {
                if (value.equalsIgnoreCase(validOption)) return value.toUpperCase();
            }
            System.out.println("Entrada no válida. Opciones permitidas: " + String.join(", ", validOptions));
            System.out.println("Inténtelo de nuevo en este mismo campo.");
        }
    }

    private static String chooseIgnoreCase(String prompt, String... validOptions) {
        while (true) {
            String value = read(prompt);
            for (String validOption : validOptions) {
                if (value.equalsIgnoreCase(validOption)) return validOption;
            }
            System.out.println("Tipo no válido. Escriba una de estas opciones: "
                    + String.join(", ", validOptions) + ".");
        }
    }

    private static String read(String prompt) {
        System.out.print(prompt);
        return IN.nextLine().trim();
    }

    private static String required(String prompt) {
        while (true) {
            String value = read(prompt);
            if (!value.isEmpty()) return value;
            System.out.println("Este campo no puede quedar vacío. Inténtelo de nuevo.");
        }
    }

    private static int integer(String prompt) {
        while (true) {
            String value = read(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Entrada incorrecta: debe ingresar un número entero. Inténtelo de nuevo.");
            }
        }
    }

    private static int integerAtLeast(String prompt, int minimum) {
        while (true) {
            int value = integer(prompt);
            if (value >= minimum) return value;
            System.out.println("El número debe ser igual o mayor que " + minimum + ". Inténtelo de nuevo.");
        }
    }
}
