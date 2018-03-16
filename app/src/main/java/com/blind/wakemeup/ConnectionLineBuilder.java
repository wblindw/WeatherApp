package com.blind.wakemeup;

import android.graphics.Color;

import com.blind.wakemeup.weather.accu.model.hourlyforecast.AccuWeatherHourlyForecast;
import com.blind.wakemeup.weather.accu.model.hourlyforecast.HourlyForecast;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class ConnectionLineBuilder<E> {

    private static final float SPACE_FROM_VALUE = 0.25f;

    protected DataProvider<E>           dataProvider;
    private   AccuWeatherHourlyForecast forecasts;
    private   float                     shift;

    public ConnectionLineBuilder(final AccuWeatherHourlyForecast forecasts,
                                 DataProvider<E> dataProvider,
                                 final float shift) {
        this.forecasts = forecasts;
        this.shift = shift;
        this.dataProvider = dataProvider;
    }

    public List<LineDataSet> buildData() {

        final List<LineDataSet> ret = new ArrayList<>();

        final List<Entry> values = new ArrayList<>();

        E previousData = null;
        List<Entry> connectorEntries = null;
        for(int i = 0; i < forecasts.forecast.size(); i++) {

            final HourlyForecast forecast = forecasts.forecast.get(i);

            final E data = dataProvider.getData(forecast);

            if(previousData == null || getComparator().compare(data,
                                                               previousData) != 0) {
                previousData = data;
                values.add(buildDataEntry(i,
                                          shift,
                                          forecast));

                // ---

                if(connectorEntries != null) {
                    connectorEntries.add(new Entry(i - SPACE_FROM_VALUE,
                                                   shift));
                    ret.add(buildFakeLineDataSet(connectorEntries));
                    connectorEntries = null;
                }

            } else {

                boolean isLastEntry = i == forecasts.forecast.size() - 1;

                if(connectorEntries == null) {
                    connectorEntries = new ArrayList<>();

                    float x = i - 1 + (isLastEntry
                             ? 0
                             : SPACE_FROM_VALUE);
                    final Entry entry = new Entry(x,
                                                  shift);
                    connectorEntries.add(entry);
                } else if (isLastEntry) {
                    final Entry entry = new Entry(i,
                                                  shift);
                    connectorEntries.add(entry);
                    ret.add(buildFakeLineDataSet(connectorEntries));
                }


            }

        }

        ret.add(buildLineDataSet(values));

        return ret;
    }

    protected LineDataSet buildFakeLineDataSet(List<Entry> entries) {
        final LineDataSet dataSet = new LineDataSet(entries,
                                                    "");
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setLineWidth(0.25f);
        dataSet.setDrawValues(false);

        dataSet.setColor(Color.BLACK);

        return dataSet;
    }

    protected abstract Comparator<E> getComparator();

    protected abstract Entry buildDataEntry(float x, float y, final HourlyForecast forecast);

    protected abstract LineDataSet buildLineDataSet(List<Entry> entries);

    @FunctionalInterface
    public interface DataProvider<E> {
        E getData(HourlyForecast forecast);
    }
}
