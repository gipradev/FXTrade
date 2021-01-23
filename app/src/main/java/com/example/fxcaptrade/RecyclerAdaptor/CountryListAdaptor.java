package com.example.fxcaptrade.RecyclerAdaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fxcaptrade.Activities.Registration;
import com.example.fxcaptrade.Home.RegistrationFragment;
import com.example.fxcaptrade.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CountryListAdaptor extends RecyclerView.Adapter<CountryListAdaptor.MyViewHolder> {


    private static final String TAG = "CountryListAdaptor";
    private final Context context;
    private final JSONArray jsonArray;

    private final CountryAdaptorListener listener;

    public CountryListAdaptor(Context applicationContext, JSONArray array,
                              CountryAdaptorListener adaptorListener) {
        this.jsonArray = array;
        this.context = applicationContext;
        this.listener = adaptorListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.country_item,null);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        try {


            final JSONObject object = jsonArray.getJSONObject(position);

            holder.countryName.setText(object.getString("country_name"));

            holder.countryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.e(TAG,"clicked"+jsonArray.getJSONObject(position).getString("country_name"));

                        listener.onCountryClick(jsonArray.getJSONObject(position).getString("country_name"),
                                jsonArray.getJSONObject(position).getString("country_code"));

                        
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });



        } catch (JSONException  e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView countryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            countryName = (TextView) itemView.findViewById(R.id.countryName);

        }
    }

    public interface CountryAdaptorListener {
        void onCountryClick(String name, String id);
    }
}
