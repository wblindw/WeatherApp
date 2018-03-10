package com.blind.wakemeup.weather.accu.model;

import com.blind.wakemeup.weather.accu.model.day.AccuWeatherCurrent;
import com.blind.wakemeup.weather.accu.model.forecast.AccuWeatherDailyForecast;
import com.blind.wakemeup.weather.accu.model.hourlyforecast.AccuWeatherHourlyForecast;

import java.util.List;


public class AccuWeather {
    public int id;
    public String name;

    public AccuWeatherCurrent current;
    public AccuWeatherDailyForecast forecast;
    public AccuWeatherHourlyForecast hourly;
}
