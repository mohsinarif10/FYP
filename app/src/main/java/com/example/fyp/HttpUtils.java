package com.example.fyp;
import com.loopj.android.http.*;

import java.util.ArrayList;
import java.util.List;

public class HttpUtils {
    public static List<Double> list = new ArrayList<Double>();
    public static List<Double> pressure = new ArrayList<Double>();
    public static List<Double> temprature = new ArrayList<Double>();
    public static List<String> emails = new ArrayList<String>();
    private static final String BASE_URL = "http://192.168.100.210:89/";
   //private static final String BASE_URL = "http://localhost:58772/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
