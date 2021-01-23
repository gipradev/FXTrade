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

public class TeamReturnListAdaptor extends RecyclerView.Adapter<TeamReturnListAdaptor.MyViewHolder> {

    private final Context context;
    private final JSONArray jsonArray;

    public TeamReturnListAdaptor(Context applicationContext, JSONArray array) {
        this.jsonArray = array;
        this.context = applicationContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.team_return_item,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            JSONObject object = jsonArray.getJSONObject(position);

            holder.displayName.setText(object.getString("display_name"));

            holder.investedAmount.setText("$ "+object.getString("invest_amount")+".00");

            String orderDate =  object.getString("return_date");
            String dateSplit [] = orderDate.split(" ");

//            SimpleDateFormat formatter=new SimpleDateFormat("E, MMM dd yyyy");
//            Date date = formatter.parse(orderDate);


            holder.r_date.setText(orderDate);
            holder.returnPer.setText("% "+object.getString("return_percentage")+".00");
            holder.returnAmount.setText("$ "+object.getString("return_amount"));

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
        private final TextView displayName,investedAmount,r_date,returnPer,returnAmount,textIcon;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            displayName = (TextView) itemView.findViewById(R.id.displayName);
            investedAmount = (TextView) itemView.findViewById(R.id.investAmount);
            returnAmount = (TextView) itemView.findViewById(R.id.returnAmount);
            returnPer = (TextView) itemView.findViewById(R.id.returnPercent);
            r_date = (TextView) itemView.findViewById(R.id.returnDate);
            textIcon = (TextView) itemView.findViewById(R.id.textIcon);
        }
    }
}
