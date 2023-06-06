package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminPage extends AppCompatActivity {
    Button content,volunt,profile,users;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        id = getIntent().getStringExtra("id");
        content = findViewById(R.id.admin_content);
        users = findViewById(R.id.admin_users);
        volunt = findViewById(R.id.admin_volunt);
        profile = findViewById(R.id.admin_prof);
        volunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(AdminPage.this, ManageUsers.class);
                intent.putExtra("id", id);
                intent.putExtra("user_type", "volunteer");
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPage.this, Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "admin");
                startActivity(intent);
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPage.this, ManageUsers.class);
                intent.putExtra("id", id);
                intent.putExtra("user_type", "user");
                startActivity(intent);
            }
        });
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPage.this, ContentPage.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "admin");
                startActivity(intent);
            }
        });





    }
}