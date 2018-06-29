package com.example.kamil.astroweather2;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GetWeather {

    public GetWeather(){

    }

    //this method is actually fetching the json string
    public void getWeatherInfo(final Context context, final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    MainActivity.weather=extractWeather(s);
                    saveFile(context,s, MainActivity.city.getName());
                    fillItems();
                    Log.d("myTag", "--------------------ZNALAZLEM POGODE");
                    MainActivity.fillData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    public static void fillItems(){
        File dir = MainActivity.context.getFilesDir();
        File[] subFiles = dir.listFiles();
        MainActivity.items.clear();
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
    protected static Weather extractWeather(String json) throws JSONException{
        Weather weather   = new Weather();
        JSONObject jsonObject = new JSONObject(json);
        jsonObject = jsonObject.getJSONObject("query");
        jsonObject = jsonObject.getJSONObject("results");
        jsonObject = jsonObject.getJSONObject("channel");

        weather.setWindSpeed(jsonObject.getJSONObject("wind").getString("speed"));
        weather.setWindDirection(jsonObject.getJSONObject("wind").getString("direction"));

        weather.setHumidity(jsonObject.getJSONObject("atmosphere").getString("humidity"));
        weather.setpressure(jsonObject.getJSONObject("atmosphere").getString("pressure"));
        weather.setVisibility(jsonObject.getJSONObject("atmosphere").getString("visibility"));

        jsonObject = jsonObject.getJSONObject("item");
        weather.setTemperature(jsonObject.getJSONObject("condition").getString("temp"));
        weather.setWeatherType(jsonObject.getJSONObject("condition").getString("text"));
        weather.setImageCurrent(jsonObject.getJSONObject("condition").getString("code"));
        JSONArray jsonArray=jsonObject.getJSONArray("forecast");

        weather.setCode1(jsonArray.getJSONObject(0).getString("code"));
        weather.setCode2(jsonArray.getJSONObject(1).getString("code"));
        weather.setCode3(jsonArray.getJSONObject(2).getString("code"));
        weather.setCode4(jsonArray.getJSONObject(3).getString("code"));
        weather.setDate1(jsonArray.getJSONObject(0).getString("date"));
        weather.setDate2(jsonArray.getJSONObject(1).getString("date"));
        weather.setDate3(jsonArray.getJSONObject(2).getString("date"));
        weather.setDate4(jsonArray.getJSONObject(3).getString("date"));
        weather.setDay1(jsonArray.getJSONObject(0).getString("day"));
        weather.setDay2(jsonArray.getJSONObject(1).getString("day"));
        weather.setDay3(jsonArray.getJSONObject(2).getString("day"));
        weather.setDay4(jsonArray.getJSONObject(3).getString("day"));
        weather.setDesc1(jsonArray.getJSONObject(0).getString("text"));
        weather.setDesc2(jsonArray.getJSONObject(1).getString("text"));
        weather.setDesc3(jsonArray.getJSONObject(2).getString("text"));
        weather.setDesc4(jsonArray.getJSONObject(3).getString("text"));
        weather.setHigh1(jsonArray.getJSONObject(0).getString("high"));
        weather.setHigh2(jsonArray.getJSONObject(1).getString("high"));
        weather.setHigh3(jsonArray.getJSONObject(2).getString("high"));
        weather.setHigh4(jsonArray.getJSONObject(3).getString("high"));
        weather.setLow1(jsonArray.getJSONObject(0).getString("low"));
        weather.setLow2(jsonArray.getJSONObject(1).getString("low"));
        weather.setLow3(jsonArray.getJSONObject(2).getString("low"));
        weather.setLow4(jsonArray.getJSONObject(3).getString("low"));

        return weather;
    }
    private void saveFile(Context context, String json, String filename){
        try {
            FileOutputStream fileOutputStream=context.getApplicationContext().openFileOutput(filename,Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void saveFile_city(Context context, String json, String filename){
        filename=filename+"_city";
        try {
            FileOutputStream fileOutputStream=context.getApplicationContext().openFileOutput(filename,Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openDataOrDownload(Context context, String fileName){
        File dir = context.getFilesDir();
        File myFile = new File(dir + "/"+fileName);
        File myFileCity = new File(dir + "/"+fileName+"_city");
        if(myFile.exists()){
            int length = (int) myFile.length();
            byte[] bytes = new byte[length];
            int lengthCity = (int) myFileCity.length();
            byte[] bytesCity = new byte[lengthCity];
            try {
                FileInputStream in = new FileInputStream(myFile);
                in.read(bytes);
                in.close();
                FileInputStream in2 = new FileInputStream(myFileCity);
                in2.read(bytesCity);
                in2.close();

                String contentsWeather = new String(bytes);
                Log.d("myTag", "--------------------ZNALAZLEM PLIK");
                String contentCity = new String(bytesCity);
                Log.d("myTag", "--------------------ZNALAZLEM PLIK MIASTA");
                MainActivity.weather=extractWeather(contentsWeather);
                if(!contentsWeather.isEmpty()) {
                    MainActivity.city = GetData.extractData(contentCity);
                    MainActivity.fillData();
                }
                Toast.makeText(context,"Zaczytano plik z pamięci", Toast.LENGTH_SHORT).show();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{

            MainActivity.getCityWoeid(fileName);
            Toast.makeText(context,"Pobieram dane z sieci", Toast.LENGTH_SHORT).show();
        }


    }
    public static void refreshFiles(Context context) {
        File dir = context.getFilesDir();
        File[] subFiles = dir.listFiles();
        Date currentDate = new Date();
        if (subFiles != null) {
            for (File file : subFiles) {
                if (!file.getName().contains("_city")&&!file.getName().equals("currentWeather")&&!file.getName().equals("currentCity"))  {
                    Date datamodyfikacji = new Date(file.lastModified());
                    long wiek = TimeUnit.MILLISECONDS.toMinutes(currentDate.getTime() - datamodyfikacji.getTime());
                    if (wiek >= 60) {
                        MainActivity.getCityWoeid(file.getName());
                    }
                }
            }
            Toast.makeText(context,"Aktualizuję pogodę z internetu", Toast.LENGTH_SHORT).show();
        }
    }


}
