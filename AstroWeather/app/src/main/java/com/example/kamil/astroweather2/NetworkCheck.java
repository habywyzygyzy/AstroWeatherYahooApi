package com.example.kamil.astroweather2;

import android.app.Application;
import android.widget.Toast;

import java.io.File;

public class NetworkCheck extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        MainActivity.context=getApplicationContext();
        if(MainActivity.isNetworkAvailable()){
            GetWeather.refreshFiles(MainActivity.context);
        }
        else{
            Toast.makeText(MainActivity.context,"Brak dostępu do internetu", Toast.LENGTH_SHORT).show();
        }
        File dir = MainActivity.context.getFilesDir();
        File[] subFiles = dir.listFiles();
        MainActivity.items.add("Dodaj nowe miasto");
        if (subFiles != null) {
            for (File file : subFiles) {
                //timeZone.setText(timeZone.getText()+file.getName());
                if(!file.getName().contains("_")&&!file.getName().equals("currentWeather")&&!file.getName().equals("currentCity")){
                    MainActivity.items.add(file.getName()) ;
                }
            }
        }

    }

}
