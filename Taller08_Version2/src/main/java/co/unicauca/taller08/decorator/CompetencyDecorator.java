package co.unicauca.taller08.decorator;
public class CompetencyDecorator extends QuestionDecorator {
 public CompetencyDecorator(QuestionFeature wrapped){super(wrapped);}

    @Override
    public String describe(){return wrapped.describe()+" | Competencia asociada";}
}
