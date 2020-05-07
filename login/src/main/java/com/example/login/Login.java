package com.example.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.annotations.BindPath;

@BindPath("login/login")
public class Login extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
