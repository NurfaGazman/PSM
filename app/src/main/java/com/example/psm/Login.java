package com.example.psm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.psm.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //clicktext signUp(id)
        binding.SignUp.setOnClickListener(this::SignUp);
    }

    public void fnLogin(View view) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.eeditTextTextEmail.getText().toString(),binding.editTextTextPassword.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Login.this, "Congrats You're Already Sign in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Homepage.class);  //panggilPage
                        startActivity(intent);
                        finish(); 
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "fail to login", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void SignUp(View view){
        Intent intent = new Intent(Login.this, Register.class);  //panggilPage
        startActivity(intent);
    }
}