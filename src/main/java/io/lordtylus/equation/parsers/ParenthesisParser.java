package io.lordtylus.equation.parsers;

import io.lordtylus.Equation;
import io.lordtylus.equation.Parenthesis;
import io.lordtylus.equation.functions.MathFunction;
import io.lordtylus.equation.functions.MathFunctionParser;
import io.lordtylus.equation.functions.StandardFunctions;
import io.lordtylus.equation.registers.EquationParserRegister;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This parser takes care of parenthesis in the equation and also matches functions directly in front of the opening parenthesis.
 * <p>
 * A valid expression for this parser would be sqrt(7+2), as the string needs to start with a function name, followed by the opening parenthesis, and end with the closing parenthesis.
 * the function name can be empty, for as long as empty matches to a {@link MathFunction} passed in the constructor.
 * <p>
 * The parts of the string inside the parentheses remain unaltered (except trimming) and are passed down the Equation for parsing, building the composite pattern of the Equation.
 * <p>
 * If the string could not be parsed, an empty optional is returned.
 */
public final class ParenthesisParser implements EquationParser {

    public static final ParenthesisParser DEFAULT = new ParenthesisParser(StandardFunctions.all());

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    private static final Pattern PARENTHESES_PATTERN = Pattern.compile("^(?<function>[a-z0-9\\s]+)?\\((?<inner>.+)(?<right>.*)\\)$", Pattern.CASE_INSENSITIVE);

    private final List<MathFunction> relevantFunctions;

    public ParenthesisParser(
            @NonNull List<MathFunction> relevantFunctions) {

        this.relevantFunctions = new ArrayList<>(relevantFunctions);
    }

    @Override
    public Optional<Parenthesis> parse(
            @NonNull String equation,
            @NonNull EquationParserRegister register) {

        try {

            String trimmedEquation = equation.trim();

            Matcher matcher = PARENTHESES_PATTERN.matcher(trimmedEquation);

            if (!matcher.matches())
                return Optional.empty();

            String inner = matcher.group("inner");
            Optional<Equation> innerEquation = Equation.parse(inner, register);

            if (innerEquation.isEmpty())
                return Optional.empty();

            String functionName = matcher.group("function");
            if (functionName == null)
                functionName = "";

            functionName = WHITESPACE_PATTERN.matcher(functionName)
                    .replaceAll("");

            Optional<MathFunction> functionOptional = MathFunctionParser.parse(relevantFunctions, functionName);
            if (functionOptional.isEmpty())
                return Optional.empty();

            MathFunction function = functionOptional.get();

            return Optional.of(new Parenthesis(function, innerEquation.get()));

        } catch (ParseException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ParseException(e);
        }
    }
}
