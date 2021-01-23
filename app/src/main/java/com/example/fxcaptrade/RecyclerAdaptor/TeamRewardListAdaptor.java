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

public class TeamRewardListAdaptor extends RecyclerView.Adapter<TeamRewardListAdaptor.MyViewHolder> {

    private final Context context;
    private final JSONArray jsonArray;

    public TeamRewardListAdaptor(Context applicationContext, JSONArray array) {
        this.jsonArray = array;
        this.context = applicationContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.team_reward_item,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            JSONObject object = jsonArray.getJSONObject(position);

            holder.transNo.setText(object.getString("display_name"));


            holder.transDate.setText(object.getString("transaction_date"));


            holder.amount.setText("$ "+object.getString("transaction_amount"));

            String name = object.getString("display_name");
            //  Log.e(TAG,name);
            if (!name.isEmpty()){
                char ch1 = name.charAt(0);
                holder.textIcon.setText(String.valueOf(ch1));
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
        private final TextView transNo,transDate,amount,textIcon;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            transNo = (TextView) itemView.findViewById(R.id.transactionNo);
            transDate = (TextView) itemView.findViewById(R.id.transactionDate);
            amount = (TextView) itemView.findViewById(R.id.amount);
            textIcon = (TextView) itemView.findViewById(R.id.textIcon);
        }
    }
}
