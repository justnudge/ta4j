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
package eu.verdelhan.ta4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

import eu.verdelhan.ta4j.mocks.MockTick;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import eu.verdelhan.ta4j.trading.rules.FixedRule;

public class TimeSeriesTest {

    private TimeSeries defaultSeries;

    private TimeSeries subSeries;

    private TimeSeries emptySeries;

    private Strategy strategy;

    private List<Tick> ticks;

    private String defaultName;

    private DateTime date;

    @Before
    public void setUp() {
        date = new DateTime(0);

        ticks = new LinkedList<Tick>();
        ticks.add(new MockTick(date.withDate(2014, 6, 13), 1d));
        ticks.add(new MockTick(date.withDate(2014, 6, 14), 2d));
        ticks.add(new MockTick(date.withDate(2014, 6, 15), 3d));
        ticks.add(new MockTick(date.withDate(2014, 6, 20), 4d));
        ticks.add(new MockTick(date.withDate(2014, 6, 25), 5d));
        ticks.add(new MockTick(date.withDate(2014, 6, 30), 6d));

        defaultName = "Series Name";

        defaultSeries = new TimeSeries(defaultName, ticks);
        subSeries = defaultSeries.subseries(2, 4);
        emptySeries = new TimeSeries();

        strategy = new Strategy(new FixedRule(0, 2, 3, 6), new FixedRule(1, 4, 7, 8));
        strategy.setUnstablePeriod(2); // Strategy would need a real test class
    }

    @Test
    public void getEndGetBeginGetTickCount() {
        // Original series
        assertEquals(0, defaultSeries.getBegin());
        assertEquals(ticks.size() - 1, defaultSeries.getEnd());
        assertEquals(ticks.size(), defaultSeries.getTickCount());
        // Sub-series
        assertEquals(2, subSeries.getBegin());
        assertEquals(4, subSeries.getEnd());
        assertEquals(3, subSeries.getTickCount());
        // Empty series
        assertEquals(-1, emptySeries.getBegin());
        assertEquals(-1, emptySeries.getEnd());
        assertEquals(0, emptySeries.getTickCount());
    }

    @Test
    public void getSeriesPeriodDescription() {
        // Original series
        assertTrue(defaultSeries.getSeriesPeriodDescription().endsWith(ticks.get(defaultSeries.getEnd()).getEndTime().toString("hh:mm dd/MM/yyyy")));
        assertTrue(defaultSeries.getSeriesPeriodDescription().startsWith(ticks.get(defaultSeries.getBegin()).getEndTime().toString("hh:mm dd/MM/yyyy")));
        // Sub-series
        assertTrue(subSeries.getSeriesPeriodDescription().endsWith(ticks.get(subSeries.getEnd()).getEndTime().toString("hh:mm dd/MM/yyyy")));
        assertTrue(subSeries.getSeriesPeriodDescription().startsWith(ticks.get(subSeries.getBegin()).getEndTime().toString("hh:mm dd/MM/yyyy")));
        // Empty series
        assertEquals("", emptySeries.getSeriesPeriodDescription());
    }

    @Test
    public void getName() {
        assertEquals(defaultName, defaultSeries.getName());
        assertEquals(defaultName, subSeries.getName());
    }

    @Test
    public void getTickWithRemovedIndexOnMovingSeriesShouldReturnFirstRemainingTick() {
        Tick tick = defaultSeries.getTick(4);
        defaultSeries.setMaximumTickCount(2);
        
        assertSame(tick, defaultSeries.getTick(0));
        assertSame(tick, defaultSeries.getTick(1));
        assertSame(tick, defaultSeries.getTick(2));
        assertSame(tick, defaultSeries.getTick(3));
        assertSame(tick, defaultSeries.getTick(4));
        assertNotSame(tick, defaultSeries.getTick(5));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getTickOnMovingAndEmptySeriesShouldThrowException() {
        defaultSeries.setMaximumTickCount(2);
        ticks.clear(); // Should not be used like this
        defaultSeries.getTick(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getTickWithNegativeIndexShouldThrowException() {
        defaultSeries.getTick(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getTickWithIndexGreaterThanTickCountShouldThrowException() {
        defaultSeries.getTick(10);
    }

    @Test
    public void getTickOnMovingSeries() {
        Tick tick = defaultSeries.getTick(4);
        defaultSeries.setMaximumTickCount(2);
        assertEquals(tick, defaultSeries.getTick(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeMaximumTickCountShouldThrowException() {
        defaultSeries.setMaximumTickCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullTickShouldThrowException() {
        defaultSeries.addTick(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTickWithEndTimePriorToSeriesEndTimeShouldThrowException() {
        defaultSeries.addTick(new MockTick(date.withDate(2000, 1, 1), 99d));
    }
    
    @Test
    public void addTick() {
        defaultSeries = new TimeSeries();
        Tick firstTick = new MockTick(date.withDate(2014, 6, 13), 1d);
        Tick secondTick = new MockTick(date.withDate(2014, 6, 14), 2d);

        assertEquals(0, defaultSeries.getTickCount());
        assertEquals(-1, defaultSeries.getBegin());
        assertEquals(-1, defaultSeries.getEnd());

        defaultSeries.addTick(firstTick);
        assertEquals(1, defaultSeries.getTickCount());
        assertEquals(0, defaultSeries.getBegin());
        assertEquals(0, defaultSeries.getEnd());

        defaultSeries.addTick(secondTick);
        assertEquals(2, defaultSeries.getTickCount());
        assertEquals(0, defaultSeries.getBegin());
        assertEquals(1, defaultSeries.getEnd());
    }

    @Test
    public void subseriesWithIndexes() {
        TimeSeries subSeries2 = defaultSeries.subseries(2, 5);
        assertEquals(defaultSeries.getName(), subSeries2.getName());
        assertEquals(2, subSeries2.getBegin());
        assertNotEquals(defaultSeries.getBegin(), subSeries2.getBegin());
        assertEquals(5, subSeries2.getEnd());
        assertEquals(defaultSeries.getEnd(), subSeries2.getEnd());
        assertEquals(4, subSeries2.getTickCount());
    }

    @Test(expected = IllegalStateException.class)
    public void subseriesOnSeriesWithMaximumTickCountShouldThrowException() {
        defaultSeries.setMaximumTickCount(3);
        defaultSeries.subseries(0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void subseriesWithInvalidIndexesShouldThrowException() {
        defaultSeries.subseries(4, 2);
    }

    @Test
    public void subseriesWithDuration() {
        TimeSeries subSeries2 = defaultSeries.subseries(1, Period.weeks(2));
        assertEquals(defaultSeries.getName(), subSeries2.getName());
        assertEquals(1, subSeries2.getBegin());
        assertNotEquals(defaultSeries.getBegin(), subSeries2.getBegin());
        assertEquals(4, subSeries2.getEnd());
        assertNotEquals(defaultSeries.getEnd(), subSeries2.getEnd());
        assertEquals(4, subSeries2.getTickCount());
    }
}
