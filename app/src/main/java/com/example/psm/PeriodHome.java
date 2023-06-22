package com.example.psm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        periodList = new Vector<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();


        binding.calendarView.setOnDateLongClickListener(
                new OnDateLongClickListener() {
                    @Override
                    public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {

                        DayViewDecorator selectedRange = new DayViewDecorator() {
                            @Override
                            public boolean shouldDecorate(CalendarDay day) {
                                //snio setting date
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(date.getYear(),date.getMonth(),date.getDay());
                                calendar.add(Calendar.DAY_OF_MONTH,5/*berapa hari nk tambah*/);

                                LocalDate endDate = LocalDateTime.fromCalendarFields(calendar).toLocalDate();
                                CalendarDay end = CalendarDay.from(endDate.getYear(),endDate.getMonthOfYear()-1,endDate.getDayOfMonth());
                                Toast.makeText(PeriodHome.this, date+"to"+endDate, Toast.LENGTH_SHORT).show();



                                return ((day.isAfter(date)|| day.equals(date)) &&
                                        (day.isBefore(end)) || day.equals(end) );
                            }

                            @Override
                            public void decorate(DayViewFacade view) {
                                view.addSpan(new DotSpan(10, Color.RED));

                            }
                        };
                        binding.calendarView.addDecorator(selectedRange);
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
       periodLoad();

    }
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
        for(Period period : periodList){
            binding.calendarView.addDecorator(period.getDecorator());
        }

    }
}