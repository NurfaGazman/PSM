package com.example.psm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.psm.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    private RequestQueue requestQueue;
    private SweetAlert swal;

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //clicktext signUp(id)
        binding.SignUp.setOnClickListener(this::SignUp);

        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();
    }

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
        Intent intent = new Intent(Login.this, Register.class);  //panggilPage
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
}