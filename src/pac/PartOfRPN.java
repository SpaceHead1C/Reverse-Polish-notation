package pac;

public class PartOfRPN {
    public enum Type {OPERATOR, CONSTANT, VARIABLE}
    
    private Type typeOfPart;
    private String value;

    public PartOfRPN(Type typeOfPart, String value) {
        this.typeOfPart = typeOfPart;
        this.value = value;
    }

    public Type getTypeOfPart() {
        return typeOfPart;
    }

    public String getValue() {
        return value;
    }
}
