package com.example.fxcaptrade.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EditProfile";

    private static final int PICK_IMAGE_REQUEST = 100;
    private String base64String;
    private TextView edit;
    private TextInputEditText displayName;
    private ExtendedFloatingActionButton update;
    private SharedPreferences user;
    private String user_id,d_name,imageString;
    private RequestQueue requestQueue;
    private ProgressDialog dialog;
    private CircleImageView imageView;
    private ProgressBar progressProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbarDashBord);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setTitle("Edit Profile");

        toolbar.setNavigationOnClickListener(this);

        user = getApplicationContext().getSharedPreferences("User", getApplicationContext().MODE_PRIVATE);
        user_id = user.getString("user_id","0");
//        d_name = user.getString("display_name","0");

        Log.e(TAG,user_id);

        requestQueue = Volley.newRequestQueue(getApplicationContext());





        edit = (TextView) findViewById(R.id.editProfile);
        displayName = (TextInputEditText) findViewById(R.id.displayName);
        update = (ExtendedFloatingActionButton) findViewById(R.id.fabUpdate);
        imageView = (CircleImageView) findViewById(R.id.proPic);
        progressProfile = (ProgressBar) findViewById(R.id.progressProfile);

        edit.setOnClickListener(this);
        update.setOnClickListener(this);

        getProfileView();


    }


    //*****************************************************************************************************************************
    private void filePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    //*****************************************************************************************************************************

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri

                InputStream is = getApplicationContext().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();


                imageView.setImageBitmap(bitmap);
                // get File path
                //converting to base 64;
                base64String = convert(bitmap);




            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getProfileView() {
        progressProfile.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "View_profile", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        progressProfile.setVisibility(View.GONE);
                        displayName.setText(object.getString("display_name"));

                        setImage(object.getString("image_url"),imageView);




                    } else {
                        progressProfile.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressProfile.setVisibility(View.GONE);
                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", user_id);
                return map;

            }
        };
        requestQueue.add(stringRequest);

    }



    public String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.editProfile:
                filePicker();
                break;
            case R.id.fabUpdate:
                validate();
                break;
            case -1:
                onBackPressed();
                break;
        }
    }

    private void validate() {


        boolean stat = false;
        if (base64String.equals("")){
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            stat = true;
        }
        if (!stat){
            updateProfile(displayName.getText().toString(), base64String,user_id);
        }
    }

    private void updateProfile(final String display, final String base64String, final String user_id) {
        progressProfile.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Personal_details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        progressProfile.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        //  finish();



                        onBackPressed();



                    } else {
                         progressProfile.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "Faild", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError" + error);
                progressProfile.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Try after sometime....", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", user_id);
                map.put("profile", base64String);
                map.put("display_name", display);
                return map;

            }
        };
        requestQueue.add(stringRequest);

    }

    private void setImage(String image_url, CircleImageView image) {
        byte[] decodedString = Base64.decode(image_url, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(decodedByte);

    }


}
