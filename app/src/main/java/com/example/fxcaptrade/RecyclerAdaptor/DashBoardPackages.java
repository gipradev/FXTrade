package com.example.fxcaptrade.RecyclerAdaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fxcaptrade.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashBoardPackages extends RecyclerView.Adapter<DashBoardPackages.MyViewHolder> {

    private final Context context;
    private final JSONArray jsonArray;

    public DashBoardPackages(Context applicationContext, JSONArray array) {
        this.jsonArray = array;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.package_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            JSONObject object = jsonArray.getJSONObject(position);
        //    holder.iDate.setText(object.getString("invest_date"));
            holder.pDate.setText(object.getString("payout_date"));
         //   holder.btc.setText(object.getString("no_bitcoin")+" BTC");
            holder.pAmount.setText("$ "+object.getString("package_amount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pAmount, iDate, pDate,btc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pAmount = (TextView) itemView.findViewById(R.id.packageAmount);
         //   iDate = (TextView) itemView.findViewById(R.id.investedDate);
            pDate = (TextView) itemView.findViewById(R.id.payOutDate);
        //    btc = (TextView) itemView.findViewById(R.id.btc);

        }
    }

}
