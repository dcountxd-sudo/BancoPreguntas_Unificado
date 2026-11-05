package co.unicauca.taller08.templatemethod;
public class EvaluationResult {
 private final boolean correct; private final String message;
 public EvaluationResult(boolean correct,String message){this.correct=correct;this.message=message;}
 public boolean isCorrect(){return correct;} public String getMessage(){return message;}
 @Override public String toString(){return message;}
}
