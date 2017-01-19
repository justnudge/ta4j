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
package eu.verdelhan.ta4j.trading.rules;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.TradingRecord;

/**
 * A simple boolean rule.
 * <p>
 * Satisfied when it has been initialized with true.
 */
public class BooleanRule extends AbstractRule {

    /** An always-true rule */
    public static final BooleanRule TRUE = new BooleanRule(true);
    
    /** An always-false rule */
    public static final BooleanRule FALSE = new BooleanRule(false);
    
    private boolean satisfied;

    /**
     * Constructor.
     * @param satisfied true for the rule to be always satisfied, false to be never satisfied
     */
    public BooleanRule(boolean satisfied) {
        this.satisfied = satisfied;
    }

    @Override
    public boolean isSatisfied(TimeSeries timeSeries, int index, TradingRecord tradingRecord) {
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
