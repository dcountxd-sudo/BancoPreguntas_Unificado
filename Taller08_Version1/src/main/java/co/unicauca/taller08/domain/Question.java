package co.unicauca.taller08.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import co.unicauca.taller08.state.DraftState;
import co.unicauca.taller08.state.QuestionState;

public class Question {
    private String statement = "";
    private QuestionType type = QuestionType.OPEN;
    private Difficultylevel difficultyLevel = Difficultylevel.MEDIUM;
    private final List<Option> options = new ArrayList<>();
    private String correctAnswer = "";
    private String explanation = "";
    private QuestionState state = new DraftState();

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement == null ? "" : statement;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type == null ? QuestionType.OPEN : type;
    }

    public Difficultylevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Difficultylevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel == null ? Difficultylevel.MEDIUM : difficultyLevel;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options.clear();
        if (options != null) {
            this.options.addAll(options);
        }
    }

    public void addOption(Option option) {
        if (option != null) {
            this.options.add(option);
        }
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer == null ? "" : correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation == null ? "" : explanation;
    }

    public QuestionState getState() {
        return state;
    }

    public void setState(QuestionState state) {
        if (state == null) {
            throw new IllegalArgumentException("El estado de la pregunta no puede ser nulo.");
        }
        this.state = state;
    }

    public void submitForReview() {
        state.submitForReview(this);
    }

    public void assignReviewer() {
        state.assignReviewer(this);
    }

    public void approve() {
        state.approve(this);
    }

    public void reject() {
        state.reject(this);
    }

    public void publish() {
        state.publish(this);
    }

    public void archive() {
        state.archive(this);
    }

    public boolean isAvailableForAssessment() {
        return state.isUsableInAssessment();
    }

    public void validate() {
        if (statement == null || statement.isBlank()) {
            throw new IllegalStateException("La pregunta debe tener un enunciado.");
        }

        if (type == null) {
            throw new IllegalStateException("El tipo de la pregunta es obligatorio.");
        }

        if ((type == QuestionType.MULTIPLE_CHOICE || type == QuestionType.SINGLE_CHOICE
            || type == QuestionType.MULTIPLE_ANSWER) && (options == null || options.isEmpty())) {
            throw new IllegalStateException("Una pregunta de selección múltiple debe tener opciones.");
        }

        if (type == QuestionType.TRUE_FALSE && (options == null || options.size() != 2)) {
            throw new IllegalStateException("Una pregunta verdadero/falso debe tener exactamente dos opciones.");
        }

        Set<String> optionTexts = new HashSet<>();
        for (Option option : options) {
            if (option == null || option.getText() == null || option.getText().isBlank()) {
                throw new IllegalStateException("Las opciones deben tener un texto válido.");
            }
            if (!optionTexts.add(option.getText().trim().toLowerCase(Locale.ROOT))) {
                throw new IllegalStateException("Las opciones de respuesta no pueden repetirse.");
            }
        }

        if (type == QuestionType.MULTIPLE_CHOICE || type == QuestionType.SINGLE_CHOICE
            || type == QuestionType.MULTIPLE_ANSWER || type == QuestionType.TRUE_FALSE) {
            long correctOptions = options.stream().filter(Option::isCorrect).count();
            if (correctOptions == 0) {
                throw new IllegalStateException("Debe existir al menos una opción correcta.");
            }
            if ((type == QuestionType.MULTIPLE_CHOICE || type == QuestionType.SINGLE_CHOICE
                    || type == QuestionType.TRUE_FALSE) && correctOptions != 1) {
                throw new IllegalStateException("Este tipo de pregunta debe tener exactamente una opción correcta.");
            }
        }
    }

    public boolean isValidAnswer(String answer) {
        if (answer == null || answer.isBlank()) {
            return false;
        }
        if (options == null || options.isEmpty()) {
            return false;
        }

        return options.stream()
                .anyMatch(option -> option.getText() != null
                        && option.getText().equalsIgnoreCase(answer.trim()));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Pregunta: ").append(statement).append(System.lineSeparator());
        builder.append("Tipo: ").append(type).append(System.lineSeparator());
        builder.append("Dificultad: ").append(difficultyLevel).append(System.lineSeparator());
        builder.append("Estado: ").append(state.getName()).append(System.lineSeparator());
        if (!options.isEmpty()) {
            builder.append("Opciones:").append(System.lineSeparator());
            for (Option option : options) {
                builder.append(" - ").append(option).append(System.lineSeparator());
            }
        }
        return builder.toString();
    }
}
