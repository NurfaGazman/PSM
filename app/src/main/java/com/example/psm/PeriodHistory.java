package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.ContactController;
import com.example.psm.Controller.PeriodClick;
import com.example.psm.Controller.PeriodController;
import com.example.psm.Controller.RequestController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.Model.Contact;
import com.example.psm.Model.Period;
import com.example.psm.Model.ViewPeriod;
import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityPeriodHistoryBinding;
import com.example.psm.databinding.ActivityPeriodHomeBinding;

import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PeriodHistory extends AppCompatActivity {
    private ActivityPeriodHistoryBinding binding;
    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;
    private Vector<Period> period;
    private PeriodController periodController;

    //display data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPeriodHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton

        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;
        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();

        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);

        period = new Vector<>();
        binding.btnNewRecord.setOnClickListener(this::goToNewRecord);

        periodController = new PeriodController(getLayoutInflater(), period, new PeriodClick() {
            @Override
            public void clickPeriod(Period period) {
              Intent editPeriod = new Intent(PeriodHistory.this,InsertPeriod.class);
              editPeriod.putExtra("period_Id",period.getPeriod_Id());
//                //tambah sini 15/8
                startActivity(editPeriod);

            }
        });

        //requestQueue.add(requestController);
        binding.listPeriod.setAdapter(periodController);
        binding.listPeriod.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //tambahan 16/8
          binding.btnNewRecord.setOnClickListener(this::goToNewRecord);
    }

        public void goToNewRecord(View view){
        Intent intent = new Intent(PeriodHistory.this, InsertPeriod.class);  //panggilPage
        startActivity(intent);


    }


    public void loadList(){

        period.clear();

        ViewPeriod.total=0;
        ViewPeriod.count=0;

        RequestController requestController = new RequestController(Request.Method.GET,
                "/api/Period" , null, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //success


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            Log.d("Test",""+jsonArray.length());


                            for(int i=0; i <jsonArray.length(); i ++) {
                                Log.d("Test", "" + i);
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

                                period.add(periodList);



                            }

                           

                            for(int i=0; i<period.size()-1; i++){
                                //get utk access specific
                                period.get(i).CalculateCycleLength(period.get(i+1));

                            }

                            //tambahan average
                            int totalAverageCycleLength = 0;
                            int validPeriodCount = 0;

                            for (Period p : period) {
                                if (p.getCycleCount() > 0) {
                                    totalAverageCycleLength += p.getAverageCycleLength();
                                    validPeriodCount++;
                                }
                            }

                            if (validPeriodCount > 0) {
                                int overallAverageCycleLength = totalAverageCycleLength / validPeriodCount;
                                // Do something with the overallAverageCycleLength value
                            }



                        Period Dummy = new Period();
                            Dummy.isHeader(true);
                            period.insertElementAt(Dummy,0);

                            periodController.notifyDataSetChanged();

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

    @Override
    protected void onStart() {
        super.onStart();
        loadList();



    }
}
