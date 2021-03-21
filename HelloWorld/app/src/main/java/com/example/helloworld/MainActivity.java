package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Hello World MainActivity", "onCreate gets executed");
//        Log.i(TAG, "onCreate: ");
//        Log.w(TAG, "onCreate: ", );
//        Log.e(TAG, "onCreate: ", );
        Log.d(TAG, "onCreate: this log's tag is auto-generated.");
        Log.d("data", "onCreate: this log can match data log filter");
    }
}