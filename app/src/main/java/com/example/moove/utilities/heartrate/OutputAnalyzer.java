package com.example.moove.utilities.heartrate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;

import com.example.moove.R;
import com.example.moove.fragments.HeartFragment;

import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

public class OutputAnalyzer {
    private final Activity activity;

    private final ChartDrawer chartDrawer;

    private MeasureStore store;

    private final int measurementInterval = 45;
    private final int measurementLength = 20000; // ensure the number of data points is the power of two
    private final int clipLength = 3500;

    private int detectedValleys = 0;
    private int ticksPassed = 0;

    private final CopyOnWriteArrayList<Long> valleys = new CopyOnWriteArrayList<>();

    private CountDownTimer timer;

    private final Handler mainHandler;

    public OutputAnalyzer(Activity activity, TextureView graphTextureView, Handler mainHandler) {
        this.activity = activity;
        this.chartDrawer = new ChartDrawer(graphTextureView);
        this.mainHandler = mainHandler;
    }

    private boolean detectValley() {
        final int valleyDetectionWindowSize = 13;
        CopyOnWriteArrayList<Measurement<Integer>> subList = store.getLastStdValues(valleyDetectionWindowSize);
        if (subList.size() < valleyDetectionWindowSize)
            return false;
        else {
            Integer referenceValue = subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement;

            for (Measurement<Integer> measurement : subList)
                if (measurement.measurement < referenceValue) return false;

            // filter out consecutive measurements due to too high measurement rate
            return (!subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement.equals(
                subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f) - 1).measurement));
        }
    }

    public void measurePulse(TextureView textureView, CameraService cameraService) {
        // 20 times a second, get the amount of red on the picture.
        // detect local minimums, calculate pulse.

        store = new MeasureStore();

        detectedValleys = 0;

        timer = new CountDownTimer(measurementLength, measurementInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                // skip the first measurements, which are broken by exposure metering
                if (clipLength > (++ticksPassed * measurementInterval)) return;

                Thread thread = new Thread(() -> {
                    Bitmap currentBitmap = textureView.getBitmap();
                    if (currentBitmap == null)
                        return;

                    int pixelCount = textureView.getWidth() * textureView.getHeight();
                    int measurement = 0;
                    int[] pixels = new int[pixelCount];

                    currentBitmap.getPixels(pixels, 0, textureView.getWidth(), 0, 0,
                        textureView.getWidth(), textureView.getHeight());

                    // extract the red component
                    // https://developer.android.com/reference/android/graphics/Color.html#decoding
                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++)
                        measurement += (pixels[pixelIndex] >> 16) & 0xff;
                    // max int is 2^31 (2147483647) , so width and height can be at most 2^11,
                    // as 2^8 * 2^11 * 2^11 = 2^30, just below the limit

                    store.add(measurement);

                    if (detectValley()) {
                        detectedValleys = detectedValleys + 1;
                        valleys.add(store.getLastTimestamp().getTime());
                        // in 13 seconds (13000 milliseconds), I expect 15 valleys. that would be a pulse of 15 / 130000 * 60 * 1000 = 69

                        String currentValue = String.format(
                            Locale.getDefault(),
                            activity.getResources().getQuantityString(R.plurals.measurement_output_template, detectedValleys),
                            (valleys.size() == 1)
                                ? Math.round((60f * (detectedValleys) / (Math.max(1, (measurementLength - millisUntilFinished - clipLength) / 1000f))))
                                : Math.round((60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f))))
                        );

                        sendMessage(HeartFragment.MESSAGE_UPDATE_REALTIME, currentValue);
                    }

                    // draw the chart on a separate thread.
                    Thread chartDrawerThread = new Thread(() -> chartDrawer.draw(store.getStdValues()));
                    chartDrawerThread.start();
                });
                thread.start();
            }

            @Override
            public void onFinish() {
                CopyOnWriteArrayList<Measurement<Float>> stdValues = store.getStdValues();

                // clip the interval to the first till the last one - on this interval, there were detectedValleys - 1 periods

                // If the camera only provided a static image, there are no valleys in the signal.
                // A camera not available error is shown, which is the most likely cause.
                if (valleys.size() == 0) {
                    mainHandler.sendMessage(Message.obtain(
                        mainHandler,
                        HeartFragment.MESSAGE_CAMERA_NOT_AVAILABLE,
                        "No valleys detected - there may be an issue when accessing the camera.")
                    );
                    return;
                }

                String currentValue = String.format(
                    Locale.getDefault(),
                    activity.getResources().getQuantityString(R.plurals.measurement_output_template, detectedValleys - 1),
                    Math.round(60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f)))
                );

                sendMessage(HeartFragment.MESSAGE_UPDATE_FINAL, currentValue);

                if (cameraService != null)
                    cameraService.stop();
            }
        };

        timer.start();
    }

    public void stop() {
        if (timer != null)
            timer.cancel();
    }

    void sendMessage(int what, Object message) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = message;
        mainHandler.sendMessage(msg);
    }
}
