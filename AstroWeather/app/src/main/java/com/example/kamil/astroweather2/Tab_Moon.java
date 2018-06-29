package com.example.kamil.astroweather2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.kamil.astroweather2.R;

import java.util.Calendar;


public class Tab_Moon extends Fragment {
    public static TextView lon;
    public static TextView lat;
    public static TextView wschodText;
    public static TextView zachodText;
    public static TextView nowText;
    public static TextView pelniaText;
    public static TextView fazaText;
    public static TextView illuminationText;
    public static AstroCalculator.Location pozycja;
    public static AstroCalculator.MoonInfo calcTime;
    public static AstroDateTime aktualnyCzas;
    public static TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rootView = inflater.inflate(R.layout.tabmoon, container, false);
        }
        else{
            rootView = inflater.inflate(R.layout.tabmoonvertical, container, false);
        }
        lon=(TextView)rootView.findViewById(R.id.dlugText);
        lat=(TextView)rootView.findViewById(R.id.szerText);

        lon.setText(String.valueOf(MainActivity.getLongitude()));
        lat.setText(String.valueOf(MainActivity.getLatitude()));

        wschodText=(TextView)rootView.findViewById(R.id.moon_wschod);
        zachodText=(TextView)rootView.findViewById(R.id.moon_zachod);

        nowText=(TextView)rootView.findViewById(R.id.moon_now);
        pelniaText=(TextView)rootView.findViewById(R.id.moon_pelnia);

        fazaText=(TextView)rootView.findViewById(R.id.moon_faza);
        illuminationText=(TextView)rootView.findViewById(R.id.moon_dzien);

        update();


        return rootView;
    }

    public static void update() {
        pozycja = new AstroCalculator.Location(MainActivity.getLatitude(),MainActivity.getLongitude());
        aktualnyCzas = new AstroDateTime(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH)+1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND),
                2,
                true);
        calcTime = new AstroCalculator(aktualnyCzas,pozycja).getMoonInfo();


        wschodText.setText("Wschód: " + String.valueOf(calcTime.getMoonrise().getHour())+":"+String.valueOf(calcTime.getMoonrise().getMinute()));
        zachodText.setText("Zachód: " + String.valueOf(calcTime.getMoonset().getHour())+":"+String.valueOf(calcTime.getMoonset().getMinute()));

        nowText.setText("Nów: " + String.valueOf(calcTime.getNextNewMoon().getDay())+"."+String.valueOf(calcTime.getNextNewMoon().getMonth()+1)+"."+String.valueOf(calcTime.getNextNewMoon().getYear()));
        pelniaText.setText("Pełnia: " + String.valueOf(calcTime.getNextFullMoon().getDay())+"."+String.valueOf(calcTime.getNextFullMoon().getMonth()+1)+"."+String.valueOf(calcTime.getNextFullMoon().getYear()));

        fazaText.setText("Faza: "+ String.format("%.0f",(calcTime.getIllumination())*100)+"%");
        illuminationText.setText("Dzień lunacji: " +String.format("%.0f",(calcTime.getAge())));
    }


}
