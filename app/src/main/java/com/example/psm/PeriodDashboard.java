package com.example.psm;


import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.RequestController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.Model.Period;
import com.example.psm.Model.User;
import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityPeriodDashboardBinding;
import com.example.psm.databinding.ActivityPeriodHomeBinding;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Vector;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PeriodDashboard extends AppCompatActivity {

    private ActivityPeriodDashboardBinding binding;
    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;
    private DatePickerDialog datePickerDialog;

    private Vector<Period> period;

    // 3- predict next period kena ad average cycle length
    // 2- average cycle length kena ada jumlah cycle length, bahagi dgn bilangan cycle length
    // 1 - data period kena load daripada database user yg insert


    //Period start date period sampai end date period . cth 1/9-6/9 5days (PL)
    //dapatkan AverageLength kena ad totallength /bilangan period length

    //predict -mostRecentPeriod :akan ad selagi user xinsert(xkan bergerak)
    //calculate the next period start date .
    //check data dlu
    //dapatkan mostRecentPeriod end date
    //convert localdate
    //calculate  next peroid based on average cycle length
    //display

    //localdate end date ,localNextPeriod


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton


        binding = ActivityPeriodDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //button function
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        binding.BtnHistory.setOnClickListener(this::goToHistory);
        //binding.BtnCalendar.setOnClickListener(this::goToPeriod);


        //binding.BtnCalendar.setBackgroundColor(Color.parseColor("#F42B82"));
        binding.BtnHistory.setBackgroundColor(Color.parseColor("#F42B82"));


//declared utk setText. by id , ambik dri layout by id
        binding.UpcomingPeriodDashboard.setText("Upcoming Period : ");
        binding.LengthDashboard.setText("Cycle Length of Period : ");

        SharedPreferences sharedPreferences = getSharedPreferences("PSM", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        period = new Vector<>();
    }



    public void loadList(){

        period.clear();


        RequestController requestController = new RequestController(Request.Method.GET,
                "/api/Period" , null, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //success


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            Log.d("Test",""+jsonArray.length());

                            for(int i=0; i <jsonArray.length(); i ++) {
                                //Log.d("Test", "" + i);
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Period periodList = new Period();

                                if(!jsonObject.isNull("start_date"))
                                    periodList.setStart_date(jsonObject.getString("start_date"));

                                if(!jsonObject.isNull("end_date"))
                                    periodList.setEnd_date(jsonObject.getString("end_date"));

                                if(!jsonObject.isNull("user_Id"))
                                    periodList.setUser_Id(jsonObject.getInt("user_Id"));

                                if(!jsonObject.isNull("period_Id"))
                                    periodList.setPeriod_Id(jsonObject.getInt("period_Id"));

                                period.add(periodList); //msukkan data dalam vertor obj

                            }

                            int totalCycleLength = 0;


                            //calculate cycle length and collect data
                            //-1 last index xdak.

                            for(int i=0; i<period.size()-1; i++){ //
                                //get utk access specific
                                // panggil the CalculateCycleLength method with the current and next period.
                                period.get(i).CalculateCycleLength(period.get(i+1)); //pass the next period object 'period.get(i+1)
                                totalCycleLength += period.get(i).getCycleLength();
                            }

                            int averageCycleLength = totalCycleLength / (period.size()-1);
                            binding.LengthDashboard.setText("Average Cycle Length: " +averageCycleLength);

                            //kena ad period length and average
                            int totalPeriodLength = 0 ;
                            for (int i=0; i<period.size(); i++){
                               totalPeriodLength += period.get(i).getPeriodLength(); //totalPeriodLength
                            }
                            int averageLength = totalPeriodLength / period.size(); //average
                            binding.PeriodLength.setText("Average Period Length: " + averageLength);

                            //periodUpcoming

                            int size = period.size(); //get size period list
                            LocalDate predictStart = period.get(size-1).getDate_start(); //get startdate from last  dalam list period
                            LocalDate today = LocalDate.now();  //current date
                            while(!predictStart.isAfter(today)){ //ulang proses selagi bukan predict start selepas hari ni
                                predictStart = predictStart.plusDays(averageCycleLength); //add averageCycleLength utk dpt predict
                                Period p = new Period(); //declare PeriodObj and iniziallez
                                p.setDate_start(predictStart); //simpan start date
                                p.setDate_end(predictStart.plusDays(averageLength)); //utk dapatkan end date

                                period.add(p); //setiap kali inialiaze akan simpan so akan tmbh2. akan ad semua

                            }
                            LocalDate predictEnd = predictStart.plusDays(averageLength);
                            binding.UpcomingPeriodDashboard.setText("Upcoming Period : " +predictStart);
                            //today(start date) ,after(start date) , before (enddate)

                            //currentDay of period
                            loadCalendar();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swal.show("Failed","Invalid", SweetAlertDialog.ERROR_TYPE);
            }
        });

        requestQueue.add(requestController);
    }

    public void goToHistory(View view){
        Intent intent = new Intent(PeriodDashboard.this, PeriodHistory.class);
        startActivity(intent);
    }

    public void loadCalendar(){
        binding.calendarView.removeDecorators();
        for(Period p : period){
            binding.calendarView.addDecorator(p.getDecorator());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadList();


    }

}