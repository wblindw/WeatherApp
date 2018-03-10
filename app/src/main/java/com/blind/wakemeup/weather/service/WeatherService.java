package com.blind.wakemeup.weather.service;


import com.blind.wakemeup.weather.model.current.CurrentWeather;
import com.blind.wakemeup.weather.model.dailyforecast.DailyWeatherForcast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.Header;


public class WeatherService {

    private static final String LOG_TAG= com.blind.wakemeup.weather.service.WeatherService.class.getSimpleName();

    private static final String API_KEY_PARAM = "x-api-key";
    private static final String API_KEY_VALUE = "afadd7fa0199801a927c61f7a22d1e36";

    private static final String API_ACCU_WEATHER_KEY_VALUE =  "tcOSKVf5FSNYGBr4gJlZYEiPv7jN6vAx";
    private static final String API_PARAM_CITY_BOSTON_KEY_VALUE = "348735";
    private static final String API_PARAM_CITY_PARIS_KEY_VALUE = "623";
    private static final String API_PARAM_CITY_REUNION_KEY_VALUE = "275418";

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String EXTRA_URL = "?q=Boston" + "&units=metric";
    private static final String CURRENT_URL = BASE_URL + "weather" + EXTRA_URL;
    private static final String FORECAST_URL = BASE_URL + "forecast" + EXTRA_URL;


    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.addHeader(API_KEY_PARAM, API_KEY_VALUE);
    }

    public static void getCurrent(final ICurrentWeatherCallback cb) {
        get(CURRENT_URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    CurrentWeather weather = new ObjectMapper().readValue(new String(responseBody), CurrentWeather.class);
                    cb.onData(weather);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public static CurrentWeather getCurrent() {
            try {
                URL url = new URL(String.format(CURRENT_URL));
                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();

                connection.addRequestProperty(API_KEY_PARAM,
                        API_KEY_VALUE);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();

                return new ObjectMapper().readValue(json.toString(), CurrentWeather.class);
            }catch(Exception e){
                e.printStackTrace();
            }
        return null;
    }



    public static void getForecast(final IWeatherForcastCallback cb) {
        get(FORECAST_URL, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    DailyWeatherForcast weather = new ObjectMapper().readValue(new String(responseBody), DailyWeatherForcast.class);
                    cb.onData(weather);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }


    private static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.post(String.format(url), null, responseHandler);
    }



}
