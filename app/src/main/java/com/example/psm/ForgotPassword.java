package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.RequestController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.databinding.ActivityForgotPasswordBinding;
import com.example.psm.databinding.ActivityLoginBinding;


import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ForgotPassword extends AppCompatActivity {
    //declared setiap activity kena ada
    private RequestQueue requestQueue;
    ActivityForgotPasswordBinding binding;
    private SweetAlert swal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSendEmail.setBackgroundColor(Color.parseColor("#F42B82"));
        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal2.getId(),swal).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton
        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;


    }
    /*
    public void fn_reset(View view){

        if(binding.oldEmail.getText().toString().isEmpty())
        {
            //messageBOx

            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Invalid")
                    .setContentText("Please enter your email")
                    .show();

        }
        else{
            JSONObject body = new JSONObject();

            try {
                body.put("email",binding.oldEmail.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestController requestController = new RequestController(Request.Method.POST,
                    "/reset", body,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {   //success

                            swal.show("Success","Valid Email" + response.toString(), SweetAlertDialog.SUCCESS_TYPE);

                            Intent intent = new Intent(getApplication(), Login.class);  //panggilPage

                            SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
                            SharedPreferences.Editor edt = sharedPreferences.edit();

                            edt.putString("token",response.toString());
                            edt.apply();
                            startActivity(intent);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //error
                            swal.show("Failed","Invalid email", SweetAlertDialog.ERROR_TYPE);
                        }
                    });

            requestQueue.add(requestController);

        }
    } */

    public void fn_reset(View view) {
        if(binding.oldEmail.getText().toString().isEmpty()

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
                body.put("email",binding.oldEmail.getText().toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestController requestController = new RequestController(Request.Method.POST,
                    "/reset", body,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {   //success

                            swal.show("Success","Valid Email" + response.toString(), SweetAlertDialog.SUCCESS_TYPE);

                            //page kiri current page kanan next page yg nk pergi

                            Intent intent = new Intent(getApplication(), Login.class);  //panggilPage

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
                            swal.show("Failed","Invalid Email", SweetAlertDialog.ERROR_TYPE);

                        }
                    });

            requestQueue.add(requestController);


        }

    }

}