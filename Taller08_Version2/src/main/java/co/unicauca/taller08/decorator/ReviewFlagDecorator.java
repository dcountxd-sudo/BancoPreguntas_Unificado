package co.unicauca.taller08.decorator;

/** Decorator para marcar una pregunta que requiere revisión adicional. */
public class ReviewFlagDecorator extends QuestionDecorator {
 public ReviewFlagDecorator(QuestionFeature wrapped){super(wrapped);}

    @Override
    public String describe(){return wrapped.describe()+" | MARCADA PARA REVISIÓN";}
}
