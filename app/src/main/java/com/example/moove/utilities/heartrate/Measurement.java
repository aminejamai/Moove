package com.example.moove.utilities.heartrate;

import java.util.Date;

class Measurement<T> {
    final Date timestamp;
    final T measurement;

    Measurement(Date timestamp, T measurement) {
        this.timestamp = timestamp;
        this.measurement = measurement;
    }
}
