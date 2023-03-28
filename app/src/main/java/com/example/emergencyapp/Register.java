package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void reggClick(View view) {
        if (view.getId() == R.id.cust_reg) {
            Intent intent = new Intent(Register.this, VoluntReg.class);
            startActivity(intent);
        } else if (view.getId() == R.id.store_reg) {
            Intent intent = new Intent(Register.this, UserReg.class);
            startActivity(intent);
        }
    }
}