package com.blind.wakemeup.weather.service;

import com.blind.wakemeup.weather.model.current.CurrentWeather;

/**
 * Created by delor on 29/01/2018.
 */

public interface ICurrentWeatherCallback {
     void onData(CurrentWeather weather);
}
