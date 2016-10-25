package com.pi.stela.sensors;

/**
 * Created by Paul on 08/10/2016.
 */
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

public class HumiditySensor extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mHumidity;
    public static String CurrentHumidity = "22%";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Récupération de la température et du taux d'humidité
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mHumidity = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        CurrentHumidity = String.valueOf(event.values[0]) + "%";
        System.out.println("HUMIDITY " + CurrentHumidity);
        Toast.makeText(this, "HUMIDITY " + CurrentHumidity, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mHumidity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}

