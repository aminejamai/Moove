package com.example.moove.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHandler {
    Context context;
    SharedPreferences sharedPreferences;

    public SharedPrefsHandler(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("apreferences",Context.MODE_PRIVATE);
    }

    public void setState(boolean value) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("editor", value);
        editor.apply();
    }

    public boolean getState() {
        return sharedPreferences.getBoolean("editor", false);
    }
}
