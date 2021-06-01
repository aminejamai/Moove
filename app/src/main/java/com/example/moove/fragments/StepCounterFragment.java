package com.example.moove.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.moove.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import static android.content.Context.SENSOR_SERVICE;

public class StepCounterFragment extends Fragment implements SensorEventListener {
    public static final int REQUEST_CODE_ACTIVITY_RECOGNITION = 1;

    private View inflatedView;
    private SensorManager sensorManager;

    private Sensor senAccelerometer;
    private Sensor senStepCounter;
    private Sensor senStepDetector;
    public static String duration;

    private int reportedSteps = 0;
    private int stepDetector = 0;

    private TextView detectText;
    private TextView time;
    public static boolean started = false;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.step_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()),
                new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQUEST_CODE_ACTIVITY_RECOGNITION);
        }

        detectText = (TextView) inflatedView.findViewById(R.id.count);
        time = (TextView) inflatedView.findViewById(R.id.timer);

        sensorManager = (SensorManager) Objects.requireNonNull(getContext())
            .getSystemService(SENSOR_SERVICE);

        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        senStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        registerSensors();

        Handler handler = new Handler(Looper.getMainLooper());

        Thread thread = new Thread(() -> {
            final long startTimer = System.currentTimeMillis();
            while (started) {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long timer = System.currentTimeMillis() - startTimer;
                duration = getFormatTime(timer);
                handler.post(() -> time.setText("Time elapsed\n" + duration));
            }
        });

        if (StepCounterFragment.started)
            thread.start();

        return inflatedView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(
                    inflatedView.findViewById(R.id.linearLayout),
                    getString(R.string.recognitionPermissionRequired),
                    Snackbar.LENGTH_LONG
                ).show();
            }
        }
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get the sensor which has triggered the event
        Sensor sensor = event.sensor;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_STEP_COUNTER:
                if (reportedSteps < 1) {
                    // Log the initial value
                    reportedSteps = (int) event.values[0];
                }
                break;

            case Sensor.TYPE_STEP_DETECTOR:
                stepDetector++;
                detectText.setText("Number of steps\n" + stepDetector);
                break;

            case Sensor.TYPE_ACCELEROMETER:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onPause() {
        super.onPause();

        unRegisterSensors();
    }

    @Override
    public void onResume() {
        super.onResume();

        registerSensors();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unRegisterSensors();
    }

    public boolean HasGotSensorCaps() {
        PackageManager pm = Objects.requireNonNull(getContext()).getPackageManager();

        int currentApiVersion = Build.VERSION.SDK_INT;

        return currentApiVersion >= 19
            && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
            && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
            && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
    }

    private void registerSensors() {
        if (!HasGotSensorCaps()) {
            Toast.makeText(getContext(), "Required sensors not supported on this device!",
                Toast.LENGTH_SHORT).show();
            return;
        }

        sensorManager.registerListener(this, senStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, senStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unRegisterSensors() {
        if (!HasGotSensorCaps())
            return;

        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR));
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }
}
