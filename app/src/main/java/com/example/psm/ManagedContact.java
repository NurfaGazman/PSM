package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaSession2;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.psm.Controller.ContactController;
import com.example.psm.Controller.SweetAlert;
import com.example.psm.Model.Contact;
import com.example.psm.Model.User;
import com.example.psm.databinding.ActivityManagedContactBinding;
import com.example.psm.databinding.ActivityManagedProfileBinding;

import java.util.Vector;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //panggil button

        requestQueue = Volley.newRequestQueue(getApplicationContext()) ;

        swal=new SweetAlert();
        getSupportFragmentManager().beginTransaction().replace(binding.frgSwal.getId(),swal).commit();

        SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);

        currentUser = new User();

        contact = new Vector<>();


        contact.add(new Contact("Fathihah","013-4310199"));



        contactController = new ContactController(getLayoutInflater(),contact);
        binding.listContact.setAdapter(contactController);
        binding.listContact.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


}