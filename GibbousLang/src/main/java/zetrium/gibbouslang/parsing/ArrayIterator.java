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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Ryzen
 */
class ArrayIterator<T> {

    private final T[] target;
    private int index = -1;
    private List<Consumer<T>> callbacks = new ArrayList();

    public ArrayIterator(T[] target) {
        this.target = target;
    }
    
    public ArrayIterator(T[] target,Consumer<T>... callbacks) {
        this.target = target;
        this.callbacks.addAll(List.of(callbacks));
    }
    
    private boolean isOutOfBound(int i) {
        return i < 0 || i >= target.length;
    }
    
    public T pop() {
        index++;
        if (isOutOfBound(index)) {
            return null;
        }
        for (var callback:callbacks) {
            callback.accept(target[index]);
        }
        return target[index];
    }
    
    public T current() {
        if (isOutOfBound(index)) {
            return null;
        }
        return target[index];
    }
    
    public T peek() {
        if (isOutOfBound(index+1)) {
            return null;
        }
        return target[index+1];
    }
    
 
    public T peek(int offset) {
        if (isOutOfBound(index+offset)) {
            return null;
        }
        return target[index+offset];
    }
    public T peekNext() {
        if (isOutOfBound(index+2)) {
            return null;
        }
        return target[index+2];
    }
    
    public boolean hasNext() {
        return !isOutOfBound(index+1);
    }

    public int getIndex() {
        return index;
    }

    public boolean addCallback(Consumer<T> e) {
        return callbacks.add(e);
    }

    public boolean removeCallback(Object o) {
        return callbacks.remove(o);
    }
    
    
    
}
