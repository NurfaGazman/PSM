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
   MediaPlayer mp1 , mp2;

    @Override
    protected void onPause() {
        super.onPause();
        if(mp1 !=null){
            mp1.release();
        }
        if(mp2 !=null){
            mp2.release();
        }
    }

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
                SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = sharedPreferences.edit();
                edt.putString("sound","Womenscream");
                edt.apply();

                mp1 =  MediaPlayer.create(Setting.this,R.raw.woman_scream);
                mp1.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer){
                        if(mp2 !=null){
                            mp2.release();
                        }

                        mp1.start();
                    }
                });
                mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mp1.release();
                    }
                });
            }
        }));

        Police_Siren.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = sharedPreferences.edit();
                edt.putString("sound","Police_Siren");
                edt.apply();

                mp2 =  MediaPlayer.create(Setting.this,R.raw.police);
                mp2.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer){
                        if(mp1 !=null){
                            mp1.release();
                        }

                        mp2.start();
                    }
                });
                mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mp2.release();
                    }
                });
            }
        }));



        //SharedPreferences sharedPreferences = getSharedPreferences("PSM" , Context.MODE_PRIVATE);
        //token = sharedPreferences.getString("token",null);

    }


}