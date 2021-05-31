package com.example.fitnessapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Savesharepreferences {
    Context context;
    SharedPreferences sharedPreferences;

    public Savesharepreferences(Context context)
        {
            this.context=context;
            sharedPreferences=context.getSharedPreferences("apreferences",Context.MODE_PRIVATE);

        }
        public void setState(boolean bo){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putBoolean("editor",bo);
            editor.apply();
        }
        public boolean getstate(){
            return sharedPreferences.getBoolean("editor",false);
        }

}
