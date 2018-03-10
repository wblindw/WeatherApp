package com.blind.wakemeup.weather.service;

import com.blind.wakemeup.weather.model.dailyforecast.DailyWeatherForcast;

/**
 * Created by delor on 29/01/2018.
 */

public interface IWeatherForcastCallback{
    void onData(DailyWeatherForcast weather);
}
