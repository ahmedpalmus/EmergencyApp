package com.example.emergencyapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emergencyapp.model.HelpRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class RequestDetails extends AppCompatActivity {
    TextView name, add_date, phone, gender, details;
    ImageView imageView;
    EditText rep;
    Button map, send, cancel, call,resp;
    HelpRequest post;
    String username, usertype,response;
    String URL = Server.ip + "sendresp.php";
LinearLayout resp_l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        username = getIntent().getStringExtra("id");
        post = (HelpRequest) getIntent().getSerializableExtra("post");
        usertype = getIntent().getStringExtra("usertype");

        name = findViewById(R.id.post_name);
        add_date = findViewById(R.id.post_add);
        phone = findViewById(R.id.post_phone);
        gender = findViewById(R.id.post_gender);
        details = findViewById(R.id.post_det);
        imageView = findViewById(R.id.roundedImageView);
        rep = findViewById(R.id.ur_response);
        map = findViewById(R.id.map_loc);
        send = findViewById(R.id.add_response);
        call = findViewById(R.id.call);
        cancel = findViewById(R.id.cancel);
        resp_l = findViewById(R.id.ll);
        resp = findViewById(R.id.resps);

        name.setText("By: " + post.getFullname());
        add_date.setText("By: " + post.getAdd_date());
        phone.setText("By: " + post.getPhone());
        gender.setText("By: " + post.getGender());
        details.setText("By: " + post.getDetails());

        getImage(post.getImage(), imageView);

        if(usertype.equals("user")){
            call.setVisibility(View.GONE);
            resp_l.setVisibility(View.GONE);
            map.setVisibility(View.GONE);
        }else{
            call.setVisibility(View.VISIBLE);
            resp_l.setVisibility(View.VISIBLE);
            map.setVisibility(View.VISIBLE);
            resp.setVisibility(View.GONE);

        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + post.getPhone()));
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response=rep.getText().toString().trim();
                if(response.length()>5){
                    SendInfo();
                }else{
                    Toast.makeText(RequestDetails.this, "Enter Valid Response", Toast.LENGTH_LONG).show();
                }
            }
        });
        resp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestDetails.this, Responses.class);
                intent.putExtra("id", username);
                intent.putExtra("post_id", post.getReq_id());
                startActivity(intent);
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMap(post.getLat(),post.getLon());
            }
        });
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
    private void SendInfo() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(RequestDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", username);
                data.put("req_id", post.getReq_id());
                data.put("response", response);

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
    public void OpenMap(String lat,String lon){

        double latitude = Double.parseDouble(lat); // The latitude value
        double longitude = Double.parseDouble(lon); // The longitude value
        String uri = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
   /* // Create a Uri object with the coordinates and marker
    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(Customer Location)");

    // Create an Intent with the action and Uri
    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

    // Set the package to the Google Maps app
    mapIntent.setPackage("com.google.android.apps.maps");

    // Verify if there is an app available to handle the Intent
    if (mapIntent.resolveActivity(getPackageManager()) != null) {
        // Start the activity
        startActivity(mapIntent);
    }*/
    }

}