package com.example.fxcaptrade.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class InvestmentReturnAdaptor extends RecyclerView.Adapter<InvestmentReturnAdaptor.MyViewHolder> {


    private final Context context;
    private final JSONArray jsonArray;

    public InvestmentReturnAdaptor(Context applicationContext, JSONArray array) {
        this.jsonArray = array;
        this.context = applicationContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.return_details,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            JSONObject object = jsonArray.getJSONObject(position);

            holder.returnAmount.setText("$ "+object.getString("return_amount"));
            holder.returnDate.setText("Return Date : "+object.getString("return_date"));//+" ↙"
            holder.investedAmount.setText("Package  : $ "+object.getString("package_name")+".00");

//            String orderDate =  object.getString("Joindate");
//            String dateSplit [] = orderDate.split(" ");
//
////            SimpleDateFormat formatter=new SimpleDateFormat("E, MMM dd yyyy");
////            Date date = formatter.parse(orderDate);
//
//
//            holder.investedDate.setText(orderDate);
//
//            holder.investedDate.setText(object.getString("Joindate"));
//
//            holder.mobile.setText(object.getString("mobile"));





        } catch (JSONException  e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView returnAmount,investedAmount,returnDate;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            returnAmount = (TextView) itemView.findViewById(R.id.returnAmount);
            investedAmount = (TextView) itemView.findViewById(R.id.investedAmount);
            returnDate = (TextView) itemView.findViewById(R.id.returnDate);


        }
    }
}
