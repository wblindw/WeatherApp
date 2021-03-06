package com.blind.wakemeup.weather.accu.service;


import android.content.Context;
import android.util.Log;

import com.blind.wakemeup.R;
import com.blind.wakemeup.utils.ContextLoader;
import com.blind.wakemeup.weather.accu.model.day.AccuWeatherCurrent;
import com.blind.wakemeup.weather.accu.model.forecast.AccuWeatherDailyForecast;
import com.blind.wakemeup.weather.accu.model.hourlyforecast.AccuWeatherHourlyForecast;
import com.blind.wakemeup.weather.accu.model.hourlyforecast.HourlyForecast;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Allow to get weather data from AccuWeather API.
 */
public class AccuWeatherService {

    private static final String LOG_TAG= AccuWeatherService.class.getSimpleName();

    private static final String API_KEY_PARAM = "apikey=";
    private static List<String> keys;

    private static int currentKeyIndex = 0;

    private static final String BASE_URL = "http://dataservice.accuweather.com/";
    private static final String EXTRA_URL = "&details=true&metric=true";
    private static final String CURRENT_URL = BASE_URL + "currentconditions/v1/";
    private static final String DAILY_FORECAST_URL = BASE_URL + "forecasts/v1/daily/5day/";
    private static final String HOURLY_FORECAST_URL = BASE_URL + "forecasts/v1/hourly/12hour/";

    /**
     * Load all available API keys for AccuWeather service.
     * @param context app. context to access API keys file.
     * @param fileId keys file resource id.
     */
    public static void loadKeys(final Context context, int fileId) {
        try {
            String str = ContextLoader.readRawTextFile(context, fileId);
            keys = Arrays.asList(str.split("\n"));
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unable to load API Keys.", e);
        }
    }

    /**
     * Get current weather for a location.
     * @param locationCode the location code (see AccuWeather web site)
     * @return the current weather for the input code. Can be <code>null</code>.
     */
    public static AccuWeatherCurrent getCurrent(int locationCode) {
        AccuWeatherCurrent weather = null;
        try {
            String result = getRawData(CURRENT_URL + locationCode);
            if(result != null) {
                weather = new ObjectMapper().readValue(result.substring(1, result.length() - 1), AccuWeatherCurrent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;
    }


    /**
     * Get the daily weather forecast for a location.
     * @param locationCode the location code (see AccuWeather web site)
     * @return the weather forecast for the input code. Can be <code>null</code>.
     */
    public static AccuWeatherDailyForecast getDailyForecast(int locationCode) {
        AccuWeatherDailyForecast weather = null;
        try {
            final String result = getRawData(DAILY_FORECAST_URL + locationCode);
            if(result != null) {
                weather = new ObjectMapper().readValue(result, AccuWeatherDailyForecast.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;
    }

    /**
     * Get the hourly weather forecast fot a location.
     * @param locationCode the location code (see AccuWeather web site)
     * @return the weather forecast for the input code. Can be <code>null</code>.
     */
    public static AccuWeatherHourlyForecast getHourlyForecast(int locationCode) {
        AccuWeatherHourlyForecast weather = null;
        try {
            final String result = getRawData(HOURLY_FORECAST_URL + locationCode);
            if(result != null) {
                final ObjectMapper mapper = new ObjectMapper();
                weather = new AccuWeatherHourlyForecast();
                weather.forecast = mapper.readValue(result, mapper.getTypeFactory().constructCollectionType(List.class, HourlyForecast.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;
    }

    /**
     * Call weather service.
     * @param urlStr the service URL.
     * @return the raw data from the service called. Can be <code>null</code> or empty.
     */
    public static String getRawData(String urlStr) {
        try {

            final URL url = new URL(String.format(urlStr + "?" + API_KEY_PARAM + keys.get(currentKeyIndex) + EXTRA_URL));
            final HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            final int code = connection.getResponseCode();

            if(code == 200) {

                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                final StringBuffer json = new StringBuffer(1024);

                String tmp = "";
                while ((tmp = reader.readLine()) != null) {
                    json.append(tmp).append("\n");
                }
                reader.close();

                return json.toString();

            } else if (code == 503) {// The allowed number of requests has been exceeded.

                Log.d(LOG_TAG, "API Key max requests reached. Try another one.");

                currentKeyIndex++;
                if(currentKeyIndex >= keys.size()) {
                    currentKeyIndex = 0;
                }
                return getRawData(urlStr    );

            } else {
                Log.e(LOG_TAG, "Unable to get weather from server: code " + code);
            }


        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String ... args) throws IOException {

        InputStream inputStream = new FileInputStream("weather.txt");
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }

        String str = resultStringBuilder.toString();
        String res = str.substring(1, str.length() - 1);

        System.out.println(res);

        System.out.println(new ObjectMapper().readValue(res, AccuWeatherCurrent.class));

        //


    }

    public static AccuWeatherCurrent getCurrentSample(int locationCode, Context context) {
        AccuWeatherCurrent forecast = null;
        try {
            String str = ContextLoader.readRawTextFile(context, R.raw.cweather);
            String res = str.substring(1, str.length() - 1);

            forecast = new ObjectMapper().readValue(res, AccuWeatherCurrent.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forecast;
    }

    public static AccuWeatherDailyForecast getDailyForecastSample(int locationCode, Context context) {
        AccuWeatherDailyForecast forecast = null;
        try {
            String str = ContextLoader.readRawTextFile(context, R.raw.fweather);
            forecast = new ObjectMapper().readValue(str, AccuWeatherDailyForecast.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forecast;
    }

    public static AccuWeatherHourlyForecast getHourlyForecastSample(int locationCode, Context context) {
        AccuWeatherHourlyForecast forecast = null;
        try {
            String str = ContextLoader.readRawTextFile(context, R.raw.hweather);
            final ObjectMapper mapper = new ObjectMapper();

            forecast = new AccuWeatherHourlyForecast();
            forecast.forecast = mapper.readValue(str, mapper.getTypeFactory().constructCollectionType(List.class, HourlyForecast.class));

        } catch (Exception e) {
            e.printStackTrace();
            forecast = null;
        }
        return forecast;
    }



}
