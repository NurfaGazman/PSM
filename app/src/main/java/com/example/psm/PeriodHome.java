package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PeriodHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}