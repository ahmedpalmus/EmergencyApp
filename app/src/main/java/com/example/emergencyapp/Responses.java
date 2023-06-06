package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.emergencyapp.model.Comment;
import com.example.emergencyapp.model.CommentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Responses extends AppCompatActivity {
    private RecyclerView dRecycle;
    private CommentAdapter dAdapter;
    ArrayList<Comment> comments;
LinearLayoutManager linearLayoutManager;
    String URL = Server.ip + "getresponses.php";

String username,post_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responses);
        username=getIntent().getStringExtra("id");
        post_id=getIntent().getStringExtra("post_id");
        comments = new ArrayList<>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        dRecycle = findViewById(R.id.post_list);
        dRecycle.setHasFixedSize(true);
        dRecycle.setLayoutManager(linearLayoutManager);

        dRecycle.smoothScrollToPosition(comments.size());
        dAdapter = new CommentAdapter(comments);

        dRecycle.setAdapter(dAdapter);
        getComments();
    }

    private void getComments() {
        class Async extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("post_id", post_id);
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
                            temp.setUsername(row.getString("fullname"));
                            temp.setTime(row.getString("add_date"));
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
}