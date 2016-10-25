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

public class TemperatureSensor extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mTemperature;
    public static String CurrentTemperature = "20°";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Récupération de la température et du taux d'humidité
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        CurrentTemperature = String.valueOf(event.values[0]) + "°";
        System.out.println("TEMPERATURE " + CurrentTemperature);
        Toast.makeText(this, "TEMPERATURE " + CurrentTemperature, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}

