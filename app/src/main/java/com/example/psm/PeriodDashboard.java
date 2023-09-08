package com.example.psm;

import static com.example.psm.PeriodHistory.averageCycleLength;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityPeriodDashboardBinding;
import com.example.psm.databinding.ActivityPeriodHomeBinding;

import org.joda.time.LocalDate;

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

        //tambahan predict
        //get current date
        LocalDate currentDate = LocalDate.now();

        //calculate the predict next period
        LocalDate predictedStartDate = currentDate.plusDays(averageCycleLength);

        // Format the predicted start date as a string in "yyyy-MM-dd" format
        String predictedStartDateStr = predictedStartDate.toString("yyyy-MM-dd");

        // Update the EditText widget for Upcoming Period with the predicted start date
        EditText upcomingPeriodDashboard = findViewById(R.id.UpcomingPeriodDashboard);
        upcomingPeriodDashboard.setText(predictedStartDateStr);


        EditText currentDayDashboard = findViewById(R.id.currentDayDashboard);
        EditText CurrentPeriodDashboard = findViewById(R.id.CurrentPeriodDashboard);
        EditText LengthDashboard = findViewById(R.id.LengthDashboard);
        EditText PeriodDashboard = findViewById(R.id.PeriodDashboard);

        currentDayDashboard.setText("");
        CurrentPeriodDashboard.setText("");
        LengthDashboard.setText("");
        PeriodDashboard.setText("");

    }

    public void goToHistory(View view){
        Intent intent = new Intent(PeriodDashboard.this, PeriodHistory.class);  //panggilPage
        startActivity(intent);
    }


}