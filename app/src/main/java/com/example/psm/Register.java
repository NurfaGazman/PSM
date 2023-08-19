package com.example.psm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.psm.databinding.ActivityRegister2Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity {
    //declared setiap activity kena ada
    private RequestQueue requestQueue;
    private SweetAlert swal;
    private ActivityRegister2Binding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityRegister2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton
        binding.btnSignup.setBackgroundColor(Color.parseColor("#F42B82"));  //color button

        //assign
        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

//setting sweet alert
        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();
    }

    public void fnRegister(View view) {
        if (binding.fullName .getText().toString().isEmpty()||
                binding.email.getText().toString().isEmpty() ||
                binding.password.getText().toString().isEmpty()
        )

        {
            //messageBOx

            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Invalid")
                    .setContentText("Please fill in all the field")
                    .show();
        }

        else
        {
            JSONObject body = new JSONObject();


            try {
                body.put("full_name",binding.fullName.getText().toString());
                body.put("email",binding.email.getText().toString());
                body.put("password",binding.password.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.d("something" ,"test");
            //1function besar ambik dari ()
            RequestController requestController = new RequestController(Request.Method.POST,
                   "/register", body,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {   //success

                            swal.show("Success","Valid Login", SweetAlertDialog.SUCCESS_TYPE);

                            //page kiri current page kanan next page yg nk pergi

                            Intent intent = new Intent(getApplication(), Login.class);  //panggilPage
                            startActivity(intent);
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //error
                            swal.show("Failed","Invalid Login", SweetAlertDialog.ERROR_TYPE);

                        }
                    });
                //sampai sini
                    requestQueue.add(requestController);

        }
    }
}