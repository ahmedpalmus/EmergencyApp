package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    EditText pass,pass2,perName,mail,phone;

    String username,type,password,name,email,thePhone;
    String URL = Server.ip +"Edit.php";
    String URL2 = Server.ip +"getUser.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username=getIntent().getStringExtra("id");
        type=getIntent().getStringExtra("type");

        pass=findViewById(R.id.prof_pass);
        pass2=findViewById(R.id.prof_pass2);
        perName=findViewById(R.id.prof_name);
        mail=findViewById(R.id.prof_email);
        phone=findViewById(R.id.prof_phone);
        getInfo();
    }

    public void editOnclick(View view){
        if(view.getId()==R.id.prof_update){
            password=pass.getText().toString().trim();
            String password2=pass2.getText().toString().trim();
            name=perName.getText().toString().trim();
            email=mail.getText().toString().trim();
            thePhone=phone.getText().toString().trim();
            if (password.length()>0 || password2.length() >0){
                if (password.length()<8) {
                    Toast.makeText(this,"Password should have more than 7 characters", Toast.LENGTH_LONG).show();
                }else if (!password.equals(password2)) {
                    Toast.makeText(this,getResources().getString(R.string.enterPass2), Toast.LENGTH_LONG).show();
                }
            }else if ( name.length() < 2) {
                Toast.makeText(this, "Enter valid name" , Toast.LENGTH_LONG).show();
            }  else if (!isValidEmail(email)) {
                Toast.makeText(this, getResources().getString(R.string.enterEmail), Toast.LENGTH_LONG).show();
            }else if (thePhone.length()!=10 || !thePhone.startsWith("05")) {
                Toast.makeText(this, getResources().getString(R.string.enterPhone), Toast.LENGTH_LONG).show();
            } else {
                Update();
            }

        }else if(view.getId()==R.id.prof_cancel){
            finish();
        }
    }
    private void Update() {
        class RegAsync extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("password", params[0]);
                data.put("email", params[1]);
                data.put("name", params[2]);
                data.put("phone", params[3]);
                data.put("type", type);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(),"Try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        if(password.length()>0)
            la.execute(password,email,name,thePhone);
        else
            la.execute("no",email,name,thePhone);

    }

    private void getInfo() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Profile.this, getResources().getString(R.string.wait), "Connecting.....");
            }
            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", username);
                data.put("type", type);
                String result = con.sendPostRequest(URL2, data);
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(),"No Results", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONArray allInfo = new JSONArray(result);
                        for (int i = 0; i < allInfo.length(); i++) {
                            JSONObject row = allInfo.getJSONObject(i);
                            name=row.getString("name");
                            email=row.getString("email");
                            thePhone=row.getString("phone");
                            perName.setText(name);
                            mail.setText(email);
                            phone.setText(thePhone);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Async la = new Async();
        la.execute();
    }
    public boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}

