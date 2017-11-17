package pac;

public class ParsingToPRNException extends Exception {
    public ParsingToPRNException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "Can not parse string expression to RPN with reason: " + super.getMessage();
    }
}
