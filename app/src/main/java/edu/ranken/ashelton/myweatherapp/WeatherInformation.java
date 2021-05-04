package edu.ranken.ashelton.myweatherapp;

import com.google.gson.annotations.SerializedName;

public class WeatherInformation {
    @SerializedName("main")
    private MainData mainData;

    //Add getter:  right mouse click, generate, getter

    public MainData getMainData() {
        return mainData;
    }
}
