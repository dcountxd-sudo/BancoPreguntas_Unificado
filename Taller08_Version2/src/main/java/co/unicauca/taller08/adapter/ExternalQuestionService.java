package co.unicauca.taller08.adapter;

/** ADAPTEE: servicio de una institución externa que devuelve la pregunta como JSON con otro formato. */
public class ExternalQuestionService {

    public String getQuestion() {
        return """
                {
                    "statement": "¿Cuál es la capital de Colombia?",
                    "answer": "Bogotá"
                }
                """;
    }
}
