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

import eu.verdelhan.ta4j.trading.rules.InPipeRule;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.simple.FixedDecimalIndicator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class InPipeRuleTest {

    private Indicator<Decimal> indicator;
    private InPipeRule rule;
    
    @Before
    public void setUp() {
        indicator = new FixedDecimalIndicator(
                Decimal.valueOf(50), 
                Decimal.valueOf(70),
                Decimal.valueOf(80), 
                Decimal.valueOf(90), 
                Decimal.valueOf(99), 
                Decimal.valueOf(60), 
                Decimal.valueOf(30), 
                Decimal.valueOf(20), 
                Decimal.valueOf(10), 
                Decimal.valueOf(0));
        rule = new InPipeRule(indicator, Decimal.valueOf(80), Decimal.valueOf(20));
    }
    
    @Test
    public void isSatisfied() {
        assertTrue(rule.isSatisfied(0));
        assertTrue(rule.isSatisfied(1));
        assertTrue(rule.isSatisfied(2));
        assertFalse(rule.isSatisfied(3));
        assertFalse(rule.isSatisfied(4));
        assertTrue(rule.isSatisfied(5));
        assertTrue(rule.isSatisfied(6));
        assertTrue(rule.isSatisfied(7));
        assertFalse(rule.isSatisfied(8));
        assertFalse(rule.isSatisfied(9));
    }
}
        