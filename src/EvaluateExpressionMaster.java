import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import static java.lang.Character.isDigit;
import static java.lang.Character.isSpaceChar;

enum FSM_TRANSITIONS
{
    REJECT(0) ,INTEGER(1),REAL(2),NEGATIVE(3),OPERATOR(4),UNKNOWN(5),SPACE(6);
    int n;
    FSM_TRANSITIONS(int n){this.n = n;}
}

public class EvaluateExpressionMaster{

    public static void main(String[] args) throws Exception {
        System.out.println(EvaluateExpressionMaster.equateEquation("((sin(-4^5)*1.4)/(sqrt(23+2)--2.8))*(cos(1%2)/(7.28*.1987)^(tan(23)))"));
    }


    public static String infix;
    public static String postfix;
    private static char currentChar;

/*
    Infix To Postfix Conversion & Evaluation
    Math Operators:
        (+) --> Addition
        (-) --> Subtraction
        (*) --> Multiplication
        (/) --> Division
        (%) --> Modulus
        (^) --> Power
        (sqrt(x)) --> Square Root of x
        (sin(x)) --> Sine of x
        (cos(x)) --> Cosine of x
        (tan(x)) --> Tangent of x
        (sec(x)) --> secant of x
        (coSec(x)) --> coSecant of x
        (cot(x)) --> cotangent of x
        (ln(x)) --> natural log of x or log of x with base e
        (log(x)) --> log of x with base 10
        (-) --> Negative Number
*/

    private static final int[][] stateTable = new int[][]{{0, FSM_TRANSITIONS.INTEGER.n, FSM_TRANSITIONS.REAL.n, FSM_TRANSITIONS.NEGATIVE.n, FSM_TRANSITIONS.OPERATOR.n, FSM_TRANSITIONS.UNKNOWN.n, FSM_TRANSITIONS.SPACE.n},
            /* STATE 1 */ {FSM_TRANSITIONS.INTEGER.n, FSM_TRANSITIONS.INTEGER.n, FSM_TRANSITIONS.REAL.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n},
            /* STATE 2 */ {FSM_TRANSITIONS.REAL.n, FSM_TRANSITIONS.REAL.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n},
            /* STATE 3 */ {FSM_TRANSITIONS.NEGATIVE.n, FSM_TRANSITIONS.INTEGER.n, FSM_TRANSITIONS.REAL.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n},
            /* STATE 4 */ {FSM_TRANSITIONS.OPERATOR.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n},
            /* STATE 5 */ {FSM_TRANSITIONS.UNKNOWN.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.UNKNOWN.n, FSM_TRANSITIONS.REJECT.n},
            /* STATE 6 */ {FSM_TRANSITIONS.SPACE.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n, FSM_TRANSITIONS.REJECT.n}};

    public static double equateEquation(String equation) throws Exception {

        postfix = "";
        ArrayList<String> tokens;
        infix = equation;
        ArrayList<String> myReplaceable = new ArrayList<>(Arrays.asList("sin", "cos", "tan", "sqrt", "cot", "sec", "coSec", "ln", "log"));
        ArrayList<String> replaceWith = new ArrayList<>(Arrays.asList("s", "c", "t", "w", "z", "x", "y", "e", "l"));
        for (int i = 0; i < myReplaceable.size(); i++)
        {
            String x = myReplaceable.get(i);
            String y = replaceWith.get(i);
            if(infix.contains(x)) {
                infix = infix.replaceAll(x, y);
            }
        }

        ConvertInfixToPostfix();

        tokens = Lexer(postfix);

        return  EvaluatePostfix(tokens);
    }

