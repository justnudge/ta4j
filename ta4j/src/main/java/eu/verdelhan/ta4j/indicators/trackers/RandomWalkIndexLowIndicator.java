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
package eu.verdelhan.ta4j.indicators.trackers;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.CachedIndicator;
import eu.verdelhan.ta4j.indicators.helpers.AverageTrueRangeIndicator;
import eu.verdelhan.ta4j.indicators.simple.MaxPriceIndicator;
import eu.verdelhan.ta4j.indicators.simple.MinPriceIndicator;

/**
 * The Class RandomWalkIndexLowIndicator.
 */
public class RandomWalkIndexLowIndicator extends CachedIndicator<Decimal>{

/**
     * 
     */
    private static final long serialVersionUID = 7405354000554357606L;

private final MaxPriceIndicator maxPrice;
    
    private final MinPriceIndicator minPrice;
    
    private final AverageTrueRangeIndicator averageTrueRange;
    
    private final Decimal sqrtTimeFrame;
    
    private final int timeFrame;
    
    /**
     * Constructor.
     *
     * @param series the series
     * @param timeFrame the time frame
     */
    public RandomWalkIndexLowIndicator(TimeSeries series, int timeFrame) {
        super(series);
        this.timeFrame = timeFrame;
        maxPrice = new MaxPriceIndicator(series);
        minPrice = new MinPriceIndicator(series);
        averageTrueRange = new AverageTrueRangeIndicator(series, timeFrame);
        sqrtTimeFrame = Decimal.valueOf(timeFrame).sqrt();
    }

    @Override
    protected Decimal calculate(int index) {
        return maxPrice.getValue(Math.max(0, index - timeFrame)).minus(minPrice.getValue(index))
                .dividedBy(averageTrueRange.getValue(index).multipliedBy(sqrtTimeFrame));
    }
}
