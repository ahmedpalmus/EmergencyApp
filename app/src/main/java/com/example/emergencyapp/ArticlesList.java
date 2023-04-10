package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.emergencyapp.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ArticlesList extends AppCompatActivity {
    String id,usertype;
    ListView simpleList;
    ArrayAdapter<String> adapter;

    public ArrayList<Post> posts;
    private final String URL = Server.ip + "getposts.php";

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_list);
        id=getIntent().getStringExtra("id");
        usertype=getIntent().getStringExtra("type");
        add = findViewById(R.id.post_add);

        if(!usertype.equals("lawyer")){
            add.setVisibility(View.GONE);
        }
        simpleList = findViewById(R.id.post_list);
        posts = new ArrayList<>();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArticlesList.this, AddArticle.class);
                intent.putExtra("id",id);
                intent.putExtra("op_type","add");
                intent.putExtra("usertype",usertype);
                startActivity(intent);
            }
        });

        getInfos();
    }

    protected void onResume() {
        super.onResume();
        getInfos();
    }
    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ArticlesList.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", id);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                posts.clear();
                String res1[] = new String[0];
                adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.post_view, R.id.item_n, res1);
                simpleList.setAdapter(adapter);
                adapter.clear();
                adapter.notifyDataSetChanged();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "No Infos", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        posts = new ArrayList<>();
                        JSONArray allReq = new JSONArray(result);
                        for (int i = 0; i < allReq.length(); i++) {
                            JSONObject row = allReq.getJSONObject(i);
                            Post temp=new Post();


                            temp.setPost_id(row.getString("post_id"));
                            temp.setUser_id(row.getString("user_id"));
                            temp.setTitle(row.getString("title"));
                            temp.setDetails(row.getString("details"));
                            temp.setAdd_date(row.getString("add_date"));
                            temp.setImage(row.getString("image"));
                            temp.setFullname(row.getString("fullname"));
                            posts.add(temp);

                        }

                        String res[] = new String[posts.size()];
                        for (int j = 0; j < posts.size(); j++) {
                            res[j] =posts.get(j).getTitle()+"\nBy : "+posts.get(j).getFullname();
                        }
                        adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.post_view, R.id.item_n, res);
                        simpleList.setAdapter(adapter);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                                Intent intent = new Intent(ArticlesList.this, ArticleDetails.class);
                                intent.putExtra("id",id);
                                intent.putExtra("post", posts.get(position));
                                intent.putExtra("usertype",usertype);

                                startActivity(intent);
                            }
                        });

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