    public static void ConvertInfixToPostfix() throws Exception {
        Stack<Character> charStack = new Stack<>();

        infix = infix.replaceAll(" ", "");

        for (int x = 0; x < infix.length(); ++x)
        {
            if (infix.charAt(x) != '-')
            {
                continue;
            }
            else if (x + 1 < infix.length() && IsMathOperator(infix.charAt(x + 1)))
            {
                continue;
            }
            if (x == 0 || infix.charAt(x - 1) == '(' || IsMathOperator(infix.charAt(x - 1)))
            {
                infix = infix.substring(0, x) + '~' + infix.substring(x + 1);
            }
        }

        for (int x = 0; x < infix.length(); ++x)
        {
            if ((isDigit(infix.charAt(x))) || (infix.charAt(x) == '.') || (infix.charAt(x) == '~'))
            {
                postfix += infix.charAt(x);
            }
            else if (isSpaceChar(infix.charAt(x)))
            {
                continue;
            }
            else if (IsMathOperator(infix.charAt(x)))
            {
                postfix += " ";
                while ((!charStack.empty()) && (OrderOfOperations(charStack.lastElement()) >= OrderOfOperations(infix.charAt(x)))) {
                    postfix += charStack.lastElement();
                    charStack.pop();
                }
                charStack.push(infix.charAt(x));
            }
            else if (infix.charAt(x) == '(')
            {
                charStack.push(infix.charAt(x));
            }
            else if (infix.charAt(x) == ')')
            {
                while ((!charStack.empty()) && (charStack.lastElement() != '('))
                {
                    postfix += charStack.lastElement();
                    charStack.pop();
                }

                if (!charStack.empty())
                {
                    charStack.pop();
                }
                else
                {
                    System.out.print( "\nPARENTHESES MISMATCH #1\n");
                    throw new Exception( "\nPARENTHESES MISMATCH #1\n");
                }
            }
            else
            {
                throw new Exception("INVALID INPUT");
            }
        }

        while (!charStack.empty())
        {
            postfix += charStack.lastElement();
            charStack.pop();
        }
    }

    private static boolean IsMathOperator(char token) {
        // this function checks if operand is a math operator
        return switch (Character.toLowerCase(token)) {
            case '+', '-', '*', '/', '%', '^', 'w', 'c', 's', 't', 'x', 'y', 'z', 'e', 'l' -> true;
            default -> false;
        };
    }

    private static int OrderOfOperations(char token) {
        return switch (Character.toLowerCase(token)) {
            case 'c', 's', 't', 'x', 'y', 'z', 'e', 'l' -> 5;
            case '^', 'w' -> 4;
            case '*', '/', '%' -> 3;
            case '-' -> 2;
            case '+' -> 1;
            default -> 0;
        };
    }

    private static ArrayList<String> Lexer(String postfix) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        currentChar = ' ';
        int col;
        int currentState = FSM_TRANSITIONS.REJECT.n;
        String currentToken = "";

