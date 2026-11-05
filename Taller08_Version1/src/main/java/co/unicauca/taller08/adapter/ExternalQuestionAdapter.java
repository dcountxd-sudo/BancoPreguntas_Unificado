package co.unicauca.taller08.adapter;

import co.unicauca.taller08.domain.Difficultylevel;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Convierte el JSON del proveedor externo al modelo de preguntas del banco. */
public class ExternalQuestionAdapter implements QuestionProvider {
    private static final Pattern QUESTION_FIELD = Pattern.compile("\"question_text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
    private static final Pattern ANSWER_FIELD = Pattern.compile("\"answer_text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
    private final ExternalQuestionService service;

    public ExternalQuestionAdapter(ExternalQuestionService service) {
        if (service == null) throw new IllegalArgumentException("El servicio externo es obligatorio.");
        this.service = service;
    }

    @Override
    public Question getQuestion() {
        String json = service.getQuestionJson();
        if (json == null || json.isBlank()) throw new IllegalStateException("El servicio externo devolvió JSON vacío.");
        String statement = extract(json, QUESTION_FIELD, "question_text");
        String expectedAnswer = extract(json, ANSWER_FIELD, "answer_text");
        Question question = new Question();
        question.setStatement(statement);
        question.setCorrectAnswer(expectedAnswer);
        question.setType(QuestionType.OPEN);
        question.setDifficultyLevel(Difficultylevel.MEDIUM);
        question.validate();
        return question;
    }

    private String extract(String json, Pattern pattern, String field) {
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) throw new IllegalStateException("Falta el campo '" + field + "' en el JSON externo.");
        return unescapeJson(matcher.group(1));
    }

    private String unescapeJson(String value) {
        return value.replace("\\\"", "\"").replace("\\n", "\n")
                .replace("\\r", "\r").replace("\\t", "\t").replace("\\\\", "\\");
    }
}
