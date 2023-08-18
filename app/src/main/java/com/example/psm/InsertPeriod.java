package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.PeriodController;
import com.example.psm.Controller.RequestController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.Model.Period;
import com.example.psm.Model.User;
import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityInsertPeriodBinding;
import com.example.psm.databinding.ActivityPeriodHomeBinding;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InsertPeriod extends AppCompatActivity {

    private ActivityInsertPeriodBinding binding;   // View binding activity
    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;  //alert dialog
    private DatePickerDialog datePickerDialog;

    //tambahan 15/8
    //// Period ID dari intent
    private int periodId;
    // Period object utk store data
    private Period period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertPeriodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton


        // Set button colors
        binding.btnDeletePeriod.setBackgroundColor(Color.parseColor("#db5a6b"));  //color button
        binding.btnSavePeriod.setBackgroundColor(Color.parseColor("#db5a6b"));  //color button

        //req queue utk network req
        requestQueue = Volley.newRequestQueue(this);

        swal = new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(), swal).commit();

        //Period object
        period = new Period();
        //load period daripada history.

        //tambahan 15/8
        // Get period ID intent
        Intent edit = getIntent();
        periodId = edit.getIntExtra("period_Id", -1);

        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);


        //// Check sama ad insert or edit
        if(periodId == -1){
            //insert
            binding.Periodtitle.setText("Insert Date" );
            binding.btnDeletePeriod.setVisibility(View.INVISIBLE);

        }else{
            //user tgh edit
            binding.Periodtitle.setText("Edit Period");
            binding.btnDeletePeriod.setVisibility(View.VISIBLE);

            //tambahan 15/8
            loadPeriodData(); //loadPeriod
        }

        //setiap kali guna token copy yang ini
        binding.btnSavePeriod.setOnClickListener(this::SavePeriod);

        //button delete
        binding.btnDeletePeriod.setOnClickListener(this::DeletePeriod);


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


//savebutton period after edit
    public void SavePeriod(View view){
        JSONObject body = new JSONObject();
        //body bentuk dalam JsonObject


        try{
            body.put("start_date",binding.startDate.getText().toString());
            body.put("end_date", binding.EndDate.getText().toString());

            RequestController requestController;


            if(periodId == -1){
                requestController = new RequestController(Request.Method.POST,
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

            }else{

                //edit period
                body.put("user_Id",period.getUser_Id());
                body.put("period_Id",period.getPeriod_Id());
                body.put("start_date",binding.startDate.getText().toString());
                body.put("end_date", binding.EndDate.getText().toString());

                requestController = new RequestController(Request.Method.PUT,
                        "/api/Period", body, token,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {   //success
                                swal.show("","Update Success", SweetAlertDialog.SUCCESS_TYPE);
                                    //clear textview after
                                binding.startDate.setText("");
                                binding.EndDate.setText("");
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) { //error
                                swal.show("FAILED","ERROR", SweetAlertDialog.ERROR_TYPE);
                            }
                        });
            }

            requestQueue.add(requestController);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    //loadPeriodData
    private void loadPeriodData() {

        RequestController requestController = new RequestController(Request.Method.GET,
                "/api/Period",null, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //success

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            //extract period data
                            for(int i=0; i <jsonArray.length(); i ++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Extract data dari JSON object and set period attribute
                                if(!jsonObject.isNull("user_Id"))
                                    period.setUser_Id(jsonObject.getInt("user_Id"));

                                if(!jsonObject.isNull("period_Id"))
                                    period.setPeriod_Id(jsonObject.getInt("period_Id"));

                                if(!jsonObject.isNull("start_date"))
                                    period.setStart_date(jsonObject.getString("start_date"));

                                if(!jsonObject.isNull("end_date"))
                                    period.setEnd_date(jsonObject.getString("end_date"));


                                binding.startDate.setText(period.getStart_date());
                                binding.EndDate.setText(period.getEnd_date());

                                //run by line(check line by line)
                                //period.getPeriod_Id= value dalam raw = period_id(period history)
                                //bila dua2 betul stop
                                if(period.getPeriod_Id()==periodId)
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //try {

                            //JSONObject jsonObject = new JSONObject(response);
                            //String startDate = jsonObject.optString("start_date");
                            //String endDate = jsonObject.optString("end_date");
                            //binding.startDate.setText(startDate);
                            //binding.EndDate.setText(endDate);

                        //} catch (JSONException e) {
                       //}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                swal.show("Failed","ERROR", SweetAlertDialog.ERROR_TYPE);
            }
        });

        requestQueue.add(requestController);
    }

    //delete function
    public void DeletePeriod(View view){
        RequestController requestController = new RequestController(Request.Method.DELETE,
                "/api/Period/"+period.getPeriod_Id(), null, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //success

                        swal.show("Delete", "Success", SweetAlertDialog.SUCCESS_TYPE);
                        //clear textview
                        binding.startDate.setText("");
                        binding.EndDate.setText("");
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { //error
                        swal.show("Failed", "Invalid delete", SweetAlertDialog.ERROR_TYPE);

                    }
                });

        requestQueue.add(requestController);

    }


}
