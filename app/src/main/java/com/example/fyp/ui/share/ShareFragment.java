package com.example.fyp.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp.HttpUtils;
import com.example.fyp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    // Array of strings...
    //List<String> mobileArray = HttpUtils.emails;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        ArrayAdapter adapter = new ArrayAdapter<String>(root.getContext(),
                android.R.layout.simple_list_item_1 , HttpUtils.emails);
        ListView listView = (ListView) root.findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        return root;
    }


}