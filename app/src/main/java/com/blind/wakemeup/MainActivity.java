package com.blind.wakemeup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blind.wakemeup.utils.ContextLoader;
import com.blind.wakemeup.utils.UnitTempEnum;
import com.blind.wakemeup.utils.Units;
import com.blind.wakemeup.utils.ViewUtils;
import com.blind.wakemeup.weather.CitySwitchGestureDetector;
import com.blind.wakemeup.weather.accu.model.AccuWeather;
import com.blind.wakemeup.weather.accu.model.forecast.AccuWeatherDailyForecast;
import com.blind.wakemeup.weather.accu.model.forecast.DailyForecast;
import com.blind.wakemeup.weather.accu.model.hourlyforecast.AccuWeatherHourlyForecast;
import com.blind.wakemeup.weather.accu.model.hourlyforecast.HourlyForecast;
import com.blind.wakemeup.weather.accu.service.AccuWeatherService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private static final boolean MOK_API = false;

    private static final TimeUnit WEATHER_UPDATE_FREQUENCY_UNIT = TimeUnit.HOURS;
    private static final int      WEATHER_UPDATE_FREQUENCY      = 12;
    private static final String   DEGREE_CHAR                   = "°";
    private static final String   TAG_DAY_FORECAST_PREFIX       = "day_forecast_";
    private static final String   TAG_CITY_PREFIX               = "city_";
    private static String CHAR_SEPARATOR;

    static {
        final NumberFormat nf = NumberFormat.getInstance();
        CHAR_SEPARATOR = (nf instanceof DecimalFormat)
                         ? ((DecimalFormat) nf).getDecimalFormatSymbols()
                                 .getDecimalSeparator() + ""
                         : ".";
        if(".".equals(CHAR_SEPARATOR)) {
            CHAR_SEPARATOR = "\\" + CHAR_SEPARATOR;
        }
    }

    private final ScheduledExecutorService weatherScheduler = Executors.newScheduledThreadPool(2);
    private final Handler                  handler          = new Handler();
    private GestureDetector citySwitchGestureDetector;
    private int             PRIMARY_COLOR;
    private int             SECONDARY_COLOR;
    private Properties      weatherIcoProperties;
    private Properties      weatherUnitsProperties;
    private Map<Integer, AccuWeather> citiesWeather          = new LinkedHashMap<>();
    private UnitTempEnum              currentTempUnitDisplay = UnitTempEnum.CELCIUS;

    /**
     * Get all the views which matches the given Tag recursively
     *
     * @param root parent view. for e.g. Layouts
     * @param tag  tag to look for
     * @return List of views
     */
    public static List<View> findViewsWithTagRecursively(ViewGroup root, Object tag) {
        List<View> allViews = new ArrayList<View>();

        final int childCount = root.getChildCount();
        for(int i = 0; i < childCount; i++) {
            final View childView = root.getChildAt(i);

            if(childView instanceof ViewGroup) {
                allViews.addAll(findViewsWithTagRecursively((ViewGroup) childView,
                                                            tag));
            } else {
                final Object tagView = childView.getTag();
                if(tagView != null && tagView.equals(tag)) {
                    allViews.add(childView);
                }
            }
        }

        return allViews;
    }

    private static String getDayForecastTag(int idx) {
        return TAG_DAY_FORECAST_PREFIX + idx;
    }

    private static String getCityViewTag(int idx) {
        return TAG_CITY_PREFIX + idx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initCityViewSwitcher();

        PRIMARY_COLOR = ContextCompat.getColor(getApplicationContext(),
                                               R.color.colorFontPrimary);
        SECONDARY_COLOR = ContextCompat.getColor(getApplicationContext(),
                                                 R.color.colorFontSecondary);

        weatherIcoProperties = ContextLoader.getProperties(getApplicationContext(),
                                                           R.raw.weather_codes_ico);
        weatherUnitsProperties = ContextLoader.getProperties(getApplicationContext(),
                                                             R.raw.units);

        initCityWeathers();

        /**
         // Change temperature unit on tap
         findViewById(R.id.cTempsValueLayout).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
        currentTempUnitDisplay = UnitTempEnum.next(currentTempUnitDisplay);
        updateView(citiesWeather.get(currentCityId));
        }
        });
         */

        // Display current values only on daily forecast on tap

        AccuWeatherService.loadKeys(getApplicationContext(),
                                    R.raw.keys);

        startUpdateService();

    }

    private void initCityWeathers() {

        final Properties citiesProperties = ContextLoader.getProperties(getApplicationContext(),
                                                                        R.raw.cities);

        citiesProperties.keySet()
                .stream()
                .forEach(id -> {
                    final AccuWeather weather = new AccuWeather();
                    weather.id = Integer.valueOf((String) id);
                    weather.name = citiesProperties.getProperty((String) id);

                    citiesWeather.put(weather.id,
                                      weather);

                    addCityView(weather.id);
                });

    }

    /**
     * Start view updates
     */
    protected void startUpdateService() {

        // Get current and forecast weather for all cities
        weatherScheduler.scheduleAtFixedRate(() -> {

                                                 citiesWeather.values()
                                                         .stream()
                                                         .forEach(weather -> {

                                                             updateWeatherData(weather);

                                                             // Update view in new thread
                                                             final ViewGroup cityWeatherView = findViewById(R.id.citiesLayout).findViewWithTag(getCityViewTag(weather.id));
                                                             handler.post(() -> updateView(cityWeatherView,
                                                                                           weather));
                                                         });

                                             },
                                             0,
                                             WEATHER_UPDATE_FREQUENCY,
                                             WEATHER_UPDATE_FREQUENCY_UNIT);

        // Get and update current date and time
        weatherScheduler.scheduleWithFixedDelay(() -> handler.post(() -> { /* updateCt(); */ }),
                                                0,
                                                1,
                                                TimeUnit.SECONDS);
    }

    private void updateWeatherData(final AccuWeather weather) {
        if(MOK_API) {
            weather.current = AccuWeatherService.getCurrentSample(weather.id,
                                                                  getApplicationContext());
            weather.forecast = AccuWeatherService.getDailyForecastSample(weather.id,
                                                                         getApplicationContext());
            weather.hourly = AccuWeatherService.getHourlyForecastSample(weather.id,
                                                                        getApplicationContext());
        } else {
            weather.current = AccuWeatherService.getCurrent(weather.id);
            weather.forecast = AccuWeatherService.getDailyForecast(weather.id);
            weather.hourly = AccuWeatherService.getHourlyForecast(weather.id);
        }
    }

    /**
     * Update the view with weather data.
     *
     * @param weather full weather data. Do nothing if <code>null</code>.
     */
    protected void updateView(final ViewGroup root, final AccuWeather weather) {

        Log.d(LOG_TAG,
              "Update weather [city:" + weather.id + "][root:" + root.getTag() + "]");

        if(weather == null || weather.current == null || weather.forecast == null) {
            return;
        }

        final DailyForecast dailyForecast = getToday(weather.forecast);

        updateTempValue(root,
                        weather,
                        dailyForecast);

        ((TextView) root.findViewById(R.id.conditionDesc)).setText(weather.current.weatherText.toUpperCase());

        ((ImageView) root.findViewById(R.id.conditionIco)).setImageResource(getWeatherIconFromIconId(weather.current.weatherIcon,
                                                                                                     ""));

        updateRainValue(root,
                        dailyForecast);
        updateSnowValue(root,
                        dailyForecast);
        updateHourlyForecast(root,
                             weather.hourly);

        final ViewGroup dayForecastLayout = root.findViewById(R.id.days_forecast);
        for(int i = 0; i < weather.forecast.dailyForecasts.size(); i++) {
            updateForecast(dayForecastLayout,
                           weather.forecast.dailyForecasts.get(i),
                           i);
        }

    }

    private void updateTempValue(final ViewGroup root,
                                 final AccuWeather weather,
                                 DailyForecast dailyForecast) {

        final String temp = getPrintableTemp(weather.current.temperature.metric.value,
                                             1);

        final String[] currentTempValue = temp.substring(0,
                                                         temp.length() - 1)
                .split(CHAR_SEPARATOR);

        if(currentTempValue != null && currentTempValue.length >= 2) {
            ((TextView) root.findViewById(R.id.tempValue)).setText(currentTempValue[0]);
            ((TextView) root.findViewById(R.id.tempDecimalValue)).setText("." + currentTempValue[1]);
        }
        ((TextView) root.findViewById(R.id.tempUnit)).setText(currentTempUnitDisplay.toString());
        ((TextView) root.findViewById(R.id.tempMaxValue)).setText(getPrintableTemp(dailyForecast.temperature.maximum.value,
                                                                                   0));
        ((TextView) root.findViewById(R.id.tempMinValue)).setText(getPrintableTemp(dailyForecast.temperature.minimum.value,
                                                                                   0));
        ((TextView) root.findViewById(R.id.tempFeelValue)).setText(getPrintableTemp(weather.current.realFeelTemperature.metric.value,
                                                                                    0));
    }

    /**
     * Update current date and time.
     */
    protected void updateCt() {

        final DateTime now = DateTime.now();
        ((TextView) findViewById(R.id.ctDayValue)).setText(now.toString("EEEE"));
        ((TextView) findViewById(R.id.ctTimeValue)).setText(now.toString("HH:mm"));
        ((TextView) findViewById(R.id.ctTimeSecValue)).setText(now.toString("ss"));
        ((TextView) findViewById(R.id.ctDateValue)).setText(now.toString("d MMM"));

    }

    /**
     * Convert and format temperature depending on decimals to display and current unit.
     *
     * @param tempC   the temperature value.
     * @param decimal number of decimal to display.
     * @return input temperature in the current unit, with the number of decimal from input and the temperature unit.
     */
    private String getPrintableTemp(Double tempC, int decimal) {

        double value = 0;
        if(currentTempUnitDisplay == UnitTempEnum.CELCIUS) {
            value = tempC;
        } else if(currentTempUnitDisplay == UnitTempEnum.FAHRENHEIT) {
            value = Units.tempCtoF(tempC);
        }
        return getPrintableDouble(value,
                                  decimal) + DEGREE_CHAR;
    }

    private String getPrintableDouble(Double value, int decimal) {
        String ret;
        if(decimal == 0) {
            ret = Math.round(value) + "";
        } else {
            ret = ((float) (Math.round(value * Math.pow(10,
                                                        decimal)) / (Math.pow(10,
                                                                              decimal)))) + "";
        }
        return ret;
    }

    private int getWeatherIconFromIconId(Integer weatherIcon, String prefix) {
        String weatherRes = weatherIcoProperties.getProperty(String.valueOf(weatherIcon));
        return ContextLoader.getResource(getApplicationContext(),
                                         "drawable",
                                         prefix + weatherRes);
    }

    /**
     * Get the current weather from all weather data.
     *
     * @param forecast the overall weather data.
     * @return the current weather, <code>null</code> if no current weather data.
     */
    private DailyForecast getToday(AccuWeatherDailyForecast forecast) {

        return forecast.dailyForecasts.size() > 1
               ? forecast.dailyForecasts.get(0)
               : null;

        /*
        DateTime now = DateTime.now();
        for(DailyForecast dForecast : forecast.dailyForecasts) {
            if(DateTime.parse(dForecast.date).getDayOfMonth() == now.getDayOfMonth()) {
                return dForecast;
            }
        }
        return null;
        */
    }

    private void initCityViewSwitcher() {
        citySwitchGestureDetector = new GestureDetector(this,
                                                        new CitySwitchGestureDetector(findViewById(com.blind.wakemeup.R.id.citiesLayout)));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        citySwitchGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * Build and add component to select a city.
     *
     * @param id a reference given to the city selection component.
     */
    private void addCityView(final int id) {

        final ViewGroup root = findViewById(R.id.citiesLayout);

        final ViewGroup cityView = (ViewGroup) LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.weather,
                         null);
        cityView.setTag(getCityViewTag(id));
        root.addView(cityView);

        initTempChart(cityView);
        initValueChart(cityView);

        cityView.findViewById(R.id.conditionLayout)
                .setOnClickListener(view -> {
                    final android.widget.ViewFlipper viewFlipper = cityView.findViewById(com.blind.wakemeup.R.id.currentWeatherViewFlipper);
                    viewFlipper.showNext();
                });
    }

    /**
     * Update current weather rain value.
     *
     * @param dailyForecast the current weather data.
     */
    private void updateRainValue(final View root, final DailyForecast dailyForecast) {

        int prob = dailyForecast.day.rainProbability;
        TextView textView = root.findViewById(R.id.rainValue);
        textView.setText(String.valueOf(prob));
        textView.setTextColor(prob > 0.f
                              ? PRIMARY_COLOR
                              : SECONDARY_COLOR);

        ((ImageView) root.findViewById(R.id.rainValueImage)).setImageResource(prob > 0.f
                                                                              ? R.drawable.rain_value
                                                                              : R.drawable.rain_value);

    }

    /**
     * Update current weather snow value.
     *
     * @param dailyForecast the current weather data.
     */
    private void updateSnowValue(final View root, final DailyForecast dailyForecast) {

        int prob = dailyForecast.day.snowProbability;
        TextView textView = root.findViewById(R.id.snowValue);
        textView.setText(String.valueOf(prob));
        textView.setTextColor(prob > 0.f
                              ? PRIMARY_COLOR
                              : SECONDARY_COLOR);

        ((ImageView) root.findViewById(R.id.snowValueImage)).setImageResource(prob > 0.f
                                                                              ? R.drawable.snow_value
                                                                              : R.drawable.snow_value);

    }

    /**
     * Build and add a view to display a weather forecast.
     *
     * @param idx
     */
    public ViewGroup addForecast(final ViewGroup root, int idx) {

        Log.d(LOG_TAG,
              "Add forecast [root:" + root.getId() + "][idx:" + idx + "]");

        final ViewGroup dayView = (ViewGroup) LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.day_forecast,
                         null);

        dayView.setTag(getDayForecastTag(idx));

        root.addView(dayView);

        return dayView;

    }

    /**
     * Update all data of a weather forecast day
     *
     * @param forecast the weather forecast data. Do nothing if <code>null</code>.
     * @param idx      the index of the weather forecast to update. If the view for the index does not exist, it will be created and added.
     */
    private void updateForecast(final ViewGroup root, DailyForecast forecast, int idx) {

        if(forecast == null) {
            return;
        }

        ViewGroup dayForecastLayout = root.findViewWithTag(getDayForecastTag(idx));
        if(dayForecastLayout == null) {
            dayForecastLayout = addForecast(root,
                                            idx);
        }

        Log.d(LOG_TAG,
              "Update forecast [forecast_view:" + dayForecastLayout.getTag() + "][idx:" + idx + "]");

        ((TextView) dayForecastLayout.findViewById(R.id.dayForecastDate)).setText(new DateTime(forecast.date).toString("E"));

        ((ImageView) dayForecastLayout.findViewById(R.id.dayCondition)).setImageResource(getWeatherIconFromIconId(forecast.day.icon,
                                                                                                                  "small_"));
        ((TextView) dayForecastLayout.findViewById(R.id.dayForecastTemp)).setText(getPrintableTemp(forecast.temperature.maximum.value,
                                                                                                   0));
        ((TextView) dayForecastLayout.findViewById(R.id.dayForecastRain)).setText(String.valueOf(forecast.day.rainProbability) + "%");
        ((TextView) dayForecastLayout.findViewById(R.id.dayForecastSnow)).setText(String.valueOf(forecast.day.snowProbability) + "%");

    }

    private void initTempChart(ViewGroup root) {

        final LineChart chart = (LineChart) ViewUtils.findViewByIdRecursively(root,
                                                                              R.id.tempChart)
                .get();
        chart.setAutoScaleMinMaxEnabled(false);

        final XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularityEnabled(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(PRIMARY_COLOR);
        xAxis.setTypeface(Typeface.create(xAxis.getTypeface(),
                                          Typeface.BOLD));

        //xAxis.setTextSize(20);

        chart.getAxisLeft()
                .setEnabled(false);
        chart.getAxisRight()
                .setEnabled(false);

        chart.setTouchEnabled(false);

        chart.setDescription(null);
        chart.setNoDataText("...");

        chart.getLegend()
                .setEnabled(false);

    }

    private void initValueChart(ViewGroup root) {

        final LineChart chart = (LineChart) ViewUtils.findViewByIdRecursively(root,
                                                                              R.id.valuesChart)
                .get();
        chart.setAutoScaleMinMaxEnabled(false);

        final XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);

        chart.getAxisLeft()
                .setEnabled(false);
        chart.getAxisRight()
                .setEnabled(false);

        chart.setTouchEnabled(false);

        chart.setDescription(null);
        chart.setNoDataText("...");

        chart.getLegend()
                .setEnabled(false);

    }

    private List<LineDataSet> buildTempForecastChart(final AccuWeatherHourlyForecast forecasts) {

        final List<Entry> realValues = new ArrayList<>();
        final List<Entry> feelValues = new ArrayList<>();

        for(int i = 0; i < forecasts.forecast.size(); i++) {
            final HourlyForecast forecast = forecasts.forecast.get(i);

            float temp = forecast.temperature.value.floatValue();
            float rTemp = forecast.realFeelTemperature.value.floatValue();

            realValues.add(new Entry(i,
                                     temp));

            feelValues.add(new Entry(i,
                                     rTemp));

        }

        final ArrayList<LineDataSet> set = new ArrayList<>();
        set.add(buildTempLineDataSet(realValues,
                                     PRIMARY_COLOR,
                                     false,
                                     (v, e, dsi, vph) -> e.getX() % 2 == 0
                                                         ? getPrintableTemp((double) v,
                                                                            1)
                                                         : ""));
        set.add(buildTempLineDataSet(feelValues,
                                     SECONDARY_COLOR,
                                     false,
                                     (v, e, dsi, vph) -> e.getX() % 2 != 0
                                                         ? getPrintableTemp((double) v,
                                                                            1)
                                                         : ""));
        return set;

    }

    private LineDataSet buildHourForecastChart(final AccuWeatherHourlyForecast forecasts,
                                               float shift) {

        final List<Entry> values = new ArrayList<>();

        for(int i = 0; i < forecasts.forecast.size(); i++) {
            values.add(new Entry(i,
                                 shift));

        }

        final LineDataSet dataSet = new LineDataSet(values,
                                                    "");
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> new org.joda.time.DateTime(forecasts.forecast.get((int) entry.getX()).dateTime).getHourOfDay() + ":00");
        dataSet.setLineWidth(0f);
        dataSet.setLineWidth(0f);
        dataSet.setColor(Color.argb(0,
                                    0,
                                    0,
                                    0));
        dataSet.setValueTypeface(Typeface.create(dataSet.getValueTypeface(),
                                                 Typeface.BOLD));
        return dataSet;

    }

    private void updateHourlyForecast(final View root, final AccuWeatherHourlyForecast forecasts) {

        if(forecasts == null) {
            return;
        }

        final LineChart tempChart = root.findViewById(R.id.tempChart);
        //        tempChart.getXAxis()
        //                .setValueFormatter((value, axis) -> {
        //                    String ret = "";
        //                    if(forecasts.forecast != null && forecasts.forecast.size() >= value) {
        //                        ret = new org.joda.time.DateTime(forecasts.forecast.get((int) value).dateTime).getHourOfDay() + ":00";
        //                    }
        //                    return ret;
        //                });

        /* float maxTemp = (float) forecasts.forecast.stream()
                .mapToDouble(value -> value.temperature.value)
                .max()
                .getAsDouble();*/

        final List<ILineDataSet> sets = new ArrayList<>();
        sets.addAll(buildTempForecastChart(forecasts));

        tempChart.setData(new LineData(sets));
        tempChart.invalidate(); // refresh

        final LineChart valuesChart = root.findViewById(R.id.valuesChart);
        final List<ILineDataSet> valueSets = new ArrayList<>();

        // -- RAIN
        valueSets.addAll(new ProbabilityChartBuilder(forecasts,
                                                     forecast -> forecast.rainProbability,
                                                     1).buildData());

        // -- SNOW
        valueSets.addAll(new ProbabilityChartBuilder(forecasts,
                                                     forecast -> forecast.snowProbability,
                                                     3).buildData());

        // -- CONDITION
        valueSets.addAll(new ConditionChartBuilder(forecasts,
                                                   forecast -> getWeatherIconFromIconId(forecast.weatherIcon,
                                                                                        "tiny_"),
                                                   5) {

            @Override
            protected Entry buildDataEntry(float x, float y, final HourlyForecast forecast) {
                final Entry entry = new Entry(x,
                                              y);
                entry.setIcon(getResources().getDrawable(dataProvider.getData(forecast),
                                                         null));
                return entry;
            }

        }.buildData());

        valueSets.add(buildHourForecastChart(forecasts,
                                             7));

        valuesChart.setData(new LineData(valueSets));
        valuesChart.invalidate(); // refresh

    }

    private LineDataSet buildTempLineDataSet(List<Entry> entries,
                                             int color,
                                             boolean fill,
                                             IValueFormatter valueFormatter) {

        final LineDataSet dataSet = new LineDataSet(entries,
                                                    "");
        dataSet.setColor(color);
        dataSet.setValueTextColor(color == 0
                                  ? PRIMARY_COLOR
                                  : SECONDARY_COLOR);

        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setCircleColor(color);
        dataSet.setCircleRadius(dataSet.getCircleRadius() / 2);

        dataSet.setValueFormatter(valueFormatter);
        dataSet.setDrawFilled(fill);
        dataSet.setDrawHighlightIndicators(true);
        dataSet.setLineWidth(0.5f);

        dataSet.setFillAlpha(100);

        dataSet.setDrawIcons(true);
        dataSet.setIconsOffset(new MPPointF(0,
                                            -dataSet.getValueTextSize() * 2));

        dataSet.setMode(LineDataSet.Mode.LINEAR);

        dataSet.setFillColor(color);
        return dataSet;
    }

}
