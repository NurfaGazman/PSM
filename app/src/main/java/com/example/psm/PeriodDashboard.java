package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityPeriodDashboardBinding;
import com.example.psm.databinding.ActivityPeriodHomeBinding;

public class PeriodDashboard extends AppCompatActivity {

    private ActivityPeriodDashboardBinding binding;
    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton

        binding = ActivityPeriodDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;
        binding.btnHistory.setOnClickListener(this::goToHistory);

    }

    public void goToHistory(View view){
        Intent intent = new Intent(PeriodDashboard.this, PeriodHistory.class);  //panggilPage
        startActivity(intent);
    }
}