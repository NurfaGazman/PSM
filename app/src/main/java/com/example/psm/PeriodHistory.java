package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PeriodHistory extends AppCompatActivity {
    private ActivityPeriodHistoryBinding binding;
    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;
    private Vector<Period> period;
    private PeriodController periodController; //recycle view


    //static ambik dri class
    //display data

    //No data sebb xdak kena mengena dgn average . duduk kt dashboard
    //cyclength kira start dgn start period date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityPeriodHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        swal = new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(), swal).commit();

        SharedPreferences sharedPreferences = getSharedPreferences("PSM", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        period = new Vector<>();
        //binding.btnNewRecord.setOnClickListener(this::goToNewRecord);


        periodController = new PeriodController(getLayoutInflater(), period, new PeriodClick() {
            @Override
            public void clickPeriod(Period period) {
                Intent editPeriod = new Intent(PeriodHistory.this, InsertPeriod.class);
                editPeriod.putExtra("period_Id", period.getPeriod_Id());
                //tambah sini 15/8
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

        period.clear(); // obj class daripada vector = collection od data. jenis Period .
        //tujuan ambik data pastu simpan data
        //dynamic array sebb tu pakai vertor.

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

                                period.add(periodList);

                            }
                            int count = 0;
                            //calculate cycle length and collect data
                            for(int i=0; i<period.size()-1; i++){
                                //get utk access specific
                                // panggil the CalculateCycleLength method with the current and next period.
                                period.get(i).CalculateCycleLength(period.get(i+1)); //pass the next period object 'period.get(i+1)
                                count++;

                            }

                            //Notification

                            sendNotification("Period Tracker Notify","The latest period length is : "+period.get(count).getPeriodLength());
                            //Toast.makeText(PeriodHistory.this, +"", Toast.LENGTH_SHORT).show();

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


    //notification
    private static final String CHANNEL_ID = "my_channel_id"; // Unique channel ID
    private static final String CHANNEL_NAME = "My Channel"; // Channel name (visible to the user)
    private static final int NOTIFICATION_ID = 1; // Unique notification ID

    public void sendNotification(String title, String description) {
        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.logo) // Replace with your own icon
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        // Send the notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
