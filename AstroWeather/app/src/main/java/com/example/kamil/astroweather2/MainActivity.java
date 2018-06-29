package com.example.kamil.astroweather2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kamil.astroweather2.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;


public class MainActivity extends AppCompatActivity {
    public static double dlugosc=0.0;
    public static double szerokosc=0.0;
    public static int czestOdsw=5;
    public static TextView timeTextView;
    private boolean startTimer = true;
    private Menu menu;
//Weather
    public static City city;
    public static Weather weather;
    public static Context context;
    private static int pressureUnit;
    private static int temperatureUnit;
    private static int speedUnit;
    private static int distUnit;
    public static List<String> items = new ArrayList<String>();
//Weather INFO:
    public static String nazwa="";
    public static String description="";
    public static String latitude="";
    public static String longitude="";
    public static String textClock="";
    public static String temperature="";
    public static String pressure="";
    public static int pogodaID;
    public static String windDirection="";
    public static String windSpeed="";
    public static String humidity="";
    public static String visibility="";
//Prognoza
    public static String date1="",date2="",date3="",date4="";
    public static String day1="",day2="",day3="",day4="";
    public static int code1;
    public static int code2;
    public static int code3;
    public static int code4;
    public static String low1="",low2="",low3="",low4="";
    public static String high1="",high2="",high3="",high4="";
    public static String desc1="",desc2="",desc3="",desc4="";
    String zegar="";

//ViewPager
    private static SectionsPagerAdapter mSectionsPagerAdapter;
    private static SectionsPagerAdapterLandscape mSectionsPagerAdapterLandscape;
    private static SectionsPagerAdapterTablet mSectionsPagerAdapterTablet;
    private static ViewPager mViewPager;
    private static ViewPager mViewPager2;
    private static ViewPager mViewPager3;


    FragmentManager fragmentManagerTab1 = getSupportFragmentManager();
    FragmentManager fragmentManagerTab2 = getSupportFragmentManager();

//Fragmenty
    static Tab_Sun tab1 = new Tab_Sun();
    static Tab_Moon tab2 = new Tab_Moon();

    static Tab_Pogoda1 tab3 = new Tab_Pogoda1();
    static Tab_Pogoda2 tab4 = new Tab_Pogoda2();
    static Tab_Prognoza tab5 = new Tab_Prognoza();
    static Tabs_astro1 tabs1 = new Tabs_astro1();
    static Tabs_weather tabs2 = new Tabs_weather();

    static Tabs_Tablet tabs_tablet = new Tabs_Tablet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab3 = new Tab_Pogoda1();
        tab4 = new Tab_Pogoda2();
        tab5 = new Tab_Prognoza();
        tabs1 = new Tabs_astro1();
        tabs2 = new Tabs_weather();
        tabs_tablet = new Tabs_Tablet();
        loadActivity();
        reopenLastCity();
        loadUnitPrefs();

        if(savedInstanceState!=null)
        {
            zegar=savedInstanceState.getString("Zegar");
            if(savedInstanceState.getBoolean("weatherisNull")) {
                dlugosc = savedInstanceState.getDouble("dlugosc");
                szerokosc = savedInstanceState.getDouble("szerokosc");
                clean();
                if (isNetworkAvailable()) {
                    getCityWoeidFromCoordinates(String.valueOf(szerokosc), String.valueOf(dlugosc));
                }
                loadActivity();
            }
        }


