package com.example.psm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.RequestController;
import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityRegister2Binding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Homepage extends AppCompatActivity {

    //declare variable string
    private String token;
    private static final int PERMISSION_REQUEST_LOCATION=1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private RequestQueue requestQueue;
    //kena ada setiap activity

    ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

//wajib letak button back jangan lupa kt manifest jugak . follow homepage jugk
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    //sampai sini

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton
        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        //ambil value dalam shared pref


        binding.btnProfile.setOnClickListener(this::goToProfile);
        binding.btnContact.setOnClickListener(this::goToContact);
        binding.btnSetting.setOnClickListener(this::goToSetting);
        binding.btnself.setOnClickListener(this::goToSelf);
        binding.btnPeriodHome.setOnClickListener(this::goToPeriodHome);
        binding.btnLocation.setOnClickListener(this::gotoLocation);
    }


    //panggil link button yang tuk dilinkkan
    // Contact
    public void goToContact(View view) {
        Intent intent = new Intent(Homepage.this, ManagedContact.class);  //panggilPage
        startActivity(intent);
    }
//manageProfile
    public void goToProfile(View view){
        Intent intent = new Intent(Homepage.this, ManagedProfile.class);  //panggilPage
        startActivity(intent);
    }
//setting
    public void goToSetting(View view){
        Intent intent = new Intent(Homepage.this, Setting.class);  //panggilPage
        startActivity(intent);
    }

//selfNote
    public void goToSelf(View view){
        Intent intent = new Intent(Homepage.this, SelfLearn.class);  //panggilPage
        startActivity(intent);
    }

//period page
    public void goToPeriodHome(View view){
        Intent intent = new Intent(Homepage.this, PeriodDashboard.class);  //panggilPage
        startActivity(intent);
    }


//location page utk send auto location kepada user lain by numberphone
public void gotoLocation(View view){
    //function utk dapat longitute and latitude
    Log.d("test","start");
    //check permission from user location
    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
        //minta permission dari user
        Log.d("test","Request");
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_LOCATION);
    } else {
        //dah ad permission
        Log.d("test","Gran");

        //request location dari user device
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                JSONObject body = new JSONObject();

                Log.d("test","loc"+location.getLatitude()+","+location.getLongitude());
                try {

                    body.put("latitude",location.getLatitude());
                    body.put("longitude",location.getLongitude());


                    RequestController requestController = new RequestController(Request.Method.POST,
                            "/api/Contact/emergency", body, token,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {   //success

                                    //swal.show("Update","Success Updated", SweetAlertDialog.SUCCESS_TYPE);
                                    Log.d("test","success");



                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) { //error
                                    //swal.show("Failed","Invalid update", SweetAlertDialog.ERROR_TYPE);
                                    Log.d("test","Error");
                                }
                            });

                    requestQueue.add(requestController);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
//utk hantar ke server location
    public void serverLocation(){

    }
    //logout btn

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnLogout:


                SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = sharedPreferences.edit();
                edt.putString("token","");
                edt.apply();

                Intent intent = new Intent(getApplication(), Login.class);
                startActivity(intent);

                break;
        }
        return  true;
    }
}
