# Equation Parser for Java

[![Build](https://github.com/LordTylus/EquationParser/actions/workflows/gradle-main.yml/badge.svg)](https://github.com/LordTylus/EquationParser/actions/workflows/gradle.yml)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/LordTylus/EquationParser/actions/workflows/gradle.yml)
[![Branches](.github/badges/branches.svg)](https://github.com/LordTylus/EquationParser/actions/workflows/gradle.yml)
[![License](https://img.shields.io/github/license/LordTylus/EquationParser)](https://github.com/LordTylus/EquationParser/blob/main/LICENSE)
![GitHub top language](https://img.shields.io/github/languages/top/LordTylus/EquationParser)

## About

This framework allows you to parse mathematical equations such as `2*x^2+5` and calculate the result for a given value
x.

In order to do that the equation is parsed using a recursive algorithm following the order of operations in a composite
tree structure.

The above example would end up like a binary tree of the following structure:

```
    +
   / \
  *   5
 / \
2   ^
   / \
  x   2
```

Parentheses and functions such as square roots are also supported.

A hypothetical equation like `2*sqrt(x+2)^3` would be converted to the following tree:

```
  *
 / \
2   ^
   / \
sqrt  3
  |
  +
 / \
x   2
```

## List of supported functions

While it is possible to add more parsers and expressions to this framework, there is a number of functions already
supported:

| Operator | Description          |
|----------|----------------------|
| +        | Addition A + B       |
| -        | Subtraction A - B    |
| *        | Multiplication A * B |
| /        | Division A / B       |
| ^        | Exponentiation A ^ B |

| Function | Pattern     | Description                                                       |
|----------|-------------|-------------------------------------------------------------------|
| NOP      | ()          | No operator, returns same number                                  |
| ABS      | abs()       | Converts number to positive                                       |
| SIN      | sin()       | Calculates Sine of number in radians                              |
| ASIN     | asin()      | Calculates Arc Sine of number in radians                          |
| SINH     | sinh()      | Calculates Hyperbolic Sine of number in radians                   |
| COS      | cos()       | Calculates Cosine of number in radians                            |
| ACOS     | acos()      | Calculates Arc Cosine of number in radians                        |
| COSH     | cosh()      | Calculates Hyperbolic Cosine of number in radians                 |
| TAN      | tan()       | Calculates Tangents of number in radians                          |
| ATAN     | atan()      | Calculates Arc Tangents of number in radians                      |
| TANH     | tanh()      | Calculates Hyperbolic Tangents of number in radians               |
| EXP      | exp()       | Calculates Euler's number raised to the power of the given number |
| LOG      | log(), ln() | Calculates the natural logarithm of the given number              |
| LOG10    | log10()     | Calculates the decimal logarithm of the given number              |
| FLOOR    | floor()     | Rounds the number down to the next whole integer                  |
| ROUND    | round()     | Rounds the number to the closest whole integer                    |
| CEIL     | ceil()      | Rounds the number up to the next whole integer                    |
| SQRT     | sqrt()      | Calculates the square root of the given number                    |
| CBRT     | cbrt()      | Calculates the cube root of the given number                      |
| RAD      | rad()       | Converts the number in degrees to radians                         |
| DEG      | deg()       | Converts the number in radians to degrees                         |

Additional roots like fourth root can be calculated by passing a fractional exponent `x^(1/4)`.

Negative numbers can be passed when put in parentheses `2*(-3)`

## Installation

For the time being the framework is not published to maven-central yet, but I am planning to do so in the future.

Until then GitHub Packages can be used.

```groovy

repositories {
    maven {
        url 'https://maven.pkg.github.com/LordTylus/EquationParser'
        credentials {
            username = "<USERNAME>"
            password = "<TOKEN>"
        }
    }
}

depedencies {
    implementation 'io.github.lordtylus.equation:equation-parser:1.0'
}
```

## Demo

```java

public static void main(String[] args) {

    String input = "2*[x]^2+5";

    Equation equation = Equation.parse(input).orElseThrow();

    SimpleStorage storage = new SimpleStorage();
    storage.putValue("x", 3);

    Result result = equation.evaluate(storage);
    System.out.println(result.asDouble()); //23.0
}
```

You can find more demos [here](src/demo/java/io/github/lordtylus/jep)
