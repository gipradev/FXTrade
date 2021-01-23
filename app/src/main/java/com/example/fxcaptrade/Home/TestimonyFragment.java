package com.example.fxcaptrade.Home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.RecyclerAdaptor.TestimonyAdaptor;
import com.example.fxcaptrade.Utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TestimonyFragment extends Fragment {
    private static final String TAG = "TestimonyFragment";

    private ImageView noResult;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    Boolean isScrolling = false, isLastPage = false;
    int currentItems, totalItems, scrolledOutItems, itemCount = 0;
    private JSONArray array = new JSONArray();
    private RequestQueue requestQueue;
    private TestimonyAdaptor testimonyList;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_testimony, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        declaration(view);
        getTestimonies();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                //  Log.e(TAG,isLastPage+"");

                if (isScrolling && (currentItems + scrolledOutItems == totalItems) && !isLastPage) {
                     loadMore();

                }
            }
        });

    }
    //**************************************************************************************************************

    //**************************************************************************************************************
    private void loadMore() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Testimony_feeds", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        JSONArray array = object.getJSONArray("data");
                        itemCount = Integer.parseInt(object.getString("list_count"));
                        generateDataNewData(array);


                    } else {

                        isLastPage = true;
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("list_count", itemCount + "");
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void generateDataNewData(final JSONArray dataArray) {
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < dataArray.length(); i++) {
                    try {
                        array.put(dataArray.getJSONObject(i));
                        testimonyList.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, 3000);
    }
    //**************************************************************************************************************

    //**************************************************************************************************************

    private void getTestimonies() {
        progressBar.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Testimony_feeds", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        array = object.getJSONArray("data");
                        itemCount = Integer.parseInt(object.getString("list_count"));
                        generateData(array);


                    } else {

                        progressBar.setVisibility(View.GONE);
                        noResult.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("list_count", itemCount+"");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void generateData(JSONArray array) {

        testimonyList = new TestimonyAdaptor(getContext(), array);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(testimonyList);

    }
    //**************************************************************************************************************

    //**************************************************************************************************************
    private void declaration(View view) {
        requestQueue = Volley.newRequestQueue(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.testimonyRecycler);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noResult = (ImageView) view.findViewById(R.id.noResult);
    }
}
