package com.information.rxjavaapplication;

import android.app.Application;

import com.information.rxjavaapplication.volley.LocalVolley;

public class RxAndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocalVolley.init(getApplicationContext());
    }
}
