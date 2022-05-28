package com.example.SE328projectraneem;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WeatherActivity extends AppCompatActivity {


    EditText city;
    RequestQueue request;
    Button setCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        city = findViewById(R.id.inp_city);
        setCity=findViewById(R.id.bttn_city);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        request = Volley.newRequestQueue(this);
        request.add(Helper.weather(this));

        setCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CityName = city.getText()+"";

                if (CityName.isEmpty()){
                    Toast.makeText(WeatherActivity.this,"City name required. Please type a city name.",Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor myEditor = sp.edit();

                myEditor.putString("WeatherCity",CityName);
                myEditor.commit();

                request.add(Helper.weather(WeatherActivity.this));
            }
        });

        request.add(Helper.weather(this));

    }

}