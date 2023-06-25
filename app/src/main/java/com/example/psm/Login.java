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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.RequestController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.databinding.ActivityLoginBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    private RequestQueue requestQueue;
    private SweetAlert swal;
    ActivityLoginBinding binding;


    //location test
    private static final int PERMISSION_REQUEST_LOCATION=1;
    private FusedLocationProviderClient fusedLocationProviderClient;

    //part audio
    MediaPlayer mp;
    private String audio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //clicktext signUp(id) and forgot password
        binding.SignUp.setOnClickListener(this::SignUp);
        binding.ResetPass.setOnClickListener(this::ForgotPassword);
        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;


        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();
        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);

        audio = sharedPreferences.getString("sound",null);
        //sound button
        binding.btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mp != null){

                    if(mp.isPlaying()){
                        mp.release();
                        mp = null;
                        return;
                    }
                }
                if(audio ==null){
                    audio="";
                }

                if(audio.equals("Womenscream")){

                    mp =  MediaPlayer.create(Login.this,R.raw.woman_scream);
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer){
                            mp.start();
                        }
                    });
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mp.release();
                        }
                    });

                } else if(audio.equals("Police_Siren")){
                    mp =  MediaPlayer.create(Login.this,R.raw.police);
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer){

                            mp.start();
                        }
                    });
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mp.release();
                        }
                    });
                }
            }
        });

    }

//function login
    public void fnLogin(View view) {
        if(binding.eeditTextTextEmail.getText().toString().isEmpty()||
                binding.editTextTextPassword.getText().toString().isEmpty()
        )
        {
          //messageBox
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Invalid")
                    .setContentText("Please fill in all the field")
                    .show();

        }
        else{
            JSONObject body = new JSONObject();

            try {
                body.put("email",binding.eeditTextTextEmail.getText().toString());
                body.put("password",binding.editTextTextPassword.getText().toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestController requestController = new RequestController(Request.Method.POST,
                    "/login", body,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {   //success

                            swal.show("Success","Valid Login" + response.toString(), SweetAlertDialog.SUCCESS_TYPE);

                            //page kiri current page kanan next page yg nk pergi

                            Intent intent = new Intent(getApplication(), Homepage.class);  //panggilPage

                            SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
                            SharedPreferences.Editor edt = sharedPreferences.edit();
                            edt.putString("token",response.toString());
                            edt.apply();
                            //hantar token ke next screen
                            ///intent.putExtra("token",);
                            startActivity(intent);
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //error
                            swal.show("Failed","Invalid Login", SweetAlertDialog.ERROR_TYPE);

                        }
                    });

                     requestQueue.add(requestController);


        }

    }

    public void SignUp(View view){
    gotoLocation();
        //Intent intent = new Intent(Login.this, Register.class);  //panggilPage
        //startActivity(intent);
    }

    public void ForgotPassword(View view){
        Intent intent = new Intent(Login.this, ForgotPassword.class);  //panggilPage
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);


        RequestController requestController = new RequestController(Request.Method.GET,
                "/api/verify", null, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //success

                        Intent intent = new Intent(getApplication(), Homepage.class);  //panggilPage

                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(requestController);
    }

    public void gotoLocation(){
        //function utk dapat longitute and latitude
        Log.d("test","start");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("test","Request");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        } else {
            Log.d("test","Gran");
            //criteria.setAccuracy(Criteria.ACCURACY_FINE);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.d("test","loc"+location.getLatitude()+","+location.getLongitude());
                }
            });


        }

    }
}