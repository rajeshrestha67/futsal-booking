package com.futsalrecord.futsalApp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.futsalrecord.futsalApp.R;
import com.futsalrecord.futsalApp.activities.futsal.FutsalAboutUsActivity;
import com.futsalrecord.futsalApp.bll.LoginBLL;
import com.futsalrecord.futsalApp.strictMode.StrictModeClass;
import com.futsalrecord.futsalApp.url.Url;


public class SplashActivity extends AppCompatActivity {

    private String futsalName, futsalPassword;
    private SensorManager sensorManager;

    final static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    private final int PERMISSION_REQUEST_CODE= 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkUserProfile()) {
                    proxForCloseApp();
                    Intent intent = new Intent(SplashActivity.this, FutsalDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    proxForCloseApp();
                    Intent intent = new Intent(SplashActivity.this, GettingStarted.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }



    private void navigateToDashboard(){
        boolean status = hasPermissions(SplashActivity.this, PERMISSIONS);
        if (status) {
            //gettingStarted();
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }



    private boolean hasPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    navigateToDashboard();
                } else {
                    Toast.makeText(this, getString(R.string.pleaseGrantAllRequestedpermission), Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, PERMISSION_REQUEST_CODE);
                }
        }
    }



    private Boolean checkUserProfile() {
        SharedPreferences sharedPreferencesUser = this.getSharedPreferences("Futsal", MODE_PRIVATE);
        futsalName = sharedPreferencesUser.getString("FutsalName", null);
        futsalPassword = sharedPreferencesUser.getString("FutsalPassword", null);

        //Night mode
//        SharedPreferences sharedPreferencesMode = getSharedPreferences("nightModePrefs", MODE_PRIVATE);
//        Boolean darkMode = sharedPreferencesMode.getBoolean("isNightMode", false);

        if (futsalName != null && futsalPassword != null) {
            login();
//            if (sharedPreferencesMode.getBoolean("isNightMode", false)) {
//                darkMode.equals(true);
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            } else {
//                darkMode.equals(false);
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            }
            return true;
        } else {
//            if (sharedPreferencesMode.getBoolean("isNightMode", false)) {
//                darkMode.equals(true);
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            } else {
//                darkMode.equals(false);
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            }
            return false;
        }
    }

    private void proxForCloseApp() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        SensorEventListener proxListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] <= 1) {
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        if (sensor != null) {
            sensorManager.registerListener(proxListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "No sensor found", Toast.LENGTH_SHORT).show();
        }

    }

    private void login() {
        String futsalNameLogin = futsalName;
        String futsalPasswordLogin = futsalPassword;

        LoginBLL loginBLL = new LoginBLL();
        StrictModeClass.StrictMode();
        if (loginBLL.checkFutsal(futsalNameLogin, futsalPasswordLogin)) {
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Token expired", Toast.LENGTH_SHORT).show();
        }
    }
}