        for (int x = 0; x < postfix.length();)
        {
            currentChar = postfix.charAt(x);

            col = Get_FSM_Col(currentChar);


            if ((currentState == FSM_TRANSITIONS.REAL.n) && (col == FSM_TRANSITIONS.REAL.n))
            {
//                System.out.println("\nINVALID INPUT #2\n");
                throw new Exception("INVALID INPUT");
            }
            currentState = stateTable[currentState][col];

            if (currentState == FSM_TRANSITIONS.REJECT.n)
            {
                if (!currentToken.equals(" ")) // we don't care about whitespace
                {
                    tokens.add(currentToken);
                }
                currentToken = "";
            }
            else
            {
                currentToken += currentChar;
                ++x;
            }
        }
        if (!currentToken.equals(" "))
        {
            tokens.add(currentToken);
        }
        return tokens;
    }

    private static int Get_FSM_Col(char currentChar) {
        if (isSpaceChar(currentChar))
        {
            return FSM_TRANSITIONS.SPACE.n;
        }

        else if (isDigit(currentChar))
        {
            return FSM_TRANSITIONS.INTEGER.n;
        }

        else if (currentChar == '.')
        {
            return FSM_TRANSITIONS.REAL.n;
        }

        else if (currentChar == '~')
        {
            // currentChar = '-';
            EvaluateExpressionMaster.currentChar = '-';
            return FSM_TRANSITIONS.NEGATIVE.n;
        }

        else if (IsMathOperator(currentChar))
        {
            return FSM_TRANSITIONS.OPERATOR.n;
        }
        return FSM_TRANSITIONS.UNKNOWN.n;
    }

    public static double EvaluatePostfix(ArrayList<String> postfix) throws Exception {
        double op1;
        double op2;
        double answer = 0;
        Stack<Double> doubleStack = new Stack<>();

        for (String s : postfix) {
            if ((isDigit((s.charAt(0))) || (s.charAt(0) == '.'))) {
                doubleStack.push(at_of(s));
            }
            else if ((s.length() > 1) && ((s.charAt(0) == '-') && (isDigit(s.charAt(1)) || (s.charAt(1) == '.')))) {
                doubleStack.push(at_of(s));
            }
            else if (IsMathOperator(s.charAt(0)) && (!doubleStack.empty())) {
                char token = Character.toLowerCase(s.charAt(0));

                if (token == 'w' || token == 's' || token == 'c' || token == 't' || token == 'x' || token == 'y' || token == 'z' || token == 'e' || token == 'l') {
                    op2 = 0;
                    op1 = doubleStack.lastElement();
                    doubleStack.pop();
                    answer = Calculate(token, op1, op2);
                    doubleStack.push(answer);
                }
                else if (doubleStack.size() > 1) {
                    op2 = doubleStack.lastElement();
                    doubleStack.pop();
                    op1 = doubleStack.lastElement();
                    doubleStack.pop();
                    answer = Calculate(token, op1, op2);
                    doubleStack.push(answer);
                }
            }
            else {
//                System.out.print("\nINVALID INPUT #3\n");
                throw new Exception("INVALID INPUT");
            }
        }
        if (!doubleStack.empty()) {
            answer = doubleStack.lastElement();
        }
        return answer;
    }

    public static double Calculate(char token, double op1, double op2) throws Exception {
            double ans;
            switch (Character.toLowerCase(token)) {
                case '+' -> ans = op1 + op2;
                case '-' -> ans = op1 - op2;
                case '*' -> ans = op1 * op2;
                case '/' -> {
                    if (op1 == 0) {
                        throw new Exception("Can't Divide by Zero");
                    }
                    else {
                        ans = op1 / op2;
                    }
                }
                case '%' -> ans = ((int) op1 % (int) op2) + (op1 - (int) op1);   //   op2 = (int)op1;
                case '^' -> ans = Math.pow(op1, op2);
                case 'w' -> ans = Math.sqrt(op1);
                case 'c' -> ans = Math.cos(op1);
                case 's' -> ans = Math.sin(op1);
                case 't' -> ans = Math.tan(op1);
                case 'x' -> ans = 1 / (Math.cos(op1));
                case 'y' -> ans = 1 / (Math.sin(op1));
                case 'z' -> ans = 1 / (Math.tan(op1));
                case 'e' -> ans = Math.log(op1);
                case 'l' -> ans = Math.log10(op1);
                default -> ans = 0;
            }
            return ans;
    }

    private static double at_of(String str) {
        double ans = 0.0;
        if (str.length() == 0) return ans;
        else {
            int i;
            for (i = 0; i < str.length(); i++) {
                if (str.charAt(i) != ' ') {
                    break;
                }
            }
            if (i != str.length() && (isDigit(str.charAt(i)) || str.charAt(i) == '.' || str.charAt(i) == '-')) {
                String dlb = "";
                for (int j = i; j < str.length(); j++) {
                    if (isDigit(str.charAt(j)) || str.charAt(j) == '.' || str.charAt(i) == '-')
                        dlb += str.charAt(j);
                    else break;
                }
                ans = Double.parseDouble(dlb);
            }
        }
        return ans;
    }
}