package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton
    }
    public void HomepageBtn(View view){
        Intent intent = new Intent(Homepage.this, Homepage.class);  //panggilPage
        startActivity(intent);
        finish();
    }
}
