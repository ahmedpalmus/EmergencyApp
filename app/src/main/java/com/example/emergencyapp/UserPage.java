package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserPage extends AppCompatActivity {
    Button reqs,articles,courses,videos,profile;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        id = getIntent().getStringExtra("id");
        reqs = findViewById(R.id.client_help);
        articles = findViewById(R.id.client_articles);
        courses = findViewById(R.id.client_course);
        videos = findViewById(R.id.client_vids);
        profile = findViewById(R.id.client_prof);
        reqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, RequestList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, CourseList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });
        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, ArticlesList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });

        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(UserPage.this, VideoList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "user");
                startActivity(intent);
            }
        });



    }
}