package com.tsystems.javaschool.tasks.calculator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Calculator {

    private final DecimalFormat DF;

    {
        Locale.setDefault(Locale.US);
        DF = new DecimalFormat("#.####");
        DF.setRoundingMode(RoundingMode.CEILING);
    }

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        List<Token> tokens = splitIntoTokens(statement);
        tokens = infixToRPN(tokens);
        Double value = calculateRPN(tokens);
        return formatDouble(value);
    }

    private List<Token> splitIntoTokens(String statement) {
        if (statement == null || statement.isEmpty())
            return null;
        List<Token> tokens = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        try {
            for (char ch : statement.toCharArray()) {
                if (ch == ' ')
                    continue;
                if (isDigitOrDot(ch)) {
                    sb.append(ch);
                } else if (isOP(ch)) {
                    if (sb.length() > 0) {
                        tokens.add(new DoubleToken(Double.valueOf(sb.toString())));
                        sb.delete(0, sb.length());
                    }
                    tokens.add(new CharacterToken(ch));
                } else {
                    return null;
                }
            }
            if (sb.length() > 0) {
                tokens.add(new DoubleToken(Double.valueOf(sb.toString())));
                sb.delete(0, sb.length());
            }
            return tokens;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private List<Token> infixToRPN(List<Token> tokens) {
        if (tokens == null)
            return null;
        Stack<CharacterToken> stack = new Stack<>();
        List<Token> res = new LinkedList<>();
        for (Token token : tokens) {
            if (token instanceof DoubleToken) {
                res.add(token);
                continue;
            }
            CharacterToken charToken = (CharacterToken) token;
            if (charToken.is('(')) {
                stack.push(charToken);
            } else if (charToken.is(')')) {
                while (true) {
                    if (stack.empty())
                        return null;
                    CharacterToken next = stack.pop();
                    if (next.is('('))
                        break;
                    res.add(next);
                }
            } else if (charToken.isOperation()) {
                while (!stack.empty() && stack.peek().getPriority() >= charToken.getPriority()) {
                    res.add(stack.pop());
                }
                stack.push(charToken);
            } else {
                return null;
            }
        }
        while (!stack.empty())
            res.add(stack.pop());
        return res;
    }

    private boolean isDigitOrDot(char ch) {
        return '0' <= ch && ch <= '9' || ch == '.';
    }

    private boolean isOperation(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private boolean isOP(char ch) {
        return isOperation(ch) || ch == '(' || ch == ')';
    }

    private Double calculateRPN(List<Token> tokens) {
        if (tokens == null)
            return null;
        Stack<DoubleToken> stack = new Stack<>();
        for (Token token : tokens) {
            if (token instanceof DoubleToken) {
                stack.push((DoubleToken) token);
                continue;
            }
            if (stack.size() < 2)
                return null;
            double y = stack.pop().getValue();
            double x = stack.pop().getValue();
            double res;
            switch (((CharacterToken) token).getCharacter()) {
                case '+':
                    res = x + y;
                    break;
                case '-':
                    res = x - y;
                    break;
                case '*':
                    res = x * y;
                    break;
                case '/':
                    if (y == 0)
                        return null;
                    res = x / y;
                    break;
                default:
                    return null;
            }
            stack.push(new DoubleToken(res));
        }
        return stack.pop().getValue();
    }

    private String formatDouble(Double value) {
        if (value == null)
            return null;
        if (value == value.intValue())
            return String.valueOf(value.intValue());
        return DF.format(value);
    }

}
