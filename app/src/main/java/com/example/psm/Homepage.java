package com.example.psm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.psm.databinding.ActivityHomepageBinding;
import com.example.psm.databinding.ActivityRegister2Binding;

import java.util.zip.Inflater;

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
        binding.btnContact.setOnClickListener(this::goToContact);
        binding.btnsetting.setOnClickListener(this::goToSetting);
        binding.btnself.setOnClickListener(this::goToSelf);
        binding.btnPeriodHome.setOnClickListener(this::goToPeriodHome);
    }



    //panggil link button yang tuk dilinkkan
    public void goToContact(View view) {
        Intent intent = new Intent(Homepage.this, ManagedContact.class);  //panggilPage
        startActivity(intent);
    }

    public void goToProfile(View view){
        Intent intent = new Intent(Homepage.this, ManagedProfile.class);  //panggilPage
        startActivity(intent);
    }

    public void goToSetting(View view){
        Intent intent = new Intent(Homepage.this, Setting.class);  //panggilPage
        startActivity(intent);
    }

    public void goToSelf(View view){
        Intent intent = new Intent(Homepage.this, SelfLearn.class);  //panggilPage
        startActivity(intent);
    }

    public void goToPeriodHome(View view){
        Intent intent = new Intent(Homepage.this, PeriodHome.class);  //panggilPage
        startActivity(intent);
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
