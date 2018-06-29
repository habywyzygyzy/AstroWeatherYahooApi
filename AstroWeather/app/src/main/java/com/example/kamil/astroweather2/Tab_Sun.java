package com.example.kamil.astroweather2;


import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.kamil.astroweather2.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Tab_Sun extends Fragment {
    public static TextView lon;
    public static TextView lat;
    public static TextView wschodText;
    public static TextView zachodText;
    public static TextView wschAzymutText;
    public static TextView zachAzymutText;
    public static TextView switText;
    public static TextView zmierzchText;
    public static TextView timeToSunSet;
    public static AstroCalculator.Location pozycja;
    public static AstroCalculator.SunInfo calcTime;
    public static AstroDateTime aktualnyCzas;
    public static int isCreated=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
           rootView = inflater.inflate(R.layout.tabsun, container, false);
        }
        else{
            rootView = inflater.inflate(R.layout.tabsunvertical, container, false);
        }
        lon=(TextView)rootView.findViewById(R.id.dlugText);
        lat=(TextView)rootView.findViewById(R.id.szerText);

        lon.setText(String.valueOf(MainActivity.getLongitude()));
        lat.setText(String.valueOf(MainActivity.getLatitude()));

        wschodText=(TextView)rootView.findViewById(R.id.sun_wschod);
        zachodText=(TextView)rootView.findViewById(R.id.sun_zachod);

        wschAzymutText=(TextView)rootView.findViewById(R.id.sun_azymut_w);
        zachAzymutText=(TextView)rootView.findViewById(R.id.sun_azymut_z);

        switText=(TextView)rootView.findViewById(R.id.sun_swit);
        zmierzchText=(TextView)rootView.findViewById(R.id.sun_zmierzch);
        //timeToSunSet=(TextView)rootView.findViewById(R.id.timetoSunset);
        isCreated=1;
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
                false);
        calcTime = new AstroCalculator(aktualnyCzas,pozycja).getSunInfo();
        String time=String.valueOf(calcTime.getTwilightEvening().getHour())+":"+String.valueOf(calcTime.getTwilightEvening().getMinute());
        DateFormat sdf = new SimpleDateFormat("hh:mm");
        DateFormat sdf2 = new SimpleDateFormat("kk:mm");
        Date currentTime = Calendar.getInstance().getTime();
        String hour = sdf2.format(currentTime);

        try {
            Date date = sdf.parse(time);
            Date dateCurrent = sdf.parse(hour);
            Log.d("myTag", "--------------------------------------------" + date);
            Log.d("myTag", "--------------------------------------------"+ dateCurrent);
            Log.d("myTag", "--------------------------------------------"+  printDifference(dateCurrent,date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        wschodText.setText("Wschód: " + String.valueOf(calcTime.getSunrise().getHour())+":"+String.valueOf(calcTime.getSunrise().getMinute()));
        zachodText.setText("Zachód: " + String.valueOf(calcTime.getSunset().getHour())+":"+String.valueOf(calcTime.getSunset().getMinute()));

        wschAzymutText.setText("Azymut(W): " + String.format("%.0f",(calcTime.getAzimuthRise()))+"°");
        zachAzymutText.setText("Azymut(Z): " + String.format("%.0f",(calcTime.getAzimuthSet()))+"°");
        //timeToSunSet.setText("Czas do zachodu: " + String.valueOf(calcTime.getSunset().getHour()-aktualnyCzas.getHour())+":"+ String.valueOf(calcTime.getSunset().getMinute()-aktualnyCzas.getMinute()));


        switText.setText("Świt: " + String.valueOf(calcTime.getTwilightMorning().getHour())+":"+String.valueOf(calcTime.getTwilightMorning().getMinute()));
        zmierzchText.setText("Zmierzch: " + String.valueOf(calcTime.getTwilightEvening().getHour())+":"+String.valueOf(calcTime.getTwilightEvening().getMinute()));
    }
    public static String printDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        //long elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return String.valueOf(elapsedHours)+":"+String.valueOf(elapsedMinutes);

    }
}
