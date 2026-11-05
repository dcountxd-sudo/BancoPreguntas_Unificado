package co.unicauca.taller08.adapter;
import java.util.*;
/** Contrato de datos de ejemplo recibido desde otro sistema. */
public class ExternalQuestionDTO  {
    public String enunciado,  contexto,  dificultad,  categoria,  subtema,  competencia,  autor,  justificacion,  fuente,  respuestaCorrecta,  tipo;
    public List<ExternalOption> opciones=new ArrayList<>();
    public ExternalQuestionDTO(String tipo, String enunciado, String respuestaCorrecta) {
        this.tipo=tipo;
        this.enunciado=enunciado;
        this.respuestaCorrecta=respuestaCorrecta;
    }
    public static class ExternalOption  {
        public String codigo, texto;
        public ExternalOption(String codigo, String texto) {
            this.codigo=codigo;
            this.texto=texto;
        }
    }
}
