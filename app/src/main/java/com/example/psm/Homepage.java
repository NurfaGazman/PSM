package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityRegister2Binding;

public class Homepage extends AppCompatActivity {

    //declare variable string
    private String token;

    //kena ada setiap activity

    ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//wajib letak button back jangan lupa kt manifest jugak . follow homepage jugk
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton

    //sampai sini

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //backbutton

        //ambil value dalam shared pref


        binding.btnProfile.setOnClickListener(this::goToProfile);

    }

    //panggil link button yang tuk dilinkkan
    public void goToProfile(View view){
        Intent intent = new Intent(Homepage.this, ManagedProfile.class);  //panggilPage
        startActivity(intent);
    }

}
