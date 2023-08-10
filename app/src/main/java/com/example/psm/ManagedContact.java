package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaSession2;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.ContactClick;
import com.example.psm.Controller.ContactController;
import com.example.psm.Controller.RequestController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.Model.Contact;
import com.example.psm.Model.User;
import com.example.psm.databinding.ActivityManagedContactBinding;
import com.example.psm.databinding.ActivityManagedProfileBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ManagedContact extends AppCompatActivity {


    private String token;
    private RequestQueue requestQueue;
    private SweetAlert swal;
    private ActivityManagedContactBinding binding;
    private User currentUser;
    private Vector<Contact> contact;
    private ContactController contactController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagedContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnAddNo.setBackgroundColor(Color.parseColor("#db5a6b"));  //color button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //panggil button

        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();

        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);

        currentUser = new User();
        contact = new Vector<>();

        binding.btnAddNo.setOnClickListener(this::fnContact);


        contactController = new ContactController(getLayoutInflater(), contact, new ContactClick() {
            @Override
            public void clickContact(Contact contact) {

                swal.confirm("Delete Contact?", "Name:" + contact.getName() + "\nNo:" + contact.getContact_no(),
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();

                                RequestController requestController = new RequestController(Request.Method.DELETE,
                                        "/api/Contact/" + contact.getContact_no(), null, token,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                swal.show("Success", "Valid", SweetAlertDialog.SUCCESS_TYPE);
                                                loadContact();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                            swal.show("Failed to Delete", "Invalid to Delete",SweetAlertDialog.ERROR_TYPE);
                                            loadContact();
                                    }

                                });
                                requestQueue.add(requestController);
                            }
                        });
            }
        });
        binding.listContact.setAdapter(contactController);
        binding.listContact.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


    public void fnContact(View view) {
        if (binding.insertName.getText().toString().isEmpty() ||
                binding.insertcontact.getText().toString().isEmpty()
        )
        {
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Please insert name and contact")
                    .setContentText("Please fill in all the field")
                    .show();

        }
        else{
            JSONObject body = new JSONObject();

            try{

                body.put("name",binding.insertName.getText().toString());
                body.put("contact_no",binding.insertcontact.getText().toString());

                //ambik user select
                String sendMessage = binding.list.getSelectedItem().toString();
                if(sendMessage.equals("None"))
                    sendMessage = "";
                body.put("message",sendMessage);


            }catch (JSONException e) {
                e.printStackTrace();
            }

            RequestController requestController = new RequestController(Request.Method.POST,
                    "/api/Contact", body, token,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {   //success

                            swal.show("Success","Valid", SweetAlertDialog.SUCCESS_TYPE);
                            loadContact();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) { //error
                            swal.show("Failed","Invalid", SweetAlertDialog.ERROR_TYPE);
                        }
                    });
                    requestQueue.add(requestController);
        }


    }

    public void loadContact(){
        contact.clear();
        RequestController requestController = new RequestController(Request.Method.GET,
                "/api/Contact", null, token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //success
                        try {
                           JSONArray jsonArray = new JSONArray(response);


                        for(int i=0; i <jsonArray.length(); i ++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Contact contactuser = new Contact();

                            if(!jsonObject.isNull("contact_no"))
                                contactuser.setContact_no(jsonObject.getString("contact_no"));

                            if(!jsonObject.isNull("message"))
                                contactuser.setMessage(jsonObject.getString("message"));

                            if(!jsonObject.isNull("name"))
                                contactuser.setName(jsonObject.getString("name"));

                            contact.add(contactuser);
                            contactController.notifyDataSetChanged();

                        }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swal.show("Failed","Invalid update", SweetAlertDialog.ERROR_TYPE);
            }
        });
        requestQueue.add(requestController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadContact();
    }
}