package com.example.emergencyapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emergencyapp.model.Vid;

import java.util.HashMap;

public class AddVideo extends AppCompatActivity {

    EditText etitle, edetail, evid_url;
    Button save, cancel, del, check;
    WebView vid_1;

    String URL = Server.ip + "addvideo.php";
    Vid post;
    TextView l1;
    String id, title, vid1_url, details, op_type, post_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        id = getIntent().getStringExtra("id");
        op_type = getIntent().getStringExtra("op_type");

        etitle = findViewById(R.id.po_title);
        edetail = findViewById(R.id.po_details);
        evid_url = findViewById(R.id.vid_url_1);
        check = findViewById(R.id.vid_save_1);
        save = findViewById(R.id.po_save);
        cancel = findViewById(R.id.po_cancel);
        del = findViewById(R.id.po_del);
        vid_1 = findViewById(R.id.vid_1);


        if (op_type.equals("edit")) {
            post = (Vid) getIntent().getSerializableExtra("post");
            etitle.setText(post.getTitle());
            edetail.setText(post.getDetails());

            post_id = post.getVideo_id();
            del.setVisibility(View.VISIBLE);
            title = post.getTitle();
            details = post.getDetails();
            vid1_url = post.getVid_url();

        }

        l1 = findViewById(R.id.l1);


        save = findViewById(R.id.po_save);
        cancel = findViewById(R.id.po_cancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddVideo.this);
                alert.setTitle("Deleting a course");
                alert.setMessage("Are You sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        op_type = "del";
                        SendInfo();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.create().show();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });
    }

    public void Save() {

        title = etitle.getText().toString().trim();
        vid1_url = evid_url.getText().toString().trim();

        details = edetail.getText().toString().trim();

        l1.setTextColor(Color.WHITE);

        boolean err = false;
        if (title.length() < 2) {
            l1.setTextColor(Color.RED);
            err = true;
        }
        if (details.length() < 2) {
            edetail.setHighlightColor(Color.RED);
            err = true;
        }
        if (vid1_url.length() < 2) {
            evid_url.setHighlightColor(Color.RED);
            err = true;
        }
        if (!err) {
            SendInfo();
        } else {
            Toast.makeText(getApplicationContext(), "Enter all the required fields", Toast.LENGTH_LONG).show();
        }


    }

    private void SendInfo() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(AddVideo.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", id);
                data.put("title", title);
                data.put("vid_url", vid1_url);
                data.put("details", details);
                data.put("op_type", op_type);
                data.put("post_id", post_id);
                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                } else if (result.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute();
    }

    public void Check() {
        //https://youtu.be/71h8MZshGSs
        vid1_url = evid_url.getText().toString().trim();
        if (vid1_url.contains(".be/")) {
            vid1_url = vid1_url.split(".be/")[1];
            int ampersandPosition = vid1_url.indexOf('&');
            if (ampersandPosition != -1) {
                vid1_url = vid1_url.substring(0, ampersandPosition);
            }
        } else {
            vid1_url = vid1_url.split("v=")[1];
            int ampersandPosition = vid1_url.indexOf('&');
            if (ampersandPosition != -1) {
                vid1_url = vid1_url.substring(0, ampersandPosition);
            }
        }
        if (vid1_url.length() > 9) {
            vid_1.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + vid1_url + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
            vid_1.getSettings().setJavaScriptEnabled(true);
            vid_1.setWebChromeClient(new WebChromeClient() {
            });

        } else {
            vid1_url = "";
            Toast.makeText(AddVideo.this, "Enter Valid Video Link", Toast.LENGTH_LONG).show();
        }

    }


}