package com.example.fxcaptrade.DashBoard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.Activities.AllWalletReport;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "WalletFragment";


    private SharedPreferences user;
    private String username;
    private RequestQueue requestQueue;
    final int flag = 0;
    private ProgressBar progressBalance,progressLive;
    private TextView walletInvestment,walletROI,walletIncome,walletSponsor,walletReward,walletTotal,totalBtc;
    private ExtendedFloatingActionButton viewReports;
    private CardView investmentCard,incomeCard,roiCard,sponsorCard,rewardCard;


    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        delarations(view);
        getSharedPreference();
        getWalletAmount();


        viewReports.setOnClickListener(this);
        investmentCard.setOnClickListener(this);
        incomeCard.setOnClickListener(this);
        roiCard.setOnClickListener(this);
        sponsorCard.setOnClickListener(this);
        rewardCard.setOnClickListener(this);





    }

    private void delarations(View view) {

        requestQueue = Volley.newRequestQueue(getActivity());
        progressBalance = (ProgressBar) view.findViewById(R.id.progressBalance);
        progressLive = (ProgressBar) view.findViewById(R.id.progressLive);

        viewReports = (ExtendedFloatingActionButton) view.findViewById(R.id.fabViewReports);

        walletInvestment = (TextView) view.findViewById(R.id.investmentWallet);
        walletROI = (TextView) view.findViewById(R.id.roiWallet);
        walletIncome = (TextView) view.findViewById(R.id.incomeWallet);
        walletReward = (TextView) view.findViewById(R.id.rewardWallet);
        walletSponsor = (TextView) view.findViewById(R.id.sponsorWallet);
        walletTotal = (TextView) view.findViewById(R.id.totalWallet);
        totalBtc = (TextView) view.findViewById(R.id.totalBTC);

        investmentCard = (CardView) view.findViewById(R.id.investmentCard);
        incomeCard = (CardView) view.findViewById(R.id.incomeCard);
        roiCard = (CardView) view.findViewById(R.id.roiCard);
        sponsorCard = (CardView) view.findViewById(R.id.sponsorCard);
        rewardCard = (CardView) view.findViewById(R.id.rewardCard);


    }



    private void getWalletAmount() {
        progressBalance.setVisibility(View.VISIBLE);
        progressLive.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Get_dahboard_wallet", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"Response"+response);
                try {

                    JSONObject jsonObject = new JSONObject(response.trim());
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        progressBalance.setVisibility(View.INVISIBLE);
                        progressLive.setVisibility(View.INVISIBLE);

                        JSONObject object = jsonObject.getJSONObject("data");

                        walletIncome.setText( "$ "+object.getString("income_wallet"));
                        walletROI.setText(  "$ "+object.getString("roi_wallet"));
                        walletInvestment.setText(  "$ "+object.getString("investment_wallet"));
                        walletReward .setText(  "$ "+object.getString("reward_wallet"));
                       // walletSponsor.setText("$ "+object.getString("reward_wallet"));
                        walletTotal .setText(  "$ "+object.getString("total_balance"));


                      //  getBTC("$ "+object.getString("total_balance"));


                    } else {

                        progressBalance.setVisibility(View.INVISIBLE);
                        progressLive.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError" + error);
                //  progressBalance.setVisibility(View.INVISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", username);

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getSharedPreference() {

        user = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        username = user.getString("user_id","0");

        Log.e(TAG,username);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fabViewReports  :

                redirect("0");
                break;
            case R.id.investmentCard:

                redirect("0");
                break;
            case R.id.incomeCard:

                redirect("1");
                break;
            case R.id.roiCard:

                redirect("2");
                break;
            case R.id.sponsorCard:

                redirect("3");
                break;
            case R.id.rewardCard:

                redirect("4");
                break;


        }
    }

    private void redirect(String index) {
        Intent intent = new Intent(getContext(), AllWalletReport.class);
        String tabIndex ;
        tabIndex  = index;
        intent.putExtra("index", tabIndex);
        getActivity().startActivity(intent);

    }


}
