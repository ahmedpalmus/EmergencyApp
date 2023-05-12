package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VolunteerPage extends AppCompatActivity {
    Button reqs,articles,courses,videos,profile;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_page);

        id = getIntent().getStringExtra("id");
        reqs = findViewById(R.id.client_help);
        articles = findViewById(R.id.client_articles);
        courses = findViewById(R.id.client_course);
        videos = findViewById(R.id.client_vids);
        profile = findViewById(R.id.client_prof);
        reqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(AdminPage.this, ManageUsers.class);
                intent.putExtra("id", id);
                startActivity(intent);*/
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerPage.this, Profile.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "volunteer");
                startActivity(intent);
            }
        });
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerPage.this, CourseList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "volunteer");
                startActivity(intent);
            }
        });
        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VolunteerPage.this, ArticlesList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "volunteer");
                startActivity(intent);
            }
        });

        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(VolunteerPage.this, QuestionList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "admin");
                startActivity(intent);*/
            }
        });



    }
}