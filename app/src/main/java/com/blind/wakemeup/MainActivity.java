package com.blind.wakemeup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.blind.wakemeup.utils.ContextLoader;
import com.blind.wakemeup.utils.UnitTempEnum;
import com.blind.wakemeup.utils.Units;
import com.blind.wakemeup.weather.accu.model.AccuWeather;
import com.blind.wakemeup.weather.accu.model.forecast.AccuWeatherForecast;
import com.blind.wakemeup.weather.accu.model.forecast.DailyForecast;
import com.blind.wakemeup.weather.accu.service.AccuWeatherService;

import org.joda.time.DateTime;

import java.io.IOException;
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

    private int PRIMARY_COLOR;
    private int SECONDARY_COLOR;

    // Current unit used to display temperatures
    private UnitTempEnum currentTempUnitDisplay = UnitTempEnum.CELCIUS;

    // Ref. to the selected city
    private Integer currentCityId;

    // City IDs and related weather
    private Map<Integer, AccuWeather> citiesWeather = new LinkedHashMap<>();

    private Properties weatherIcoProperties;
    private Properties weatherUnitsProperties;

    // Weather update scheduler
    private final ScheduledExecutorService weatherScheduler = Executors.newScheduledThreadPool(2);

    private static final TimeUnit   WEATHER_UPDATE_FREQUENCY_UNIT = TimeUnit.HOURS;
    private static final int        WEATHER_UPDATE_FREQUENCY      = 12;

    // Handler used to update the view when weather data is updated.
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        PRIMARY_COLOR = ContextCompat.getColor(getApplicationContext(), R.color.colorFontPrimary);
        SECONDARY_COLOR = ContextCompat.getColor(getApplicationContext(), R.color.colorFontSecondary);

        weatherIcoProperties = ContextLoader.getProperties(getApplicationContext(), R.raw.weather_codes_ico);
        weatherUnitsProperties = ContextLoader.getProperties(getApplicationContext(), R.raw.units);

        final Properties citiesProperties = ContextLoader.getProperties(getApplicationContext(), R.raw.cities);
        for(Object id : citiesProperties.keySet()) {

            final AccuWeather weather = new AccuWeather();
            weather.id = Integer.valueOf((String) id);
            weather.name = citiesProperties.getProperty((String) id);
            citiesWeather.put(weather.id, weather);

            addCity(weather.id);

            // get first element as default city
            if(currentCityId == null) {
                setCurrentCity(weather.id);
            }

        }

        findViewById(R.id.tempsLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTempUnitDisplay = UnitTempEnum.next(currentTempUnitDisplay);
                updateView(citiesWeather.get(currentCityId));
            }
        });


        AccuWeatherService.loadKeys(getApplicationContext(), R.raw.keys);

        startUpdateService();

    }

    /**
     * Start view updates
     */
    protected void startUpdateService() {

        // Get current and forecast weather for all cities
        weatherScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                // Get weathers
                for(Integer cityID : citiesWeather.keySet()) {
                    final AccuWeather weather = citiesWeather.get(cityID);
                    weather.current = AccuWeatherService.getCurrentSample(cityID, getApplicationContext());
                    weather.forecast = AccuWeatherService.getForecastSample(cityID, getApplicationContext());
                }

                // Update view in new thread
                handler.post(new Runnable(){
                    public void run(){

                        final AccuWeather weather = citiesWeather.get(currentCityId);

                        updateView(weather);

                        for(int i = 0; i<weather.forecast.dailyForecasts.size(); i++) {
                            updateForecast(weather.forecast.dailyForecasts.get(i), i);
                        }
                    }
                });
            }
        }, 0, WEATHER_UPDATE_FREQUENCY, WEATHER_UPDATE_FREQUENCY_UNIT);

        // Get and update current date and time
        weatherScheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable(){
                    public void run(){
                        updateCt();
                    }
                });
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Update the view with weather data.
     * @param weather full weather data. Do nothing if <code>null</code>.
     */
    protected void updateView(final AccuWeather weather) {

        if(weather == null || weather.current == null || weather.forecast == null) {
            return;
        }

        final DailyForecast dailyForecast = getToday(weather.forecast);

        ((TextView) findViewById(R.id.tempValue)).setText(getPrintableTemp(weather.current.temperature.metric.value, 1));
        ((TextView) findViewById(R.id.tempUnit)).setText(currentTempUnitDisplay.toString());
        ((TextView) findViewById(R.id.tempMaxValue)).setText(getPrintableTemp(dailyForecast.temperature.maximum.value, 0));
        ((TextView) findViewById(R.id.tempMinValue)).setText(getPrintableTemp(dailyForecast.temperature.minimum.value, 0));
        ((TextView) findViewById(R.id.tempFeelValue)).setText(getPrintableTemp(weather.current.realFeelTemperature.metric.value, 0));

        updateRainValue(dailyForecast);
        updateSnowValue(dailyForecast);

        updateMainWeather(weather);

        int idx = 0;
        for(final DailyForecast forecast : weather.forecast.dailyForecasts) {
            updateForecast(forecast, idx);
            idx++;
        }
    }

    /**
     * Update current date and time.
     */
    protected  void updateCt() {

        final DateTime now = DateTime.now();
        ((TextView) findViewById(R.id.ctDayValue)).setText(now.toString("EEEE"));
        ((TextView) findViewById(R.id.ctTimeValue)).setText(now.toString("HH:mm"));
        ((TextView) findViewById(R.id.ctTimeSecValue)).setText(now.toString("ss"));
        ((TextView) findViewById(R.id.ctDateValue)).setText(now.toString("d MMM"));

    }

    /**
     * Convert and format temperature depending on decimals to display and current unit.
     * @param tempC  the temperature value.
     * @param decimal number of decimal to display.
     * @return input temperature in the current unit, with the number of decimal from input and the temperature unit.
     */
    private String getPrintableTemp(Double tempC, int decimal) {

        double value = 0;
        if(currentTempUnitDisplay == UnitTempEnum.CELCIUS) {
         value = tempC;
        }
         else  if(currentTempUnitDisplay == UnitTempEnum.FAHRENHEIT) {
            value = Units.tempCtoF(tempC);
        }

        if(decimal == 0) {
            return  Math.round(value) + "";
        } else {
            return ((float) (Math.round(value * Math.pow(10, decimal)) / (Math.pow(10, decimal)))) + "";
        }
    }

    /**
     * Update main weather data of the current day.
     * @param weather the weather data.
     */
    private void updateMainWeather(AccuWeather weather) {
        ((TextView) findViewById(R.id.weatherDesc)).setText(weather.current.weatherText.toUpperCase());
        ((ImageView) findViewById(R.id.ico_weather)).setImageResource(getWeatherIconFromIconId(weather.current.weatherIcon, ""));
    }

    private int getWeatherIconFromIconId(Integer weatherIcon, String prefix) {
        String weatherRes = weatherIcoProperties.getProperty(String.valueOf(weatherIcon));
        return ContextLoader.getResource(getApplicationContext(), "drawable", prefix + weatherRes);
    }

    /**
     * Get the current weather from all weather data.
     * @param forecast the overall weather data.
     * @return the current weather, <code>null</code> if no current weather data.
     */
    private DailyForecast getToday(AccuWeatherForecast forecast) {

        return forecast.dailyForecasts.size() > 1 ? forecast.dailyForecasts.get(0) : null;

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


    /**
     * Build and add component to select a city.
     * @param id a reference given to the city selection component.
     */
    private void addCity(final int id) {

        final Button button = new Button(this);
        button.setText(citiesWeather.get(id).name);
        button.setPadding(10, 10, 10, 10);
        button.setId(id);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER;
        params.leftMargin = 10;
        params.rightMargin = 10;
        button.setLayoutParams(params);

        applyButtonSelectionStyle(button, false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentCity(id);
            }
        });
        ((ViewGroup) findViewById(R.id.citiesLayout)).addView(button);

    }

    /**
     * Set selected city from id.
     * @param id the id of the city to select.
     */
    private void setCurrentCity(int id) {

        if(currentCityId == null || currentCityId != id) {
            if(currentCityId != null) {
                applyButtonSelectionStyle((Button) findViewById(currentCityId), false);
            }
            applyButtonSelectionStyle((Button) findViewById(id), true);
        }

        currentCityId = id;
        updateView(citiesWeather.get(currentCityId));

    }

    /**
     * Update button style depending on selection state.
     * @param button the button to update.
     * @param selected the selection state, <code>true</code> selected, <code>false</code> otherwise.
     */
    private void applyButtonSelectionStyle(Button button, boolean selected) {
        button.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
        button.setTextColor(ContextCompat.getColor(getApplicationContext(), selected ? R.color.colorFontPrimary : R.color.colorFontSecondary));
        button.setBackgroundResource(selected ? R.drawable.button_bg_selected : R.drawable.button_bg_unselected);
    }

    /**
     * Update current weather rain value.
     * @param dailyForecast the current weather data.
     */
    private void updateRainValue(final DailyForecast dailyForecast){

        int prob = dailyForecast.day.rainProbability;
        TextView textView = (TextView) findViewById(R.id.rainValue);
        textView.setText(String.valueOf(prob));
        textView.setTextColor(prob > 0.f ? PRIMARY_COLOR : SECONDARY_COLOR);

        ((ImageView) findViewById(R.id.rainValueImage)).setImageResource(prob > 0.f ? R.drawable.rain_value : R.drawable.rain_value_off);

    }

    /**
     * Update current weather snow value.
     * @param dailyForecast the current weather data.
     */
    private void updateSnowValue(final DailyForecast dailyForecast){

        int prob = dailyForecast.day.snowProbability;
        TextView textView = (TextView) findViewById(R.id.snowValue);
        textView.setText(String.valueOf(prob));
        textView.setTextColor(prob > 0.f ? PRIMARY_COLOR : SECONDARY_COLOR);

        ((ImageView) findViewById(R.id.snowValueImage)).setImageResource(prob > 0.f ? R.drawable.snow_value : R.drawable.snow_value_off);

    }

    private final static String FORECAST_DAY_TAG = "forecast_day_";
    private final static String FORECAST_ICO_TAG = "forecast_ico_";
    private final static String FORECAST_SNOW_TAG = "forecast_snow_";
    private final static String FORECAST_TEMP_TAG = "forecast_temp_";
    private final static String FORECAST_RAIN_TAG = "forecast_rain_";

    /**
     * Build and add a view to display a weather forecast.
     * @param idx
     */
    public void addForecast(int idx) {

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        final TextView dateView = new TextView(this);
        dateView.setTag(FORECAST_DAY_TAG + idx);
        dateView.setLayoutParams(params);
        dateView.setTextColor(PRIMARY_COLOR);
        dateView.setTextSize(30);

        final ImageView forecastIcon = new ImageView(this);
        forecastIcon.setTag(FORECAST_ICO_TAG + idx);
        forecastIcon.setLayoutParams(params);

        final ViewGroup tempView = buildForecastValueView(this, R.drawable.small_temp_value, FORECAST_TEMP_TAG + idx);
        final ViewGroup rainView = buildForecastValueView(this, R.drawable.small_rain_value, FORECAST_RAIN_TAG + idx);
        final ViewGroup snowView = buildForecastValueView(this, R.drawable.small_snow_value, FORECAST_SNOW_TAG + idx);

        final LinearLayout forecastView = new LinearLayout(this);
        forecastView.setOrientation(LinearLayout.VERTICAL);
        forecastView.addView(dateView);
        forecastView.addView(forecastIcon);
        forecastView.addView(tempView);
        forecastView.addView(rainView);
        forecastView.addView(snowView);
        forecastView.setPadding(20, 0, 0, 0);

        ((ViewGroup) findViewById(R.id.forecastLayout)).addView(forecastView);

    }

    /**
     * Update all data of a weather forecast day
     * @param forecast the weather forecast data. Do nothing if <code>null</code>.
     * @param idx the index of the weather forecast to update. If the view for the index does not exist, it will be created and added.
     */
    private void updateForecast(DailyForecast forecast, int idx) {

        if(forecast == null) {
            return;
        }

        final ViewGroup forecastLayout =  ((ViewGroup) findViewById(R.id.forecastLayout));

        if(forecastLayout.findViewWithTag(FORECAST_ICO_TAG + idx) == null){
            addForecast(idx);
        }

        ((ImageView) forecastLayout.findViewWithTag(FORECAST_ICO_TAG + idx)).setImageResource(getWeatherIconFromIconId(forecast.day.icon, "small_"));
        ((TextView) forecastLayout.findViewWithTag(FORECAST_DAY_TAG + idx)).setText(new DateTime(forecast.date).toString("E"));

        ((TextView) findViewWithTagRecursively(forecastLayout, FORECAST_TEMP_TAG + idx)).setText(getPrintableTemp(forecast.temperature.maximum.value, 0));
        ((TextView) findViewWithTagRecursively(forecastLayout, FORECAST_RAIN_TAG + idx)).setText(String.valueOf(forecast.day.rainProbability) + "%");
        ((TextView) findViewWithTagRecursively(forecastLayout, FORECAST_SNOW_TAG + idx)).setText(String.valueOf(forecast.day.snowProbability) + "%");
    }

    /**
     * Build a view to display one data of a weather forecast. Contains an icon and a value.
     * @param context current app. context.
     * @param icoId the id of the icon to display.
     * @param valueTag the tag of the data displayed.
     * @return the view.
     */
    private ViewGroup buildForecastValueView(final Context context, final int icoId, final String valueTag) {

        final ImageView ico = new ImageView(context);
        ico.setImageResource(icoId);

        final TextView value = new TextView(context);
        value.setTag(valueTag);
        value.setTextColor(PRIMARY_COLOR);
        value.setTextSize(20);
        value.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(ico);
        layout.addView(value);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.START;
        params.topMargin = 5;

        ico.setLayoutParams(params);
        layout.setLayoutParams(params);

        params.leftMargin = 5;
        value.setLayoutParams(params);

        return layout;
    }

    /**
     * Get all the views which matches the given Tag recursively
     * @param root parent view. for e.g. Layouts
     * @param tag tag to look for
     * @return List of views
     */
    public static List<View> findViewsWithTagRecursively(ViewGroup root, Object tag){
        List<View> allViews = new ArrayList<View>();

        final int childCount = root.getChildCount();
        for(int i=0; i<childCount; i++){
            final View childView = root.getChildAt(i);

            if(childView instanceof ViewGroup){
                allViews.addAll(findViewsWithTagRecursively((ViewGroup)childView, tag));
            }
            else{
                final Object tagView = childView.getTag();
                if(tagView != null && tagView.equals(tag))
                    allViews.add(childView);
            }
        }

        return allViews;
    }

    /**
     * Get all the views which matches the given Tag recursively
     * @param root parent view. for e.g. Layouts
     * @param tag tag to look for
     * @return List of views
     */
    public static View findViewWithTagRecursively(ViewGroup root, Object tag){

        View result = null;
        final int childCount = root.getChildCount();
        for(int i=0; i<childCount; i++){
            final View childView = root.getChildAt(i);

            if(childView instanceof ViewGroup){
                result = findViewWithTagRecursively((ViewGroup)childView, tag);
            }

            if(result == null) {
                final Object tagView = childView.getTag();
                if(tagView != null && tagView.equals(tag))
                    return childView;
            } else {
                return result;
            }
        }

        return result;
    }

}
