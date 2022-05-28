package com.example.SE328projectraneem;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

public class Helper {


    public static ImageView addMyIcon(AppCompatActivity a, @Nullable View.OnClickListener clickListener){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(a);
        ImageView img = new ImageView(a);

        ActionBar actionBar = a.getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams( Gravity.END
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        img.setLayoutParams(layoutParams);
        actionBar.setCustomView(img);

        String myIcon = sp.getString("icon","");
        Glide.with(a).load(myIcon).into(img);

        if (clickListener == null){
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String city = sp.getString("WeatherCity","Athens");
                    float temp = sp.getFloat("temp",-1);
                    float humidity = sp.getFloat("humidity",-1);
                    int clouds = sp.getInt("clouds",-1);

                    AlertDialog.Builder myDialog = new AlertDialog.Builder(a);
                    myDialog.setTitle("Weather in "+city);
                    String message="Temperature: "+ temp +"°C\n"+
                            "Chance of Rain: "+clouds+"%\n"+
                            "Humidity: "+humidity+"% \n"+"";

                    myDialog.setMessage(message);

                    myDialog.show();
                }
            });

        }

        else{
            img.setOnClickListener(clickListener);
        }

        return img;
    }

    public static JsonObjectRequest weather(AppCompatActivity a)
    {
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(a);

        String city = sp.getString("WeatherCity","Athens");

        String url ="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=21b4cebb0db9e16315a175ce7597ed89&units=metric";

        JsonObjectRequest myJSON = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject myWeather = response.getJSONObject("main");
                    String icon= response.getJSONArray("weather").getJSONObject(0).getString("icon");
                    String iconLink="https://openweathermap.org/img/w/"+icon+".png";

                    //JSONObject myWeather = response.getJSONObject("x");

                    SharedPreferences.Editor myEditor = sp.edit();
                    myEditor.putString("icon",iconLink);
                    myEditor.putFloat("temp",(float)myWeather.getDouble("temp"));
                    myEditor.putInt("clouds",response.getJSONObject("clouds").getInt("all"));
                    myEditor.putFloat("humidity",(float)myWeather.getDouble("humidity"));
                    myEditor.commit();

                    addMyIcon(a,null);
                }


                    catch (Exception x) { x.printStackTrace(); }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(a,"Problem Detected: "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        return myJSON;
    }
}
