package com.pi.stela.sensors;

/**
 * Created by Paul on 08/10/2016.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONObject;
import com.pi.stela.R;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherController {
    Handler handler;
    public WeatherController(){
        handler = new Handler();
    }
    public static JSONObject getJSON(String city){
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=57761704d5b9dfb6d53dde5fb3020b25");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";

            while((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if(data.getInt("cod") != 200) {
                System.out.println("Cancelled");
                return null;
            }

            return data;
        }catch(Exception e){
            Log.d("STELA", "ERREUR :" +  e);
            return null;
        }
    }
    public void updateWeatherData(final String city, final Context MN, final TextView TemperatureDisplay, final TextView HumidityDisplay, final TextView weatherDisplay){
        new Thread(){
            public void run(){
                final JSONObject json = WeatherController.getJSON(city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(MN,
                                    "Sorry, no weather data found.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(MN,
                                    "Weather data found.",
                                    Toast.LENGTH_LONG).show();
                            renderWeather(json, MN, TemperatureDisplay, HumidityDisplay, weatherDisplay);
                        }
                    });
                }
            }
        }.start();
    }
    private void renderWeather(JSONObject json, Context MN, TextView TemperatureDisplay, TextView HumidityDisplay, TextView weatherDisplay){
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            TemperatureDisplay.setText(String.valueOf((int) (main.getDouble("temp") - 273.15)) + "°");
            HumidityDisplay.setText(String.valueOf((int) (main.getDouble("humidity"))) + "%");
            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000, MN, weatherDisplay);
        }catch(Exception e){
            Log.e("MeteoController", "One or more fields not found in the JSON data" + e);
        }
    }
    private void setWeatherIcon(int actualId, long sunrise, long sunset, Context MN, TextView weatherDisplay){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = MN.getString(R.string.weather_sunny);
            } else {
                icon = MN.getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = MN.getString(R.string.weather_thunder);
                    break;
                case 3 : icon = MN.getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = MN.getString(R.string.weather_foggy);
                    break;
                case 8 : icon = MN.getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = MN.getString(R.string.weather_snowy);
                    break;
                case 5 : icon = MN.getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherDisplay.setText(icon);
    }
}

