package pac;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperandValidator {
    public enum State {S, W, FW, NN, NR, FN, E}

    private State currentState;
    private String expression;
    private int nn = 0;
    private double nr = 0D;
    private String param = "";
    private String message = "";

    private Set<Character> digits = Stream.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            .collect(Collectors.toCollection(HashSet::new));
    private Set<Character> chars = Stream.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
            .collect(Collectors.toCollection(HashSet::new));

    public OperandValidator() {
        this.expression = "";
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getMessage() {
        return message;
    }

    public State validate() throws IllegalArgumentException {
        int nDegree = 1;
        int rDegree = 1;
        StringBuilder sbParam = new StringBuilder();
        char[] tokens = expression.toCharArray();
        currentState = State.S;

        if (tokens.length == 0) {
            currentState = State.NN;

            return currentState;
        }

        for (char token : tokens) {
            switch (currentState) {
                case S:
                    if (digits.contains(token)) {
                        currentState = State.NN;
                        nn = Character.digit(token, 10);
                        if (nn > 0) {
                            nDegree *= 10;
                        }
                    } else if (chars.contains(Character.toLowerCase(token))) {
                        currentState = State.W;
                        sbParam.append(token);
                    } else if (token == '.' || token == ',') {
                        currentState = State.NR;
                        rDegree *= 10;
                    } else {
                        currentState = State.E;
                        message = "Unexpected symbol '" + token + "' in '" + expression + "'";

                        return currentState;
                    }
                    break;
                case NN:
                    if (digits.contains(token)) {
                        nn += Character.digit(token, 10) * nDegree;
                        if (nn > 0) {
                            nDegree *= 10;
                        }
                    } else if (token == '.' || token == ',') {
                        currentState = State.NR;
                        nr = nn;
                        rDegree *= 10;
                    } else {
                        currentState = State.E;
                        message = "Unexpected symbol '" + token + "' in '" + expression + "'";

                        return currentState;
                    }
                    break;
                case W:
                    if (chars.contains(Character.toLowerCase(token))) {
                        sbParam.append(token);
                    } else {
                        currentState = State.E;
                        message = "Unexpected symbol '" + token + "' in '" + expression + "'";

                        return currentState;
                    }
                    break;
                case NR:
                    if (digits.contains(token)) {
                        nr += (double)Character.digit(token, 10) / rDegree;
                        rDegree *= 10;
                    } else {
                        currentState = State.E;
                        message = "Unexpected symbol '" + token + "' in '" + expression + "'";

                        return currentState;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected state: " + currentState);
            }
        }

        if (currentState == State.NN) {
            currentState = State.FN;
            nr = (double)nn;
        } else if (currentState == State.NR) {
            currentState = State.FN;
        } else if (currentState == State.W) {
            currentState = State.FW;
            param = sbParam.toString();
        } else {
            throw new IllegalArgumentException("Unexpected state: " + currentState);
        }

        return currentState;
    }
}