        context=MainActivity.this;
        if(startTimer){t.start();startTimer=false;}
        t2.start();

    }





    public static double getLongitude() {
        return dlugosc;
    }
    public static double getLatitude() {
        return szerokosc;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private void loadActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        timeTextView=(TextView)findViewById(R.id.timeText);
        tab1=new Tab_Sun();
        tab2=new Tab_Moon();
        tabs1 = new Tabs_astro1();
        tabs2 = new Tabs_weather();
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager2 = (ViewPager) findViewById(R.id.containerLandscape);
        mViewPager3 = (ViewPager) findViewById(R.id.containerTablet);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (isTablet(context)){
                mSectionsPagerAdapterTablet = new SectionsPagerAdapterTablet(getSupportFragmentManager());

                mViewPager3.setAdapter(mSectionsPagerAdapterTablet);

            }else {
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                mViewPager.setAdapter(mSectionsPagerAdapter);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
            }
        }
        else {
            if (isTablet(context)){
                mSectionsPagerAdapterTablet = new SectionsPagerAdapterTablet(getSupportFragmentManager());
                mViewPager3.setAdapter(mSectionsPagerAdapterTablet);

            }else {
                mSectionsPagerAdapterLandscape = new SectionsPagerAdapterLandscape(getSupportFragmentManager());

                mViewPager2.setAdapter(mSectionsPagerAdapterLandscape);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

                mViewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager2));
            }
        }


    }

    private static void clean(){
        weather=null;
        tab3=new Tab_Pogoda1();
        tab4=new Tab_Pogoda2();
        tabs1=new Tabs_astro1();
        tabs2=new Tabs_weather();
        tabs_tablet = new Tabs_Tablet();
        timeTextView.setText("");
        if(mSectionsPagerAdapter!=null) {
            mSectionsPagerAdapter.notifyDataSetChanged();
            if(mViewPager!=null){
                int temp = mViewPager.getCurrentItem();
                mViewPager.setAdapter(mSectionsPagerAdapter);
                mViewPager.setCurrentItem(temp);
            }
        }
        if(mSectionsPagerAdapterLandscape!=null) {
            mSectionsPagerAdapterLandscape.notifyDataSetChanged();
            if(mViewPager2!=null) {
                int temp = mViewPager2.getCurrentItem();
                mViewPager2.setAdapter(mSectionsPagerAdapterLandscape);
                mViewPager2.setCurrentItem(temp);
            }
        }
        if(mSectionsPagerAdapterTablet!=null) {
            mSectionsPagerAdapterTablet.notifyDataSetChanged();
            if(mViewPager3!=null) {
                mViewPager3.setAdapter(mSectionsPagerAdapterTablet);
            }
        }
    }

    public static void fillData(){
        timeTextView.setText(city.getName());
        latitude=city.getLatitude();
        longitude=city.getLongitude();
        textClock=city.getTimeZoneID();
        temperature=convertTemperature(weather.getTemperature());
        pressure=convertPressure(weather.getpressure());
        description=weather.getWeatherType();
        pogodaID=context.getResources().getIdentifier("drawable/"+getWeatherIcon(weather.getImageCurrent()), null, context.getPackageName());
        windDirection=weather.getWindDirection()+"º";
        windSpeed=convertSpeed(weather.getWindSpeed());
        humidity=weather.getHumidity()+"%";
        visibility=convertDist(weather.getVisibility());
        day1=weather.getDay1();day2=weather.getDay2();day3=weather.getDay3();day4=weather.getDay4();
        date1=weather.getDate1();date2=weather.getDate2();date3=weather.getDate3();date4=weather.getDate4();
        low1=convertTemperature(weather.getLow1());low2=convertTemperature(weather.getLow2());low3=convertTemperature(weather.getLow3());low4=convertTemperature(weather.getLow4());
        high1=convertTemperature(weather.getHigh1());high2=convertTemperature(weather.getHigh2());high3=convertTemperature(weather.getHigh3());high4=convertTemperature(weather.getHigh4());
        desc1=weather.getDesc1();desc2=weather.getDesc2();desc3=weather.getDesc3();desc4=weather.getDesc4();

        code1=context.getResources().getIdentifier("drawable/"+getWeatherIcon(weather.getCode1()), null, context.getPackageName());
        code2=context.getResources().getIdentifier("drawable/"+getWeatherIcon(weather.getCode2()), null, context.getPackageName());
        code3=context.getResources().getIdentifier("drawable/"+getWeatherIcon(weather.getCode3()), null, context.getPackageName());
        code4=context.getResources().getIdentifier("drawable/"+getWeatherIcon(weather.getCode4()), null, context.getPackageName());

        dlugosc = Double.parseDouble(city.getLongitude());
        szerokosc = Double.parseDouble(city.getLatitude());
        tab1=new Tab_Sun();
        tab2=new Tab_Moon();
        tab3=new Tab_Pogoda1();
        tab4=new Tab_Pogoda2();
        tabs1=new Tabs_astro1();
        tabs2=new Tabs_weather();
        tabs_tablet=new Tabs_Tablet();

        if(mSectionsPagerAdapter!=null) {
            mSectionsPagerAdapter.notifyDataSetChanged();
            if(mViewPager!=null){
                int temp = mViewPager.getCurrentItem();
                mViewPager.setAdapter(mSectionsPagerAdapter);
                mViewPager.setCurrentItem(temp);
            }
        }
        if(mSectionsPagerAdapterLandscape!=null) {
            mSectionsPagerAdapterLandscape.notifyDataSetChanged();
            if(mViewPager2!=null) {
                int temp = mViewPager2.getCurrentItem();
                mViewPager2.setAdapter(mSectionsPagerAdapterLandscape);
                mViewPager2.setCurrentItem(temp);
            }
        }
        if(mSectionsPagerAdapterTablet!=null) {
            mSectionsPagerAdapterTablet.notifyDataSetChanged();
            if(mViewPager3!=null) {
                mViewPager3.setAdapter(mSectionsPagerAdapterTablet);
            }
        }
        if(Tab_Pogoda1.temperature!=null) {
            Tab_Pogoda1.fillInfo();
        }
        if(Tab_Pogoda2.windDirection!=null) {
            Tab_Pogoda2.fillInfo();
        }
        if(Tab_Prognoza.code1!=null) {
            Tab_Prognoza.fillInfo();
        }

    }

    public static void reloadUnits(){


        temperature=convertTemperature(weather.getTemperature());
        pressure=convertPressure(weather.getpressure());

        windSpeed=convertSpeed(weather.getWindSpeed());
        visibility=convertDist(weather.getVisibility());
        low1=convertTemperature(weather.getLow1());low2=convertTemperature(weather.getLow2());low3=convertTemperature(weather.getLow3());low4=convertTemperature(weather.getLow4());
        high1=convertTemperature(weather.getHigh1());high2=convertTemperature(weather.getHigh2());high3=convertTemperature(weather.getHigh3());high4=convertTemperature(weather.getHigh4());

        tab1=new Tab_Sun();
        tab2=new Tab_Moon();
        tab3=new Tab_Pogoda1();
        tab4=new Tab_Pogoda2();
        tabs1=new Tabs_astro1();
        tabs2=new Tabs_weather();
        if(mSectionsPagerAdapter!=null) {
            mSectionsPagerAdapter.notifyDataSetChanged();
            if(mViewPager!=null){
                int temp = mViewPager.getCurrentItem();
                mViewPager.setAdapter(mSectionsPagerAdapter);
                mViewPager.setCurrentItem(temp);
            }
        }
        if(mSectionsPagerAdapterLandscape!=null) {
            mSectionsPagerAdapterLandscape.notifyDataSetChanged();
            if(mViewPager2!=null) {;
                mViewPager2.setAdapter(mSectionsPagerAdapterLandscape);
            }
        }
        if(Tab_Pogoda1.temperature!=null) {
            Tab_Pogoda1.fillInfo();
        }
        if(Tab_Pogoda2.windDirection!=null) {
            Tab_Pogoda2.fillInfo();
        }
        if(Tab_Prognoza.code1!=null) {
            Tab_Prognoza.fillInfo();
        }
    }

    public static String getWeatherIcon(String input){
        int name=0;
        try {
            name=Integer.valueOf(input);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String file="";
        switch (name){
            case 0:
                file="weather_tornado";
                break;
            case 1:case 2:case 19:
                file="weather_hurricane";
                break;
            case 3: case 4: case 37: case 38: case 39: case 45: case 47:
                file="weather_cloud_lightning";
                break;
            case 5:case 6:case 7: case 18:case 35:case 42:
                file="weather_mixedrain";
                break;
            case 8:case 9:
                file="weather_drizzle";
                break;
            case 10: case 11: case 12: case 40:
                file="weather_cloud_rain";
                break;
            case 13: case 14:case 15:case 16:case 41: case 43:case 46:
                file="weather_cloud_snow";
                break;
            case 17:
                file="weather_cloud_hail";
                break;
            case 20: case 21: case 22:
                file="weather_cloud_fog";
                break;
            case 23: case 24:
                file="weather_wind";
                break;
            case 25:
                file="weather_cold";
                break;
            case 26: case 27: case 28: case 44:
                file="weather_cloud";
                break;
            case 29: case 30:
                file="weather_cloud_sun";
                break;
            case 31: case 33:
                file="weather_moon";
                break;
            case 32:case 34:
                file="weather_sun";
                break;
            case 36:
                file="weather_hot";
                break;
            default:break;
        }
        return file;
    }

//Weather stuff
    public static void getCityWoeid(String lokalizacja) {
        String YQL = String.format("Select * from geo.places(1) where text='%s'",lokalizacja);
        String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        GetData getData = new GetData();
        getData.getWOEID(context,endpoint, false);
    }

    public static void getCityWoeidFromCoordinates(String lat, String lon) {
        String YQL = String.format("SELECT * FROM geo.places WHERE text='(%s1,%s2)'",lat, lon);
        String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        GetData getData = new GetData();
        getData.getWOEID(context,endpoint, false);
    }

    public static void checkifExists(String lokalizacja) {
        String YQL = String.format("Select * from geo.places(1) where text='%s'",lokalizacja);
        String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        GetData getData = new GetData();
        getData.getWOEID(context,endpoint, true);
    }

    public static void getWeather(String woeid) {
        String YQL = String.format("select * from weather.forecast where woeid=%s",woeid);
        String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        GetWeather  getWeather= new GetWeather();
        getWeather.getWeatherInfo(context,endpoint);
    }

    public static boolean isNetworkAvailable() {
        boolean outcome = false;
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            for (NetworkInfo tempNetworkInfo : networkInfos) {
                if (tempNetworkInfo.isConnected()) {
                    outcome = true;
                    break;
                }
            }
        }
        return outcome;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

            this.menu=menu;
             zegar=(DateFormat.format("kk:mm:ss", Calendar.getInstance().getTime())).toString();
            MenuItem clock = (MenuItem)this.menu.findItem(R.id.clock);
            clock.setTitle(zegar);
            MenuItem myItem = menu.findItem(R.id.freq);
            myItem.setTitle("Czestotliwość: "+czestOdsw+" min");

        return super.onPrepareOptionsMenu(menu);
    }

    public void closeApp(View v){
        finishAndRemoveTask();
    }
