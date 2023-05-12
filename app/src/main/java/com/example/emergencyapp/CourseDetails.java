package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emergencyapp.model.Post;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class CourseDetails extends AppCompatActivity {
    String username,usertype;
    Post post;
    TextView title,place,period,start,contact,detail,add_date;

    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        username=getIntent().getStringExtra("id");
        post=(Post) getIntent().getSerializableExtra("post");
        usertype=getIntent().getStringExtra("usertype");

        title=findViewById(R.id.mem_title);
        place=findViewById(R.id.mem_place);
        period=findViewById(R.id.mem_period);
        start=findViewById(R.id.mem_start);
        contact=findViewById(R.id.mem_contact);
        detail=findViewById(R.id.mem_details);
        add_date=findViewById(R.id.mem_add);

        edit=findViewById(R.id.mem_edit);

        if( usertype.equals("admin")){
            edit.setVisibility(View.VISIBLE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetails.this, AddCourse.class);
                intent.putExtra("id",username);
                intent.putExtra("op_type","edit");
                intent.putExtra("post",post);

                startActivity(intent);
                finish();
            }
        });
/*        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment=cmnt.getText().toString().trim();
                if(comment.length()<2){
                    Toast.makeText(ArticleDetails.this,"Enter Your Comment",Toast.LENGTH_LONG).show();
                }else{
                    sendComment();
                }
            }
        });*/

        title.setText(post.getTitle());
        place.setText(post.getPlace());
        period.setText(post.getPeriod());
        start.setText(post.getStart());
        contact.setText(post.getContact());
        detail.setText(post.getDetails());
        add_date.setText("Added on: "+post.getAdd_date());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
/*        comments = new ArrayList<>();


        dRecycle = findViewById(R.id.comment_list);
        dRecycle.setHasFixedSize(true);
        dRecycle.setLayoutManager(linearLayoutManager);

        dRecycle.smoothScrollToPosition(comments.size());
        dAdapter = new CommentAdapter(comments);

        dRecycle.setAdapter(dAdapter);
        getComments();*/

    }
    /*
        private void sendComment() {
            class RegAsync extends AsyncTask<String, Void, String> {
                private Dialog loadingDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loadingDialog = ProgressDialog.show(ArticleDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
                }

                @Override
                protected String doInBackground(String... params) {
                    Connection con = new Connection();
                    HashMap<String, String> data = new HashMap<>();
                    data.put("user", username);
                    data.put("post",post.getPost_id() );
                    data.put("details",comment );
                    String result = con.sendPostRequest(URL2, data);
                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    loadingDialog.dismiss();
                    if (result.isEmpty() || result.equals("Error"))
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                    else if (result.equals("failure")) {
                        Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_LONG).show();
                    } else if (result.equalsIgnoreCase("success")) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                        getComments();
                    }
                }
            }
            RegAsync la = new RegAsync();
            la.execute();
        }

        private void getComments() {
            class Async extends AsyncTask<String, Void, String> {
                @Override
                protected String doInBackground(String... params) {
                    Connection con = new Connection();
                    HashMap<String, String> data = new HashMap<>();
                    data.put("post", post.getPost_id());
                    String result = con.sendPostRequest(URL, data);
                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    comments.clear();
                    dAdapter.notifyDataSetChanged();
                    if (result.isEmpty() || result.equals("Error")){
                    }
                    else if (result.equals("failure")) {
                    } else {
                        try {
                            comments = new ArrayList<>();
                            JSONArray allReq = new JSONArray(result);
                            for (int i = 0; i < allReq.length(); i++) {

                                JSONObject row = allReq.getJSONObject(i);
                                Comment temp = new Comment();
                                temp.setUsername(row.getString("username"));
                                temp.setTime(row.getString("comment_date"));
                                temp.setContent(row.getString("content"));

                                comments.add(temp);
                            }
                            dAdapter.setmList(comments);
                            dAdapter.notifyDataSetChanged();

                            int x=8;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            Async la = new Async();
            la.execute();
        }
    */

}