package co.unicauca.taller08.decorator;
public class FeedbackDecorator extends QuestionDecorator {
 public FeedbackDecorator(QuestionFeature wrapped){super(wrapped);}

    @Override
    public String describe(){return wrapped.describe()+" | Con retroalimentación";}
}
