package com.blind.wakemeup;

import android.graphics.Color;

import com.blind.wakemeup.weather.accu.model.hourlyforecast.AccuWeatherHourlyForecast;
import com.blind.wakemeup.weather.accu.model.hourlyforecast.HourlyForecast;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;

import java.util.Comparator;
import java.util.List;

public class ProbabilityChartBuilder extends ConnectionLineBuilder<Integer> {

    private final IValueFormatter formatter;

    public ProbabilityChartBuilder(final AccuWeatherHourlyForecast forecasts, DataProvider<Integer> dataProvider, float shift) {
        super(forecasts, dataProvider, shift);
        this.formatter = buildValueFormatter(forecasts.forecast.stream()
                                                     .mapToInt(value -> dataProvider.getData(value))
                                                     .toArray());
    }

    private IValueFormatter buildValueFormatter(int[] values) {
        return (value, entry, dataSetIndex, viewPortHandlernew) -> {
            int index = (int) entry.getX();
            final int cValue = values[index];
            final Integer pValue = index > 0
                                   ? values[index - 1]
                                   : null;

            final String ret = (pValue == null || cValue != pValue)
                               ? cValue + "%"
                               : "";

            return ret;

        };
    }

    protected LineDataSet buildLineDataSet(List<Entry> entries) {

        final LineDataSet dataSet = new LineDataSet(entries,
                                                    "");
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setValueFormatter(formatter);
        dataSet.setLineWidth(0f);
        dataSet.setColor(Color.argb(0,
                                    0,
                                    0,
                                    0));

        return dataSet;
    }

    protected  Entry buildDataEntry(float x, float y, final HourlyForecast forecast) {
        return new Entry(x,
                  y);
    }

    @Override
    protected Comparator<Integer> getComparator() {
        return Comparator.naturalOrder();
    }

}