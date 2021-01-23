package com.example.fxcaptrade.WalletReports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fxcaptrade.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoiWalletListAdaptor extends RecyclerView.Adapter<RoiWalletListAdaptor.MyViewHolder> {


    private final Context context;
    private final JSONArray jsonArray;

    public RoiWalletListAdaptor(Context applicationContext, JSONArray array) {
        this.jsonArray = array;
        this.context = applicationContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.wallet_report_item,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {

            JSONObject object = jsonArray.getJSONObject(position);

            holder.transNo.setText("$ "+object.getString("invest_amount"));



            holder.transDate.setText(object.getString("invested_date")+" ↗");

//            if(object.getString("transaction_status").equalsIgnoreCase("Green")){
//
//                holder.amount.setTextColor(Color.parseColor("#66BB6A"));
//
//            }else if(object.getString("transaction_status").equalsIgnoreCase("Red")){
//
//                holder.amount.setTextColor(Color.parseColor("#EC407A"));
//
//            }
            if (!object.getString("return_amount").equals(null)){
                holder.returnDate.setVisibility(View.VISIBLE);
                holder.amount.setText("$ "+object.getString("return_amount"));
                holder.returnDate.setText(object.getString("return_date")+" ↙");
            }





        } catch (JSONException  e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView transNo,transDate,amount,returnDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            transNo = (TextView) itemView.findViewById(R.id.transactionNo);
            transDate = (TextView) itemView.findViewById(R.id.transactionDate);
            amount = (TextView) itemView.findViewById(R.id.amount);
            returnDate = (TextView) itemView.findViewById(R.id.returnDate);

        }
    }
}
