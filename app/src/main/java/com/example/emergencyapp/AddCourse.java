package com.example.emergencyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emergencyapp.model.Post;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddCourse extends AppCompatActivity {
    EditText etitle, edetail,eplace,eperiod,econtact;
    Button save, cancel,del,start_date;
    Calendar myCalendar;

    String URL = Server.ip + "addcourse.php";
    Post post;
    TextView l1,l2,l3,l4,l5,l6,l7;
    String id, title,place,period,contact,start,  details, op_type,post_id="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        id = getIntent().getStringExtra("id");
        op_type = getIntent().getStringExtra("op_type");

        etitle = findViewById(R.id.po_title);
        edetail = findViewById(R.id.po_details);
        eplace = findViewById(R.id.po_place);
        eperiod = findViewById(R.id.po_period);
        econtact = findViewById(R.id.po_contatc);
        start_date = findViewById(R.id.po_start);

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(start_date);

            }
        });

        del = findViewById(R.id.po_del);

        if (op_type.equals("edit")) {
            post = (Post) getIntent().getSerializableExtra("post");
            etitle.setText(post.getTitle());
            edetail.setText(post.getDetails());
            eplace.setText(post.getPlace());
            eperiod.setText(post.getPeriod());
            econtact.setText(post.getContact());
            start_date.setText(post.getStart());

            post_id=post.getPost_id();
            del.setVisibility(View.VISIBLE);
            title=post.getTitle();
            details=post.getDetails();
            place=post.getPlace();
            period=post.getPeriod();
            contact=post.getContact();
            start=post.getStart();

        }

        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        l4 = findViewById(R.id.l4);
        l6 = findViewById(R.id.l6);
        l7 = findViewById(R.id.l7);

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
                AlertDialog.Builder alert = new AlertDialog.Builder(AddCourse.this);
                alert.setTitle("Deleting a course");
                alert.setMessage("Are You sure?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        op_type="del";
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


    }
    public void setDate(Button vdate) {
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                vdate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(AddCourse.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void Save() {

        title = etitle.getText().toString().trim();
        place = eplace.getText().toString().trim();
        period = eperiod.getText().toString().trim();
        contact = econtact.getText().toString().trim();
        start = start_date.getText().toString().trim();
        details = edetail.getText().toString().trim();

        l1.setTextColor(Color.WHITE);

        boolean err = false;
        if (title.length() < 2) {
            l1.setTextColor(Color.RED);
            err = true;
        }
        if (details.length() < 2) {
            l7.setHighlightColor(Color.RED);
            err = true;
        }if (place.length() < 2) {
            l2.setHighlightColor(Color.RED);
            err = true;
        }if (period.length() < 2) {
            l3.setHighlightColor(Color.RED);
            err = true;
        }if (contact.length() < 2) {
            l6.setHighlightColor(Color.RED);
            err = true;
        }if (start.equals("Select Date")) {
            l4.setHighlightColor(Color.RED);
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
                loadingDialog = ProgressDialog.show(AddCourse.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("user_id", id);
                data.put("title", title);
                data.put("place", place);
                data.put("period", period);
                data.put("start", start);
                data.put("contact", contact);
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
}