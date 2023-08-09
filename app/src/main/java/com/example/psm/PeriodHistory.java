package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityPeriodHistoryBinding;
import com.example.psm.databinding.ActivityPeriodHomeBinding;

public class PeriodHistory extends AppCompatActivity {

    private ActivityPeriodHistoryBinding binding;
    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPeriodHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton

        binding.btnNewRecord.setOnClickListener(this::goToNewRecord);
        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

    }


    public void goToNewRecord(View view){
        Intent intent = new Intent(PeriodHistory.this, InsertPeriod.class);  //panggilPage
        startActivity(intent);
    }

}