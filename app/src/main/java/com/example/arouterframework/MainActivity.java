package com.example.arouterframework;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.annotations.BindPath;
import com.example.login.Login;

@BindPath("main/main")
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view) {
        Log.e("MainActivity", "btnclick");
        startActivity(new Intent(this, Login.class));
    }
}
