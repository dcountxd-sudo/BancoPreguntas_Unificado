package co.unicauca.taller08.adapter;

/** Implementación de ejemplo de un servicio externo que devuelve JSON. */
public class SampleExternalQuestionService implements ExternalQuestionService {
    private final String questionJson;

    public SampleExternalQuestionService() {
        this("{\"question_text\":\"¿Cuál es la capital de Colombia?\",\"answer_text\":\"Bogotá\"}");
    }

    public SampleExternalQuestionService(String questionJson) {
        if (questionJson == null || questionJson.isBlank()) {
            throw new IllegalArgumentException("El JSON externo es obligatorio.");
        }
        this.questionJson = questionJson;
    }

    @Override
    public String getQuestionJson() { return questionJson; }
}
