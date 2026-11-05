package co.unicauca.taller08.service;
import co.unicauca.taller08.domain.*;
/** Valida reglas generales antes de publicar o evaluar una pregunta. */
public class QuestionValidator  {
    public void validate(Question q) {
        if(q==null) throw new IllegalArgumentException("La pregunta es obligatoria");
        if(q.getStatement()==null||q.getStatement().trim().isEmpty()) throw new IllegalStateException("El enunciado es obligatorio");
        if(q.getType()==null) throw new IllegalStateException("Debe seleccionar un tipo de pregunta");
        if(q.getCorrectAnswer()==null||q.getCorrectAnswer().trim().isEmpty()) throw new IllegalStateException("Debe definir la respuesta correcta");
        if(q.getType()==QuestionType.MULTIPLE_CHOICE && (q.getOptions()==null||q.getOptions().size()<2)) throw new IllegalStateException("La selección múltiple requiere al menos dos opciones");
        if(q.getType()==QuestionType.TRUE_FALSE && q.getOptions().size()!=2) throw new IllegalStateException("Verdadero/Falso debe tener dos opciones");
    }
}
