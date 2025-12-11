/*
    Copyright (c) 2025 Tomáš Zídek

    Permission is hereby granted, free of charge, to any person
    obtaining a copy of this software and associated documentation
    files (the "Software"), to deal in the Software without
    restriction, including without limitation the rights to use,
    copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the
    Software is furnished to do so, subject to the following
    conditions:

    The above copyright notice and this permission notice shall be
    included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
    OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
    NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
    HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
    WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
    OTHER DEALINGS IN THE SOFTWARE.*/
package zetrium.gibbouslang.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 *
 * @author Ryzen
 */
public class DefaultLexer implements Lexer {

    @Override
    public Token[] lex(String source) throws LexingException {
        List<Token> tokens = new ArrayList();
        //final IntPointer curLine = new IntPointer(0);
        //final IntPointer curChar = new IntPointer(-1);
        Position pos = new Position(0, -1);

        ArrayIterator<Character> iter = new ArrayIterator<>(BoxedArrayConverter.toBoxed(source.toCharArray()));
        iter.addCallback(c -> {
            pos.incrementColumn();
        });
        iter.addCallback(c -> {
            if (Objects.equals(c, Character.valueOf('\n'))) {
                pos.incrementLine();
                pos.setColumn(0);
            }
        });
        while (iter.hasNext()) {
            var startPos = pos.clone();
            char cur = iter.pop();
            switch (cur) {
                case '*' -> {
                    tokens.add(new Token(TokenType.STAR, "*", startPos));
                }
                case '/' -> {
                    tokens.add(new Token(TokenType.SLASH, "/", startPos));
                }
                case '+' -> {
                    tokens.add(new Token(TokenType.PLUS, "+", startPos));
                }
                case '-' -> {
                    tokens.add(new Token(TokenType.MINUS, "-", startPos));
                }
                case '=' -> {
                    if (iter.hasNext() && iter.peek() == '=') {
                        iter.pop();
                        tokens.add(new Token(TokenType.DOUBLE_EQUALS, "==", startPos));
                    } else {
                        tokens.add(new Token(TokenType.EQUALS, "=", startPos));
                    }
                }

                case '"' -> {
                    StringBuilder strVal = new StringBuilder();
                    while (iter.hasNext() && iter.peek() != '"') {
                        strVal.append(iter.pop());
                    }
                    if (!iter.hasNext()) {
                        throw new LexingException("End of file reached while lexing string starting at " + startPos);
                    }
                    iter.pop();
                    tokens.add(new Token(TokenType.STRING, strVal.toString(), startPos));
                }

                default -> {
                    if (isLetter(cur)) {
                        StringBuilder identifVal = new StringBuilder();
                        identifVal.append(cur);
                        while (iter.hasNext() && isLetter(iter.peek())) {
                            identifVal.append(iter.pop());
                        }
                        tokens.add(new Token(switch (identifVal.toString()) {
                            case "let" ->
                                TokenType.KW_LET;
                            case "const" ->
                                TokenType.KW_CONST;
                            case "var" ->
                                TokenType.KW_VAR;
                            default ->
                                TokenType.IDENTIFIER;
                        }, identifVal.toString(), startPos));
                        continue;
                    } else if (isNumber(cur)) {
                        StringBuilder numVal = new StringBuilder();
                        numVal.append(cur);
                        while (iter.hasNext() && isNumber(iter.peek())) {
                            numVal.append(iter.pop());
                        }
                        tokens.add(new Token(TokenType.INTEGER, numVal.toString(), startPos));
                        continue;
                    } else if (Character.isWhitespace(cur)) {
                        continue;
                    }
                    throw new LexingException(
                            "Lexing failed at " + startPos);

                }

            }

        }
        return tokens.toArray(Token[]::new);

    }

    public boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    public boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }
}
