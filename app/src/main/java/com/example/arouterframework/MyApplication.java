package com.example.arouterframework;

import android.app.Application;

import com.example.arouter.ARouter;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.getInstance().init(this);
    }
}
