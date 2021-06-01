package com.example.moove.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.moove.R;
import com.example.moove.database.DBManager;
import com.example.moove.exceptions.UninitializedDatabaseException;
import com.example.moove.models.User;
import com.example.moove.utilities.heartrate.CameraService;
import com.example.moove.utilities.heartrate.OutputAnalyzer;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HeartFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    public static final int REQUEST_CODE_CAMERA = 0;
    public static final int MESSAGE_UPDATE_REALTIME = 1;
    public static final int MESSAGE_UPDATE_FINAL = 2;
    public static final int MESSAGE_CAMERA_NOT_AVAILABLE = 3;

    private OutputAnalyzer analyzer;
    private View view;

    @SuppressLint("HandlerLeak")
    private Handler mainHandler;

    private CameraService cameraService;

    @Override
    public void onResume() {
        super.onResume();

        if (view == null)
            return;

        analyzer = new OutputAnalyzer(this.getActivity(), view.findViewById(R.id.graphTextureView), mainHandler);

        TextureView cameraTextureView = view.findViewById(R.id.textureView2);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();

        if (previewSurfaceTexture != null) {
            // this first appears when we close the application and switch back
            // - TextureView isn't quite ready at the first onResume.
            Surface previewSurface = new Surface(previewSurfaceTexture);

            // show warning when there is no flash
            if (!Objects.requireNonNull(getContext()).getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                Snackbar.make(
                    view.findViewById(R.id.constraintLayout),
                    getString(R.string.noFlashWarning),
                    Snackbar.LENGTH_LONG
                ).show();
            }

            cameraService.start(previewSurface);
            analyzer.measurePulse(cameraTextureView, cameraService);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraService.stop();

        if (analyzer != null)
            analyzer.stop();

        if (view == null)
            return;

        analyzer = new OutputAnalyzer(this.getActivity(), view.findViewById(R.id.graphTextureView), mainHandler);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(
        @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.heartrate_fragment, container, false);

        ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()),
            new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                if (msg.what == MESSAGE_UPDATE_REALTIME)
                    ((TextView) view.findViewById(R.id.textView)).setText(msg.obj.toString());

                if (msg.what == MESSAGE_UPDATE_FINAL) {
                    User.currentUser.setLastHeartRate(Integer.parseInt(msg.obj.toString().split(" BPM")[0]));

                    Map<String, Integer> dataMap = new HashMap<>();
                    dataMap.put("lastHeartRate", User.currentUser.getLastHeartRate());

                    try {
                        DBManager.getInstance().updateUser(dataMap, User.currentUser.getId());
                    } catch (UninitializedDatabaseException e) {
                        e.printStackTrace();
                    }
                }

                if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE) {
                    ((TextView) view.findViewById(R.id.textView)).setText(R.string.camera_not_found);
                    analyzer.stop();
                }
            }
        };

        cameraService = new CameraService(this.getActivity(), mainHandler);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(
                    view.findViewById(R.id.constraintLayout),
                    getString(R.string.cameraPermissionRequired),
                    Snackbar.LENGTH_LONG
                ).show();
            }
        }
    }
}
