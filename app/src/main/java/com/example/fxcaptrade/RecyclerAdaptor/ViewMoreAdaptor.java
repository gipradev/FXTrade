package com.example.fxcaptrade.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fxcaptrade.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewMoreAdaptor extends RecyclerView.Adapter<ViewMoreAdaptor.MyViewHolder> {


    private final Context context;
    private final JSONArray jsonArray;


    public ViewMoreAdaptor(Context applicationContext, JSONArray array) {
        this.jsonArray = array;
        this.context = applicationContext;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.view_more_item,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Activity activity = (Activity) context;

        try {
            final JSONObject object = jsonArray.getJSONObject(position);

            holder.packageAmount.setText("$ "+object.getString("invest_amount"));
            
            holder.payoutDate.setText("Payout Day "+object.getString("payout_date") );
            holder.returnAmount.setText("$ "+object.getString("payout_amount"));
            holder.investedDate.setText(object.getString("invest_date"));
            holder.percent.setText("% "+object.getString("return_percentage"));




        } catch (JSONException  e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView packageAmount,investedDate,returnAmount,percent,payoutDate;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            packageAmount = (TextView) itemView.findViewById(R.id.packageAmount);
            investedDate = (TextView) itemView.findViewById(R.id.investedDate);
            returnAmount = (TextView) itemView.findViewById(R.id.returnAmount);
            percent = (TextView) itemView.findViewById(R.id.percent);
            payoutDate = (TextView) itemView.findViewById(R.id.payoutDate);

        }
    }



}
