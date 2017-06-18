package com.max.algs.util;

import java.util.ArrayDeque;
import java.util.Deque;

public final class ArithConverterUtils {


    private ArithConverterUtils() {
        throw new IllegalStateException("Can't instantiate ArithConverterUtils");
    }

    /**
     * Convert infix notation to reverse polish notation.
     *
     * @return
     */
    public static String infixToRpn(String infix) {

        Deque<String> operands = new ArrayDeque<String>();

        Deque<Character> operators = new ArrayDeque<Character>();

        char[] arr = infix.toCharArray();

        for (char ch : arr) {
            if (ch == '(') {
                operators.push(ch);
            }
            else if (ch == ')') {

                char op = operators.pop();

                while (op != '(') {
                    compineOperation(op, operands);
                    op = operators.pop();
                }

            }
            // letter
            else if (Character.isLetter(ch)) {
                operands.push(String.valueOf(ch));
            }
            // operator
            else {

                while (!operators.isEmpty()) {

                    char prevOp = operators.pop();

                    if (operatorPriority(prevOp) > operatorPriority(ch)) {
                        //handle prevOp
                        compineOperation(prevOp, operands);
                    }
                    else {
                        operators.push(prevOp);
                        break;
                    }
                }

                operators.push(ch);
            }

        }

        while (!operators.isEmpty()) {
            char op = operators.pop();
            compineOperation(op, operands);
        }

        String rpn = operands.pop();

        return rpn;
    }

    private static void compineOperation(char op, Deque<String> operands) {

        String operator2 = operands.pop();
        String operator1 = operands.pop();

        operands.push(operator1 + operator2 + String.valueOf(op));

    }

    /*
     Priority: '+', '-', '*', '/', '^'
         op	ASCII code
         + 		43
        -		45
         *		42 ( 42 + 4 = 46 )
        /		47
        ^		94
     */
    private static int operatorPriority(char op) {

        int priority = ((int) op);

        if (op == '*') {
            priority += 4;
        }

        return priority;
    }


}
