package com.example.psm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.RequestController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.Model.Contact;
import com.example.psm.Model.Period;
import com.example.psm.databinding.ActivityPeriodHomeBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PeriodHome extends AppCompatActivity {

    private ActivityPeriodHomeBinding binding;
    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;
    private Vector<Period> periodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding = ActivityPeriodHomeBinding.inflate(getLayoutInflater());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        periodList = new Vector<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();
        binding.periodLenght.setText("7");
        binding.cyclelenght.setText("28");

        binding.calendarView.setOnDateLongClickListener(
                new OnDateLongClickListener() {
                    @Override
                    public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay selectedDate) {
                        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        //selectedDate.toString();
                        LocalDate date = new LocalDate(selectedDate.getYear(),selectedDate.getMonth(),selectedDate.getDay());


                       Period selectedPeriod = dbCalander(selectedDate);
                       if(selectedPeriod != null){
                           //klu user select range yang ad period
                           swal.dialog("Change", "Do you want to change?\n Delete or Update", "Delete", "Update",
                                   new SweetAlertDialog.OnSweetClickListener() {
                                       @Override
                                       public void onClick(SweetAlertDialog sweetAlertDialog) {
                                           sweetAlertDialog.dismiss();
                                           deletePeriod(selectedPeriod);

                                       }
                                   }, new SweetAlertDialog.OnSweetClickListener() {
                                       @Override
                                       public void onClick(SweetAlertDialog sweetAlertDialog) {
                                           sweetAlertDialog.dismiss();
                                           swal.dialog("Change", "Do you want to change?\n Start or End Date", "Start", "End"
                                                   , new SweetAlertDialog.OnSweetClickListener() {
                                                       @Override
                                                       public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                           selectedPeriod.setStart_date(date.toString("yyyy-MM-dd"));
                                                           updatePeriod(selectedPeriod);
                                                           sweetAlertDialog.dismiss();
                                                           //action text1

                                                       }
                                                   }, new SweetAlertDialog.OnSweetClickListener() {

                                                       //action text2
                                                       @Override
                                                       public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                           selectedPeriod.setEnd_date(date.toString("yyyy-MM-dd"));
                                                           updatePeriod(selectedPeriod);
                                                           sweetAlertDialog.dismiss();
                                                       }
                                                   });
                                       }
                                   });

                       }else{
                           //klu user select tp xdak data date
                          int nearestIndex = findNearest(date); //utk cari index dalam period list yang paling dekat dengan date user select
                          if(nearestIndex != -1){
                              //yang ad date dekt dengan 30hari
                              swal.dialog("update", "Update Previous Date", "Yes", "No",
                                      new SweetAlertDialog.OnSweetClickListener() {
                                          @Override
                                          public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            //kalau yes
                                              Period period =   periodList.get(nearestIndex);
                                              period.setCloserDate(date);
                                              updatePeriod(period);
                                              sweetAlertDialog.dismiss();
                                          }
                                      }, new SweetAlertDialog.OnSweetClickListener() {
                                          @Override
                                          public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            //kalau No
                                              sweetAlertDialog.dismiss();
                                              swal.confirm("New Record?", "New Record Date of Period", new SweetAlertDialog.OnSweetClickListener() {
                                                  @Override
                                                  public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    Period period = new Period(date.toString("yyyy-MM-dd"),Integer.parseInt(
                                                            binding.periodLenght.getText().toString()));
                                                    newPeriod(period);
                                                    sweetAlertDialog.dismiss();
                                                  }
                                              });
                                          }
                                      });
                          }

                          else{
                              swal.confirm("New Record?", "New Record Date of Period", new SweetAlertDialog.OnSweetClickListener() {
                                  @Override
                                  public void onClick(SweetAlertDialog sweetAlertDialog) {
                                      Period period = new Period(date.toString("yyyy-MM-dd"),Integer.parseInt(
                                              binding.periodLenght.getText().toString()));
                                      newPeriod(period);
                                      sweetAlertDialog.dismiss();
                                  }
                              });
                          }

                       }

                    }
                }

        );
    }

    @Override
    protected void onStart() {
        super.onStart();
       periodLoad();

    }
    //utk checking dari database punya data date
    public Period dbCalander(CalendarDay calendarDay){
        for(Period period : periodList){
            //check calander
            if  ((calendarDay.isAfter(period.getStart())|| calendarDay.equals(period.getStart())) &&
                    (calendarDay.isBefore(period.getEnd())) || calendarDay.equals(period.getEnd()) ) {

                return period;
            }
        }
        return null;
    }

    //load date period daripada database
    public void periodLoad(){
        periodList.clear();
        RequestController requestController = new RequestController(Request.Method.GET,
                "/api/Period", null, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);


                            for(int i=0; i <jsonArray.length(); i ++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Period period = new Period();

                                if(!jsonObject.isNull("user_Id"))
                                    period.setUser_Id(jsonObject.getInt("user_Id"));

                                if(!jsonObject.isNull("period_Id"))
                                    period.setPeriod_Id(jsonObject.getInt("period_Id"));

                                if(!jsonObject.isNull("start_date"))
                                    period.setStart_date(jsonObject.getString("start_date"));

                                if(!jsonObject.isNull("end_date"))
                                    period.setEnd_date(jsonObject.getString("end_date"));
                                periodList.add(period);

                            }
                            loadCalendar();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
         requestQueue.add(requestController);
    }
    public void loadCalendar(){
        binding.calendarView.removeDecorators();
        for(Period period : periodList){
            binding.calendarView.addDecorator(period.getDecorator());
        }

    }
    //update period kt database
    public void updatePeriod(Period period){
        JSONObject body = new JSONObject();

        try {

            body.put("user_Id",period.getUser_Id());
            body.put("period_Id",period.getPeriod_Id());
            body.put("start_date",period.getStart_date());
            body.put("end_date",period.getEnd_date());



            RequestController requestController = new RequestController(Request.Method.PUT,
                    "/api/Period", body, token,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {   //success
                            periodLoad();
                        }

                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //error
                            swal.show("Failed","Invalid Update", SweetAlertDialog.ERROR_TYPE);

                        }
                    });

            requestQueue.add(requestController);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public int findNearest(LocalDate localdate){
        int minimumIndex = -1;
        int minimumDay = 30;

        for(int i = 0; i < periodList.size(); i++){
            if (periodList.get(i).DayDifferent(localdate)<=minimumDay){
                minimumDay = periodList.get(i).DayDifferent(localdate);
                minimumIndex = i;
            }
        }
        return minimumIndex;
    }

    //insert database utk period baru
    public void newPeriod(Period period){
        JSONObject body = new JSONObject();

        try {

            body.put("start_date",period.getStart_date());
            body.put("end_date",period.getEnd_date());



            RequestController requestController = new RequestController(Request.Method.POST,
                    "/api/Period", body, token,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {   //success
                            periodLoad();
                        }

                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //error
                            swal.show("Failed","Invalid insert", SweetAlertDialog.ERROR_TYPE);

                        }
                    });

            requestQueue.add(requestController);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //function delete of period selected from database

    public void deletePeriod(Period period) {

        RequestController requestController = new RequestController(Request.Method.DELETE,
                "/api/Period/"+period.getPeriod_Id(), null, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //success
                        periodLoad();
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