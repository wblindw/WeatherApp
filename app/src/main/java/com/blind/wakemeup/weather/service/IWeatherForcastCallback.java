package com.blind.wakemeup.weather.service;

import com.blind.wakemeup.weather.model.forecast.WeatherForcast;

/**
 * Created by delor on 29/01/2018.
 */

public interface IWeatherForcastCallback{
    void onData(WeatherForcast weather);
}
