package com.example.emergencyapp;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencyapp.model.HelpRequest;
import com.example.emergencyapp.model.Post;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

public class AddRequest extends AppCompatActivity {
    EditText edetail,phone;
    Spinner gender;
    private static final int PERMISSION_REQUEST_CODE = 200;

    Button save, cancel, img_btn,map_btn;
    String URL = Server.ip + "addrequest.php";
    ImageView image;

    String id, Image = "none",Phone,Gender,lat=" ",lon=" ", details;
TextView l1,l2,l3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        id = getIntent().getStringExtra("id");

        edetail = findViewById(R.id.help_details);
        img_btn = findViewById(R.id.help_image);
        image = findViewById(R.id.add_pimage);
        phone = findViewById(R.id.reg_phone);
        gender = findViewById(R.id.reg_gender);
        map_btn = findViewById(R.id.help_location);

        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);

        save = findViewById(R.id.help_save);
        cancel = findViewById(R.id.help_cancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser(400);
            }
        });
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();            }
        });
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            getLocation();
            if (lat.length() < 3)
                Toast.makeText(AddRequest.this, "" +
                        "Try agian to get the location", Toast.LENGTH_LONG).show();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                Toast.makeText(AddRequest.this, "Allow Location permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getLocation() {
        lat="18.107809438262883";
        lon="43.1144998134949";
        // Define a LocationListener
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                // lat = "" + location.getLatitude();
                //   lon = "" + location.getLongitude();

                return;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        // Get a reference to the LocationManager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        // or locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void showFileChooser(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedimg = data.getData();
            try {
                image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap univLogo = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                univLogo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                Image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Save() {

        Phone = phone.getText().toString().trim();
        details = edetail.getText().toString().trim();
        Gender=gender.getSelectedItem().toString().toLowerCase(Locale.ROOT);

        l1.setTextColor(Color.BLACK);
        l2.setTextColor(Color.BLACK);
        l3.setTextColor(Color.BLACK);

        boolean err = false;
        if (Phone.length() != 10 || !Phone.startsWith("05")) {
            err = true;
            String temp = l2.getText().toString();
            l2.setText(temp + " *");
            l2.setTextColor(Color.RED);
            Toast.makeText(this, getResources().getString(R.string.enterPhone), Toast.LENGTH_LONG).show();
        }
        if (details.length() < 2) {
            edetail.setHighlightColor(Color.RED);
            err = true;
        }
        if (Image.length() < 7) {
            Toast.makeText(this, "Select image", Toast.LENGTH_LONG).show();
            err = true;
        }if (lon.length() < 7) {
            Toast.makeText(this, "Select location", Toast.LENGTH_LONG).show();
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
                loadingDialog = ProgressDialog.show(AddRequest.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", id);
                data.put("phone", Phone);
                data.put("gender", Gender);
                data.put("image", Image);
                data.put("details", details);
                data.put("lat", lat);
                data.put("lon", lon);
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
}