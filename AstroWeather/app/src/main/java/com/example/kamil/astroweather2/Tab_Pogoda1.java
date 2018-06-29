package com.example.kamil.astroweather2;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.kamil.astroweather2.R;


public class Tab_Pogoda1 extends Fragment {

    public static ImageView imageView;
    public static TextView description;
    public static TextView latitude;
    public static TextView longitude;
    public static TextClock textClock;
    public static TextView temperature;
    public static TextView pressure;
    public static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        if(MainActivity.weather!=null) {
            rootView = inflater.inflate(R.layout.tab_pogoda1, container, false);

            initiateFindViewBy(rootView);
            context = getActivity();
            fillInfo();
        }else{
            rootView = inflater.inflate(R.layout.empty, container, false);
        }

        return rootView;
    }

    public static void initiateFindViewBy(View rootView) {
        latitude=(TextView) rootView.findViewById(R.id.dlugText);
        longitude=(TextView) rootView.findViewById(R.id.szerText);
        description=(TextView) rootView.findViewById(R.id.weatherText);
        textClock = (TextClock)rootView.findViewById(R.id.textClock);
        temperature=(TextView) rootView.findViewById(R.id.temperature);
        pressure=(TextView) rootView.findViewById(R.id.pressure);
        imageView=(ImageView) rootView.findViewById(R.id.weatherImage);

}
    public static void fillInfo(){
        latitude.setText(MainActivity.latitude);
        longitude.setText(MainActivity.longitude);
        description.setText(MainActivity.description);
        textClock.setTimeZone(MainActivity.textClock);
        temperature.setText(MainActivity.temperature);
        pressure.setText(MainActivity.pressure);
        imageView.setImageResource(MainActivity.pogodaID);
    }


}
