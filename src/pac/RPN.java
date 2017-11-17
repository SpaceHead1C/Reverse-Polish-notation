package pac;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RPN {
    private ArrayList<?> postfix;
    private Set<String> variables;
    private Set<String> operators = Stream.of("+", "-", "*", "/", "^").collect(Collectors.toCollection(HashSet::new));
    private Set<String> delimiters = Stream.of("(", ")", "+", "-", "*", "/", "^").collect(Collectors.toCollection(HashSet::new));
    private String delimString = "() +-*/^";
    private String infixExpression;
    private boolean ready;

    public RPN() {
        infixExpression = "";
        ready = false;
        postfix = new ArrayList<>();
        variables = new HashSet<>();

    }

    public void parseExpression(String infixExpression) {}

    public double evaluate() {
        return 0D;
    }

    public String getInfixExpression() {
        return infixExpression;
    }

    public ArrayList<?> getPostfix() {
        return (ArrayList<?>)postfix.clone();
    }
}
