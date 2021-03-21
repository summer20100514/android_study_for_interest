package com.example.activitytest;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {
    private static final String TAG = "FirstActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        Button button1 = (Button)findViewById(R.id.button_1);
        Log.d(TAG, "onCreate: button1 id is(button1.getId()) " + button1.getId());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v;
                Log.d(TAG, "onClick: view id is(v.getId()) " + v.getId());
                Log.d(TAG, "onClick: view id is(btn.getId()) " + btn.getId());
                Toast.makeText(FirstActivity.this, "You clicked button 1", Toast.LENGTH_SHORT).show();
            }
        });
    }
}