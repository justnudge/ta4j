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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eu.verdelhan.ta4j.Rule;
import eu.verdelhan.ta4j.TimeSeries;

public class AndRuleTest {

    private Rule satisfiedRule;
    private Rule unsatisfiedRule;
    
    @Before
    public void setUp() {
        satisfiedRule = new BooleanRule(true);
        unsatisfiedRule = new BooleanRule(false);
    }
    
    @Test
    public void isSatisfied() {
        TimeSeries series = new TimeSeries();
        assertFalse(satisfiedRule.and(BooleanRule.FALSE).isSatisfied(series, 0));
        assertFalse(BooleanRule.FALSE.and(satisfiedRule).isSatisfied(series, 0));
        assertFalse(unsatisfiedRule.and(BooleanRule.FALSE).isSatisfied(series, 0));
        assertFalse(BooleanRule.FALSE.and(unsatisfiedRule).isSatisfied(series, 0));
        
        assertTrue(satisfiedRule.and(BooleanRule.TRUE).isSatisfied(series, 10));
        assertTrue(BooleanRule.TRUE.and(satisfiedRule).isSatisfied(series, 10));
        assertFalse(unsatisfiedRule.and(BooleanRule.TRUE).isSatisfied(series, 10));
        assertFalse(BooleanRule.TRUE.and(unsatisfiedRule).isSatisfied(series, 10));
    }
}
        
