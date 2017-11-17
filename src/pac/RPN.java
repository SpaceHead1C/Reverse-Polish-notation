package pac;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RPN {
    private ArrayList<PartOfRPN> postfix;
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

    public void parseExpression(String infixExpression) throws ParsingToPRNException {
        ready = false;
        postfix.clear();
        Stack<String> stackOperations = new Stack<>();
        OperandValidator ov = new OperandValidator();
        StringTokenizer tokenizer = new StringTokenizer(infixExpression, delimString, true);
        String currentToken;

        while (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken();

            if (!tokenizer.hasMoreTokens() && isOperator(currentToken)) {
                throw new ParsingToPRNException("Incorrect expression.");
            }
            if (currentToken.equals(" ")) {
                continue;
            }
            if (isDelimiter(currentToken)) {
                switch (currentToken) {
                    case "(":
                        stackOperations.push(currentToken);
                        break;
                    case ")":
                        while (!stackOperations.peek().equals("(")) {
                            postfix.add(new PartOfRPN(PartOfRPN.Type.OPERATOR, stackOperations.pop()));
                            if (stackOperations.isEmpty()) {
                                throw new ParsingToPRNException("Brackets not matched.");
                            }
                        }

                        stackOperations.pop();
                        break;
                    default:
                        while (!stackOperations.isEmpty() && (priority(currentToken) <= priority(stackOperations.peek()))) {
                            postfix.add(new PartOfRPN(PartOfRPN.Type.OPERATOR, stackOperations.pop()));
                        }

                        stackOperations.push(currentToken);
                        break;
                }
            } else {
                ov.setExpression(currentToken);
                try {
                    OperandValidator.State state = ov.validate();
                    switch (state) {
                        case FN:
                            postfix.add(new PartOfRPN(PartOfRPN.Type.CONSTANT, currentToken));
                            break;
                        case FW:
                            postfix.add(new PartOfRPN(PartOfRPN.Type.VARIABLE, currentToken));
                            break;
                        case E:
                            throw new ParsingToPRNException(ov.getMessage());
                        default:
                            throw new ParsingToPRNException("Unexpected state of operand validator: " + state);
                    }
                } catch (IllegalArgumentException e) {
                    throw new ParsingToPRNException(e.getMessage());
                }
            }
        }

        while (!stackOperations.isEmpty()) {
            if (isOperator(stackOperations.peek())) {
                postfix.add(new PartOfRPN(PartOfRPN.Type.OPERATOR, stackOperations.pop()));
            } else {
                throw new ParsingToPRNException("Brackets not matched.");
            }
        }

        this.infixExpression = infixExpression;
        ready = true;
    }

    private boolean isOperator (String token) {
        return operators.contains(token);
    }

    private boolean isDelimiter(String token) {
        return delimiters.contains(token);
    }

    private int priority(String token) {
        switch (token) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            case "(":
                return 4;
            default:
                return 5;
        }
    }

    public double evaluate(VariablesMap variablesMap) throws ExceptionInInitializerError, InputMismatchException, ArithmeticException {
        if (!ready) {
            throw new ExceptionInInitializerError("Postfix expression does not set");
        }
//        if (check content match){
//            throw new InputMismatchException("message");
//        }

        HashMap<String, Double> map = variablesMap.getMap();
        Stack<Double> stack = new Stack<>();
        Double a, b;
        for (PartOfRPN operand : postfix) {
            switch (operand.getTypeOfPart()) {
                case CONSTANT:
                    stack.push(Double.valueOf(operand.getValue()));
                    break;
                case VARIABLE:
                    stack.push(map.get(operand.getValue()));
                    break;
                case OPERATOR:
                    switch (operand.getValue()) {
                        case "+":
                            stack.push(stack.pop() + stack.pop());
                            break;
                        case "-":
                            b = stack.pop();
                            a = stack.pop();
                            stack.push(a - b);
                            break;
                        case "*":
                            stack.push(stack.pop() * stack.pop());
                            break;
                        case "/":
                            b = stack.pop();
                            a = stack.pop();
                            try {
                                stack.push(a / b);
                            } catch (ArithmeticException e) {
                                throw e;
                            }
                            break;
                        case "^":
                            stack.push(Math.pow(stack.pop(), stack.pop()));
                            break;
                    }
                    break;
            }
        }

        return stack.pop();
    }

    public String getInfixExpression() {
        return infixExpression;
    }

    public ArrayList<PartOfRPN> getPostfix() {
        return (ArrayList<PartOfRPN>)postfix.clone();
    }

    public HashSet<String> getVariables() {
        HashSet<String> outputSet = new HashSet<>();

        for (PartOfRPN operand : postfix) {
            if (operand.getTypeOfPart() == PartOfRPN.Type.VARIABLE) {
                outputSet.add(operand.getValue());
            }
        }

        return outputSet;
    }
}
