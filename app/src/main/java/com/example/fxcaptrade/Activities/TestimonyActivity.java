package com.example.fxcaptrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestimonyActivity extends AppCompatActivity {
    private static final String TAG = "TestimonyActivity";

    private CircleImageView imageView;
    private TextInputEditText inputText;
    private ExtendedFloatingActionButton saveFab;
    private SharedPreferences user;
    private String userID;
    private View progressBar;
    private RequestQueue requestQueue;
    private TextView displayName;
    private ImageButton navButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimony);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(33, 24, 81));
        }

        declarations();
        user = getApplicationContext().getSharedPreferences("User", getApplicationContext().MODE_PRIVATE);
        userID = user.getString("user_id", "0");
        Log.e(TAG, userID);
        init();


        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTextLength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void checkTextLength(String toString) {
        if (toString.length() < 15) {
            inputText.setError("Testimony need minimum 15 letters !");
        }
    }

    private void init() {
        getUserDetails(userID);
    }

    private void getUserDetails(final String userID) {
        progressBar.setVisibility(View.VISIBLE);
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

                        progressBar.setVisibility(View.INVISIBLE);
                        displayName.setText(object.getString("display_name"));

                        setImage(object.getString("image_url"), imageView);


                    } else {
                        // progress.setVisibility(View.GONE);
                        progressBar.setVisibility(View.INVISIBLE);

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", userID);
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

    private void declarations() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.progressTestimony);
        imageView = (CircleImageView) findViewById(R.id.imageView);
        displayName = (TextView) findViewById(R.id.displayName);
        inputText = (TextInputEditText) findViewById(R.id.testimonyText);
        saveFab = (ExtendedFloatingActionButton) findViewById(R.id.fabSave);
        navButton = (ImageButton) findViewById(R.id.navButton);
    }

    public void saveTestimony(View view) {
        if (inputText.getText().toString().equals("")) {
            inputText.setError("Testimony is empty !");
            inputText.requestFocus();
        } else if (inputText.getText().toString().length() < 15) {
            inputText.setError("Testimony need minimum 15 letters !");
        } else {
            uploadTestimony(inputText.getText().toString(),userID);
        }
    }

    private void uploadTestimony(final String data, final String userID) {
        progressBar.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Upload_testimony", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(TestimonyActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        inputText.setText("");
                        inputText.setError(null);

                    } else {
                        // progress.setVisibility(View.GONE);
                        progressBar.setVisibility(View.INVISIBLE);

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", userID);
                map.put("testimony",data);
                return map;

            }
        };
        requestQueue.add(stringRequest);
    }
}