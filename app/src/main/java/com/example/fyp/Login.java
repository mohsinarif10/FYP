package com.example.fyp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp.ui.home.HomeFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE=1000;
    public  RequestQueue mQueue;
    private Button btnLogin;
    private EditText txtPassword, txtUserName;
    private TextView txtMessage;
    String userName = "admin";
    String userPasword = "admin";
       @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        jsonParse();
        jsonEmail();
        btnLogin = findViewById(R.id.btnLogin);
        txtMessage = findViewById(R.id.textView2);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                validate(txtUserName.getText().toString(),txtPassword.getText().toString());
            }
        });
    }

    public void jsonParse() {
       //String url = "http://marjanjewellers.co/bilal/api.php";
        String url = "http://99.252.197.175/bilal/api.php";
        final double[] data;
        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("employees");
                            double[] altitudeArr = new double[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject altitude = jsonArray.getJSONObject(i);
                                altitudeArr[i]= altitude.getDouble("Altitude");
                                HttpUtils.pressure.add(altitude.getDouble("Air Pressure"));
                                HttpUtils.temprature.add(altitude.getDouble("temp"));
                                HttpUtils.list.add(altitudeArr[i]);
                            }



                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

   mQueue.add(request);


    }
    public void jsonEmail() {
        mQueue = Volley.newRequestQueue(this);
        //String url = "http://marjanjewellers.co/bilal/api_get_emails.php";
       String url = "http://99.252.197.175/bilal/api_get_emails.php";
        final double[] data;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("emails");
                            double[] emailArry = new double[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject altitude = jsonArray.getJSONObject(i);
                                HttpUtils.emails.add(altitude.getString("email"));
                                HttpUtils.list.add(emailArry[i]);
                            }



                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);


    }
    private void validate(String Name, String Password) {
        if (Name.contentEquals((userName)) && (Password.contentEquals(userPasword))) {
            Intent i = new Intent(Login.this, MainActivity.class);
            Toast.makeText(this, "Successfully Login", Toast.LENGTH_LONG).show();
            startActivity(i);
        } else {
            String text = "Invalid Credentials.Try again please...! ";
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }
}
