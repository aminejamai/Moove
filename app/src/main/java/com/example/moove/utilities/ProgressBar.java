package com.example.moove.utilities;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.moove.R;

public class ProgressBar {
    public static ProgressDialog createCircularDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return progressDialog;
    }
}
