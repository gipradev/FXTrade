package com.example.fxcaptrade.RecyclerAdaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fxcaptrade.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestimonyAdaptor extends RecyclerView.Adapter<TestimonyAdaptor.MyViewHolder> {

    private final Context context;
    private final JSONArray jsonArray;

    public TestimonyAdaptor(Context applicationContext, JSONArray array) {
        this.jsonArray = array;
        this.context = applicationContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.testimony_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {

            JSONObject object = jsonArray.getJSONObject(position);

            String tab = context.getResources().getString(R.string.tab);

            holder.date.setText(object.getString("d_date"));
            holder.data.setText(tab+tab+tab+tab+object.getString("c_testimony"));
            holder.displayName.setText(object.getString("c_display_name"));
            holder.location.setText(object.getString("countryname"));
            String imageString = object.getString("c_profile");

            setImage(imageString,holder.profilePic);

        } catch (JSONException  e) {
            e.printStackTrace();
        }

    }

    private void setImage(String imageString, ImageView profilePic) {
        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        profilePic.setImageBitmap(decodedByte);

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView displayName, date, time, data , location ;
        ImageView profilePic ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = (ImageView)itemView.findViewById(R.id.personImage);
            displayName = (TextView) itemView.findViewById(R.id.personName);
            location = (TextView) itemView.findViewById(R.id.location);
            data = (TextView) itemView.findViewById(R.id.testimonyData);
            date = (TextView) itemView.findViewById(R.id.dateTestimony);
        }
    }

}
