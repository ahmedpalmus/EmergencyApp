package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.emergencyapp.model.Post;
import com.example.emergencyapp.model.Vid;

public class VideoDetails extends AppCompatActivity {
    String username,usertype;
    Vid post;
    TextView title,detail,add_date;
WebView vid_1;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);
        username=getIntent().getStringExtra("id");
        post=(Vid) getIntent().getSerializableExtra("post");
        usertype=getIntent().getStringExtra("usertype");

        title=findViewById(R.id.mem_title);
        vid_1=findViewById(R.id.vid_1);
        detail=findViewById(R.id.mem_details);
        add_date=findViewById(R.id.mem_add);

        edit=findViewById(R.id.fm_edit);

        if( usertype.equals("admin")){
            edit.setVisibility(View.VISIBLE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoDetails.this, AddVideo.class);
                intent.putExtra("id",username);
                intent.putExtra("op_type","edit");
                intent.putExtra("post",post);

                startActivity(intent);
                finish();
            }
        });

        title.setText(post.getTitle());

        detail.setText(post.getDetails());
        add_date.setText("Added on: "+post.getAdd_time());

        vid_1.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + post.getVid_url() + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
        vid_1.getSettings().setJavaScriptEnabled(true);
        vid_1.setWebChromeClient(new WebChromeClient() {
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

    }


}