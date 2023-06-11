package com.example.psm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.android.volley.toolbox.Volley;

public class Setting extends AppCompatActivity {

    private String token;
   Button Womenscream, Police_Siren;
   MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Womenscream = findViewById(R.id.Women_scream);
        Police_Siren = findViewById(R.id.Siren);

        Womenscream.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mp =  MediaPlayer.create(Setting.this,R.raw.woman_scream);
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
        }));

        Police_Siren.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mp =  MediaPlayer.create(Setting.this,R.raw.police);
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
        }));


    
        //SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        //token = sharedPreferences.getString("token",null);

    }


}