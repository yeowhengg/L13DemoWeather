package com.myapplicationdev.android.id20042741.demoweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    ListView lvWeather;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvWeather = findViewById(R.id.lvWeather);
        client = new AsyncHttpClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Weather> alWeather = new ArrayList<Weather>();
        ArrayAdapter<Weather> aaWeather = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alWeather);

        client.get("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast", new JsonHttpResponseHandler(){
            String area;
            String forecast;

            public void onSuccess (int statusCode, Header[] headers, JSONObject response){
                try{
                    // the order for this would be jsonArray, firstObj, then jsonArrForecast
                    // this is because in the website, the structure is
                    // json object named item first
                    // followed by the json object named 0
                    // then inside the json object 0, it contains the forecast name which is an array
                    
                    JSONArray jsonArrItems = response.getJSONArray("items"); // get the name JSON object that is called item in data.gov.sg
                    JSONObject firstObj = jsonArrItems.getJSONObject(0); // this is 0 as before we get to the forecast name, it is a child of the "0" json object
                    JSONArray jsonArrForecasts = firstObj.getJSONArray("forecasts");

                    for(int i = 0; i < jsonArrForecasts.length(); i++){
                        JSONObject jsonObjForecast = jsonArrForecasts.getJSONObject(i);
                        area = jsonObjForecast.getString("area");
                        forecast = jsonObjForecast.getString("forecast");

                        Weather weather = new Weather(area, forecast);
                        alWeather.add(weather);
                        Log.i("debug", alWeather.get(0).toString());
                    }

                }catch (JSONException e){

                }

                lvWeather.setAdapter(aaWeather);

            } // closes onsuccess
        });
    } // closes onResume
}