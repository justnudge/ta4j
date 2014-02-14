package eu.verdelhan.ta4j.series;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.TimeSeriesSlicer;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

/**
 *
 *
 */
public class PartialMemorizedSlicer implements TimeSeriesSlicer {

	protected TimeSeries series;

	protected Period period;

	protected List<TimeSeries> splittedSeries;

	protected DateTime periodBegin;

	protected int periodsPerSlice;

	private static Logger LOG = Logger.getLogger(PartialMemorizedSlicer.class);

	/**
	 * @param series the time series
	 * @param period
	 * @param periodBegin
	 * @param periodsPerSlice
	 */
	public PartialMemorizedSlicer(TimeSeries series, Period period, DateTime periodBegin, int periodsPerSlice) {
		if (period == null) {
			throw new NullPointerException("Period cannot be null");
		}
		if (periodsPerSlice < 1) {
			throw new IllegalArgumentException("Periods per slice must be greater than 1");
		}
		
		int index = series.getBegin();

		DateTime initialSeriesDate = series.getTick(index).getEndTime();
		if (periodBegin.isBefore(initialSeriesDate) && !periodBegin.equals(initialSeriesDate))
			periodBegin = series.getTick(series.getBegin()).getEndTime();

		Interval interval = new Interval(periodBegin, periodBegin.plus(period));

		while (series.getTick(index).getEndTime().isBefore(interval.getStart()))
			index++;

		this.series = new ConstrainedTimeSeries(series, index, series.getEnd());
		this.period = period;
		this.splittedSeries = new ArrayList<TimeSeries>();
		this.periodBegin = periodBegin;
		this.periodsPerSlice = periodsPerSlice;
		split();
	}

	/**
	 * @param series the time series
	 * @param period
	 * @param periodsPerSlice
	 */
	public PartialMemorizedSlicer(TimeSeries series, Period period, int periodsPerSlice) {
		this(series, period, series.getTick(series.getBegin()).getEndTime(), periodsPerSlice);
	}

	@Override
	public TimeSeriesSlicer applyForSeries(TimeSeries series) {
		return applyForSeries(series, this.periodBegin);
	}

	public TimeSeriesSlicer applyForSeries(TimeSeries series, DateTime periodBegin) {
		return new PartialMemorizedSlicer(series, this.period, periodBegin, this.periodsPerSlice);
	}

	@Override
	public String getName() {
		return getClass().getSimpleName() + " Period: " + periodToString();
	}

	@Override
	public Period getPeriod() {
		return period;
	}

	@Override
	public String getPeriodName() {
		return this.periodBegin.toString("hh:mm dd/MM/yyyy - ")
				+ series.getTick(series.getEnd()).getEndTime().toString("hh:mm dd/MM/yyyy");
	}

	@Override
	public TimeSeries getSeries() {
		return series;
	}

	@Override
	public TimeSeries getSlice(int position) {
		return splittedSeries.get(position);
	}

	@Override
	public DateTime getDateBegin() {
		return periodBegin;
	}

	@Override
	public int getNumberOfSlices() {
		return splittedSeries.size();
	}

	@Override
	public double getAverageTicksPerSlice() {
		double sum = 0;
		for (TimeSeries subSeries : splittedSeries) {
			sum += subSeries.getSize();
		}
		return getNumberOfSlices() > 0 ? sum / getNumberOfSlices() : 0;
	}

	private void split() {
		LOG.debug(String.format("Spliting %s  ", series));

		DateTime begin = periodBegin;
		DateTime end = begin.plus(period);

		Interval interval = new Interval(begin, end);
		int index = series.getBegin();

		List<Integer> begins = new ArrayList<Integer>();
		begins.add(index);
		while (index <= series.getEnd()) {

			if (interval.contains(series.getTick(index).getEndTime())) {
				index++;
			} else if (end.plus(period).isAfter(series.getTick(index).getEndTime())) {
				createSlice(begins.get(Math.max(begins.size() - periodsPerSlice, 0)), index - 1);

				LOG.debug(String.format("Interval %s before  %s ", interval, series.getTick(index).getEndTime()));

				int sliceBeginIndex = index;
				begins.add(sliceBeginIndex);
				begin = end;
				end = begin.plus(period);
				interval = new Interval(begin, end);
				index++;
			} else {
				begin = end;
				end = begin.plus(period);
				interval = new Interval(begin, end);
			}
		}
		createSlice(begins.get(Math.max(begins.size() - periodsPerSlice, 0)), series.getEnd());
	}

	private void createSlice(int begin, int end) {
		LOG.debug(String.format("New series from %d to %d ", begin, end));
		ConstrainedTimeSeries slice = new ConstrainedTimeSeries(series, begin, end);
		splittedSeries.add(slice);
	}

	/**
	 * @return the period as a string
	 */
	private String periodToString() {
		StringBuilder sb = new StringBuilder("");
		if (period.getYears() > 0) {
			sb.append(period.getYears()).append(" year(s) ,");
		}
		if (period.getMonths() > 0) {
			sb.append(period.getMonths()).append(" month(s) ,");
		}
		if (period.getDays() > 0) {
			sb.append(period.getDays()).append(" day(s) ,");
		}
		if (period.getHours() > 0) {
			sb.append(period.getHours()).append(" hour(s) ,");
		}
		if (period.getMinutes() > 0) {
			sb.append(period.getMinutes()).append(" minute(s)");
		}
		return sb.toString();
	}
}