package com.pi.stela.main;

/**
 * Created by Paul on 08/10/2016.
 */

import com.pi.stela.R;
import com.pi.stela.objects.Reminder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

public class AgendaActivity extends Fragment {

    private View mLeak; // retenue
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
        if (!AlreadyBuild) {
            AlreadyBuild = true;
            final MainActivity MN = MainActivity.MN;

            //Initialisation des Layouts
            RelativeLayout MainStelaRL = new RelativeLayout(MN);
            RelativeLayout AgendaScrollViewRL = new RelativeLayout(MN);

            //Initialisation d'un ScrollView
            final ScrollView AgendaScrollView = new ScrollView(MN);

            //Instanciations des proportions
            final double S = MainActivity.S;
            final double W = MainActivity.W;
            final double H = MainActivity.H;

            //Initialisation du nombre d'or
            final double GoldNumber = (double) ((1 + Math.sqrt(5)) / 2);

            //Initialisation des paramètres des ScrollViews
            RelativeLayout.LayoutParams AgendaScrollViewParams = new
                    RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), LayoutParams.WRAP_CONTENT);

            //Application des marges du ScrollView
            AgendaScrollViewParams.setMargins(0, (int) (370 * GoldNumber * H), 0, 0);

            //Application des règles du ScrollView
            AgendaScrollViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //Application de ses paramètres au ScrollView
            AgendaScrollView.setLayoutParams(AgendaScrollViewParams);

            //Application de ses Paddings au RelativeLayout
            AgendaScrollViewRL.setPadding(0, 0, 0, (int) (50 * GoldNumber * H));

            //Initialisation des polices
            Typeface RobotoFont = Typeface.createFromAsset(MN.getAssets(), "robotoregular.ttf");

            //Initialisation des positions
            int PositionY = 0;

            /*


            ICI ON MET TOUT CE QUE L'ON A A METTRE! LET'S GO!



             */
            //On récupère la date du jour
            Calendar calendar = new GregorianCalendar();
            DateFormat DateFormatYear = new SimpleDateFormat("yyyy");
            DateFormat DateFormatMonth = new SimpleDateFormat("MMMM");
            DateFormat DateFormatDayInTheMonth = new SimpleDateFormat("dd");
            DateFormat DateFormatDay = new SimpleDateFormat("DD");
            String year = DateFormatYear.format(calendar.getTime());
            String month = DateFormatMonth.format(calendar.getTime());
            String dayInTheMonth = DateFormatDayInTheMonth.format(calendar.getTime());
            String day = DateFormatDay.format(calendar.getTime());
            month = month.substring(0, 1).toUpperCase() + month.substring(1);

            //Initialisation des TextViews
            TextView DisplayContactName = new TextView(MN);

            //Application de leur texte aux TextViews
            DisplayContactName.setText(month + " " + year);

            //Application de leur police aux TextViews
            DisplayContactName.setTypeface(RobotoFont);

            //Application de la taille de leur police aux TextViews
            DisplayContactName.setTextSize((float) (GoldNumber * 35 * S));

            //Application de la couleur de la police des TextViews
            DisplayContactName.setTextColor(Color.parseColor("#FFFFFF"));

            //Initialisation des paramètres des TextViews
            RelativeLayout.LayoutParams DisplayContactNameParams = new
                    RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int) (55 * GoldNumber * H));

            //Application des marges aux paramètres des TextViews
            DisplayContactNameParams.setMargins(0, (int) (PositionY * GoldNumber * H), 0, 0);

            //Application des règles aux paramètres des TextViews
            DisplayContactNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //Application de leur paramètre aux TextViews
            DisplayContactName.setLayoutParams(DisplayContactNameParams);

            //Application de la gravité dans les TextViews
            DisplayContactName.setGravity(Gravity.CENTER);

            //Mise en place des TextViews dans le Layout principal
            AgendaScrollViewRL.addView(DisplayContactName);

            //Ajout du Layout au ScrollView
            AgendaScrollView.addView(AgendaScrollViewRL);

            //Hide de la ScrollBar du ScrollView
            AgendaScrollView.setVerticalScrollBarEnabled(false);
            AgendaScrollView.setHorizontalScrollBarEnabled(false);

            //Ajout du ScrollView au Layout principal
            MainStelaRL.addView(AgendaScrollView);

            /*
					 *
					 *								ImageViews
					 *
					 */

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
            UnderlineDisplayParams.setMargins(0, (int) ((PositionY + 60) * GoldNumber * H), 0, 0);

            //Application des règles aux paramètres
            UnderlineDisplayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //Application de leur paramètre aux ImageViews
            UnderlineDisplay.setLayoutParams(UnderlineDisplayParams);

            //Mise en place des ImageViews dans le Layout principal
            AgendaScrollViewRL.addView(UnderlineDisplay);

            PositionY = PositionY + 75;

            /*
					 *
					 *								TextViews
					 *
					 */

            //Initialisation des TextViews
            final TextView Monday = new TextView(MN);
            final TextView Tuesday = new TextView(MN);
            final TextView Wednesday = new TextView(MN);
            final TextView Thursday = new TextView(MN);
            final TextView Friday = new TextView(MN);
            final TextView Saturday = new TextView(MN);
            final TextView Sunday = new TextView(MN);

            //Definition de la taille des TextViews
            int DayDisplayWidth = (int) ((600 * GoldNumber * W) / 7);

            //Distribution de leur police aux TextViews
            Monday.setTypeface(RobotoFont);
            Tuesday.setTypeface(RobotoFont);
            Wednesday.setTypeface(RobotoFont);
            Thursday.setTypeface(RobotoFont);
            Friday.setTypeface(RobotoFont);
            Saturday.setTypeface(RobotoFont);
            Sunday.setTypeface(RobotoFont);

            //Application de leur texte aux TextViews
            Monday.setText("L");
            Tuesday.setText("M");
            Wednesday.setText("M");
            Thursday.setText("J");
            Friday.setText("V");
            Saturday.setText("S");
            Sunday.setText("D");

            //Application de la taille des TextViews
            Monday.setTextSize((float) (GoldNumber * 30 * S));
            Tuesday.setTextSize((float) (GoldNumber * 30 * S));
            Wednesday.setTextSize((float) (GoldNumber * 30 * S));
            Thursday.setTextSize((float) (GoldNumber * 30 * S));
            Friday.setTextSize((float) (GoldNumber * 30 * S));
            Saturday.setTextSize((float) (GoldNumber * 30 * S));
            Sunday.setTextSize((float) (GoldNumber * 30 * S));

            //Application de la couleur de la police des TextViews
            Monday.setTextColor(Color.parseColor("#FFFFFF"));
            Tuesday.setTextColor(Color.parseColor("#FFFFFF"));
            Wednesday.setTextColor(Color.parseColor("#FFFFFF"));
            Thursday.setTextColor(Color.parseColor("#FFFFFF"));
            Friday.setTextColor(Color.parseColor("#FFFFFF"));
            Saturday.setTextColor(Color.parseColor("#FFFFFF"));
            Sunday.setTextColor(Color.parseColor("#FFFFFF"));

            //Initialisation des paramètres des TextViews
            RelativeLayout.LayoutParams MondayParams = new
                    RelativeLayout.LayoutParams(DayDisplayWidth, (int) (50 * GoldNumber * H));
            RelativeLayout.LayoutParams TuesdayParams = new
                    RelativeLayout.LayoutParams(DayDisplayWidth, (int) (50 * GoldNumber * H));
            RelativeLayout.LayoutParams WednesdayParams = new
                    RelativeLayout.LayoutParams(DayDisplayWidth, (int) (50 * GoldNumber * H));
            RelativeLayout.LayoutParams ThursdayParams = new
                    RelativeLayout.LayoutParams(DayDisplayWidth, (int) (50 * GoldNumber * H));
            RelativeLayout.LayoutParams FridayParams = new
                    RelativeLayout.LayoutParams(DayDisplayWidth, (int) (50 * GoldNumber * H));
            RelativeLayout.LayoutParams SaturdayParams = new
                    RelativeLayout.LayoutParams(DayDisplayWidth, (int) (50 * GoldNumber * H));
            RelativeLayout.LayoutParams SundayParams = new
                    RelativeLayout.LayoutParams(DayDisplayWidth, (int) (50 * GoldNumber * H));

            //Application des marges aux paramètres des ImageViews
            MondayParams.setMargins(0, (int) (PositionY * H * GoldNumber), 0, 0);
            TuesdayParams.setMargins(DayDisplayWidth, (int) (PositionY * H * GoldNumber), 0, 0);
            WednesdayParams.setMargins(DayDisplayWidth * 2, (int) (PositionY * H * GoldNumber), 0, 0);
            ThursdayParams.setMargins(DayDisplayWidth * 3, (int) (PositionY * H * GoldNumber), 0, 0);
            FridayParams.setMargins(DayDisplayWidth * 4, (int) (PositionY * H * GoldNumber), 0, 0);
            SaturdayParams.setMargins(DayDisplayWidth * 5, (int) (PositionY * H * GoldNumber), 0, 0);
            SundayParams.setMargins(DayDisplayWidth * 6, (int) (PositionY * H * GoldNumber), 0, 0);

            //Application de leur paramètre aux ImageViews
            Monday.setLayoutParams(MondayParams);
            Tuesday.setLayoutParams(TuesdayParams);
            Wednesday.setLayoutParams(WednesdayParams);
            Thursday.setLayoutParams(ThursdayParams);
            Friday.setLayoutParams(FridayParams);
            Saturday.setLayoutParams(SaturdayParams);
            Sunday.setLayoutParams(SundayParams);

            //Application de la gravité dans les TextViews
            Monday.setGravity(Gravity.CENTER_HORIZONTAL);
            Tuesday.setGravity(Gravity.CENTER_HORIZONTAL);
            Wednesday.setGravity(Gravity.CENTER_HORIZONTAL);
            Thursday.setGravity(Gravity.CENTER_HORIZONTAL);
            Friday.setGravity(Gravity.CENTER_HORIZONTAL);
            Saturday.setGravity(Gravity.CENTER_HORIZONTAL);
            Sunday.setGravity(Gravity.CENTER_HORIZONTAL);

            //Mise en place des ImageViews dans le Layout principal
            AgendaScrollViewRL.addView(Monday);
            AgendaScrollViewRL.addView(Tuesday);
            AgendaScrollViewRL.addView(Wednesday);
            AgendaScrollViewRL.addView(Thursday);
            AgendaScrollViewRL.addView(Friday);
            AgendaScrollViewRL.addView(Saturday);
            AgendaScrollViewRL.addView(Sunday);

            //On récupère le nom du jour du premier jour du mois
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            String FirstDayOfMonth = String.valueOf(cal.getTime()).substring(0, 3);

            int PositionFirstDay = cal.getTime().getDay();

            Calendar cal2 = Calendar.getInstance();
            PositionY = PositionY + 65;
            for (int n = 1; n <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); n++) {

                //Initialisation des TextViews
                final TextView FirstDay = new TextView(MN);

                //Distribution de leur police aux TextViews
                FirstDay.setTypeface(RobotoFont);

                //Application de leur texte aux TextViews
                FirstDay.setText(String.valueOf(n));

                //Application de la taille des TextViews
                FirstDay.setTextSize((float) (GoldNumber * 30 * S));

                //Application de la couleur de la police des TextViews
                FirstDay.setTextColor(Color.parseColor("#FFFFFF"));

                //Initialisation des paramètres des TextViews
                RelativeLayout.LayoutParams FirstDayParams = new
                        RelativeLayout.LayoutParams(DayDisplayWidth, (int) (50 * GoldNumber * H));

                //Application des marges aux paramètres des ImageViews
                FirstDayParams.setMargins(DayDisplayWidth * (PositionFirstDay - 1), (int) (PositionY * GoldNumber * H), 0, 0);

                //Application de leur paramètre aux ImageViews
                FirstDay.setLayoutParams(FirstDayParams);

                //Application de la gravité dans les TextViews
                FirstDay.setGravity(Gravity.CENTER_HORIZONTAL);

                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(2016, Calendar.OCTOBER, n, 0, 0, 0);
                long startDay = calendar2.getTimeInMillis();
                Calendar calendar3 = Calendar.getInstance();
                calendar3.set(2016, Calendar.OCTOBER, n, 23, 59, 59);
                long endDay = calendar3.getTimeInMillis();

                String[] projection = new String[]{BaseColumns._ID, CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART};
                String selection = CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTSTART + "<= ?";
                String[] selectionArgs = new String[]{Long.toString(startDay), Long.toString(endDay)};

                if (ActivityCompat.checkSelfPermission(MN.getApplicationContext(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    Cursor cur = MN.getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection, selection, selectionArgs, null);
                    if(cur.getCount() > 0 && !dayInTheMonth.equals(String.valueOf(n))){
                        //Initialisation des ImageViews
                        final ImageView SquareSelecter = new ImageView(MN);

                        //Distribution de leur image aux ImageViews
                        SquareSelecter.setImageResource(R.drawable.square);

                        //Application des paramètres de contenance des images aux ImageViews
                        SquareSelecter.setScaleType(ScaleType.CENTER_INSIDE);

                        //Initialisation des paramètres des ImageViews
                        RelativeLayout.LayoutParams SquareSelecterParams = new
                                RelativeLayout.LayoutParams(DayDisplayWidth, (int) (60 * GoldNumber * H));

                        //Application des marges aux paramètres des ImageViews
                        SquareSelecterParams.setMargins(DayDisplayWidth * (PositionFirstDay - 1), (int) (PositionY * GoldNumber * H - (5 * GoldNumber * H)), 0, 0);

                        //Application de leur paramètre aux ImageViews
                        SquareSelecter.setLayoutParams(SquareSelecterParams);

                        //Application de l'opacité
                        SquareSelecter.setAlpha((float) (0.5));

                        //Mise en place des ImageViews dans le Layout principal
                        AgendaScrollViewRL.addView(SquareSelecter);
                    }
                }

                if(dayInTheMonth.equals(String.valueOf(n))){
                    //Initialisation des ImageViews
                    final ImageView CircleSelecter = new ImageView(MN);

                    //Distribution de leur image aux ImageViews
                    CircleSelecter.setImageResource(R.drawable.circle);

                    //Application des paramètres de contenance des images aux ImageViews
                    CircleSelecter.setScaleType(ScaleType.CENTER_INSIDE);

                    //Initialisation des paramètres des ImageViews
                    RelativeLayout.LayoutParams CircleSelecterParams = new
                            RelativeLayout.LayoutParams(DayDisplayWidth, (int) (60 * GoldNumber * H));

                    //Application des marges aux paramètres des ImageViews
                    CircleSelecterParams.setMargins(DayDisplayWidth * (PositionFirstDay - 1), (int) (PositionY * GoldNumber * H - (5 * GoldNumber * H)), 0, 0);

                    //Application de leur paramètre aux ImageViews
                    CircleSelecter.setLayoutParams(CircleSelecterParams);

                    //Application de l'opacité
                    CircleSelecter.setAlpha((float) (0.75));

                    //Mise en place des ImageViews dans le Layout principal
                    AgendaScrollViewRL.addView(CircleSelecter);
                }

                //Mise en place des ImageViews dans le Layout principal
                AgendaScrollViewRL.addView(FirstDay);

                if(PositionFirstDay == 7){
                    PositionY = PositionY + 75;
                    PositionFirstDay = 1;
                }else{
                    PositionFirstDay = PositionFirstDay + 1;
                }
            }

            PositionY = PositionY + 75;

            //Initialisation des TextViews
            TextView DisplayRappels = new TextView(MN);

            //Application de leur texte aux TextViews
            DisplayRappels.setText("Rappels");

            //Application de leur police aux TextViews
            DisplayRappels.setTypeface(RobotoFont);

            //Application de la taille de leur police aux TextViews
            DisplayRappels.setTextSize((float) (GoldNumber * 35 * S));

            //Application de la couleur de la police des TextViews
            DisplayRappels.setTextColor(Color.parseColor("#FFFFFF"));

            //Initialisation des paramètres des TextViews
            RelativeLayout.LayoutParams DisplayRappelsParams = new
                    RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int) (55 * GoldNumber * H));

            //Application des marges aux paramètres des TextViews
            DisplayRappelsParams.setMargins(0, (int) (PositionY * GoldNumber * H), 0, 0);

            //Application des règles aux paramètres des TextViews
            DisplayRappelsParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //Application de leur paramètre aux TextViews
            DisplayRappels.setLayoutParams(DisplayRappelsParams);

            //Application de la gravité dans les TextViews
            DisplayRappels.setGravity(Gravity.CENTER);

            //Mise en place des TextViews dans le Layout principal
            AgendaScrollViewRL.addView(DisplayRappels);

            /*
					 *
					 *								ImageViews
					 *
					 */

            //Initialisation des ImageViews
            final ImageView UnderlineDisplayRappels = new ImageView(MN);

            //Distribution de leur image aux ImageViews
            UnderlineDisplayRappels.setImageResource(R.drawable.bar_menu);

            //Application des paramètres de contenance des images aux ImageViews
            UnderlineDisplayRappels.setScaleType(ScaleType.CENTER);

            //Initialisation des paramètres des ImageViews
            RelativeLayout.LayoutParams UnderlineDisplayRappelsParams = new
                    RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), (int) (3 * GoldNumber * H));

            //Application des marges aux paramètres des ImageViews
            UnderlineDisplayRappelsParams.setMargins(0, (int) ((PositionY + 60) * GoldNumber * H), 0, 0);

            //Application des règles aux paramètres
            UnderlineDisplayRappelsParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //Application de leur paramètre aux ImageViews
            UnderlineDisplayRappels.setLayoutParams(UnderlineDisplayRappelsParams);

            //Mise en place des ImageViews dans le Layout principal
            AgendaScrollViewRL.addView(UnderlineDisplayRappels);

            Reminder Rappel1 = new Reminder(2, "Finir l'agenda sur Stela.");
            Reminder Rappel2 = new Reminder(1, "Réviser pour le DS de maths.");
            Reminder Rappel3 = new Reminder(0, "Ne pas oublier de respirer.");

            ArrayList<Reminder> Reminders = new ArrayList<Reminder>();
            Reminders.add(Rappel1);
            Reminders.add(Rappel2);
            Reminders.add(Rappel3);
            PositionY = PositionY + 100;
            for(int i = 0; i < Reminders.size(); i++){
                //Initialisation des ImageViews
                final ImageView DisplayPriority = new ImageView(MN);

                //Distribution de leur image aux ImageViews
                switch(Reminders.get(i).GetPriority()){
                    case 0: DisplayPriority.setImageResource(R.drawable.priority_0);
                        break;
                    case 1: DisplayPriority.setImageResource(R.drawable.priority_1);
                        break;
                    case 2: DisplayPriority.setImageResource(R.drawable.priority_2);
                        break;
                    default: DisplayPriority.setImageResource(R.drawable.priority_0);
                        break;
                }

                //Application des paramètres de contenance des images aux ImageViews
                DisplayPriority.setScaleType(ScaleType.CENTER_INSIDE);

                //Initialisation des paramètres des ImageViews
                RelativeLayout.LayoutParams DisplayPriorityParams = new
                        RelativeLayout.LayoutParams((int) (75 * GoldNumber * W), (int) (75 * GoldNumber * H));

                //Application des marges aux paramètres des ImageViews
                DisplayPriorityParams.setMargins(0, (int) (PositionY * GoldNumber * H), 0, 0);

                //Application de leur paramètre aux ImageViews
                DisplayPriority.setLayoutParams(DisplayPriorityParams);

                //Application de leur opacité aux ImageViews
                DisplayPriority.setAlpha((float) (0.75));

                //Mise en place des ImageViews dans le Layout principal
                AgendaScrollViewRL.addView(DisplayPriority);

                //Initialisation des TextViews
                TextView DisplayReminder = new TextView(MN);

                //Application de leur texte aux TextViews
                DisplayReminder.setText(Reminders.get(i).GetMessage());

                //Application de leur police aux TextViews
                DisplayReminder.setTypeface(RobotoFont);

                //Application de la taille de leur police aux TextViews
                DisplayReminder.setTextSize((float) (GoldNumber * 30 * S));

                //Application de la couleur de la police des TextViews
                DisplayReminder.setTextColor(Color.parseColor("#FFFFFF"));

                //Initialisation des paramètres des TextViews
                RelativeLayout.LayoutParams DisplayReminderParams = new
                        RelativeLayout.LayoutParams((int) (600 * GoldNumber * W - (75 * GoldNumber * W)), (int) (100 * GoldNumber * H));

                //Application des marges aux paramètres des TextViews
                DisplayReminderParams.setMargins((int) (75 * GoldNumber * W), (int) ((PositionY - (25 / 2)) * GoldNumber * H), 0, 0);

                //Application des règles aux paramètres des TextViews
                DisplayReminderParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                //Application de leur paramètre aux TextViews
                DisplayReminder.setLayoutParams(DisplayReminderParams);

                //Application de la gravité dans les TextViews
                DisplayReminder.setGravity(Gravity.CENTER);

                //Mise en place des TextViews dans le Layout principal
                AgendaScrollViewRL.addView(DisplayReminder);

                PositionY = PositionY + 125;
            }
            return MainStelaRL;
        }else{
            return mLeak;
        }
    }
}
