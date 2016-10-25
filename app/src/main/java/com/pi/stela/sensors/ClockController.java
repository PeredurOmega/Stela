package com.pi.stela.sensors;

/**
 * Created by Paul on 08/10/2016.
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.pi.stela.main.MainActivity;

import android.os.AsyncTask;
import android.os.Looper;
import android.widget.TextView;

public class ClockController extends AsyncTask<Void, Void, Void> {
    private TextView TimeDisplay;
    public ClockController(TextView timeDisplay){
        TimeDisplay = timeDisplay;
    }
    @Override
    protected Void doInBackground(Void... params) {
        try {
            synchronized(TimeDisplay){
                System.out.println("will wait");
                TimeDisplay.wait(GetRestSecond());
                System.out.println("has waited");
                ChangeTime();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }
        }
        return null;
    }
    public void ChangeTime(){
        System.out.println("CHANGINGTIME " + GetCurrentTime());
        new Thread(new Runnable() {
            public void run() {
                System.out.println("TIMECHANGE ");
                MainActivity MN = MainActivity.MN;
                MN.runOnUiThread(new Runnable() {
                    public void run() {
                        System.out.println("Is changing Time " + GetCurrentTime());
                        TimeDisplay.setText(GetCurrentTime());
                        //MainActivity.ChangeText(TimeDisplay, GetCurrentTime());
                        ClockController TimeController = new ClockController(TimeDisplay);
                        TimeController.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        System.out.println("Has recalled himself");
                    }
                });
            }
        }).run();;
    }
    public int GetRestSecond(){
        Calendar MainCalendar = Calendar.getInstance();
        SimpleDateFormat TimeFormat = new SimpleDateFormat("ms");
        String CurrentTime = TimeFormat.format(MainCalendar.getTime());
        System.out.println(CurrentTime);
        int CurrentSecond = 60000-Integer.parseInt(CurrentTime);
        if(CurrentSecond == 0){
            CurrentSecond = 1000;
        }
        return CurrentSecond;
    }
    public String GetCurrentTime(){
        Calendar MainCalendar = Calendar.getInstance();
        SimpleDateFormat TimeFormat = new SimpleDateFormat("HH:mm");
        String CurrentTime = TimeFormat.format(MainCalendar.getTime());
        System.out.println(CurrentTime);
        return CurrentTime;
    }
}

