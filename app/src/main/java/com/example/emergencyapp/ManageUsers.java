package com.example.emergencyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageUsers extends AppCompatActivity {
    String username,user_type,type,title,msg;

    private final String URL2 =Server.ip + "blockAccount.php";
    String URL3 = Server.ip + "getusers.php";
    public ArrayList<String> users;
    public ArrayList<String> usersIDs;
    ListView simpleList;
    int pos;
    RadioButton active,block;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        username = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("user_type");

        active=findViewById(R.id.active);
        block=findViewById(R.id.block);
        simpleList = findViewById(R.id.req_list5);

        active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(active.isChecked()){
                    user_type="active";
                    title="Blocking a user";
                    msg="Are You sure that you want to Block the user:";
                }else{
                    user_type="block";
                    title="Activating a user";
                    msg="Are You sure that you want to Activate the user:";
                }
                getUsers();
            }
        });
        user_type="active";
        title="Blocking a user";
        msg="Are You sure that you want to Block the user:";
        getUsers();
    }
    public void getUsers() {
        class Async extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("op_type", user_type);
                data.put("usertype", type);
                String result = con.sendPostRequest(URL3, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    simpleList.setAdapter(null);
                    Toast.makeText(getApplicationContext(),"No Results", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        simpleList.setAdapter(null);

                        users = new ArrayList<>();
                        usersIDs = new ArrayList<>();

                        JSONArray allReq = new JSONArray(result);
                        for (int i = 0; i < allReq.length(); i++) {

                            JSONObject row = allReq.getJSONObject(i);
                            String username=row.getString("username");
                            String name=row.getString("name");
                            users.add("Username: "+username+"\nName: "+name);
                            usersIDs.add(username);
                        }
                        String res[] = new String[users.size()];
                        for (int j = 0; j < users.size(); j++) {
                            res[j] = users.get(j);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_view, R.id.item_n, res);
                        simpleList.setAdapter(adapter);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {
                                pos=position;
                                AlertDialog.Builder alert = new AlertDialog.Builder(ManageUsers.this);
                                alert.setTitle(title);
                                alert.setMessage(msg+usersIDs.get(position)+"?");
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        BlockUser(usersIDs.get(pos));
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Async la = new Async();
        la.execute();
    }

    public void BlockUser(String user) {

        class packTask extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ManageUsers.this, getResources().getString(R.string.wait), "Connecting....");
            }

            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("userId", params[0]);
                data.put("op_type", user_type);
                data.put("usertype", type);
                String result = con.sendPostRequest(URL2, data);
                return result;
            }

            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                } else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "No Results", Toast.LENGTH_LONG).show();
                } else if (result.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                    getUsers();
                }
            }
        }

        packTask log = new packTask();
        log.execute(user);

    }


}