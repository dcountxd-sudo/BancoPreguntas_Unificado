package co.unicauca.taller08.adapter;

import co.unicauca.taller08.domain.Question;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ADAPTER (de objeto): implementa QuestionProvider y delega en ExternalQuestionService,
 * transformando el JSON externo al modelo interno Question.
 */
public class ExternalQuestionAdapter implements QuestionProvider {

    private final ExternalQuestionService service;
    private final ExternalQuestionMapper mapper = new ExternalQuestionMapper();

    public ExternalQuestionAdapter(ExternalQuestionService service) {
        if (service == null) throw new IllegalArgumentException("El servicio externo es obligatorio");
        this.service = service;
    }

    @Override
    public Question getQuestion() {
        String data = service.getQuestion();

        // Transformar los datos externos al modelo interno Question.
        ExternalQuestionDTO dto = new ExternalQuestionDTO(
                "abierta", field(data, "statement"), field(data, "answer"));
        return mapper.adapt(dto);
    }

    private String field(String json, String name) {
        Matcher m = Pattern.compile("\"" + name + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").matcher(json);
        if (!m.find()) {
            throw new IllegalArgumentException("El servicio externo no envió el campo '" + name + "'");
        }
        return m.group(1).replace("\\\"", "\"");
    }
}
