/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j.indicators.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.verdelhan.ta4j.indicators.AbstractIndicator;

/**
 * A fixed indicator.
 * @param <T> the type of returned value (Double, Boolean, etc.)
 */
public abstract class FixedIndicator<T> extends AbstractIndicator<T> {

    /**
     * 
     */
    private static final long serialVersionUID = -1663159386205082641L;
    private final List<T> values = new ArrayList<T>();

    /**
     * Constructor.
     * @param values the values to be returned by this indicator
     */
    public FixedIndicator(T... values) {
        super(null);
        this.values.addAll(Arrays.asList(values));
    }
    
    public void addValue(T value) {
        this.values.add(value);
    }

    @Override
    public T getValue(int index) {
        return values.get(index);
    }
}
