package com.example.h0136544.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BlackLight";
    private SensorManager mySensorManager;
    private TextView als_lable;
    private TextView als_view;
    private TextView backlight_lable;
    private TextView backlight_view;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private void delay(int ms) {
        try{
            Thread.currentThread();
            Thread.sleep(ms);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

    }
    private int getSystemBrightness() {
        int systemBrightnes = 0;
        try {
            systemBrightnes = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "systemBrightnes " + systemBrightnes);
        return systemBrightnes;
    }
/*
    private void dispalyBrightness() throws InterruptedException {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String mBackLight = Integer.toString(getSystemBrightness());
                    //backlight_view.setText(mBackLight);
                    delay(1000);
                }
            }
        });

        String mBackLight;
        while(true){
            mBackLight = Integer.toString(getSystemBrightness());
            backlight_view.setText(mBackLight);
            delay(1000);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        als_lable = (TextView) findViewById(R.id.als_lable);
        als_view = (TextView) findViewById(R.id.als_view);
        backlight_lable = (TextView) findViewById(R.id.backlight_lable);
        backlight_view = (TextView) findViewById(R.id.backlight_view);


        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensorManager.registerListener(myLightSensor,
                mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        new BacklightTread().start();
        /*
        try {
            dispalyBrightness();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        */
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    final SensorEventListener myLightSensor = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
                String mValue = Float.toString(sensorEvent.values[0]);
                Log.i(TAG, "Light value " + mValue);
                //String mBackLight = Integer.toString(getSystemBrightness());
                als_view.setText(mValue);
                //backlight_view.setText(mBackLight);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            int baclklight = 0;
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    baclklight = getSystemBrightness();
                    backlight_view.setText(Integer.toString(baclklight));
            }
        }
    };

    class BacklightTread extends Thread{
        public void run() {
            do {
                try{
                    Thread.sleep(1000);
                    android.os.Message msg = new Message();
                    msg.what = 1;  //Don't have any mean
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(true);
        }
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}