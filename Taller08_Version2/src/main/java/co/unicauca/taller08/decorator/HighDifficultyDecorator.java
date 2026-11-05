package co.unicauca.taller08.decorator;
public class HighDifficultyDecorator extends QuestionDecorator {
 public HighDifficultyDecorator(QuestionFeature wrapped){super(wrapped);}

    @Override
    public String describe(){return wrapped.describe()+" | Dificultad alta";}
}
