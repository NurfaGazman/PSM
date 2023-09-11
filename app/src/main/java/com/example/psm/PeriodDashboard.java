package com.example.psm;

import static com.example.psm.PeriodHistory.averageCycleLength;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityPeriodDashboardBinding;
import com.example.psm.databinding.ActivityPeriodHomeBinding;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.w3c.dom.Text;

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
        binding.BtnHistory.setOnClickListener(this::goToHistory);
        binding.BtnCalendar.setOnClickListener(this::goToPeriod);
        binding.BtnInsert.setOnClickListener((this::goToInsert));


        binding.BtnCalendar.setBackgroundColor(Color.parseColor("#F42B82"));
        binding.BtnHistory.setBackgroundColor(Color.parseColor("#F42B82"));
        binding.BtnInsert.setBackgroundColor(Color.parseColor("#F42B82"));

        //tambahan predict
        //get current date
        LocalDate currentDate = LocalDate.now();
        //calculate the predict next period
        LocalDate predictedStartDate = currentDate.plusDays(averageCycleLength);

        //Upcoming Period with the predicted start date
        String predictedStartDateStr = predictedStartDate.toString("yyyy-MM-dd");
        EditText upcomingPeriodDashboard = findViewById(R.id.UpcomingPeriodDashboard);
        upcomingPeriodDashboard.setText("Upcoming Period: " + predictedStartDateStr);

        //currentDate
        EditText currentDateDashboard = findViewById(R.id.CurrentPeriodDashboard);
        String currentDateStr = currentDate.toString("yyyy-MM-dd");
        currentDateDashboard.setText("" + currentDateStr);

        // Set the cycle length
        TextView cycleLengthTextView = findViewById(R.id.LengthDashboard);
        cycleLengthTextView.setText("Cycle Length: " + averageCycleLength);


        // Calculate the current day of the period
        int currentDayInPeriod = calculateCurrentDayInPeriod(currentDate, predictedStartDate);

        // Display the current day in a TextView
        TextView currentDayTextView = findViewById(R.id.currentDayDashboard);
        currentDayTextView.setText("" + currentDayInPeriod + " days left");

//        TextView periodLengthTextView = findViewById(R.id.PeriodDashboard);
//        periodLengthTextView.setText("Period Length : " + );
    }


    private int calculateCurrentDayInPeriod(LocalDate currentDate, LocalDate predictedStartDate) {
        // Calculate the number of days between the current date and the predicted start date
        int currentDayInPeriod = Days.daysBetween( currentDate , predictedStartDate).getDays();
        // Add 1 to make it 1-based instead of 0-based
        return currentDayInPeriod + 1;
    }

    public void goToHistory(View view){
        Intent intent = new Intent(PeriodDashboard.this, PeriodHistory.class);  //panggilPage
        startActivity(intent);
    }

    public void goToPeriod(View view){
        Intent intent = new Intent(PeriodDashboard.this, PeriodHome.class);  //panggilPage
        startActivity(intent);
    }

    public void goToInsert(View view){
        Intent intent = new Intent(PeriodDashboard.this, InsertPeriod.class);  //panggilPage
        startActivity(intent);
    }

}