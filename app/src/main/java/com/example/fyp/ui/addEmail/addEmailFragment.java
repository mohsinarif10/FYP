package com.example.fyp.ui.addEmail;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp.HttpUtils;
import com.example.fyp.Login;
import com.example.fyp.R;
import com.example.fyp.ui.accountInfo.accountInfoFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class addEmailFragment extends Fragment {
    private AddEmailViewModel mViewModel;
    TextView txtName;
    TextView txtEmail;
    CheckBox chEverDay;
    CheckBox chMonthly;
    static final int INTERNET_REQ = 23;
    static final String REQ_TAG = "VACTIVITY";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(AddEmailViewModel.class);
        View root = inflater.inflate(R.layout.add_email_fragment, container, false);

        Button buttonEmail = root.findViewById(R.id.btnSaveEmail);
        txtName = root.findViewById(R.id.txtName);
        txtEmail = root.findViewById(R.id.txtEmail);
        chEverDay = root.findViewById(R.id.ch_EveryDayReport);
        chMonthly = root.findViewById(R.id.ch_MonthlyReport);
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = txtName.getText().toString();
                String Email = txtEmail.getText().toString();
                String everyday_report = "0";
                String monthly_report = "0";
                if (chMonthly.isChecked()) {
                    monthly_report = "1";
                }
                if (chEverDay.isChecked()) {
                    everyday_report = "1";
                }

                // PostData(Name, Email);
                getStringResponse(Name, Email, everyday_report, monthly_report);
                //jsonEmail1();
            }
        });
        return root;
    }

    public void getStringResponse(String Name,final String Email, String everyday_report, String monthly_report) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        //String url = "http://marjanjewellers.co/bilal/api_insert_emails.php?name=" + Name + "&email=" + Email + "&everyday_report=" + everyday_report + "&monthly_report=" + monthly_report + "";
        String url = "http://99.252.197.175/bilal/api_insert_emails.php?name=" + Name + "&email=" + Email + "&everyday_report=" + everyday_report + "&monthly_report=" + monthly_report + "";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HttpUtils.emails.add(Email);
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setTag(REQ_TAG);
        queue.add(stringRequest);
    }

    public void jsonEmail1() {
        HttpUtils.emails.clear();

//        RequestQueue bQueue = Volley.newRequestQueue(getContext());
//        String url = "http://marjanjewellers.co/bilal/api_get_emails.php";
//        final double[] data;
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("emails");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject altitude = jsonArray.getJSONObject(i);
//                                HttpUtils.emails.add(altitude.getString("email"));
//                            }
//
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        bQueue.add(request);


    }


    public void PostData(String EName, String EEmail) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://marjanjewellers.co/bilal/api_insert_emails.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                //tv_volley_result.setText(s);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(), "request was aborted" + volleyError, Toast.LENGTH_LONG).show();
                        //tv_volley_result.setText("request was aborted" + volleyError);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", "name");
                params.put("email", "mohsinarif@yahoo.com");
                params.put("everyday_report", "1");
                params.put("monthly_report", "1");
                return params;
            }
        };
        // 3 take post Request added to queue
        queue.add(stringRequest);
    }


}
