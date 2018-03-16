package com.blind.wakemeup;

import android.graphics.Color;

import com.blind.wakemeup.weather.accu.model.hourlyforecast.AccuWeatherHourlyForecast;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.Comparator;
import java.util.List;

public abstract class ConditionChartBuilder extends ConnectionLineBuilder<Integer> {


    public ConditionChartBuilder(final AccuWeatherHourlyForecast forecasts, DataProvider<Integer> dataProvider, final float shift) {
        super(forecasts, dataProvider, shift);
    }

    @Override
    protected LineDataSet buildLineDataSet(final List<Entry> entries) {
        final LineDataSet dataSet = new LineDataSet(entries,
                                                    "");
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(0f);
        dataSet.setColor(Color.argb(0,
                                    0,
                                    0,
                                    0));
        dataSet.setDrawIcons(true);
        return dataSet;
    }

    @Override
    protected Comparator<Integer> getComparator() {
        return Comparator.naturalOrder();
    }

}