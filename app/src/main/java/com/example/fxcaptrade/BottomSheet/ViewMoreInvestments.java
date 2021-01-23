package com.example.fxcaptrade.BottomSheet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.RecyclerAdaptor.ViewMoreAdaptor;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewMoreInvestments extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = "EditDisplayName";


    private final Context context;
    private final String activeUserId;


    private Toolbar toolBar;
    private RecyclerView listRecycler;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;
    private ViewMoreAdaptor activityList;
    private LinearLayoutManager linearLayoutManager;

    public ViewMoreInvestments(Context applicationContext, String active_user_id) {


        this.context = applicationContext;
        this.activeUserId = active_user_id;



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_more_investmets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        declarations(view);
        getDetils(activeUserId);

        toolBar.setTitle("Investment History");
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setNavigationIcon(R.drawable.ic_back);
        toolBar.setNavigationOnClickListener(this);


    }



    private void declarations(View view) {

        listRecycler = (RecyclerView) view.findViewById(R.id.recyclerList);
        toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        progressBar = (ProgressBar) view.findViewById(R.id.progressActiveList);
        toolBar = (Toolbar) view.findViewById(R.id.toolbarViewMore);

    }

    private void getDetils(final String activeUserId) {

        progressBar.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Active_referal_details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        progressBar.setVisibility(View.GONE);

                        listRecycler.setVisibility(View.VISIBLE);
                        JSONArray array = object.getJSONArray("data");
                        generateData(array);


                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No data Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Try after some times..", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "VolleyError" + error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("activeuser_id", activeUserId);
                return map;

            }
        };
        requestQueue.add(stringRequest);
    }

    private void generateData(JSONArray array) {
        activityList = new ViewMoreAdaptor(getContext(), array);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRecycler.setHasFixedSize(true);
        listRecycler.setLayoutManager(linearLayoutManager);
        listRecycler.setAdapter(activityList);
    }


    @Override
    public void onClick(View v) {

        ViewMoreInvestments.this.dismiss();
    }
}

