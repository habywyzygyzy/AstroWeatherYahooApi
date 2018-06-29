package com.example.kamil.astroweather2;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetData {

    public GetData(){

    }

    //this method is actually fetching the json string
    public void getWOEID(final Context context, final String urlWebService, final boolean checkOnly) {

        class GetJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    if(checkOnly){
                        City cityTemp=new City();
                        if(s != null) {
                            cityTemp=extractData(s);
                            MainActivity.items.add(cityTemp.getName());
                            MainActivity.showDialogBox(MainActivity.context);
                        }
                        else
                            Toast.makeText(context,"Takie miasto nie istnieje", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        MainActivity.city = extractData(s);
                        GetWeather.saveFile_city(context, s, MainActivity.city.getName());
                        Log.d("myTag", "--------------------ZNALAZLEM miasto");
                        MainActivity.getWeather(MainActivity.city.getWoeid());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Takie miasto nie istnieje", Toast.LENGTH_SHORT).show();
                    Log.d("myTag", "--------------------NIEZNALAZLEM miasta");
                    if(checkOnly){
                        MainActivity.showDialogBox(MainActivity.context);
                    }
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

    public static String extractWOEID(String json) throws JSONException{

        JSONObject jsonObject = new JSONObject(json);
        jsonObject = jsonObject.getJSONObject("query");
        jsonObject = jsonObject.getJSONObject("results");
        jsonObject = jsonObject.getJSONObject("place");
        String woeid = jsonObject.getString("woeid");

        return woeid;
    }

    public static City extractData(String json) throws JSONException{
        City city   = new City();
        if(json != null) {
            JSONObject jsonObject = new JSONObject(json);
            jsonObject = jsonObject.getJSONObject("query");
            jsonObject = jsonObject.getJSONObject("results");
            jsonObject = jsonObject.getJSONObject("place");

            city.setWoeid(jsonObject.getString("woeid"));
            city.setName(jsonObject.getString("name"));
            city.setLatitude(jsonObject.getJSONObject("centroid").getString("latitude"));
            city.setLongitude(jsonObject.getJSONObject("centroid").getString("longitude"));
            city.setTimeZoneID(jsonObject.getJSONObject("timezone").getString("content"));
        }

        return city;
    }



}