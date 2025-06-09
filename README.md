# Equation Parser for Java

[![Build](https://github.com/LordTylus/EquationParser/actions/workflows/gradle-main.yml/badge.svg)](https://github.com/LordTylus/EquationParser/actions/workflows/gradle-main.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.lordtylus.equation/equation-parser.svg)](https://central.sonatype.com/artifact/io.github.lordtylus.equation/equation-parser)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/LordTylus/EquationParser/actions/workflows/gradle.yml)
[![Branches](.github/badges/branches.svg)](https://github.com/LordTylus/EquationParser/actions/workflows/gradle.yml)
[![License](https://img.shields.io/github/license/LordTylus/EquationParser)](https://github.com/LordTylus/EquationParser/blob/main/LICENSE)
![GitHub top language](https://img.shields.io/github/languages/top/LordTylus/EquationParser)

## About

This framework allows you to parse mathematical expressions such as `2*[x]^2+5` and calculate the result for a given
value x.

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

A hypothetical equation like `2*sqrt([x]+2)^3` would be converted to the following tree:

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

## Variables

Per default variables are expected to be wrapped between brackets [ and ].
This behavior can be changed via configuration.

You can find out how in the demos [here](src/demo/java/io/github/lordtylus/jep)

Different variable styles have different up and downsides.

| Feature                               | two distinct symbols                     | one symbol    | no symbol         |
|---------------------------------------|------------------------------------------|---------------|-------------------|
| Supported                             | ✅ [hello]                                | ✅ :hello:     | ✅ hello           |
| Supports whitespaces                  | ✅ [h e l l o]                            | ✅ :h e l l o: | ✅ h e l l o       |
| Supports leading trailing whitespaces | ✅ [ hello ]                              | ✅ : hello :   | ❌ [hello]         |
| Supports operators                    | ✅ [h+o]                                  | ✅ :h+o:       | ❌ [h]+[o]         |
| Supports number-only names            | ✅ [123]                                  | ✅ :123:       | ❌ 123 <- constant |
| Supports nesting in name              | ✅ [h[1-1]o],<br/>❌ [h]1-1[o] parse error | ❌ parse error | ❌ [h1]-[1o]       |

Depending on how you want to use this framework you should choose what type of variables fits your use-case best.

The default supports most types of variable names, and can be used when you don't have much control over the variable
names yourself. This can happen if a different system writes those to a database, and you just read them from there.
Additionally, it offers the least risk to encounter parsing errors.

The default option might be too complicated for users to type in manually, so you can instead choose one of the other
options. However, be aware that these options are more prone to errors due to a reduced feature set. Or unwanted
limits in variable names.

As you can see in the examples in the table above. Without any symbol you can produce valid strings that will be parsed,
but not lead to the desired result.

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

This framework is published to both Maven Central using Sonatype, and GitHub Packages.

You can install the latest release here:

Maven:

```xml

<dependency>
    <groupId>io.github.lordtylus.equation</groupId>
    <artifactId>equation-parser</artifactId>
    <version>1.1.1</version>
</dependency>
```

Gradle:

```groovy
implementation 'io.github.lordtylus.equation:equation-parser:1.1.1'
```

Or you can install the latest snapshot build here:

```groovy
repositories {
    mavenCentral()
    maven {
        url 'https://central.sonatype.com/repository/maven-snapshots/'
    }
}

depedencies {
    implementation 'io.github.lordtylus.equation:equation-parser:1.2.0-SNAPSHOT'
}
```

## Demo

```java
public static void main(String[] args) {

    String input = "(7+3)*(6-3)+216/3^3";

    Equation equation = Equation.parse(input).get();

    Result result = equation.evaluate();

    System.out.println(result.asDouble()); //38.0
}
```

You can find more demos [here](src/demo/java/io/github/lordtylus/jep)

## Parsing Errors

This framework is able to report errors if they occur.

Things like missing parenthesis, or unknown functions will be reported, and if configured even an exception can be
thrown. However, the default just returns an Optional-like object containing the parsed equation, or an error for
debugging.

```java
public static void main(String[] args) {

    String input = "(7+3*2+12";

    EquationOptional equation = Equation.parse(input);

    System.out.println(equation.isPresent()); //False
    System.out.println(equation.hasError()); //True
    System.out.println(equation.getErrorMessage()); //Parenthesis mismatch, ')' missing!
}
```

## Performance

This framework is designed in a way that it only needs to parse an equation once, and can evaluate it with different
variables as many times as needed.

Additionally, neither parsing nor evaluation depends on any internal states making it safe for multithreaded
applications.

Here is an overview of a very basic performance test. Please note that evaluation times are dependent on many factors
and therefore will likely differ for you. However, it should give a brief overview of what you can expect.

```
Evauated expression: (7+3)*(6-3)+216/3^3+[x]
Tested with: AMD Ryzen 7 3700X 8-Core Processor

Parsing (Single Treaded):
1 million passes: 1648 ms
10 million passes: 13844 ms
100 million passes: 139002 ms

Parsing (Multithreaded via IntStream.range().parallel()):
1 million passes: 386 ms
10 million passes: 1797 ms
100 million passes: 17799 ms

Evaluating (Single Threaded):
1 million passes: 403 ms
10 million passes: 2246 ms
100 million passes: 22357 ms

Evaluating (Multithreaded via IntStream.range().parallel()):
1 million passes: 146 ms
10 million passes: 841 ms
100 million passes: 7573 ms
```

The code for these tests you can find in the [demos](src/demo/java/io/github/lordtylus/jep).

## How does it work?

To get the evaluation result 3 tasks need to be performed.

- Tokenizing the input string.
- Building a tree by parsing the tokens.
- Solving the tree bottom up to calculate the result.

### Tokenizing

Tokenizing reads the input string exactly once from left to right and splits it on each operator and parenthesis.

An input string like `2*sqrt(2+[x])^2+5` would therefore be separated in tokens as follows:

`2, *, sqrt(, 2, +, [x], ), ^, 2, +, 5`

The tokenizer already keeps track of opening and closing parenthesis, so that the later parsing step runs a bit quicker.
Additionally, everything between brackets is ignored, making it safe to use operators, whitespaces or
parenthesis between them.

Notice that the sqrt function is still attached to the opening parenthesis. This is not a mistake. A parenthesis token
already has the function stored in a separate field, to help the parser in a later step.

### Parsing

Once the string is tokenized a recursive algorithm is used to parse the token list. For that, each parser is called one
by
one to see if it can work with the current list.

The parenthesis parser checks if the first and last element of the token list are a pair of parentheses. If so it parses
the function and passes everything between to the next parsers.

The operation parser checks what the lowest ranking operator (which is not between parenthesis) is and splits the list
there.

In the above example the list is first separated into this:

`2, *, sqrt(, 2, +, [x], ), ^, 3` + `5`

Each sublist is then parsed individually.

- `2` * `sqrt(, 2, +, [x], ), ^, 3`
- `sqrt(, 2, +, [x], )` ^ `3`
- sqrt(`2, +, [x]`)
- `2` + `[x]`

This will represent a tree structure.

```
    +
   / \
  *   5
 / \ 
2   ^
   / \
sqrt  3
  |
  +
 / \
2   x
```

Finally, if there's neither a parenthesis nor operator left for recursion, the remaining values are converted to numbers
or retrieved from storage in case of variable.

### Solving

With the tree in place, all that is left to do is to solve starting with the lowest branch.
So in the above case `2+x` is evaluated first. The square root of the result is taken, and then raised to the 3rd power.
And so on and so forth.

Since the first two steps only need to be performed once, the third step can be repeated with different variable storage
values as many times as needed.

## Vision for future

It would be very nice, if this framework could also solve for variables in the future.

Something like 2*x=4 and the framework is able to solve for x on its own. Alternatively, if using two variables like
2/x+y where y=2+x it can then solve for x. However, I have not a single clue right now how to accomplish that, so it
will be something for future me to find out. 