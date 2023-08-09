package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.RequestController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.Model.Period;
import com.example.psm.Model.User;
import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityInsertPeriodBinding;
import com.example.psm.databinding.ActivityPeriodHomeBinding;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InsertPeriod extends AppCompatActivity {

    private ActivityInsertPeriodBinding binding;
    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;
    private DatePickerDialog datePickerDialog;

    private Period period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityInsertPeriodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton

        binding.btnDeletePeriod.setBackgroundColor(Color.parseColor("#db5a6b"));  //color button
        binding.btnSavePeriod.setBackgroundColor(Color.parseColor("#db5a6b"));  //color button
        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();

        period = new Period();
        //load period daripd history.
        if(period.getPeriod_Id() == -1){
            //insert
            binding.Periodtitle.setText("Insert Date" );
            binding.btnDeletePeriod.setVisibility(View.INVISIBLE);

        }else{
            //user tgh edit
            binding.Periodtitle.setText("Edit Period");
            binding.btnDeletePeriod.setVisibility(View.VISIBLE);
        }

        //setiap kali guna token copy yang ini
        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);

        binding.btnSavePeriod.setOnClickListener(this::SavePeriod);

        //date picker utk start date
        binding.startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) fnInvokeDatePickerStart();
            }
        });

        //date picker utk end date
        binding.EndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) fnInvokeDatePickerEnd();
            }
        });

    }
//start date picker
    private void fnInvokeDatePickerStart() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(InsertPeriod.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month = String.valueOf((monthOfYear + 1));
                if ((monthOfYear + 1) < 10) {
                    month = "0" + month;
                }
                String day = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) {
                    day = "0" + day;
                }
                binding.startDate.setText(year + "-" + month + "-" + day);
                binding.startDate.setError(null);
                binding.startDate.clearFocus();
            }
        }, year, month, day);

        LocalDate MaxDate = LocalDate.now();
        datePickerDialog.getDatePicker().setMaxDate(MaxDate.toDateTime(new LocalTime()).getMillis());
        datePickerDialog.show();
    }


//End date picker
    private void fnInvokeDatePickerEnd() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(InsertPeriod.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month = String.valueOf((monthOfYear + 1));
                if ((monthOfYear + 1) < 10) {
                    month = "0" + month;
                }
                String day = String.valueOf(dayOfMonth);
                if (dayOfMonth < 10) {
                    day = "0" + day;
                }
                binding.EndDate.setText(year + "-" + month + "-" + day);
                binding.EndDate.setError(null);
                binding.EndDate.clearFocus();
            }
        }, year, month, day);

//limitation date utk end date

        String startDate = binding.startDate.getText().toString();
        if(!startDate.isEmpty()){
            LocalDate localDate = new LocalDate(startDate);
            datePickerDialog.getDatePicker().setMinDate(localDate.toDateTime(new LocalTime()).getMillis());
        }

        datePickerDialog.show();
    }

    public void SavePeriod(View view){
        JSONObject body = new JSONObject();
//body bentuk dalam JsonObject
    try{
        body.put("start_date",binding.startDate.getText().toString());
        body.put("end_date", binding.EndDate.getText().toString());

        RequestController requestController = new RequestController(Request.Method.POST,
                "/api/Period", body, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //success

                        swal.show("","Success", SweetAlertDialog.SUCCESS_TYPE);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { //error
                        swal.show("","ERROR", SweetAlertDialog.ERROR_TYPE);
                    }
                });

        requestQueue.add(requestController);

    }catch (JSONException e){
            e.printStackTrace();
    }

    }
}