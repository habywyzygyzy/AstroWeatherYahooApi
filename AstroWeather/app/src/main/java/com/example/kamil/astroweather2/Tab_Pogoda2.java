package com.example.kamil.astroweather2;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kamil.astroweather2.R;


public class Tab_Pogoda2 extends Fragment {

    public static TextView windDirection;
    public static TextView windSpeed;
    public static TextView humidity;
    public static TextView visibility;
    public static Context context;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        if(MainActivity.weather!=null) {
            rootView = inflater.inflate(R.layout.tab_pogoda2, container, false);

            initiateFindViewBy(rootView);
            context = getActivity();
            fillInfo();
            imageView = (ImageView) rootView.findViewById(R.id.windDirectionImage);
            imageView.setImageResource(R.drawable.windback);
        }else{
            rootView = inflater.inflate(R.layout.empty, container, false);
        }
        return rootView;
    }

    public static void initiateFindViewBy(View rootView) {
        windDirection=(TextView) rootView.findViewById(R.id.windDirection);
        windSpeed=(TextView) rootView.findViewById(R.id.windSpeed);
        humidity=(TextView) rootView.findViewById(R.id.humidity);
        visibility=(TextView) rootView.findViewById(R.id.visibility);

}
    public static void fillInfo(){
        windDirection.setText("Kierunek: "+MainActivity.windDirection);
        windSpeed.setText("Prędkość: "+MainActivity.windSpeed);
        humidity.setText(MainActivity.humidity);
        visibility.setText(MainActivity.visibility);
    }


}