////////////////////////////////////////////////menu/////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.cities) {

            showDialogBox(MainActivity.this);
        }
        if (id == R.id.units) {

            showUnitBox();
        }
        if (id == R.id.lokalizacja) {

            AlertDialog.Builder wspolrzedne = new AlertDialog.Builder(MainActivity.this);
            wspolrzedne.setTitle("Współrzędne");
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.wspolrzedne, null);

            wspolrzedne.setView(dialogView);

            final EditText textDlugosc = (EditText) dialogView.findViewById(R.id.dlugoscX);
            final EditText textSzerokosc = (EditText) dialogView.findViewById(R.id.szerokoscX);
            wspolrzedne.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    if (textDlugosc != null && textSzerokosc != null) {
                        if(textDlugosc.getText().toString().matches("")){
                            textDlugosc.setText("0");
                        }
                        if(textSzerokosc.getText().toString().matches("")){
                            textSzerokosc.setText("0");
                        }
                        dlugosc = Double.parseDouble(textDlugosc.getText().toString());
                        szerokosc = Double.parseDouble(textSzerokosc.getText().toString());
                    }
                    clean();
                    if(isNetworkAvailable()) {
                        getCityWoeidFromCoordinates(String.valueOf(szerokosc), String.valueOf(dlugosc));
                    }
                    loadActivity();
                }
            });
            wspolrzedne.show();
        }

        if (id == R.id.freq) {
            showFreq(MainActivity.this);

        }
        if (id == R.id.exit) {
            closeApp(getWindow().getDecorView().getRootView());
        }

        return super.onOptionsItemSelected(item);
    }

    public static void showFreq(final Context context2) {
        final EditText freq_input = new EditText(context2);
        freq_input.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder freq = new AlertDialog.Builder(context2);
        freq_input.setHint("minuty");
        freq.setMessage("Podaj częstotliwość odświeżenia");
        freq.setTitle("Częstotliwość");
        freq.setView(freq_input);
        freq.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                if(!freq_input.getText().toString().equals("")) {
                    czestOdsw = Integer.parseInt(freq_input.getText().toString());
                }
            }
        });
        freq.show();
    }

    public static void showDialogBox(final Context context1) {
        final AlertDialog.Builder build = new AlertDialog.Builder(context1);
        build.setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items.get(which).equals("Dodaj nowe miasto")){

                    final EditText city_input = new EditText(context1);
                    city_input.setText("");
                    city_input.setHint("miasto,pl");
                    final AlertDialog.Builder city = new AlertDialog.Builder(context1);
                    city.setMessage("Wpisz nazwę miasta i skrót państwa");
                    city.setTitle("Dodawanie miasta");
                    city.setView(city_input);
                    city.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //if (city_input != null){
                                if(isNetworkAvailable()){
                                    checkifExists(city_input.getText().toString());
                                }
                                else if (city_input.getText().toString().isEmpty()) {
                                    Toast.makeText(context,"Nie ma takiego miasta", Toast.LENGTH_SHORT).show();
                                    showDialogBox(context);
                                }
                                else{
                                    Toast.makeText(context,"Brak dostępu do internetu", Toast.LENGTH_SHORT).show();
                                    showDialogBox(context);
                                }
                            //}
                        }
                    });
                    city.show();
                }
                else{
                    GetWeather.openDataOrDownload(context, items.get(which));
                }
            }
        });
        final AlertDialog ad = build.create();
        ad.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                final ListView lv = ad.getListView(); //this is a ListView with your "buds" in it
                lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Toast.makeText(MainActivity.context, "Usunięto miasto: "+lv.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                        deleteCity(lv.getItemAtPosition(position).toString());
                        dlugosc=0.0;
                        szerokosc=0.0;
                        GetWeather.fillItems();
                        clean();
                        ad.dismiss();
                        MainActivity.showDialogBox(MainActivity.context);
                        return true;
                    }
                });
            }
        });
        ad.show();
    }

    private void showUnitBox(){
        AlertDialog.Builder jednostki = new AlertDialog.Builder(MainActivity.this);
        jednostki.setTitle("Jednostki");
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.jednostki, null);

        jednostki.setView(dialogView);
        final ToggleSwitch pressureSwitch = (ToggleSwitch) dialogView.findViewById(R.id.pressureSwitch);
        ArrayList<String> pressure = new ArrayList<>();
        pressure.add("inHG");
        pressure.add("hPa");
        pressureSwitch.setLabels(pressure);
        pressureSwitch.setCheckedTogglePosition(pressureUnit);
        pressureSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                pressureUnit=position;
            }
        });

        final ToggleSwitch temperatureSwitch = (ToggleSwitch) dialogView.findViewById(R.id.temperatureSwitch);
        ArrayList<String> temperature = new ArrayList<>();
        temperature.add("F");
        temperature.add("C");
        temperatureSwitch.setLabels(temperature);
        temperatureSwitch.setCheckedTogglePosition(temperatureUnit);
        temperatureSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                temperatureUnit=position;
            }
        });

        final ToggleSwitch speedSwitch = (ToggleSwitch) dialogView.findViewById(R.id.speedSwitch);
        ArrayList<String> speed = new ArrayList<>();
        speed.add("mph");
        speed.add("kmh");
        speedSwitch.setLabels(speed);
        speedSwitch.setCheckedTogglePosition(speedUnit);
        speedSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                speedUnit=position;
            }
        });

        final ToggleSwitch distSwitch = (ToggleSwitch) dialogView.findViewById(R.id.distSwitch);
        ArrayList<String> distance = new ArrayList<>();
        distance.add("mi");
        distance.add("km");
        distSwitch.setLabels(distance);
        distSwitch.setCheckedTogglePosition(distUnit);
        distSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){
            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                distUnit=position;
            }
        });
        jednostki.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(weather!=null) {
                    reloadUnits();
                }
            }
        });
        jednostki.show();
    }

    public void refreshButton(MenuItem item){
        if(MainActivity.isNetworkAvailable()){
            GetWeather.refreshFiles(MainActivity.context);
        }
        else{
            Toast.makeText(MainActivity.context,"Brak dostępu do internetu", Toast.LENGTH_SHORT).show();
        }
    }

