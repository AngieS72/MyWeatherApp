package edu.ranken.ashelton.myweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    // Declare program constants
    final String CN = "City Name ";
    final String CBE = "Cannot Be Empty";
    final String WNF = "Was Not Found By API";
    final String UPF = "Unexpected Program Failure";

    // Declare instance variables
    EditText etCity;
    TextView tvResults;
    TextView tvMinTemp;
    TextView tvMaxTemp;
    ImageView ivLogo;
    Button btnGetTemperature;
    Button btnClear;

    // Create APICall interface object
    APICall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get referecnces to widgets
        etCity = findViewById(R.id.etCity);
        tvResults = findViewById(R.id.tvResults);
        tvMinTemp = findViewById(R.id.tvMinTemp);
        tvMaxTemp = findViewById(R.id.tvMaxTemp);

        btnGetTemperature = findViewById(R.id.btnGetTemperature);
        btnClear = findViewById(R.id.btnClear);

        //set cursor focus to City et when program begins
        etCity.requestFocus();

        // Create Retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverInformation.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Associate Retrofit object with APICall
        apiCall = retrofit.create(APICall.class);

      /*  //Have image fade in on start
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);
        image.startAnimation(animation1);
*/


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCity.setText("");
                tvResults.setText("");
                tvMinTemp.setText("");
                tvMaxTemp.setText("");
                etCity.requestFocus();
            }
        });


        btnGetTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code that fires when Get Temp button is clicked
                //Get weather information.  The city is from whatever user enters in etCity
                // and the key is from api key openweathermap.org.

                //Have image fade in on start
// Commented out - could not get it to work.  Will keep trying but commented out for due date.
                 /*   ImageView image = (ImageView) findViewById(R.id.imageView);
                    Animation animation1 =
                            AnimationUtils.loadAnimation(getApplicationContext(),
                                    R.anim.fade);
                    image.startAnimation(animation1);*/


                try {
                    if (etCity.getText().toString().trim().equals("")) {
                        throw new IllegalArgumentException();
                    }
                    //if we get to here, something was entered,  checking now for
                    Call<WeatherInformation> call = apiCall.getWeatherData(
                            etCity.getText().toString(),
                            serverInformation.API_KEY);

                    //Callback function
                    call.enqueue(new Callback<WeatherInformation>() {

                        @Override
                        public void onResponse(Call<WeatherInformation> call,
                                               Response<WeatherInformation> response) {
//If city not found in API database, a 404 error should return.
// So just show an associated toast and return.
                            if (response.code() == 404) {
                                Toast.makeText(getApplicationContext(), CN + WNF, Toast.LENGTH_SHORT).show();
                                etCity.setText("");
                                etCity.requestFocus();
                                return;
                            }

                            // We have entered a valid city
                            WeatherInformation weatherInformation = response.body();
                            double theTemperature = weatherInformation.getMainData().getTemperature();
                            double theMinTemp = weatherInformation.getMainData().getTempMin();
                            double theMaxTemp = weatherInformation.getMainData().getTempMax();

                            String theText = "The current temperature in\n";
                            // Convert the temp from K to F
                            theTemperature = (theTemperature - 273.15) * 1.8 + 32;
                            theText += etCity.getText().toString() +
                                    " is " + Math.round(theTemperature) + "\u00B0";
                            tvResults.setText(theText);

                            String theMinTempText = "Minimum Temp: ";
                            theMinTemp = (theMinTemp  - 273.15) * 1.8 + 32;
                            theMinTempText += Math.round(theMinTemp) + "\u00B0";
                            tvMinTemp.setText(theMinTempText);

                            String theMaxTempText = "Maximum Temp: ";
                            theMaxTemp = (theMaxTemp  - 273.15) * 1.8 + 32;
                            theMaxTempText += Math.round(theMaxTemp) + "\u00B0";
                            tvMaxTemp.setText(theMaxTempText);
                        }

                        //what you want it to do if it doesn't work. this should never happen
                        @Override
                        public void onFailure(Call<WeatherInformation> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), UPF, Toast.LENGTH_SHORT).show();

                        }
                    });


                } catch (IllegalArgumentException iae) {
                    Toast.makeText(getApplicationContext(), CN + CBE, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}