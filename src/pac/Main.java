package pac;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String infix;

        System.out.println("Enter infix expression:");

        Scanner scanner = new Scanner(System.in);
        infix = scanner.nextLine();

        RPN rpn = new RPN();
        try {
            rpn.parseExpression(infix);
        } catch (ParsingToPRNException e) {
            System.out.println(e.getMessage());
            return;
        }

        VariablesMap variablesMap = new VariablesMap();
        double output;

        System.out.println("Enter file name or 'exit' for end:");
        String filename = scanner.nextLine();
        while (!filename.toLowerCase().trim().equals("exit")) {
            try {
                variablesMap.load(filename);
            } catch (IllegalArgumentException | IOException e) {
                System.out.println(e.getMessage());
                System.out.println();
                System.out.println("Enter file name or 'exit' for end:");
                filename = scanner.nextLine();
                continue;
            }

            try {
                output = rpn.evaluate(variablesMap);
                System.out.print("Output: ");
                System.out.println(output);
            } catch (ExceptionInInitializerError | InputMismatchException | ArithmeticException e) {
                System.out.println(e.getMessage());
                System.out.println();
            } finally {
                System.out.println();
                System.out.println("Enter file name or 'exit' for end:");
                filename = scanner.nextLine();
            }
        }
    }
}
