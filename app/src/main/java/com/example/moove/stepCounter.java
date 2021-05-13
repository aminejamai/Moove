package com.example.moove;




import android.app.Activity;
import android.content.Context;
import android.hardware.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import static android.content.Context.SENSOR_SERVICE;

public class stepCounter extends Fragment implements SensorEventListener {

    private Context context;
   public static int calories;
    // Reference to the sensor manager

    private SensorManager sensorManager;

    // The sensors to be employed

    private Sensor senAccelerometer;
    private Sensor senStepCounter;
    private Sensor senStepDetector;
    public static String duration;
    // Properties to store step data

    private int stepsTaken = 0;
    private int reportedSteps = 0;
    private int stepDetector = 0;

    // GUI Components to display data

    private TextView countText;
    private TextView detectText;
    private TextView accelText;
    private TextView time;
    public static boolean started=false;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View inflatedView = inflater.inflate(R.layout.steps, container, false);



        context=getActivity();


        // Reference the GUI components

        detectText = (TextView)inflatedView.findViewById(R.id.count);
        countText = (TextView)inflatedView.findViewById(R.id.count2);
        accelText = (TextView)inflatedView.findViewById(R.id.count3);
        time = (TextView)inflatedView.findViewById(R.id.timer);
        // Reference/Assign the sensor manager

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);

        // Reference/Assign the sensors

        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        senStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // Register the sensors for event callback

        registerSensors();



        Thread thread = new Thread() {


            long startTimer=System.currentTimeMillis();
            @Override
            public void run() {

                int temp = 0;
                while (started) {


                    long  timer =  System.currentTimeMillis()
                            - startTimer;

                    time.setText(getFormatTime(timer));
                    duration=getFormatTime(timer);
                }

            }

        };

        if(stepCounter.started==true){
            thread.start();
        }



        return inflatedView;

    }



    private String getFormatTime(long time) {
        time = time / 1000;
        long second = time % 60;
        long minute = (time % 3600) / 60;
        long hour = time / 3600;


        String strSecond = ("00" + second)
                .substring(("00" + second).length() - 2);

        String strMinute = ("00" + minute)
                .substring(("00" + minute).length() - 2);
        String strHour = ("00" + hour).substring(("00" + hour).length() - 2);

        return strHour + ":" + strMinute + ":" + strSecond;

    }




/*
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps);

        // Reference the context

        //context = this;

        // Check to see if the device has capability for step detection
        // If not show a toast to the user detailing so.

        if(!HasGotSensorCaps()){
            showToast("Required sensors not supported on this device!");
            return;
        }

        // Reference the GUI components

        detectText = (TextView)inflatedView.findViewById(R.id.count);
        countText = (TextView)inflatedView.findViewById.(R.id.count2);
        accelText = (TextView)inflatedView.findViewById(R.id.count3);

        // Reference/Assign the sensor manager

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);

        // Reference/Assign the sensors

        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        senStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // Register the sensors for event callback

        registerSensors();
    }*/





    // Method registered with the sensor manager which is called upon a sensor
    // update event, this is set via the registerSensors() method.
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        // Get the sensor which has triggered the event

        Sensor sensor = event.sensor;

        // Perform differing functionality depending upon
        // the sensor type (caller)

        switch (event.sensor.getType())
        {
            case Sensor.TYPE_STEP_COUNTER:

                if (reportedSteps < 1){

                    // Log the initial value

                    reportedSteps = (int)event.values [0];
                }

                // Calculate steps taken based on
                // first value received.

                stepsTaken = (int)event.values [0] - reportedSteps;

                // Output the value to the simple GUI

                countText.setText("Cnt: " + stepsTaken);

                break;

            case Sensor.TYPE_STEP_DETECTOR:

                // Increment the step detector count

                stepDetector++;

                // Output the value to the simple GUI

                detectText.setText("Det: " + stepDetector);

                break;

            case  Sensor.TYPE_ACCELEROMETER:

                // Get the accelerometer values and set them to a string with 2dp

                String x = String.format("%.02f", event.values[0]);
                String y = String.format("%.02f", event.values[1]);
                String z = String.format("%.02f", event.values[2]);

                // Output the string to the GUI

                accelText.setText("Acc:" + x + "," + y + "," + z);

                break;
        }
    }

    // Method registered with the sensor manager which is called upon a sensor
    // accuracy update event, this is set via the registerSensors() method.
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause()
    {
        super.onPause();

        // Un-Register the sensors

        unRegisterSensors();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Register the sensors

        registerSensors();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // Un-Register the sensors

        unRegisterSensors();
    }

    // Method to check that the running device has the required capability to
    // perform step detection.
    public boolean HasGotSensorCaps()
    {
        PackageManager pm = context.getPackageManager();

        // Require at least Android KitKat

        int currentApiVersion = Build.VERSION.SDK_INT;

        // Check that the device supports the step counter and detector sensors

        return currentApiVersion >= 19
                && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
                && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
                && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
    }

    // Simple function that registers all of the required sensor listeners
    // with the sensor manager.
    private void registerSensors()
    {
        // Double check that the device has the required sensor capabilities

        if(!HasGotSensorCaps()){

            Toast.makeText(context, "Required sensors not supported on this device!", Toast.LENGTH_SHORT).show();

            return;
        }

        // Provide a little feedback via a toast

        Toast.makeText(context, "Registering sensors!", Toast.LENGTH_SHORT).show();

        // Register the listeners. Used for receiving notifications from
        // the SensorManager when sensor values have changed.

        sensorManager.registerListener(this, senStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, senStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Simple function that un-registers all of the required sensor listeners
    // from the sensor manager.
    private void unRegisterSensors()
    {
        // Double check that the device has the required sensor capabilities
        // If not then we can simply return as nothing will have been already
        // registered
        if(!HasGotSensorCaps()){
            return;
        }

        // Perform un-registration of the sensor listeners

        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }


}

/**
 * <copyright file="SimpleStepActivity.java" company="dyadica.co.uk">
 * Copyright (c) 2010, 2015 All Right Reserved, http://www.dyadica.co.uk
 * This source is subject to the dyadica.co.uk Permissive License.
 * Please see the http://www.dyadica.co.uk/permissive-license file for more information.
 * All other rights reserved.
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 * </copyright>
 * <author>SJB</author>
 * <contact>https://www.facebook.com/ADropInTheDigitalOcean</contact>
 * <date>24.11.2013</date>
 * <summary>A simple pedometer script for Android which makes use of dedicated low-power step detecting chips</summary>

 **/