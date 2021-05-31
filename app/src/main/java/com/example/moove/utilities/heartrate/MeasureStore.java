package com.example.moove.utilities.heartrate;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class MeasureStore {
    private final CopyOnWriteArrayList<Measurement<Integer>> measurements = new CopyOnWriteArrayList<>();
    private int minimum = 2147483647;
    private int maximum = -2147483648;

    private final int rollingAverageSize = 4;

    public void add(int measurement) {
        Measurement<Integer> measurementWithDate = new Measurement<>(new Date(), measurement);

        measurements.add(measurementWithDate);
        if (measurement < minimum) minimum = measurement;
        if (measurement > maximum) maximum = measurement;
    }

    public CopyOnWriteArrayList<Measurement<Float>> getStdValues() {
        CopyOnWriteArrayList<Measurement<Float>> stdValues = new CopyOnWriteArrayList<>();

        for (int i = 0; i < measurements.size(); i++) {
            int sum = 0;
            for (int rollingAverageCounter = 0; rollingAverageCounter < rollingAverageSize;
                 rollingAverageCounter++)
                sum += measurements.get(Math.max(0, i - rollingAverageCounter)).measurement;

            Measurement<Float> stdValue =
                    new Measurement<>(
                            measurements.get(i).timestamp,
                            ((float)sum / rollingAverageSize - minimum ) / (maximum - minimum)
                    );
            stdValues.add(stdValue);
        }

        return stdValues;
    }

    public CopyOnWriteArrayList<Measurement<Integer>> getLastStdValues(int count) {
        if (count < measurements.size())
            return new CopyOnWriteArrayList<>(measurements.subList(measurements.size() - 1 - count, measurements.size() - 1));
        else
            return measurements;
    }

    public Date getLastTimestamp() {
        return measurements.get(measurements.size() - 1).timestamp;
    }
}
