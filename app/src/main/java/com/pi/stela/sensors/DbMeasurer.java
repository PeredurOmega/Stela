package com.pi.stela.sensors;

/**
 * Created by Paul on 08/10/2016.
 */

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.AsyncTask;

public class DbMeasurer extends AsyncTask<Void, Void, Void>{
    MediaRecorder mRecorder;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    @Override
    protected Void doInBackground(Void... params) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile("/dev/null");
        try {
            mRecorder.prepare();
            mRecorder.start();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                        System.out.println("DB PASS");
                        System.out.println("DB PASS" + soundDb(getAmplitude()) + " dB");
                        run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public double soundDb(double amp){
        return  20 * Math.log10(getAmplitudeEMA() / amp);
    }
    public double getAmplitude() {
        if (mRecorder != null){
            return  (mRecorder.getMaxAmplitude());
        }else{
            return 0;
        }
    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
}

