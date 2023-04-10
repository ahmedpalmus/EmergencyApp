package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencyapp.model.Comment;
import com.example.emergencyapp.model.CommentAdapter;
import com.example.emergencyapp.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ArticleDetails extends AppCompatActivity {
    String username,comment,usertype;
    Post post;
    TextView title,fullname,detail,add_date;
    ImageView imageView;
    EditText cmnt;
    ArrayList<Comment> comments;
    private final String URL = Server.ip + "getComments.php";
    private final String URL2 = Server.ip + "sendComment.php";

    private RecyclerView dRecycle;
    private CommentAdapter dAdapter;

    Button add,edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        username=getIntent().getStringExtra("id");
        post=(Post) getIntent().getSerializableExtra("post");
        usertype=getIntent().getStringExtra("usertype");

        title=findViewById(R.id.post_title);
        fullname=findViewById(R.id.post_name);
        detail=findViewById(R.id.post_det);
        add_date=findViewById(R.id.post_add);
        imageView=findViewById(R.id.roundedImageView);
        add=findViewById(R.id.add_comment);
        cmnt=findViewById(R.id.post_comm);
        edit=findViewById(R.id.edit_post);

        if(username.equals(post.getUser_id()) || usertype.equals("admin")){
            edit.setVisibility(View.VISIBLE);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArticleDetails.this, AddArticle.class);
                intent.putExtra("id",username);
                intent.putExtra("op_type","edit");
                intent.putExtra("post",post);

                startActivity(intent);
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment=cmnt.getText().toString().trim();
                if(comment.length()<2){
                    Toast.makeText(ArticleDetails.this,"Enter Your Comment",Toast.LENGTH_LONG).show();
                }else{
                    sendComment();
                }
            }
        });

        title.setText(post.getTitle());
        fullname.setText(post.getFullname());
        detail.setText(post.getDetails());
        add_date.setText(post.getAdd_date());

        getImage(post.getImage(), imageView);

        comments = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        dRecycle = findViewById(R.id.comment_list);
        dRecycle.setHasFixedSize(true);
        dRecycle.setLayoutManager(linearLayoutManager);

        dRecycle.smoothScrollToPosition(comments.size());
        dAdapter = new CommentAdapter(comments);

        dRecycle.setAdapter(dAdapter);
        getComments();

    }

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

    public void getImage(final String img, final ImageView viewHolder) {

        class packTask extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap image1 = null;
                java.net.URL url = null;
                try {
                    url = new URL(Server.ip + img);
                    image1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image1;
            }

            protected void onPostExecute(Bitmap image) {
                viewHolder.setImageBitmap(image);
            }
        }
        packTask t = new packTask();
        t.execute();
    }
}
