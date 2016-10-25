package com.pi.stela.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.pi.stela.R;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;

import com.pi.stela.sensors.*;
import com.pi.stela.graphics.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.UserDictionary;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnInitListener {

    private TextToSpeech mTts;
    private boolean TTSInitialized = false;
    private boolean previouslyInitialized = false;
    public static MainActivity MN = null;
    public static StelaActivity SA = null;
    public static BackgroundAnimator BA = null;
    public static WeatherController MC;
    private PagerAdapter mPagerAdapter;
    private RelativeLayout MainStelaRL;
    public static double S;
    public static double W;
    public static double H;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MN = MainActivity.this;
        BA = new BackgroundAnimator();
        MC = new WeatherController();
        SA = new StelaActivity();

        Uri mSmsinboxQueryUri = Uri.parse("content://sms");
        Cursor cursor1 = getContentResolver().query(mSmsinboxQueryUri, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, null, null, null);
        startManagingCursor(cursor1);
        String[] columns = new String[]{"address", "person", "date", "body", "type"};
        if (cursor1.getCount() > 0) {
            String count = Integer.toString(cursor1.getCount());
            while (cursor1.moveToNext()) {
                ArrayList<String> ObjectSMS = new ArrayList<String>();
                String address = cursor1.getString(cursor1.getColumnIndex(columns[0]));
                String name = cursor1.getString(cursor1.getColumnIndex(columns[1]));
                String date = cursor1.getString(cursor1.getColumnIndex(columns[2]));
                String msg = cursor1.getString(cursor1.getColumnIndex(columns[3]));
                String type = cursor1.getString(cursor1.getColumnIndex(columns[4]));
               /*Toast.makeText(this, address + " " + name + " " + date + " " + msg + " " + type + " " + getContactName(
                       getApplicationContext(),
                       cursor1.getString(cursor1
                               .getColumnIndexOrThrow("address"))), Toast.LENGTH_SHORT).show();*/
                String ContactName = getContactName(
                        getApplicationContext(),
                        cursor1.getString(cursor1
                                .getColumnIndexOrThrow("address")));
                String number = getContactNumber(
                        getApplicationContext(),
                        cursor1.getString(cursor1
                                .getColumnIndexOrThrow("address")));
                ObjectSMS.add(date);
                ObjectSMS.add(msg);
                ObjectSMS.add(type);
                if (ContactName == null) {
                    ContactName = address;
                    ObjectSMS.add(ContactName);
                    ObjectSMS.add(number);
                    SMSActivity.SMS.add(ObjectSMS);
                    if (!SMSActivity.Contacts.contains(ContactName)) {
                        SMSActivity.Contacts.add(ContactName);
                    }
                } else {
                    ObjectSMS.add(ContactName);
                    ObjectSMS.add(number);
                    SMSActivity.SMS.add(ObjectSMS);
                    if (!SMSActivity.Contacts.contains(ContactName)) {
                        SMSActivity.Contacts.add(ContactName);
                    }
                }
                //Toast.makeText(MainActivity.this, "type: " + type + "Call to number: " + ContactName + " " + msg, Toast.LENGTH_SHORT).show();
            }
        }
		/*String[] colums = new String[]{
				CallLog.Calls._ID,
				CallLog.Calls.NUMBER,
				CallLog.Calls.DATE,
				CallLog.Calls.DURATION,
				CallLog.Calls.TYPE};*/
        Cursor c;
        Uri mCallsinboxQueryUri = Uri.parse("content://call_log/calls");
        c = getContentResolver().query(mCallsinboxQueryUri, new String[]{"_id", "number", "date", "duration", "type"}, null, null, null);
        startManagingCursor(c);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String date = c.getString(2);
                String number = c.getString(1);
                String type = c.getString(4);
                ArrayList<String> CallDetails = new ArrayList<String>();
                String ContactName = getContactName(
                        getApplicationContext(), number);
                if (ContactName == null) {
                    ContactName = number;
                    CallDetails.add(ContactName);
                    CallDetails.add(date);
                    CallDetails.add(type);
                    CallDetails.add(number);
                    AppelsActivity.CallsLog.add(CallDetails);
                } else {
                    CallDetails.add(ContactName);
                    CallDetails.add(date);
                    CallDetails.add(type);
                    CallDetails.add(number);
                    AppelsActivity.CallsLog.add(CallDetails);
                }
                //Log.i("CallLog", "type: " + c.getString(4) + "Call to number: " + c.getString(1) + ", registered at: " + new Date(date).toString());
                //Toast.makeText(MainActivity.this, "type: " + c.getString(4) + "Call to number: " + c.getString(1), Toast.LENGTH_SHORT).show();
            }
        }
		/*
		//Détecter la luminosité ambiente
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		SensorManager  sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	    Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	    SensorEventListener listener = new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
				double mLightQuantity = event.values[0];
				//Contrôler la luminosité de l'écran
				WindowManager.LayoutParams layout = getWindow().getAttributes();
				if(mLightQuantity >= 0){
					layout.screenBrightness = (float) ((0.000000002154435 * (Math.exp(0.092103403719762 * (mLightQuantity + 200)))));
					getWindow().setAttributes(layout);
				}else{
					layout.screenBrightness = 0;
					getWindow().setAttributes(layout);
				}
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
	    };
	    sensorManager.registerListener(
	            listener, lightSensor, SensorManager.SENSOR_DELAY_UI);*/

        //Contrôle e la présence de la synthèse vocal
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, 0x01);
        UserDictionary.Words.addWord(this, "Stela", 10000, "Stela", Locale.getDefault());
		/*DbMeasurer DBM = new DbMeasurer();
		DBM.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);*/

        //Initialisation des ViewPagers
        final ViewPager MainStelaVP = (ViewPager) findViewById(R.id.StelaMainViewPager1);

        //Initialisation des Layouts
        RelativeLayout MainStelaRL = (RelativeLayout) findViewById(R.id.StelaMainRelativeLayout1);

        //Configuration des Layouts
        MainStelaRL.setFocusableInTouchMode(true);
        MainStelaRL.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        //Animation des Layouts
        BA.AnimateBackground(MainStelaRL, MainActivity.this);

        //Initialisation des polices
        Typeface RobotoFont = Typeface.createFromAsset(getAssets(), "robotoregular.ttf");
        Typeface weatherFont = Typeface.createFromAsset(MN.getAssets(), "weather.ttf");

        //Initialisation du nombre d'or
        final double GoldNumber = (double) ((1 + Math.sqrt(5)) / 2);

        //Instanciation de la classe Proportion et initialisation des proportions
        Proportions Proportions = new Proportions();
        H = Proportions.HProportion(getWindowManager());
        W = Proportions.WProportion(getWindowManager());
        S = Proportions.SProportion(getWindowManager());

		/*
		 *
		 * 								TextViews
		 *
		 */

        //Initialisation des TextViews
        TextView TimeDisplay = new TextView(MainActivity.this);
        TextView DateDisplay = new TextView(MainActivity.this);
        final TextView TemperatureDisplay = new TextView(MainActivity.this);
        final TextView HumidityDisplay = new TextView(MainActivity.this);
        final TextView WeatherDisplay = new TextView(MainActivity.this);

        //Récupération de la date et de l'heure actuelle
        Calendar MainCalendar = Calendar.getInstance();
        SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat DateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
        String CurrentTime = TimeFormat.format(MainCalendar.getTime());
        String CurrentDate = DateFormat.format(MainCalendar.getTime());

        //Récupération de la température et du taux d'humidité
        String CurrentTemperature = TemperatureSensor.CurrentTemperature;
        String CurrentHumidity = HumiditySensor.CurrentHumidity;

        //Application de leur texte aux TextViews
        TimeDisplay.setText(CurrentTime);
        DateDisplay.setText(CurrentDate);
        TemperatureDisplay.setText(CurrentTemperature);
        HumidityDisplay.setText(CurrentHumidity);
        WeatherDisplay.setText(this.getString(R.string.weather_sunny));

        //Application de leur police aux TextViews
        TimeDisplay.setTypeface(RobotoFont);
        DateDisplay.setTypeface(RobotoFont);
        TemperatureDisplay.setTypeface(RobotoFont);
        HumidityDisplay.setTypeface(RobotoFont);
        WeatherDisplay.setTypeface(weatherFont);

        //Application de la taille de leur police aux TextViews
        TimeDisplay.setTextSize((float) (GoldNumber * 100 * S));
        DateDisplay.setTextSize((float) (GoldNumber * 30 * S));
        TemperatureDisplay.setTextSize((float) (GoldNumber * 40 * S));
        HumidityDisplay.setTextSize((float) (GoldNumber * 40 * S));
        WeatherDisplay.setTextSize((float) (GoldNumber * 50 * S));

        //Application de la couleur de la police des TextViews
        TimeDisplay.setTextColor(Color.parseColor("#FFFFFF"));
        DateDisplay.setTextColor(Color.parseColor("#FFFFFF"));
        TemperatureDisplay.setTextColor(Color.parseColor("#FFFFFF"));
        HumidityDisplay.setTextColor(Color.parseColor("#FFFFFF"));
        WeatherDisplay.setTextColor(Color.parseColor("#FFFFFF"));

        //Initialisation des paramètres des TextViews
        RelativeLayout.LayoutParams TimeDisplayParams = new
                RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (200 * GoldNumber * H));
        RelativeLayout.LayoutParams DateDisplayParams = new
                RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (100 * GoldNumber * H));
        RelativeLayout.LayoutParams TemperatureDisplayParams = new
                RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (75 * GoldNumber * H));
        RelativeLayout.LayoutParams HumidityDisplayParams = new
                RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (75 * GoldNumber * H));
        RelativeLayout.LayoutParams WeatherDisplayParams = new
                RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (75 * GoldNumber * H));

        //Application des marges aux paramètres des TextViews
        TimeDisplayParams.setMargins(0, (int) (25 * GoldNumber * H), 0, 0);
        DateDisplayParams.setMargins(0, (int) (150 * GoldNumber * H), 0, 0);
        TemperatureDisplayParams.setMargins((int) (Proportions.Width / 4), (int) (195 * GoldNumber * H), 0, 0);
        HumidityDisplayParams.setMargins((int) (Proportions.Width / 2), (int) (195 * GoldNumber * H), (int) ((Proportions.Width / 4) - (10 * W)), 0);
        WeatherDisplayParams.setMargins(0, (int) (190 * GoldNumber * H), 0, 0);

        //Application de leur paramètre aux TextViews
        TimeDisplay.setLayoutParams(TimeDisplayParams);
        DateDisplay.setLayoutParams(DateDisplayParams);
        TemperatureDisplay.setLayoutParams(TemperatureDisplayParams);
        HumidityDisplay.setLayoutParams(HumidityDisplayParams);
        WeatherDisplay.setLayoutParams(WeatherDisplayParams);

        //Application de la gravité dans les TextViews
        TimeDisplay.setGravity(Gravity.CENTER_HORIZONTAL);
        DateDisplay.setGravity(Gravity.CENTER_HORIZONTAL);
        TemperatureDisplay.setGravity(Gravity.START);
        HumidityDisplay.setGravity(Gravity.END);
        WeatherDisplay.setGravity(Gravity.CENTER);

        //Mise en place des TextViews dans le Layout principal
        MainStelaRL.addView(TimeDisplay);
        MainStelaRL.addView(DateDisplay);
        MainStelaRL.addView(TemperatureDisplay);
        MainStelaRL.addView(HumidityDisplay);
        MainStelaRL.addView(WeatherDisplay);

        //Application du contrôleur du temps aux TextViews
        ClockController TimeController = new ClockController(TimeDisplay);
        TimeController.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		/*
		 *
		 *								ImageViews
		 *
		 */

        //Initialisation des ImageViews
        final ImageView MenuDisplay = new ImageView(MainActivity.this);

        //Distribution de leur image aux ImageViews
        MenuDisplay.setImageResource(R.drawable.bar_menu);

        //Application des paramètres de contenance des images aux ImageViews
        MenuDisplay.setScaleType(ScaleType.FIT_CENTER);

        //Initialisation des paramètres des ImageViews
        RelativeLayout.LayoutParams MenuDisplayParams = new
                RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), (int) (60 * GoldNumber * H));

        //Application des marges aux paramètres des ImageViews
        MenuDisplayParams.setMargins(0, (int) (300 * GoldNumber * H), 0, 0);

        //Application des règles aux paramètres
        MenuDisplayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        //Application de leur paramètre aux ImageViews
        MenuDisplay.setLayoutParams(MenuDisplayParams);

        //Mise en place des ImageViews dans le Layout principal
        MainStelaRL.addView(MenuDisplay);

		/*
		 *
		 *								Buttons
		 *
		 */

        //Initialisation des Buttons
        final Button MenuStelaDisplay = new Button(MainActivity.this);
        final Button MenuSMSDisplay = new Button(MainActivity.this);
        final Button MenuAppelsDisplay = new Button(MainActivity.this);
        final Button MenuAgendaDisplay = new Button(MainActivity.this);

        //Application des Backgrounds aux Buttons
        MenuStelaDisplay.setBackgroundColor(Color.TRANSPARENT);
        MenuSMSDisplay.setBackgroundColor(Color.TRANSPARENT);
        MenuAppelsDisplay.setBackgroundColor(Color.TRANSPARENT);
        MenuAgendaDisplay.setBackgroundColor(Color.TRANSPARENT);

        //Distribution de leur texte aux Buttons
        MenuStelaDisplay.setText("Stela");
        MenuSMSDisplay.setText("SMS");
        MenuAppelsDisplay.setText("Appels");
        MenuAgendaDisplay.setText("Agenda");

        //Application des polices aux Buttons
        MenuStelaDisplay.setTypeface(RobotoFont);
        MenuSMSDisplay.setTypeface(RobotoFont);
        MenuAppelsDisplay.setTypeface(RobotoFont);
        MenuAgendaDisplay.setTypeface(RobotoFont);

        //Application des tailles des polices aux Buttons
        MenuStelaDisplay.setTextSize((float) (GoldNumber * 30 * S));
        MenuSMSDisplay.setTextSize((float) (GoldNumber * 30 * S));
        MenuAppelsDisplay.setTextSize((float) (GoldNumber * 30 * S));
        MenuAgendaDisplay.setTextSize((float) (GoldNumber * 30 * S));

        //Application des couleurs de polices aux Buttons
        MenuStelaDisplay.setTextColor(Color.parseColor("#FFFFFF"));
        MenuSMSDisplay.setTextColor(Color.parseColor("#FFFFFF"));
        MenuAppelsDisplay.setTextColor(Color.parseColor("#FFFFFF"));
        MenuAgendaDisplay.setTextColor(Color.parseColor("#FFFFFF"));

        //Application de la gravité dans les Buttons
        MenuStelaDisplay.setGravity(Gravity.CENTER);
        MenuSMSDisplay.setGravity(Gravity.CENTER);
        MenuAppelsDisplay.setGravity(Gravity.CENTER);
        MenuAgendaDisplay.setGravity(Gravity.CENTER);

        //Application des marges (paddings) aux buttons
        MenuStelaDisplay.setPadding(0, 0, 0, 0);
        MenuSMSDisplay.setPadding(0, 0, 0, 0);
        MenuAppelsDisplay.setPadding(0, 0, 0, 0);
        MenuAgendaDisplay.setPadding(0, 0, 0, 0);

        //Initialisation des paramètres des Buttons
        RelativeLayout.LayoutParams MenuStelaDisplayParams = new
                RelativeLayout.LayoutParams((int) (150 * GoldNumber * W), (int) (60 * GoldNumber * H));
        RelativeLayout.LayoutParams MenuSMSDisplayParams = new
                RelativeLayout.LayoutParams((int) (150 * GoldNumber * W), (int) (60 * GoldNumber * H));
        RelativeLayout.LayoutParams MenuAppelsDisplayParams = new
                RelativeLayout.LayoutParams((int) (150 * GoldNumber * W), (int) (60 * GoldNumber * H));
        RelativeLayout.LayoutParams MenuAgendaDisplayParams = new
                RelativeLayout.LayoutParams((int) (151 * GoldNumber * W), (int) (60 * GoldNumber * H));

        //Application des marges aux paramètres des Buttons
        MenuStelaDisplayParams.setMargins((int) ((Proportions.Width - (600 * GoldNumber * W)) / 2), (int) (300 * GoldNumber * H), 0, 0);
        MenuSMSDisplayParams.setMargins((int) (((Proportions.Width - (600 * GoldNumber * W)) / 2) + 1 * 150 * GoldNumber * W), (int) (300 * GoldNumber * H), 0, 0);
        MenuAppelsDisplayParams.setMargins((int) (((Proportions.Width - (600 * GoldNumber * W)) / 2) + 2 * 150 * GoldNumber * W), (int) (300 * GoldNumber * H), 0, 0);
        MenuAgendaDisplayParams.setMargins((int) (((Proportions.Width - (600 * GoldNumber * W)) / 2) + 3 * 150 * GoldNumber * W), (int) (300 * GoldNumber * H), 0, 0);

        //Application de leur paramètre aux Buttons
        MenuStelaDisplay.setLayoutParams(MenuStelaDisplayParams);
        MenuSMSDisplay.setLayoutParams(MenuSMSDisplayParams);
        MenuAppelsDisplay.setLayoutParams(MenuAppelsDisplayParams);
        MenuAgendaDisplay.setLayoutParams(MenuAgendaDisplayParams);

        //Application des OnClickListener aux Buttons
        MenuStelaDisplay.setBackgroundResource(R.drawable.bar_menu_selected);
        MenuStelaDisplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainStelaVP.setCurrentItem(0);
                MenuStelaDisplay.setBackgroundResource(R.drawable.bar_menu_selected);
                MenuSMSDisplay.setBackgroundColor(Color.TRANSPARENT);
                MenuAppelsDisplay.setBackgroundColor(Color.TRANSPARENT);
                MenuAgendaDisplay.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        MenuSMSDisplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainStelaVP.setCurrentItem(1);
                MenuSMSDisplay.setBackgroundResource(R.drawable.bar_menu_selected);
                MenuStelaDisplay.setBackgroundColor(Color.TRANSPARENT);
                MenuAppelsDisplay.setBackgroundColor(Color.TRANSPARENT);
                MenuAgendaDisplay.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        MenuAppelsDisplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainStelaVP.setCurrentItem(2);
                MenuAppelsDisplay.setBackgroundResource(R.drawable.bar_menu_selected);
                MenuStelaDisplay.setBackgroundColor(Color.TRANSPARENT);
                MenuSMSDisplay.setBackgroundColor(Color.TRANSPARENT);
                MenuAgendaDisplay.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        MenuAgendaDisplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainStelaVP.setCurrentItem(3);
                MenuAgendaDisplay.setBackgroundResource(R.drawable.bar_menu_selected);
                MenuStelaDisplay.setBackgroundColor(Color.TRANSPARENT);
                MenuSMSDisplay.setBackgroundColor(Color.TRANSPARENT);
                MenuAppelsDisplay.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        //Mise en place des Buttons dans le Layout principal
        MainStelaRL.addView(MenuStelaDisplay);
        MainStelaRL.addView(MenuSMSDisplay);
        MainStelaRL.addView(MenuAppelsDisplay);
        MainStelaRL.addView(MenuAgendaDisplay);

		/*
		 *
		 * 								Fragments
		 *
		 */

        // Création de la liste de Fragments que fera défiler le PagerAdapter
        List fragments = new Vector();

        // Ajout des Fragments dans la liste
        fragments.add(Fragment.instantiate(MainActivity.this, StelaActivity.class.getName()));
        fragments.add(Fragment.instantiate(MainActivity.this, SMSActivity.class.getName()));
        fragments.add(Fragment.instantiate(MainActivity.this, AppelsActivity.class.getName()));
        fragments.add(Fragment.instantiate(MainActivity.this, AgendaActivity.class.getName()));

        // Création de l'adapter qui s'occupera de l'affichage de la liste de
        // Fragments
        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        // Affectation de l'adapter au ViewPager
        MainStelaVP.setAdapter(this.mPagerAdapter);
        MainStelaVP.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    MenuAgendaDisplay.setBackgroundColor(Color.TRANSPARENT);
                    MenuStelaDisplay.setBackgroundResource(R.drawable.bar_menu_selected);
                    MenuSMSDisplay.setBackgroundColor(Color.TRANSPARENT);
                    MenuAppelsDisplay.setBackgroundColor(Color.TRANSPARENT);
                } else if (arg0 == 1) {
                    MenuAgendaDisplay.setBackgroundColor(Color.TRANSPARENT);
                    MenuStelaDisplay.setBackgroundColor(Color.TRANSPARENT);
                    MenuSMSDisplay.setBackgroundResource(R.drawable.bar_menu_selected);
                    MenuAppelsDisplay.setBackgroundColor(Color.TRANSPARENT);
                } else if (arg0 == 2) {
                    MenuAgendaDisplay.setBackgroundColor(Color.TRANSPARENT);
                    MenuStelaDisplay.setBackgroundColor(Color.TRANSPARENT);
                    MenuSMSDisplay.setBackgroundColor(Color.TRANSPARENT);
                    MenuAppelsDisplay.setBackgroundResource(R.drawable.bar_menu_selected);
                } else if (arg0 == 3) {
                    MenuAgendaDisplay.setBackgroundResource(R.drawable.bar_menu_selected);
                    MenuStelaDisplay.setBackgroundColor(Color.TRANSPARENT);
                    MenuSMSDisplay.setBackgroundColor(Color.TRANSPARENT);
                    MenuAppelsDisplay.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
        /*
         *
         * 								Météo
         *
         */
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600000, 0, new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                try {
                Location locations = locationManager.getLastKnownLocation(provider);
                List<String> providerList = locationManager.getAllProviders();
                if (null != locations && null != providerList && providerList.size() > 0) {
                    double longitude = locations.getLongitude();
                    double latitude = locations.getLatitude();
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (null != listAddresses && listAddresses.size() > 0) {
                            String _Location = listAddresses.get(0).getLocality();
                            System.out.println("VILLE : " + _Location);
                            Toast.makeText(MainActivity.this, "Ready to listen!" + _Location, Toast.LENGTH_SHORT).show();
                            MC.updateWeatherData(_Location, MN, TemperatureDisplay, HumidityDisplay, WeatherDisplay);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (SecurityException s) {
                s.printStackTrace();
            }
            }
        });
    }
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x01) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS && !TTSInitialized) {
                // Succès, au moins un moteur de TTS à été trouvé, on l'instancie
                if(previouslyInitialized){
                    previouslyInitialized = true;
                    mTts.shutdown();
                    mTts = new TextToSpeech(this, this);
                    SA.mTts = mTts;
                }else if(!previouslyInitialized){
                    previouslyInitialized = true;
                    mTts = new TextToSpeech(this, this);
                    SA.mTts = mTts;
                }
            } else {
                // Echec, aucun moteur n'a été trouvé, on propose à l'utilisateur d'en installer un depuis le Market
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
        if (requestCode == 1234)
        {
            if (resultCode == RESULT_OK)
            {
                ArrayList<String> matches = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (matches.size() == 0)
                {
                    mTts.speak("Je n'ai rien entendu.", TextToSpeech.QUEUE_FLUSH, null);
                }
                else
                {
                    String mostLikelyThingHeard = matches.get(0);
                    String magicWord = "Stella";
                    if (mostLikelyThingHeard.equals(magicWord))
                    {
                        mTts.speak("Vous avez dit le mot magique!", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else
                    {
                        mTts.speak("Le mot magique n'est pas " + mostLikelyThingHeard + " réessayer", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                Toast.makeText(MainActivity.this, "Entendu : " + matches, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, "Rien", Toast.LENGTH_LONG).show();
            }
        }
    }
    public String getContactName(Context context, String address){
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(address));
        Cursor cursor = cr.query(uri,
                new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }
    public String getContactNumber(Context context, String address){
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(address));
        Cursor cursor = cr.query(uri,
                new String[] { PhoneLookup.NUMBER }, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactNumber = null;
        if (cursor.moveToFirst()) {
            contactNumber = cursor.getString(cursor
                    .getColumnIndex(PhoneLookup.NUMBER));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactNumber;
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            //mTts.setLanguage(Locale.US);
            TTSInitialized = true;
            SA.TTSInitialized = TTSInitialized;
			/*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the magic word");
	        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
	        startActivityForResult(intent, 1234);*/

        }else{
            Toast.makeText(MainActivity.this, "IMPOSSIBLE", Toast.LENGTH_SHORT).show();
        }
    }
    public RelativeLayout GetRelativeLayout(){
        return MainStelaRL;
    }

}

