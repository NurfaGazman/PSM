package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.RequestController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.Model.User;
import com.example.psm.databinding.ActivityManagedProfileBinding;
import com.example.psm.databinding.ActivityRegister2Binding;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ManagedProfile extends AppCompatActivity {

    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;
    private ActivityManagedProfileBinding binding;

    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityManagedProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton

        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();


        //setiap kali guna token copy yang ini
        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);

        currentUser = new User();
        binding.btnSave.setOnClickListener(this::UserUpdate);

    }

    @Override
    protected void onStart() {
        //ambik function parents
        super.onStart();
        loadUser();

    }

    public void loadUser(){
        RequestController requestController = new RequestController(Request.Method.GET,
                "/api/User", null, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //success
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //binding.FullName.setText(jsonObject.getString("full_name"));


                            //currentUser.setUser_Id(jsonObject.getInt("user_Id"));

                            if(!jsonObject.isNull("full_name"))
                                currentUser.setFull_name(jsonObject.getString("full_name"));

                            if(!jsonObject.isNull("email"))
                                currentUser.setEmail(jsonObject.getString("email"));

                            if(!jsonObject.isNull("birth_date"))
                                currentUser.setBirth_date(jsonObject.getString("birth_date"));

                            if(!jsonObject.isNull("address"))
                                currentUser.setAddress(jsonObject.getString("address"));

                            if(!jsonObject.isNull("blood_gp"))
                                currentUser.setBlood_gp(jsonObject.getString("blood_gp"));

                            if(!jsonObject.isNull("medical_Id"))
                                currentUser.setMedical_id(jsonObject.getString("medical_Id"));


                            refreshInfo();


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
    public void refreshInfo(){
        binding.FullName.setText(currentUser.getFull_name());
        binding.address.setText(currentUser.getAddress());
        binding.dateBirth.setText(currentUser.getBirth_date());
        binding.editTextTextPersonName2.setText(currentUser.getMedical_id());
        

        binding.radioGroup.clearCheck();
        if(currentUser.getBlood_gp().equals("A"))
        {
            binding.BloodA.setChecked(true);

        }else if (currentUser.getBlood_gp().equals("B"))
        {
            binding.BloodB.setChecked(true);

        }else if (currentUser.getBlood_gp().equals("AB"))
        {
            binding.BloodAb.setChecked(true);

        }else if (currentUser.getBlood_gp().equals("O"))
        {
            binding.BloodO.setChecked(true);
        }

    }
    public void  UserUpdate(View view) {
        JSONObject body = new JSONObject();


        try {

            body.put("full_name",binding.FullName.getText().toString());
            body.put("address",binding.address.getText().toString());
            body.put("medical_Id",binding.editTextTextPersonName2.getText().toString());
            body.put("birth_date",binding.dateBirth.getText().toString());
            int bloodGroupBtn = binding.radioGroup.getCheckedRadioButtonId();

            if(bloodGroupBtn!= -1){
                RadioButton blood_gp = findViewById(bloodGroupBtn);
                body.put("blood_gp",blood_gp.getText().toString());
            }

            RequestController requestController = new RequestController(Request.Method.PUT,
                    "/api/User", body, token,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {   //success

                            swal.show("Update","Success Updated", SweetAlertDialog.SUCCESS_TYPE);


                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //error
                            swal.show("Failed","Invalid update", SweetAlertDialog.ERROR_TYPE);

                        }
                    });

            requestQueue.add(requestController);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}