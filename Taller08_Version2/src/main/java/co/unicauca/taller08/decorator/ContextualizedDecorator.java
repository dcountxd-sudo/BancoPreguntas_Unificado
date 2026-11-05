package co.unicauca.taller08.decorator;
public class ContextualizedDecorator extends QuestionDecorator {
 public ContextualizedDecorator(QuestionFeature wrapped){super(wrapped);}

    @Override
    public String describe(){return wrapped.describe()+" | Contextualizada";}
}
