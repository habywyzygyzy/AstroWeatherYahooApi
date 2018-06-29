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


public class Tab_Prognoza extends Fragment {

    public static TextView date1;
    public static TextView date2;
    public static TextView date3;
    public static TextView date4;
    public static ImageView code1;
    public static ImageView code2;
    public static ImageView code3;
    public static ImageView code4;
    public static TextView desc1;
    public static TextView desc2;
    public static TextView desc3;
    public static TextView desc4;
    public static TextView low1;
    public static TextView low2;
    public static TextView low3;
    public static TextView low4;
    public static TextView high1;
    public static TextView high2;
    public static TextView high3;
    public static TextView high4;
    public static TextView day1;
    public static TextView day2;
    public static TextView day3;
    public static TextView day4;
    public static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        if(MainActivity.weather!=null) {
            rootView = inflater.inflate(R.layout.tab_pogoda3, container, false);

            initiateFindViewBy(rootView);
            context = getActivity();
            fillInfo();
        }else{
            rootView = inflater.inflate(R.layout.empty, container, false);
        }
        return rootView;
    }

    public static void initiateFindViewBy(View rootView) {
        date1=(TextView) rootView.findViewById(R.id.date1);
        date2=(TextView) rootView.findViewById(R.id.date2);
        date3=(TextView) rootView.findViewById(R.id.date3);
        date4=(TextView) rootView.findViewById(R.id.date4);

        desc1=(TextView) rootView.findViewById(R.id.desc1);
        desc2=(TextView) rootView.findViewById(R.id.desc2);
        desc3=(TextView) rootView.findViewById(R.id.desc3);
        desc4=(TextView) rootView.findViewById(R.id.desc4);

        low1=(TextView) rootView.findViewById(R.id.low1);
        low2=(TextView) rootView.findViewById(R.id.low2);
        low3=(TextView) rootView.findViewById(R.id.low3);
        low4=(TextView) rootView.findViewById(R.id.low4);

        high1=(TextView) rootView.findViewById(R.id.high1);
        high2=(TextView) rootView.findViewById(R.id.high2);
        high3=(TextView) rootView.findViewById(R.id.high3);
        high4=(TextView) rootView.findViewById(R.id.high4);

        day1=(TextView) rootView.findViewById(R.id.day1);
        day2=(TextView) rootView.findViewById(R.id.day2);
        day3=(TextView) rootView.findViewById(R.id.day3);
        day4=(TextView) rootView.findViewById(R.id.day4);

        code1=(ImageView) rootView.findViewById(R.id.code1);
        code2=(ImageView) rootView.findViewById(R.id.code2);
        code3=(ImageView) rootView.findViewById(R.id.code3);
        code4=(ImageView) rootView.findViewById(R.id.code4);
}
    public static void fillInfo(){
        date1.setText(MainActivity.date1);date2.setText(MainActivity.date2);date3.setText(MainActivity.date3);date4.setText(MainActivity.date4);
        day1.setText(MainActivity.day1);day2.setText(MainActivity.day2);day3.setText(MainActivity.day3);day4.setText(MainActivity.day4);
        low1.setText("↓"+MainActivity.low1);low2.setText("↓"+MainActivity.low2);low3.setText("↓"+MainActivity.low3);low4.setText("↓"+MainActivity.low4);
        high1.setText("↑"+MainActivity.high1);high2.setText("↑"+MainActivity.high2);high3.setText("↑"+MainActivity.high3);high4.setText("↑"+MainActivity.high4);
        desc1.setText(MainActivity.desc1);desc2.setText(MainActivity.desc2);desc3.setText(MainActivity.desc3);desc4.setText(MainActivity.desc4);

        code1.setImageResource(MainActivity.code1);code2.setImageResource(MainActivity.code2);code3.setImageResource(MainActivity.code3);code4.setImageResource(MainActivity.code4);
    }


}
