package com.example.emergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class UserReg extends AppCompatActivity {

    EditText userN, pass, pass2, perName, addr, phone, email;
    TextView l1, l2, l3, l4, l5, l6, l7;
Spinner gender;
    String user, password, name, address, thePhone, Email, gen="male";
    String URL = Server.ip + "register.php";
    Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);
        userN = findViewById(R.id.reg_username);
        pass = findViewById(R.id.reg_pass);
        pass2 = findViewById(R.id.reg_pass2);
        perName = findViewById(R.id.reg_name);
        addr = findViewById(R.id.reg_address);
        phone = findViewById(R.id.reg_phone);
        email = findViewById(R.id.reg_email);
        gender = findViewById(R.id.reg_gender);


        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        l4 = findViewById(R.id.l4);
        l5 = findViewById(R.id.l5);
        l6 = findViewById(R.id.l6);
        l7 = findViewById(R.id.l7);


        reg = findViewById(R.id.reg_reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegOnclick(view);
            }
        });


    }

    public void RegOnclick(View view) {
        if (view.getId() == R.id.reg_reg) {

            l1.setText(getResources().getString(R.string.username));
            l1.setTextColor(Color.BLACK);
            l2.setText(getResources().getString(R.string.password));
            l2.setTextColor(Color.BLACK);
            l3.setText(getResources().getString(R.string.conform_pasword));
            l3.setTextColor(Color.BLACK);
            l4.setText(getResources().getString(R.string.pername));
            l4.setTextColor(Color.BLACK);
            l5.setText(getResources().getString(R.string.address));
            l5.setTextColor(Color.BLACK);
            l6.setText(getResources().getString(R.string.phone));
            l6.setTextColor(Color.BLACK);
            l7.setText(getResources().getString(R.string.email));
            l7.setTextColor(Color.BLACK);

            user = userN.getText().toString().trim();
            password = pass.getText().toString().trim();
            String password2 = pass2.getText().toString().trim();
            name = perName.getText().toString().trim();
            address = addr.getText().toString().trim();
            thePhone = phone.getText().toString().trim();
            Email = email.getText().toString().trim();
            gen=gender.getSelectedItem().toString().toLowerCase(Locale.ROOT);

            boolean error = false;
            if (user.length() < 3) {
                error = true;
                String temp = l1.getText().toString();
                l1.setText(temp + " *");
                l1.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.enterUsername), Toast.LENGTH_LONG).show();
            }
            if (password.length() < 8) {
                error = true;
                String temp = l2.getText().toString();
                l2.setText(temp + " *");
                l2.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.enterPass), Toast.LENGTH_LONG).show();
            }
            if (!isValidPassword(password)) {
                error = true;
                String temp = l2.getText().toString();
                l2.setText(temp + " *");
                l2.setTextColor(Color.RED);
                Toast.makeText(this, "The password must contain letters and numbers", Toast.LENGTH_LONG).show();
            }
            if (!password.equals(password2)) {
                error = true;
                String temp = l2.getText().toString();
                l2.setText(temp + " *");
                l2.setTextColor(Color.RED);
                temp = l3.getText().toString();
                l3.setText(temp + " *");
                l3.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.enterPass2), Toast.LENGTH_LONG).show();
            }
            if (name.length() < 2 || name.length() > 30 || hasNumbers(name)) {
                error = true;
                String temp = l4.getText().toString();
                l4.setText(temp + " *");
                l4.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.enterPersonal), Toast.LENGTH_LONG).show();
            }
            if (address.length() < 3) {
                error = true;
                String temp = l5.getText().toString();
                l5.setText(temp + " *");
                l5.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.enterAddress), Toast.LENGTH_LONG).show();
            }
            if (!isValidEmail(Email)) {
                error = true;
                String temp = l7.getText().toString();
                l7.setText(temp + " *");
                l7.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.enterEmail), Toast.LENGTH_LONG).show();
            }
            if (thePhone.length() != 10 || !thePhone.startsWith("05")) {
                error = true;
                String temp = l6.getText().toString();
                l6.setText(temp + " *");
                l6.setTextColor(Color.RED);
                Toast.makeText(this, getResources().getString(R.string.enterPhone), Toast.LENGTH_LONG).show();
            }

            if (!error) {
                Registration();
            }

        }

    }

    private void Registration() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(UserReg.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", params[0]);
                data.put("password", params[1]);
                data.put("name", params[2]);
                data.put("address", params[3]);
                data.put("phone", params[4]);
                data.put("email", params[5]);
                data.put("gender", gen);


                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noResult), Toast.LENGTH_LONG).show();
                } else if (result.equalsIgnoreCase("Exist")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.enterREgistration), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserReg.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute(user, password, name, address, thePhone, Email);
    }

    public boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
    public boolean hasNumbers(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (Character.isDigit(name.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}

