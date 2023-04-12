package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContentPage extends AppCompatActivity {
    Button articles,videos,courses;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_page);

        id = getIntent().getStringExtra("id");
        articles = findViewById(R.id.ad_articles);
        videos = findViewById(R.id.ad_video);
        courses = findViewById(R.id.ad_courses);
        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(ContentPage.this, ContentPage.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentPage.this, ContentPage.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentPage.this, ArticlesList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "admin");
                startActivity(intent);
            }
        });





    }
}