///////////////////////////////////////////Obsluga ViewPagera///////////////////////////////////
    class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return tab1;
                case 1:
                    return tab2;
                case 2:
                    return tab3;
                case 3:
                    return tab4;
                case 4:
                    return tab5;
                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Słońce";
                case 1:
                    return "Księżyc";
                case 2:
                    return "Pogoda 1";
                case 3:
                    return "Pogoda 2";
                case 4:
                    return "Prognoza";
            }
            return null;
        }
    }
    class SectionsPagerAdapterLandscape extends FragmentPagerAdapter {

        public SectionsPagerAdapterLandscape(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return tabs1;
                case 1:
                    return tabs2;
                case 2:
                    return tab5;
                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Astro";
                case 1:
                    return "Weather";
                case 2:
                    return "Prognoza";
            }
            return null;
        }
    }
    class SectionsPagerAdapterTablet extends FragmentPagerAdapter {

        public SectionsPagerAdapterTablet(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return tabs_tablet;
                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
            }
            return null;
        }
    }
///////////////////////////////////////////Zapis i odczyt z pliku////////////////////////////////
    public void saveUnitPrefs(){
        SharedPreferences sharedPreferences=getSharedPreferences("userUnits",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("pressureUnit",pressureUnit);
        editor.putInt("tempUnit",temperatureUnit);
        editor.putInt("speedUnit",speedUnit);
        editor.putInt("distUnit",distUnit);
        editor.putInt("czestOdsw",czestOdsw);
        editor.apply();
    }
    public void loadUnitPrefs(){
        SharedPreferences sharedPreferences=getSharedPreferences("userUnits",Context.MODE_PRIVATE);
        pressureUnit=sharedPreferences.getInt("pressureUnit",0);
        temperatureUnit=sharedPreferences.getInt("tempUnit",0);
        speedUnit=sharedPreferences.getInt("speedUnit",0);
        distUnit=sharedPreferences.getInt("distUnit",0);
        czestOdsw=sharedPreferences.getInt("czestOdsw",5);
    }

    public void saveCurrentWeather(){
        if(weather!=null) {
            Gson gson = new Gson();
            String weatherJson = gson.toJson(weather);
            try {
                FileOutputStream fileOutputStream = MainActivity.this.openFileOutput("currentWeather", Context.MODE_PRIVATE);
                fileOutputStream.write(weatherJson.getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void saveCurrentCity(){
        if(weather!=null) {
            Gson gson = new Gson();
            String cityJson = gson.toJson(city);
            try {
                FileOutputStream fileOutputStream = MainActivity.this.openFileOutput("currentCity", Context.MODE_PRIVATE);
                fileOutputStream.write(cityJson.getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Weather openCurrentWeather(){
        File dir = MainActivity.this.getFilesDir();
        File weatherFile = new File(dir + "/currentWeather");
        String contents="";
        if(weatherFile.exists()){
            try {
                int length = (int) weatherFile.length();
                byte[] bytes = new byte[length];
                FileInputStream in = new FileInputStream(weatherFile);
                in.read(bytes);
                in.close();
                contents = new String(bytes);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        Gson gson = new Gson();
        Weather weather = gson.fromJson(contents, Weather.class);
        return weather;
    }
    public City openCurrentCity(){
        File dir = MainActivity.this.getFilesDir();
        File cityFile = new File(dir + "/currentCity");
        String contents="";
        if(cityFile.exists()){
            try {
                int length = (int) cityFile.length();
                byte[] bytes = new byte[length];
                FileInputStream in = new FileInputStream(cityFile);
                in.read(bytes);
                in.close();
                contents = new String(bytes);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        Gson gson = new Gson();
        City city = gson.fromJson(contents, City.class);
        return city;
    }

    private static void deleteCity(String filename) {
        File dir = context.getFilesDir();
        File myWeather = new File(dir + "/" + filename);
        File myCurrentWeather = new File(dir + "/currentWeather");
        File myCurrentCity = new File(dir + "/currentCity");
        File myCity = new File(dir + "/" + filename+"_city");
        myWeather.delete();
        myCity.delete();
        myCurrentWeather.delete();
        myCurrentCity.delete();
    }
    private void reopenLastCity() {
        File dir = MainActivity.this.getFilesDir();
        File weatherFile = new File(dir + "/currentWeather");
        if(weatherFile.exists()){
            weather=openCurrentWeather();
            city=openCurrentCity();
            fillData();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveUnitPrefs();
        if(weather!=null) {
            saveCurrentWeather();
            saveCurrentCity();
        }
    }
///////////////////////////////////////////conversion/////////////////////////////////
    private static String convertPressure(String input){
        if(pressureUnit==0) {
            double value = Double.parseDouble(input)/33.86;//inHG
            return String.format("%.2f", value)+"inHg";
        }
        else{
            return input+"hPa";//hPA
        }
    }
    private static String convertTemperature(String input){
        if(temperatureUnit==0) {
            return input+"ºF";//F
        }
        else{
            double value = ((Double.parseDouble(input)-32)*5)/9;//C
            return String.format("%.0f", value)+"ºC";
        }
    }
    private static String convertSpeed(String input){
        if(speedUnit==0) {
            return input+"mph";//mph
        }
        else{
            double value = Double.parseDouble(input)*1.60934;//kmh
            return String.format("%.0f", value)+"kmh";
        }
    }
    private static String convertDist(String input){
        if(distUnit==0) {
            return input+"mi";//m
        }
        else{
            double value = Double.parseDouble(input)*1.60934;//km
            return String.format("%.0f", value)+"km";
        }
    }


///////////////////////////////////////////Zapis i odczyt podczas obrotu///////////////////////
@Override
public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    MenuItem clock = menu.findItem(R.id.clock);
    savedInstanceState.putString("Zegar", clock.getTitle().toString());
    savedInstanceState.putDouble("dlugosc", dlugosc);
    savedInstanceState.putDouble("szerokosc",szerokosc);
    savedInstanceState.putBoolean("weatherisNull",weather==null);
};

/////////////////////////////////////////////Wątki////////////////////////////////////////////
    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MenuItem clock = menu.findItem(R.id.clock);
                            clock.setTitle(DateFormat.format("kk:mm:ss", Calendar.getInstance().getTime()));
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };
    Thread t2 = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    TimeUnit.MINUTES.sleep(czestOdsw);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadActivity();
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };
}