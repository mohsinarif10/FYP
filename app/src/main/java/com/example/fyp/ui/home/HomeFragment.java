package com.example.fyp.ui.home;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp.HttpUtils;
import com.example.fyp.JavaMailAPI;
import com.example.fyp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    View root;
    GraphView graph2,graph1,graph3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
//        final GraphView  graph = (GraphView) root.findViewById(R.id.graph);
        Button buttonEmergency = root.findViewById(R.id.btn_Emergency);
        Button buttonTodayReport = root.findViewById(R.id.btn_TodayReport);
        Button buttonRefresh = root.findViewById(R.id.btn_Refresh);
         graph2 = (GraphView) root.findViewById(R.id.graph);
         graph1 = (GraphView) root.findViewById(R.id.graph1);
         graph3 = (GraphView) root.findViewById(R.id.graph3);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jsonParse1();
            }
        });


        buttonEmergency.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String message = "Your beloved one is in critical situation & need assistance on emergency basis, his/her last vital information are as follows Pressure:-  " + HttpUtils.pressure.get(HttpUtils.pressure.size() - 1).toString() + " Altitude:-" + HttpUtils.list.get(HttpUtils.list.size() - 1).toString() + " Temprature:-" + HttpUtils.temprature.get(HttpUtils.temprature.size() - 1).toString();
                //String message="message";
                String subject = "App Alert";
                sendMail(message, subject);
            }
        });
        buttonTodayReport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String message = "<table>\n" +
                        "  <thead>\n" +
                        "   <tr>\n" +
                        "    <th>Altitude</th>\n" +
                        "    <th>Pressure</th>\n" +
                        "    <th>Temprature</th>\n" +
                        "  </tr>\n" +
                        "  </thead>\n" +
                        "  <tbody>";
                for (int i = 0; i <= HttpUtils.temprature.size() - 1; i++) {
                    message += "<tr>\n" +
                            "<td>" + HttpUtils.list.get(i) + "</td>\n" +
                            "<td>" + HttpUtils.pressure.get(i) + "</td>\n" +
                            "<td>" + HttpUtils.temprature.get(i) + "</td>\n" +
                            "</tr>";
                }
                message += "</tbody>\n" +
                        "</table>";
                String subject = "Today Report";
                sendMail(message, subject);
            }
        });
        fillGraph(root);

        return root;

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendMail(String message, String subject) {
        String mail = "";
        for (int i = 0; i <= HttpUtils.emails.size(); i++) {
            mail = String.join(",", HttpUtils.emails);
        }
        JavaMailAPI javaMailAPI = new JavaMailAPI(getContext(), mail, subject, message);
        javaMailAPI.execute();

    }

    public void fillGraph(View root) {
        DataPoint[] graphdata1 = new DataPoint[HttpUtils.pressure.size()];
        for (int j = 0; j < graphdata1.length; j++) {
            graphdata1[j] = new DataPoint(j, (int) Math.round(HttpUtils.pressure.get(j)));
        }



        PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries(graphdata1);
        graph2.getGridLabelRenderer().setHumanRounding(false);
        graph2.addSeries(series2);


        DataPoint[] graphdata = new DataPoint[HttpUtils.list.size()];
        for (int j = 0; j < graphdata.length; j++) {
            graphdata[j] = new DataPoint(j, (int) Math.round(HttpUtils.list.get(j)));
        }



        BarGraphSeries<DataPoint> series1 = new BarGraphSeries<DataPoint>(graphdata);
        graph1.getGridLabelRenderer().setHumanRounding(false);
        graph1.addSeries(series1);


        DataPoint[] graphdata3 = new DataPoint[HttpUtils.temprature.size()];
        for (int j = 0; j < graphdata3.length; j++) {
            graphdata3[j] = new DataPoint(j, (int) Math.round(HttpUtils.temprature.get(j)));
        }

        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<DataPoint>(graphdata3);
        graph3.getGridLabelRenderer().setHumanRounding(false);
        graph3.addSeries(series3);


    }

    public void jsonParse1() {
        graph1.removeAllSeries();
        graph2.removeAllSeries();
        graph3.removeAllSeries();
        HttpUtils.pressure.clear();
        HttpUtils.list.clear();
        HttpUtils.temprature.clear();

        //String url = "http://marjanjewellers.co/bilal/api.php";
        String url = "http://99.252.197.175/bilal/api.php";
        final double[] data;
        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("employees");
                            double[] altitudeArr = new double[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject altitude = jsonArray.getJSONObject(i);
                                altitudeArr[i] = altitude.getDouble("Altitude");
                                HttpUtils.pressure.add(altitude.getDouble("Air Pressure"));
                                HttpUtils.temprature.add(altitude.getDouble("temp"));
                                HttpUtils.list.add(altitudeArr[i]);
                            }
                            fillGraph(root);



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

}