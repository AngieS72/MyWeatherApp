package edu.ranken.ashelton.myweatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APICall {
    // WeatherInformation is the name of the object that we receive from the server.
    // In other words, this is the ENTIRE JSON object shown in openweathermap.

    //Later, we will get MainData from the WeatherInformation object.

    //We are building the endpoint below via the @Query statements.

    @GET("weather")
    Call<WeatherInformation> getWeatherData(@Query("q") String city,
                                            @Query("appid") String apiKey);

}
