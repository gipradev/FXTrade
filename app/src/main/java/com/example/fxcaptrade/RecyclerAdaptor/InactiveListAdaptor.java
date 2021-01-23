package com.example.fxcaptrade.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

import com.example.fxcaptrade.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InactiveListAdaptor extends RecyclerView.Adapter<InactiveListAdaptor.MyViewHolder> {
    private static final String TAG = "InactiveListAdaptor";


    private final Context context;
    private final JSONArray jsonArray;

    public InactiveListAdaptor(Context applicationContext, JSONArray array) {
        this.jsonArray = array;
        this.context = applicationContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.inactive_item,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {


            JSONObject object = jsonArray.getJSONObject(position);

            holder.installedDate.setText(object.getString("install_date"));
            holder.displayName.setText(object.getString("display_name") );
            holder.country.setText(object.getString("country_name"));

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
        private final TextView installedDate,displayName,textIcon;
        private final Button country;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            installedDate = (TextView) itemView.findViewById(R.id.installedDate);
            displayName = (TextView) itemView.findViewById(R.id.displayName);
            country = (Button) itemView.findViewById(R.id.country);
            textIcon = (TextView) itemView.findViewById(R.id.textIcon);


        }
    }
}
