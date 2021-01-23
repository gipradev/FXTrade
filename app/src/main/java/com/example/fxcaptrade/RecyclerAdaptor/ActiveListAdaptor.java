package com.example.fxcaptrade.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fxcaptrade.BottomSheet.ViewMoreInvestments;
import com.example.fxcaptrade.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActiveListAdaptor extends RecyclerView.Adapter<ActiveListAdaptor.MyViewHolder> {
    private static final String TAG = "ActiveListAdaptor";


    private final Context context;
    private final JSONArray jsonArray;
    private final OnActiveListListener listener;

    public ActiveListAdaptor(Context applicationContext, JSONArray array,
                             OnActiveListListener listListener) {
        this.jsonArray = array;
        this.context = applicationContext;
        this.listener = listListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.active_item,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Activity activity = (Activity) context;

        try {
            final JSONObject object = jsonArray.getJSONObject(position);

            holder.packageAmount.setText("$ "+object.getString("investment_amount"));
            
            holder.displayName.setText(object.getString("display_name") );
            holder.userName.setText(object.getString("username"));
            holder.investedDate.setText(object.getString("Joindate"));

            holder.moreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.e(TAG,object.getString("userid"));
                        listener.onMoreClick(object.getString("userid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

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
        private final TextView packageAmount,investedDate,displayName,userName,textIcon;
        private final Button moreView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            packageAmount = (TextView) itemView.findViewById(R.id.investedAmount);
            investedDate = (TextView) itemView.findViewById(R.id.joinDate);
            displayName = (TextView) itemView.findViewById(R.id.displayName);
            userName = (TextView) itemView.findViewById(R.id.userName);
            moreView = (Button) itemView.findViewById(R.id.viewMoreButton);
            textIcon = (TextView) itemView.findViewById(R.id.textIcon);

        }
    }


    public  interface OnActiveListListener{

        void onMoreClick(String user_id);

    }
}
