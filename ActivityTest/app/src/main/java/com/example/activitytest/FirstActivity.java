package com.example.activitytest;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {
    private static final String TAG = "FirstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        Log.d(TAG, "onCreate: ");
        Button button1 = (Button) findViewById(R.id.button_1);
        Log.d(TAG, "onCreate: button1 id is(button1.getId()) " + button1.getId());
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button btn = (Button) v;
//                Log.d(TAG, "onClick: view id is(v.getId()) " + v.getId());
//                Log.d(TAG, "onClick: view id is(btn.getId()) " + btn.getId());
//                Toast.makeText(FirstActivity.this, "You clicked button 1", Toast.LENGTH_SHORT).show();

//                finish(); // destroy activity

//                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
//                startActivity(intent);

//                Intent intent = new Intent("com.example.activitytest.ACTION_START");
//                intent.addCategory("com.example.activitytest.MY_CATEGORY");
//                startActivity(intent);

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("https://summerxu.net"));
//                startActivity(intent);

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:13691163628"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add_item:
                Toast.makeText(this, "You clicked Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.remove_item:
                Toast.makeText(this, "You clicked Remove", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}