package com.example.fxcaptrade.DashBoard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTeamFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MyTeamFragment";

    private ExtendedFloatingActionButton fabShare;
    private FloatingActionButton fabTeam, fabReward, fabTeamReturns, fabRefInvest;
    private TextView totalTeam,totalRefInvest,totalTeamReturns,totalTeamRewards,shareLink;
    private SharedPreferences user;
    private String userID;
    private RequestQueue requestQueue;
    private ProgressBar progress;

    public MyTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_team, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        declarations(view);
        getSharedPreference();
        getSummaryData();

        fabShare.setOnClickListener(this);
        fabTeam.setOnClickListener(this);
        fabRefInvest.setOnClickListener(this);
        fabTeamReturns.setOnClickListener(this);
        fabReward.setOnClickListener(this);
    }

    private void getSharedPreference() {

        user = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        userID = user.getString("user_id","0");

        Log.e(TAG,userID);


    }

    private void getSummaryData() {
        progress.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Myteam_summary", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"Response"+response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        progress.setVisibility(View.GONE);

                        shareLink.setText(object.getString("share_link"));
                        totalTeam.setText(object.getString("active_ref_count")+" / "+object.getString("inactive_ref_count"));
                        totalRefInvest.setText("$ "+object.getString("total_referal_investment"));
                        totalTeamReturns.setText("$ "+object.getString("total_roi"));
                        totalTeamRewards.setText("$ "+object.getString("total_rewards"));


                    }else{
                        progress.setVisibility(View.GONE);
                      //  Toast.makeText(getActivity(), "No data Found", Toast.LENGTH_SHORT).show();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", userID);
                return map;

            }
        };
        requestQueue.add(stringRequest);

    }

    private void declarations(View view) {


        requestQueue = Volley.newRequestQueue(getContext());
        progress = (ProgressBar) view.findViewById(R.id.progressMyTeam);

        shareLink = (TextView) view.findViewById(R.id.sharealink);
        totalTeam = (TextView) view.findViewById(R.id.totalTeam);
        totalRefInvest = (TextView) view.findViewById(R.id.totalRefInvest);
        totalTeamReturns = (TextView) view.findViewById(R.id.totalTeamReturns);
        totalTeamRewards = (TextView) view.findViewById(R.id.totalTeamRewards);
        
        fabShare = (ExtendedFloatingActionButton) view.findViewById(R.id.fabShare);
        fabRefInvest = (FloatingActionButton) view.findViewById(R.id.fabRefInvest);
        fabReward = (FloatingActionButton) view.findViewById(R.id.fabReward);
        fabTeamReturns = (FloatingActionButton) view.findViewById(R.id.fabTeamReturns);
        fabTeam = (FloatingActionButton) view.findViewById(R.id.fabTeam);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fabShare:
                shareLink();
                break;
            case R.id.fabTeam:

                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.containerFragment, new ActiveInactiveFragment()).addToBackStack(null).commit();
                break;

            case R.id.fabRefInvest:

                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.containerFragment, new ReferralInvestmentFragment()).addToBackStack(null).commit();
                break;

            case R.id.fabTeamReturns:

                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.containerFragment, new TeamReturnsFragment()).addToBackStack(null).commit();
                break;

            case R.id.fabReward:

                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.containerFragment, new TeamRewardsFragment()).addToBackStack(null).commit();
                break;

        }

    }

    private void shareLink() {
        try {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = shareLink.getText().toString();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "FX Factory");
//            String shareMessage =   shareLink.getText().toString() + BuildConfig.APPLICATION_ID + "\n\n";
//            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }
}
