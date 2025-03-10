package com.example.fxcaptrade.DashBoard;

import android.content.SharedPreferences;
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
import com.example.fxcaptrade.RecyclerAdaptor.InactiveListAdaptor;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.WebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InactiveFragment extends Fragment {
    private static final String TAG = "InactiveFragment";

    private View activityView;
    private RequestQueue requestQueue;
    private RecyclerView listRecycler;
    private ProgressBar progressBar;
    private SharedPreferences user;
    private String userID;
    private LinearLayoutManager linearLayoutManager;
    private InactiveListAdaptor inactivityList;
    private ImageView noData;
    Boolean isScrolling = false , isLastPage =  false;
    int currentItems, totalItems, scrolledOutItems;
    private int itemCount = 0;
    private JSONArray array = new JSONArray();


    public InactiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inactive, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        declarations(view);
        getSharedPreference();
        getInactiveList(userID);



//        Boolean isScrolling = false , isLastPage =  false;
//        int currentItems,totalItems,scrolledOutItems;

        listRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                if (isScrolling && (currentItems + scrolledOutItems == totalItems) && !isLastPage) {

                    LoadMore(userID);

                }
            }
        });


    }

    private void getInactiveList(final String userID) {
        progressBar.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Inactive_referal_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                       progressBar.setVisibility(View.INVISIBLE);
                        listRecycler.setVisibility(View.VISIBLE);

                        array = object.getJSONArray("data");
                        itemCount = Integer.parseInt(object.getString("list_count"));

                        generateData(array);


                    }
                    //else if (status.equals("2")) {
//                       progressBar.setVisibility(View.INVISIBLE);
//                       isLastPage = true;
//
//                       Toast.makeText(getActivity(), "No more Data found...", Toast.LENGTH_SHORT).show();
//                    }
                    else {

                       progressBar.setVisibility(View.INVISIBLE);
                       noData.setVisibility(View.VISIBLE);
                        //   Toast.makeText(getActivity(), "No data Found", Toast.LENGTH_SHORT).show();
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
                map.put("user_id", userID);
                map.put("list_count", String.valueOf(itemCount));
                return map;

            }
        };
        requestQueue.add(stringRequest);
    }


    private void LoadMore(final String userID) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Inactive_referal_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {
                        listRecycler.setVisibility(View.VISIBLE);
                        JSONArray dataArray = object.getJSONArray("data");
                        itemCount = Integer.parseInt(object.getString("list_count"));
                        generateDataNewData(dataArray);

                    } else if (status.equals("2")) {

                      //  Toast.makeText(getActivity(), "No more Data found...", Toast.LENGTH_SHORT).show();
                    }
                    else {

//                        noData.setVisibility(View.VISIBLE);
//                        Toast.makeText(getActivity(), "No data Found", Toast.LENGTH_SHORT).show();
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
                map.put("user_id", userID);
                map.put("list_count", String.valueOf(itemCount));
                return map;

            }
        };
        requestQueue.add(stringRequest);
    }

    private void generateDataNewData(final JSONArray jsonArray) {
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                for(int i = 0 ; i < jsonArray.length() ; i++){
                    try {
                        array.put(jsonArray.getJSONObject(i));
                        inactivityList.notifyDataSetChanged();
                       progressBar.setVisibility(View.INVISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        },3000);

    }


    private void generateData(JSONArray array) {

        inactivityList = new InactiveListAdaptor(getContext(), array);
        inactivityList.notifyDataSetChanged();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listRecycler.setHasFixedSize(true);
        listRecycler.setLayoutManager(linearLayoutManager);
        listRecycler.setAdapter(inactivityList);


    }

    private void declarations(View view) {
        activityView = view;
        requestQueue = Volley.newRequestQueue(getContext());
        listRecycler = (RecyclerView) view.findViewById(R.id.inactiveMembers);
        progressBar = (ProgressBar) view.findViewById(R.id.progressInactive);
       
        noData = (ImageView) view.findViewById(R.id.noResult);


    }

    private void getSharedPreference() {

        user = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        userID = user.getString("user_id", "0");
        Log.e(TAG, userID);


    }
}
