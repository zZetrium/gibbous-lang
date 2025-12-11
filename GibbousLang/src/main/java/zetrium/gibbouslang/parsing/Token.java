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

import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author Ryzen
 */
public record Token(TokenType type, String value, int startLine, int endLine, int startIndex, int endIndex) {

    public Token(TokenType type, String value, int startLine, int endLine, int startIndex, int endIndex) {
        this.type = type;
        this.value = value;
        this.startLine = startLine;
        this.endLine = endLine;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public Token(TokenType type, String value, int startLine, int startIndex) {
        this(type, value, startLine, (int) value.chars().filter(c -> c == '\n').count(), startIndex, value.length() - value.lastIndexOf('\n') - 1);
    }
    
    public Token(TokenType type, String value,Position pos) {
        this(type,value,pos.getLine(),pos.getColumn());
    }

}
