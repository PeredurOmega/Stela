package com.pi.stela.main;

/**
 * Created by Paul on 08/10/2016.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import com.pi.stela.R;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AppelsActivity extends Fragment {

    public static ArrayList<ArrayList<String>> CallsLog = new ArrayList<ArrayList<String>>();

    private View mLeak; // retained
    private boolean AlreadyBuild = false;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLeak = getView(); // now cleaning up!
        System.out.println("JE SUIS DESTROY");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!AlreadyBuild){
            AlreadyBuild = true;
            final MainActivity MN = MainActivity.MN;

            //Initialisation des Layouts
            RelativeLayout MainStelaRL = new RelativeLayout(MN);
            RelativeLayout CallsScrollViewRL = new RelativeLayout(MN);

            //Initialisation d'un ScrollView
            final ScrollView CallsScrollView = new ScrollView(MN);

            //Instanciations des proportions
            final double S = MainActivity.S;
            final double W = MainActivity.W;
            final double H = MainActivity.H;

            //Initialisation du nombre d'or
            final double GoldNumber = (double)((1 + Math.sqrt(5))/2);

            //Initialisation des paramètres des Buttons
            RelativeLayout.LayoutParams CallsScrollViewParams = new
                    RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            //Application des marges du ScrollView
            CallsScrollViewParams.setMargins(0, (int) (370 * GoldNumber * H), 0, 0);

            //Application de ses paramètres au ScrollView
            CallsScrollView.setLayoutParams(CallsScrollViewParams);

            //Application de ses Paddings au RelativeLayout
            CallsScrollViewRL.setPadding(0, 0, 0, (int) (50 * GoldNumber * H));

            //Initialisation des polices
            Typeface RobotoFont = Typeface.createFromAsset(MN.getAssets(), "robotoregular.ttf");

            //Initialisation des positions
            int PositionY = -95;

            Collections.reverse(CallsLog);

            for(int i = 0; i < CallsLog.size(); i++){
                PositionY = PositionY + 125;
			/*
			 *
			 *								Buttons
			 *
			 */
                //Nom du contact
                final ArrayList<String> CallDetails = CallsLog.get(i);

                //Initialisation des Buttons
                final Button ContactDisplay = new Button(MN);
                final ImageButton Type = new ImageButton(MN);

                //Obtention du type d'appel
                String type = CallDetails.get(2);

                //Application des Backgrounds aux Buttons
                ContactDisplay.setBackground(MN.getResources().getDrawable(R.drawable.sms_bar));
                if(type.equals(String.valueOf(Calls.MISSED_TYPE))){
                    Type.setImageDrawable(MN.getResources().getDrawable(R.drawable.missed_call));
                }else if(type.equals(String.valueOf(Calls.INCOMING_TYPE))){
                    Type.setImageDrawable(MN.getResources().getDrawable(R.drawable.entered_call));
                }else{
                    Type.setImageDrawable(MN.getResources().getDrawable(R.drawable.out_call));
                }

                Type.setBackgroundColor(Color.TRANSPARENT);

                //Conversion en date
                Date date = new Date(Long.parseLong(CallDetails.get(1)));
                String minutes = String.valueOf(date.getMinutes());
                if(minutes.length() != 2){
                    minutes = "0" + minutes;
                }
                String hours = String.valueOf(date.getHours());
                if(hours.length() != 2){
                    hours = "0" + hours;
                }

                if(CallsLog.size() > i + 1){
                    String TextForThisDay = getTextForThisDay(date);
                    if(TextForThisDay != null){
                        PositionY = PositionY - 20;
					/*
					 *
					 * 							TextViews
					 *
					 */

                        //Initialisation des TextViews
                        TextView DisplayDate = new TextView(MN);

                        //Application de leur texte aux TextViews
                        DisplayDate.setText(TextForThisDay);

                        //Application de leur police aux TextViews
                        DisplayDate.setTypeface(RobotoFont);

                        //Application de la taille de leur police aux TextViews
                        DisplayDate.setTextSize((float) (GoldNumber * 35 * S));

                        //Application de la couleur de la police des TextViews
                        DisplayDate.setTextColor(Color.parseColor("#FFFFFF"));

                        //Initialisation des paramètres des TextViews
                        RelativeLayout.LayoutParams DisplayDateParams = new
                                RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int) (55 * GoldNumber * H));

                        //Application des marges aux paramètres des TextViews
                        DisplayDateParams.setMargins(0, (int) (PositionY * GoldNumber * H), 0, 0);

                        //Application des règles aux paramètres des TextViews
                        DisplayDateParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                        //Application de leur paramètre aux TextViews
                        DisplayDate.setLayoutParams(DisplayDateParams);

                        //Application de la gravité dans les TextViews
                        DisplayDate.setGravity(Gravity.CENTER);

                        //Mise en place des TextViews dans le Layout principal
                        CallsScrollViewRL.addView(DisplayDate);

					/*
					 *
					 *								ImageViews
					 *
					 */
                        //Incrémentation de la PositionY
                        PositionY = PositionY + 60;

                        //Initialisation des ImageViews
                        final ImageView UnderlineDisplay = new ImageView(MN);

                        //Distribution de leur image aux ImageViews
                        UnderlineDisplay.setImageResource(R.drawable.bar_menu);

                        //Application des paramètres de contenance des images aux ImageViews
                        UnderlineDisplay.setScaleType(ScaleType.CENTER);

                        //Initialisation des paramètres des ImageViews
                        RelativeLayout.LayoutParams UnderlineDisplayParams = new
                                RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), (int) (3 * GoldNumber * H));

                        //Application des marges aux paramètres des ImageViews
                        UnderlineDisplayParams.setMargins(0, (int) (PositionY * GoldNumber * H), 0, 0);

                        //Application des règles aux paramètres
                        UnderlineDisplayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                        //Application de leur paramètre aux ImageViews
                        UnderlineDisplay.setLayoutParams(UnderlineDisplayParams);

                        //Mise en place des ImageViews dans le Layout principal
                        CallsScrollViewRL.addView(UnderlineDisplay);

                        //Redéfinition de la PositionY
                        PositionY = PositionY + 30;
                    }
                }

                //Obtention des infos
                String ContactName = CallDetails.get(0);
                if(ContactName.length() > 14){
                    ContactName = ContactName.substring(0, 14);
                }

                //Distribution de leur texte aux Buttons
                ContactDisplay.setText(Html.fromHtml(String.valueOf("<font color=#ffffff>" + ContactName + "</font>"  + "<font color=#ffffff> à " + hours + ":" + minutes + "</font>")));

                //Application des polices aux Buttons
                ContactDisplay.setTypeface(RobotoFont);

                //Application des tailles des polices aux Buttons
                ContactDisplay.setTextSize((float) (GoldNumber * 30 * S));

                //Application des couleurs de polices aux Buttons
                //ContactDisplay.setTextColor(Color.parseColor("#FFFFFF"));

                //Application de la gravité dans les Buttons
                ContactDisplay.setGravity(Gravity.CENTER);
                Type.setScaleType(ScaleType.FIT_CENTER);

                //Application des marges (paddings) aux buttons
                ContactDisplay.setPadding(0, 0, 0, 0);

                //Initialisation des paramètres des Buttons
                RelativeLayout.LayoutParams ContactDisplayParams = new
                        RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), (int) (100 * GoldNumber * H));
                RelativeLayout.LayoutParams TypeParams = new
                        RelativeLayout.LayoutParams((int) (100 * GoldNumber * W), (int) (100 * GoldNumber * H));

                //Application des marges aux paramètres des Buttons
                ContactDisplayParams.setMargins(0, (int) (PositionY * GoldNumber * H), 0, 0);
                TypeParams.setMargins((int) (50 * GoldNumber * H), (int) (PositionY * GoldNumber * H + (5 * GoldNumber * H)), 0, 0);

                //Application des règles aux paramètres
                ContactDisplayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                TypeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT );

                //Application de leur paramètre aux Buttons
                ContactDisplay.setLayoutParams(ContactDisplayParams);
                Type.setLayoutParams(TypeParams);

                //Application des OnClickListener aux Buttons
                ContactDisplay.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast t = Toast.makeText(MN, "Tu souhaites parler à " + CallDetails.get(3), Toast.LENGTH_SHORT);
                        t.show();
                    }
                });
                ContactDisplay.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent dialIntent = new Intent(Intent.ACTION_CALL);
                        dialIntent.setData(Uri.parse("tel:" + MN.getContactNumber(MN, CallDetails.get(3))));
                        startActivity(dialIntent);
                    }
                });
                //Mise en place des Buttons dans le Layout principal
                CallsScrollViewRL.addView(ContactDisplay);
                CallsScrollViewRL.addView(Type);
            }
            //Ajout du Layout au ScrollView
            CallsScrollView.addView(CallsScrollViewRL);

            //Hide de la ScrollBar du ScrollView
            CallsScrollView.setVerticalScrollBarEnabled(false);
            CallsScrollView.setHorizontalScrollBarEnabled(false);

            //Ajout du ScrollView au Layout principal
            MainStelaRL.addView(CallsScrollView);

            return MainStelaRL;
        }else{
            return mLeak;
        }
    }
    private boolean ThisDayUnused = true;
    private boolean YesterDayUnused = true;
    private boolean BeforeYesterDayUnused = true;
    private boolean ThisWeekUnused = true;
    private boolean ThisMonthUnused = true;
    private boolean ThisYearUnused = true;
    public String getTextForThisDay(Date NextDate){
        //Texte pour le jour traité
        String textForThisDay = null;

        //La date traitée
        Calendar nextDate = Calendar.getInstance();
        nextDate.setTime(NextDate);

        //Aujourd'hui
        Calendar today = Calendar.getInstance(); // aujourd'hui

        //Hier
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        //Avant-Hier
        Calendar beforeyesterday = Calendar.getInstance();
        beforeyesterday.add(Calendar.DAY_OF_YEAR, -2);

        //Cette semaine
        Calendar thisweek = Calendar.getInstance();
        thisweek.add(Calendar.DAY_OF_YEAR, -7);

        if (today.get(Calendar.DAY_OF_YEAR) == nextDate.get(Calendar.DAY_OF_YEAR) && today.get(Calendar.YEAR) == nextDate.get(Calendar.YEAR)){
            if(ThisDayUnused){
                ThisDayUnused = false;
                textForThisDay = "Aujourd'hui";
            }
        }else if (yesterday.get(Calendar.YEAR) == nextDate.get(Calendar.YEAR)
                && yesterday.get(Calendar.DAY_OF_YEAR) == nextDate.get(Calendar.DAY_OF_YEAR)) {
            if(YesterDayUnused){
                YesterDayUnused = false;
                textForThisDay = "Hier";
            }
        }else if (beforeyesterday.get(Calendar.YEAR) == nextDate.get(Calendar.YEAR)
                && beforeyesterday.get(Calendar.DAY_OF_YEAR) == nextDate.get(Calendar.DAY_OF_YEAR)){
            if(BeforeYesterDayUnused){
                BeforeYesterDayUnused = false;
                textForThisDay = "Avant-hier";
            }
        }else if (thisweek.get(Calendar.YEAR) == nextDate.get(Calendar.YEAR)
                && thisweek.get(Calendar.DAY_OF_YEAR) <= nextDate.get(Calendar.DAY_OF_YEAR) && nextDate.get(Calendar.DAY_OF_YEAR) < beforeyesterday.get(Calendar.DAY_OF_YEAR)){
            if(ThisWeekUnused){
                ThisWeekUnused = false;
                textForThisDay = "Cette semaine";
            }
        }else if (ThisMonthUnused){
            if(ThisMonthUnused){
                ThisMonthUnused = false;
                textForThisDay = "Plus anciens";
            }
        }
        return textForThisDay;
    }
}

