package com.example.fxcaptrade.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

public class InvestmentListAdaptor extends RecyclerView.Adapter<InvestmentListAdaptor.MyViewHolder> {


    private final Context context;
    private final JSONArray jsonArray;

    public InvestmentListAdaptor(Context applicationContext, JSONArray array) {
        this.jsonArray = array;
        this.context = applicationContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.investment_item,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {

            JSONObject object = jsonArray.getJSONObject(position);

            holder.packageAmount.setText("$ "+object.getString("package_amount")+".00");

            String orderDate =  object.getString("invested_date");
            String dateSplit [] = orderDate.split(" ");

            holder.investedDate.setText(orderDate+" ↗");
         //   holder.hashCode.setText(object.getString("hashcode") );
            holder.paymentAmount.setText(object.getString("no_bitcoin")+" BTC");
            holder.transactionStatus.setText(object.getString("order_status"));
            if(object.getString("order_status").equalsIgnoreCase("confirmed")){

                holder.confirmDate.setVisibility(View.VISIBLE);
                holder.payOutDate.setVisibility(View.VISIBLE);
                holder.paymentAmount.setVisibility(View.VISIBLE);

                holder.confirmDate.setText(object.getString("activation_date"));
                holder.payOutDate.setText("Payout Day "+object.getString("payout_release_day")+" ↙");
                holder.paymentAmount.setText("$ "+object.getString("maxreturn_amount"));

                holder.transactionStatus.setBackgroundColor(Color.parseColor("#66BB6A"));


            }else if (object.getString("order_status").equalsIgnoreCase("pending")){

                holder.transactionStatus.setBackgroundColor(Color.parseColor("#211851"));

            }else if(object.getString("order_status").equalsIgnoreCase("rejected")){

                holder.transactionStatus.setBackgroundColor(Color.parseColor("#EC407A"));

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

        private final TextView packageAmount,investedDate,payOutDate,paymentAmount,confirmDate;
        Button transactionStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            packageAmount = (TextView) itemView.findViewById(R.id.packageAmount);
            investedDate = (TextView) itemView.findViewById(R.id.investedDate);
            payOutDate = (TextView) itemView.findViewById(R.id.payOutDate);
            paymentAmount = (TextView) itemView.findViewById(R.id.paymentAmount);
            transactionStatus = (Button) itemView.findViewById(R.id.transStatus);
            confirmDate = (TextView) itemView.findViewById(R.id.confirmDate);
        }
    }
}
