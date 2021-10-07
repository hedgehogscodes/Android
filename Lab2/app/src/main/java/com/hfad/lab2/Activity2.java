package com.hfad.lab2;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
    }


    @Override
    public void onBackPressed(){
        try {

            Intent intent = new Intent(Activity2.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception e) {}
    }
}
