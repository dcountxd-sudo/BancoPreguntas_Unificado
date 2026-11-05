package co.unicauca.taller08.adapter;
import co.unicauca.taller08.builder.*;
import co.unicauca.taller08.director.QuestionDirector;
import co.unicauca.taller08.domain.*;
import java.util.*;
/** Convierte el DTO externo al modelo Question (usa los Builders del Taller 07). Lo usa ExternalQuestionAdapter. */
public class ExternalQuestionMapper  {
    public Question adapt(ExternalQuestionDTO dto) {
        if(dto==null)throw new IllegalArgumentException("El DTO externo es obligatorio");
        String type=dto.tipo==null?"":dto.tipo.trim().toLowerCase();
        QuestionBuilder b;
        if(type.equals("verdadero_falso")||type.equals("verdadero/falso")||type.equals("true_false"))b=new TrueFalseQuestionBuilder();
        else if(type.equals("abierta")||type.equals("open"))b=new OpenQuestionBuilder();
        else b=new MultipleChoiceQuestionBuilder();
        List<Option> opts=new ArrayList<>();
        if(dto.opciones!=null)for(ExternalQuestionDTO.ExternalOption o:dto.opciones)opts.add(new Option(o.codigo, o.texto));
        String correct=value(dto.respuestaCorrecta, "");
        if(b instanceof TrueFalseQuestionBuilder) {
            if(correct.equalsIgnoreCase("verdadero")||correct.equalsIgnoreCase("true"))correct="V";
            if(correct.equalsIgnoreCase("falso")||correct.equalsIgnoreCase("false"))correct="F";
        }
        return new QuestionDirector(b).make(value(dto.enunciado, "Pregunta importada"), value(dto.contexto, "Importada desde sistema externo"), difficulty(dto.dificultad), value(dto.categoria, "Importadas"), value(dto.subtema, "General"), value(dto.competencia, "General"), value(dto.autor, "Sistema externo"), value(dto.justificacion, "Pendiente de ampliar"), value(dto.fuente, "Sistema externo"), opts, correct);
    }
    private String value(String v, String fallback) {
        return v==null||v.trim().isEmpty()?fallback:v;
    }
    private DifficultyLevel difficulty(String d) {
        if(d==null)return DifficultyLevel.MEDIO;
        try {
            return DifficultyLevel.valueOf(d.trim().toUpperCase());
        } catch(Exception e) {
            return DifficultyLevel.MEDIO;
        }
    }
}
