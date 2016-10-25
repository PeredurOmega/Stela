package com.pi.stela.main;

/**
 * Created by Paul on 08/10/2016.
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.pi.stela.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;

public class SMSActivity extends Fragment {

    public static ArrayList<String> Contacts = new ArrayList<String>();
    public static ArrayList<ArrayList<String>> SMS = new ArrayList<ArrayList<String>>();

    //Type de SMS
    public static final int MESSAGE_TYPE_ALL    = 0;
    public static final int MESSAGE_TYPE_INBOX  = 1;
    public static final int MESSAGE_TYPE_SENT   = 2;
    public static final int MESSAGE_TYPE_DRAFT  = 3;
    public static final int MESSAGE_TYPE_OUTBOX = 4;
    public static final int MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
    public static final int MESSAGE_TYPE_QUEUED = 6; // for messages to send later

    private View mLeak; // retained
    private boolean AlreadyBuild = false;
    int PositionY3;
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
            return GetSMSView();
        }else{
            return mLeak;
        }
    }
    public void ReallySendSMS(String number, String message, Context MN){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
        } catch (Exception e) {
            Toast.makeText(MN,
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public static int getHeight(Context context, CharSequence text, int textSize, int deviceWidth, Typeface typeface,int padding) {
        TextView textView = new TextView(context);
        textView.setPadding(padding,0,padding,padding);
        textView.setTypeface(typeface);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }
    public View GetSMSView() {
        final MainActivity MN = MainActivity.MN;

        //Initialisation des Layouts
        final RelativeLayout MainStelaRL = new RelativeLayout(MN);
        final RelativeLayout SMSScrollViewRL = new RelativeLayout(MN);

        //Initialisation d'un ScrollView
        ScrollView SMSScrollView = new ScrollView(MN);

        //Instanciations des proportions
        final double S = MainActivity.S;
        final double W = MainActivity.W;
        final double H = MainActivity.H;

        //Initialisation du nombre d'or
        final double GoldNumber = (double)((1 + Math.sqrt(5))/2);

        //Initialisation des paramètres des ScrollViews
        RelativeLayout.LayoutParams SMSScrollViewParams = new
                RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        //Application des marges du ScrollView
        SMSScrollViewParams.setMargins(0, (int) (370 * GoldNumber * H), 0, 0);

        //Application de ses paramètres au ScrollView
        SMSScrollView.setLayoutParams(SMSScrollViewParams);

        //Application de ses Paddings au RelativeLayout
        SMSScrollViewRL.setPadding(0, 0, 0, (int) (50 * GoldNumber * H));

        //Initialisation des polices
        final Typeface RobotoFont = Typeface.createFromAsset(MN.getAssets(), "robotoregular.ttf");

        //Initialisation des positions
        int PositionY = -70;

        for(int i = 0; i < Contacts.size(); i++){
            PositionY = PositionY + 100;
			/*
			 *
			 *								Buttons
			 *
			 */
            //Nom du contact
            final String ContactName = Contacts.get(i);

            //Initialisation des Buttons
            final Button ContactDisplay = new Button(MN);

            //Application des Backgrounds aux Buttons
            ContactDisplay.setBackground(MN.getResources().getDrawable(R.drawable.sms_bar));

            //Distribution de leur texte aux Buttons
            ContactDisplay.setText(ContactName);

            //Application des polices aux Buttons
            ContactDisplay.setTypeface(RobotoFont);

            //Application des tailles des polices aux Buttons
            ContactDisplay.setTextSize((float) (GoldNumber * 30 * S));

            //Application des couleurs de polices aux Buttons
            ContactDisplay.setTextColor(Color.parseColor("#FFFFFF"));

            //Application de la gravité dans les Buttons
            ContactDisplay.setGravity(Gravity.CENTER);

            //Application des marges (paddings) aux buttons
            ContactDisplay.setPadding(0, 0, 0, 0);

            //Initialisation des paramètres des Buttons
            RelativeLayout.LayoutParams ContactDisplayParams = new
                    RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), (int) (75 * GoldNumber * H));

            //Application des marges aux paramètres des Buttons
            ContactDisplayParams.setMargins(0, (int) (PositionY * GoldNumber * H), 0, 0);

            //Application des règles aux paramètres
            ContactDisplayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            //Application de leur paramètre aux Buttons
            ContactDisplay.setLayoutParams(ContactDisplayParams);

            //Application des OnClickListeners aux Buttons
            ContactDisplay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainStelaRL.removeAllViews();
					/*
					 *
					 * 							TextViews
					 *
					 */

                    //Initialisation des TextViews
                    TextView DisplayContactName = new TextView(MN);

                    //Application de leur texte aux TextViews
                    DisplayContactName.setText(ContactName);

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
                    DisplayContactNameParams.setMargins(0, (int) (370 * GoldNumber * H), 0, 0);

                    //Application des règles aux paramètres des TextViews
                    DisplayContactNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    //Application de leur paramètre aux TextViews
                    DisplayContactName.setLayoutParams(DisplayContactNameParams);

                    //Application de la gravité dans les TextViews
                    DisplayContactName.setGravity(Gravity.CENTER);

                    //Mise en place des TextViews dans le Layout principal
                    MainStelaRL.addView(DisplayContactName);

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
                    UnderlineDisplayParams.setMargins(0, (int) ((370 + 60) * GoldNumber * H), 0, 0);

                    //Application des règles aux paramètres
                    UnderlineDisplayParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    //Application de leur paramètre aux ImageViews
                    UnderlineDisplay.setLayoutParams(UnderlineDisplayParams);

                    //Mise en place des ImageViews dans le Layout principal
                    MainStelaRL.addView(UnderlineDisplay);

                        /*
					 *
					 *								ImageButtons
					 *
					 */

                    //Initialisation des ImageButtons
                    final ImageButton BackDisplay = new ImageButton(MN);

                    //Distribution de leur image aux ImageButtons
                    BackDisplay.setBackground(MN.getDrawable(R.drawable.retour));

                    //Initialisation des paramètres des ImageButtons
                    RelativeLayout.LayoutParams BackDisplayParams = new
                            RelativeLayout.LayoutParams((int) (45 * GoldNumber * H), (int) (45 * GoldNumber * H));

                    //Application des marges aux paramètres des ImageButtons
                    BackDisplayParams.setMargins((int) (50 * GoldNumber * W), (int) (377.5 * GoldNumber * H), 0, 0);

                    //Application de leur paramètre aux ImageButtons
                    BackDisplay.setLayoutParams(BackDisplayParams);

                    //Application de leur OnClickListener aux ImageButtons
                    BackDisplay.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment currentFragment = getFragmentManager().findFragmentByTag(SMSActivity.this.getTag());
                            ViewGroup current = (ViewGroup) currentFragment.getView();
                            current.removeAllViews();
                            current.addView(GetSMSView());
                            /*FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                            fragTransaction.detach(currentFragment);
                            fragTransaction.attach(currentFragment);
                            fragTransaction.commit();*/
                        }
                    });

                    //Mise en place des ImageButtons dans le Layout principal
                    MainStelaRL.addView(BackDisplay);

					/*
					 *
					 * 						Affichage des SMS
					 *
					 */

                    //Initialisation des ScrollViews
                    final ScrollView SMSConversationSV = new ScrollView(MN);

                    //Initialisation des paramètres des ScrollViews
                    RelativeLayout.LayoutParams SMSConversationSVParams = new
                            RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), LayoutParams.WRAP_CONTENT);

                    //Application des marges du ScrollView
                    SMSConversationSVParams.setMargins(0, (int) (432 * GoldNumber * H), 0, (int) (100 * GoldNumber * H));

                    //Application des règles du ScrollView
                    SMSConversationSVParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    //Application des paramètres
                    SMSConversationSV.setLayoutParams(SMSConversationSVParams);

                    //Initialisation des RelativeLayouts
                    final RelativeLayout SMSRelativeLayout = new RelativeLayout(MN);

                    //Initialisation des paramètres des RelativeLayout
                    RelativeLayout.LayoutParams SMSRelativeLayoutParams = new
                            RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                    //Application des paramètres
                    SMSRelativeLayout.setLayoutParams(SMSRelativeLayoutParams);

                    //Initialisation des positions
                    int PositionY2 = 30;

                    //Instanciation de l'ArrayList des SMS avec le contact
                    ArrayList<ArrayList<String>> AllContactSMS = new ArrayList<ArrayList<String>>();

                    //Récupération des SMS avec le contact
                    for (int j = 0; j < SMS.size(); j++) {
                        ArrayList<String> SMSToTest = SMS.get(j);
                        if (SMSToTest.get(3).equals(ContactName)) {
                            AllContactSMS.add(SMSToTest);
                        }
                        if (j + 1 == SMS.size()) {
                            for (int k = 0; k < AllContactSMS.size(); k++) {

                                Collections.sort(AllContactSMS, new Comparator<ArrayList<String>>() {
                                    @Override
                                    public int compare(ArrayList<String> lhs, ArrayList<String> rhs) {
                                        int resultat = 0;
                                        if (new Date(Long.parseLong(lhs.get(0))).before(new Date(Long.parseLong(rhs.get(0))))) {
                                            resultat = -1;
                                        }
                                        if (new Date(Long.parseLong(lhs.get(0))).after(new Date(Long.parseLong(rhs.get(0))))) {
                                            resultat = 1;
                                        }
                                        if (new Date(Long.parseLong(lhs.get(0))).equals(new Date(Long.parseLong(rhs.get(0))))) {
                                            resultat = 0;
                                        }
                                        return resultat;
                                    }
                                });

                                //Récupération des détails du SMS
                                ArrayList<String> ThisSMS = AllContactSMS.get(k);
                                Date date = new Date(Long.parseLong(ThisSMS.get(0)));
                                String minutes = String.valueOf(date.getMinutes());
                                if (minutes.length() != 2) {
                                    minutes = "0" + minutes;
                                }
                                String hours = String.valueOf(date.getHours());
                                if (hours.length() != 2) {
                                    hours = "0" + hours;
                                }
                                String message = ThisSMS.get(1);
                                String type = ThisSMS.get(2);
                                String person = ThisSMS.get(3);

                                //Initialisation des boutons
                                final Button thisSMS = new Button(MN);

                                //Initialisation des paramètres des boutons
                                RelativeLayout.LayoutParams thisSMSParams = new
                                        RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), LayoutParams.WRAP_CONTENT);

                                //Application des marges aux paramètres des Buttons
                                if (Integer.parseInt(type) == 1) {    //Messages reçues
                                    thisSMSParams.setMargins((int) (100 * GoldNumber * W), (int) (PositionY2 * GoldNumber * H), 0, 30);
                                } else {                                //Messages envoyées
                                    thisSMSParams.setMargins(0, (int) (PositionY2 * GoldNumber * H), (int) (100 * GoldNumber * W), 30);
                                }

                                //Application des Backgrounds aux Buttons
                                thisSMS.setBackground(MN.getResources().getDrawable(R.drawable.sms_bar));

                                //Distribution de leur texte aux Buttons
                                thisSMS.setText(message);

                                //Application des polices aux Buttons
                                thisSMS.setTypeface(RobotoFont);

                                //Application des tailles des polices aux Buttons
                                thisSMS.setTextSize((float) (GoldNumber * 25 * S));

                                //Application des couleurs de polices aux Buttons
                                thisSMS.setTextColor(Color.parseColor("#FFFFFF"));

                                //Application de la gravité dans les Buttons
                                thisSMS.setGravity(Gravity.CENTER);

                                //Application de leur paramètre aux Buttons
                                thisSMS.setLayoutParams(thisSMSParams);

                                //Ajout de la View au Layout
                                SMSRelativeLayout.addView(thisSMS);

                                //Incrémentation de la PositionY
								/*if(message.length() > 25){
									String Longueur = String.valueOf(message.length()/25 + 1);
									if(Longueur.contains(".")){
										Longueur.substring(0, Longueur.indexOf(".") - 1);
										PositionY2 = PositionY2 +  100 + 25 * (Integer.parseInt(Longueur));
									}else{
										PositionY2 = PositionY2 + 100 + 25 * (Integer.parseInt(Longueur));
									}
								}else{
									PositionY2 = PositionY2 + 100;
								}*/
                                Rect bounds = new Rect();
                                Paint paint = new Paint();
                                paint.setTextSize((float) (GoldNumber * 25 * S));
                                paint.getTextBounds(message, 0, message.length(), bounds);

                                int width = (int) Math.ceil((float) bounds.width() / (600 * GoldNumber * W - (100 * GoldNumber * W)));
                                //PositionY2 = PositionY2 + 50 + (int) ( 5 * GoldNumber * 25 * S * width);
                                int SMSHeight = getHeight(MN, message, (int) (GoldNumber * 25 * S), (int) (500 * GoldNumber * W), RobotoFont, (int) (GoldNumber * 21 * W));
                                PositionY2 = PositionY2 + 50 + SMSHeight;

                                //Affichage de l'heure à laquelle a été envoyée le sms
                                //Initialisation du TextView
                                TextView time = new TextView(MN);

                                //Application de leur texte aux TextViews
                                time.setText(hours + ":" + minutes);

                                //Application de leur police aux TextViews
                                time.setTypeface(RobotoFont);

                                //Application de la taille de leur police aux TextViews
                                time.setTextSize((float) (GoldNumber * 25 * S));

                                //Application de la couleur de la police des TextViews
                                time.setTextColor(Color.parseColor("#FFFFFF"));

                                //Initialisation des paramètres des TextViews
                                RelativeLayout.LayoutParams timeParams = new
                                        RelativeLayout.LayoutParams((int) (100 * GoldNumber * W), (int) (55 * GoldNumber * H));

                                //Application des marges aux paramètres des TextViews
                                timeParams.setMargins(0, (int) (GoldNumber * H * PositionY2 - 50 - (((55 + SMSHeight) / 2))), 0, 0);
                                if (Integer.parseInt(type) == 1) {    //Messages reçues
                                    timeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                } else {                                //Messages envoyées
                                    timeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                }

                                //Application de leur paramètre aux TextViews
                                time.setLayoutParams(timeParams);

                                //Application de la gravité dans les TextViews
                                time.setGravity(Gravity.CENTER);

                                //Mise en place des TextViews dans le Layout principal
                                SMSRelativeLayout.addView(time);
                            }
                        }
                    }
                    //Ajout du RelativeLayout au ScrollView
                    SMSConversationSV.addView(SMSRelativeLayout);

                    //Ajout du ScrollView au RelativeLayout
                    MainStelaRL.addView(SMSConversationSV);

                    //Scroll Down dans le ScrollView
                    SMSConversationSV.post(new Runnable() {
                        @Override
                        public void run() {
                            SMSConversationSV.fullScroll(ScrollView.FOCUS_DOWN);
                            //Hide de la ScrollBar du ScrollView
                            SMSConversationSV.setVerticalScrollBarEnabled(false);
                            SMSConversationSV.setHorizontalScrollBarEnabled(false);
                        }
                    });

					/*
					 *
					 * 							EditText et ImageButton pour l'envoie de SMS
					 *
					 */
                    //Initialisation des ImageButtons et des EditTexts
                    final ImageButton SendSMS = new ImageButton(MN);
                    final EditText WriteSMS = new EditText(MN);

                    //Initialisation des paramètres des ImageButtons et EditTexts
                    RelativeLayout.LayoutParams SendSMSParams = new
                            RelativeLayout.LayoutParams((int) (100 * GoldNumber * W), (int) (100 * GoldNumber * H));
                    RelativeLayout.LayoutParams WriteSMSParams = new
                            RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (100 * GoldNumber * H));

                    //Application des marges aux paramètres des Buttons
                    SendSMSParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    SendSMSParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    WriteSMSParams.setMargins(0, 0, (int) (100 * GoldNumber * W), 0);
                    WriteSMSParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                    //Application des Backgrounds aux Buttons et EditTexts
                    SendSMS.setBackground(MN.getResources().getDrawable(R.drawable.send_sms_button));
                    WriteSMS.setBackgroundColor(Color.parseColor("#10FFFFFF"));

                    //Application des Paddings aux EditTexts
                    WriteSMS.setPadding((int) (5 * GoldNumber * W), 0, (int) (5 * GoldNumber * W), 0);

                    //Application du Hint texte à l'EditText
                    WriteSMS.setHint("Envoyer un message");

                    //Application de la couleur du Hint texte de l'EditText
                    WriteSMS.setHintTextColor(Color.parseColor("#80FFFFFF"));
                    ;

                    //Application des polices aux EditTexts
                    WriteSMS.setTypeface(RobotoFont);

                    //Application des tailles des polices aux EditTexts
                    WriteSMS.setTextSize((float) (GoldNumber * 25 * S));

                    //Application des couleurs de polices aux EditTexts
                    WriteSMS.setTextColor(Color.parseColor("#FFFFFF"));

                    //Application de la gravité dans les EditTexts
                    WriteSMS.setGravity(Gravity.CENTER);

                    //Application de la mise en forme du Button
                    SendSMS.setScaleType(ScaleType.FIT_CENTER);

                    //Modification du Clavier de l'EditText
                    WriteSMS.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    WriteSMS.setImeOptions(EditorInfo.IME_ACTION_SEND);

                    //Listener de l'ImageButton pour envoyer un SMS
                    PositionY3 = PositionY2;
                    SendSMS.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Envoi du SMS
                            String number = null;
                            for (int f = 0; f < SMS.size(); f++) {
                                if (SMS.get(f).get(3).equals(ContactName)) {
                                    number = SMS.get(f).get(4);
                                }
                            }
                            ReallySendSMS(number, String.valueOf(WriteSMS.getText()), MN);
                            //Récupération des détails du SMS
                            Date date = new Date();
                            String minutes = String.valueOf(date.getMinutes());
                            if (minutes.length() != 2) {
                                minutes = "0" + minutes;
                            }
                            String hours = String.valueOf(date.getHours());
                            if (hours.length() != 2) {
                                hours = "0" + hours;
                            }
                            Editable message = WriteSMS.getText();

                            //Initialisation des boutons
                            final Button thisSMS = new Button(MN);

                            //Initialisation des paramètres des boutons
                            RelativeLayout.LayoutParams thisSMSParams = new
                                    RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), LayoutParams.WRAP_CONTENT);

                            //Application des marges aux paramètres des Buttons
                            thisSMSParams.setMargins(0, (int) (PositionY3 * GoldNumber * H), (int) (100 * GoldNumber * W), 30);

                            //Application des Backgrounds aux Buttons
                            thisSMS.setBackground(MN.getResources().getDrawable(R.drawable.sms_bar));

                            //Distribution de leur texte aux Buttons
                            thisSMS.setText(message);

                            //Application des polices aux Buttons
                            thisSMS.setTypeface(RobotoFont);

                            //Application des tailles des polices aux Buttons
                            thisSMS.setTextSize((float) (GoldNumber * 25 * S));

                            //Application des couleurs de polices aux Buttons
                            thisSMS.setTextColor(Color.parseColor("#FFFFFF"));

                            //Application de la gravité dans les Buttons
                            thisSMS.setGravity(Gravity.CENTER);

                            //Application de leur paramètre aux Buttons
                            thisSMS.setLayoutParams(thisSMSParams);

                            //Ajout de la View au Layout
                            SMSRelativeLayout.addView(thisSMS);

                            //Incrémentation de la PositionY
							/*if(message.length() > 25){
								String Longueur = String.valueOf(message.length()/25 + 1);
								if(Longueur.contains(".")){
									Longueur.substring(0, Longueur.indexOf(".") - 1);
									PositionY2 = PositionY2 +  100 + 25 * (Integer.parseInt(Longueur));
								}else{
									PositionY2 = PositionY2 + 100 + 25 * (Integer.parseInt(Longueur));
								}
							}else{
								PositionY2 = PositionY2 + 100;
							}*/
                            Rect bounds = new Rect();
                            Paint paint = new Paint();
                            paint.setTextSize((float) (GoldNumber * 25 * S));
                            paint.getTextBounds(String.valueOf(WriteSMS.getText()), 0, String.valueOf(WriteSMS.getText()).length(), bounds);

                            int width = (int) Math.ceil((float) bounds.width() / (600 * GoldNumber * W - (100 * GoldNumber * W)));
                            //PositionY2 = PositionY2 + 50 + (int) ( 5 * GoldNumber * 25 * S * width);
                            int SMSHeight = getHeight(MN, message, (int) (GoldNumber * 25 * S), (int) (500 * GoldNumber * W), RobotoFont, (int) (20 * W));
                            PositionY3 = PositionY3 + 50 + SMSHeight;

                            //Affichage de l'heure à laquelle a été envoyée le sms
                            //Initialisation du TextView
                            TextView time = new TextView(MN);

                            //Application de leur texte aux TextViews
                            time.setText(hours + ":" + minutes);

                            //Application de leur police aux TextViews
                            time.setTypeface(RobotoFont);

                            //Application de la taille de leur police aux TextViews
                            time.setTextSize((float) (GoldNumber * 25 * S));

                            //Application de la couleur de la police des TextViews
                            time.setTextColor(Color.parseColor("#FFFFFF"));

                            //Initialisation des paramètres des TextViews
                            RelativeLayout.LayoutParams timeParams = new
                                    RelativeLayout.LayoutParams((int) (100 * GoldNumber * W), (int) (55 * GoldNumber * H));

                            //Application des marges aux paramètres des TextViews
                            timeParams.setMargins(0, (int) (GoldNumber * H * PositionY3 - 50 - (((55 + SMSHeight) / 2))), 0, 0);
                            timeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                            //Application de leur paramètre aux TextViews
                            time.setLayoutParams(timeParams);

                            //Application de la gravité dans les TextViews
                            time.setGravity(Gravity.CENTER);

                            //Mise en place des TextViews dans le Layout principal
                            SMSRelativeLayout.addView(time);

                            //Nettoyage de l'EditText
                            WriteSMS.setText("");

                            //Scroll Down dans le ScrollView
                            SMSConversationSV.post(new Runnable() {
                                @Override
                                public void run() {
                                    SMSConversationSV.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });

                            //Hide de la ScrollBar du ScrollView
                            SMSConversationSV.setVerticalScrollBarEnabled(false);
                            SMSConversationSV.setHorizontalScrollBarEnabled(false);
                        }
                    });

                    //Listener pour EditText on clique sur send
                    WriteSMS.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                //Envoi du SMS
                                String number = null;
                                for (int f = 0; f < SMS.size(); f++) {
                                    if (SMS.get(f).get(3).equals(ContactName)) {
                                        number = SMS.get(f).get(4);
                                    }
                                }
                                ReallySendSMS(number, String.valueOf(WriteSMS.getText()), MN);
                                //Récupération des détails du SMS
                                Date date = new Date();
                                String minutes = String.valueOf(date.getMinutes());
                                if (minutes.length() != 2) {
                                    minutes = "0" + minutes;
                                }
                                String hours = String.valueOf(date.getHours());
                                if (hours.length() != 2) {
                                    hours = "0" + hours;
                                }
                                Editable message = WriteSMS.getText();

                                //Initialisation des boutons
                                final Button thisSMS = new Button(MN);

                                //Initialisation des paramètres des boutons
                                RelativeLayout.LayoutParams thisSMSParams = new
                                        RelativeLayout.LayoutParams((int) (600 * GoldNumber * W), LayoutParams.WRAP_CONTENT);

                                //Application des marges aux paramètres des Buttons
                                thisSMSParams.setMargins(0, (int) (PositionY3 * GoldNumber * H), (int) (100 * GoldNumber * W), 30);

                                //Application des Backgrounds aux Buttons
                                thisSMS.setBackground(MN.getResources().getDrawable(R.drawable.sms_bar));

                                //Distribution de leur texte aux Buttons
                                thisSMS.setText(message);

                                //Application des polices aux Buttons
                                thisSMS.setTypeface(RobotoFont);

                                //Application des tailles des polices aux Buttons
                                thisSMS.setTextSize((float) (GoldNumber * 25 * S));

                                //Application des couleurs de polices aux Buttons
                                thisSMS.setTextColor(Color.parseColor("#FFFFFF"));

                                //Application de la gravité dans les Buttons
                                thisSMS.setGravity(Gravity.CENTER);

                                //Application de leur paramètre aux Buttons
                                thisSMS.setLayoutParams(thisSMSParams);

                                //Ajout de la View au Layout
                                SMSRelativeLayout.addView(thisSMS);

                                //Incrémentation de la PositionY
								/*if(message.length() > 25){
									String Longueur = String.valueOf(message.length()/25 + 1);
									if(Longueur.contains(".")){
										Longueur.substring(0, Longueur.indexOf(".") - 1);
										PositionY2 = PositionY2 +  100 + 25 * (Integer.parseInt(Longueur));
									}else{
										PositionY2 = PositionY2 + 100 + 25 * (Integer.parseInt(Longueur));
									}
								}else{
									PositionY2 = PositionY2 + 100;
								}*/
                                Rect bounds = new Rect();
                                Paint paint = new Paint();
                                paint.setTextSize((float) (GoldNumber * 25 * S));
                                paint.getTextBounds(String.valueOf(WriteSMS.getText()), 0, String.valueOf(WriteSMS.getText()).length(), bounds);

                                int width = (int) Math.ceil((float) bounds.width() / (600 * GoldNumber * W - (100 * GoldNumber * W)));
                                //PositionY2 = PositionY2 + 50 + (int) ( 5 * GoldNumber * 25 * S * width);
                                int SMSHeight = getHeight(MN, message, (int) (GoldNumber * 25 * S), (int) (500 * GoldNumber * W), RobotoFont, (int) (20 * W));
                                PositionY3 = PositionY3 + 50 + SMSHeight;

                                //Affichage de l'heure à laquelle a été envoyée le sms
                                //Initialisation du TextView
                                TextView time = new TextView(MN);

                                //Application de leur texte aux TextViews
                                time.setText(hours + ":" + minutes);

                                //Application de leur police aux TextViews
                                time.setTypeface(RobotoFont);

                                //Application de la taille de leur police aux TextViews
                                time.setTextSize((float) (GoldNumber * 25 * S));

                                //Application de la couleur de la police des TextViews
                                time.setTextColor(Color.parseColor("#FFFFFF"));

                                //Initialisation des paramètres des TextViews
                                RelativeLayout.LayoutParams timeParams = new
                                        RelativeLayout.LayoutParams((int) (100 * GoldNumber * W), (int) (55 * GoldNumber * H));

                                //Application des marges aux paramètres des TextViews
                                timeParams.setMargins(0, (int) (GoldNumber * H * PositionY3 - 50 - (((55 + SMSHeight) / 2))), 0, 0);
                                timeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                                //Application de leur paramètre aux TextViews
                                time.setLayoutParams(timeParams);

                                //Application de la gravité dans les TextViews
                                time.setGravity(Gravity.CENTER);

                                //Mise en place des TextViews dans le Layout principal
                                SMSRelativeLayout.addView(time);

                                //Nettoyage de l'EditText
                                WriteSMS.setText("");

                                //Scroll Down dans le ScrollView
                                SMSConversationSV.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        SMSConversationSV.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });

                                return true;
                            }
                            return true;
                        }
                    });

                    //Empêchement de l'abaissement du Keyboard


                    //Application de leur paramètre aux Buttons
                    SendSMS.setLayoutParams(SendSMSParams);
                    WriteSMS.setLayoutParams(WriteSMSParams);

                    //Ajout des Views au Layout
                    MainStelaRL.addView(SendSMS);
                    MainStelaRL.addView(WriteSMS);

                }
            });

            //Mise en place des Buttons dans le Layout principal
            SMSScrollViewRL.addView(ContactDisplay);

        }
        //Ajout du Layout au ScrollView
        SMSScrollView.addView(SMSScrollViewRL);

        //Hide de la ScrollBar du ScrollView
        SMSScrollView.setVerticalScrollBarEnabled(false);
        SMSScrollView.setHorizontalScrollBarEnabled(false);

        //Ajout du ScrollView au Layout principal
        MainStelaRL.addView(SMSScrollView);

        return MainStelaRL;
    }
}

