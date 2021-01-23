package com.example.fxcaptrade.DashBoard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.example.fxcaptrade.BottomSheet.EditDisplayName;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.Utils.WebServices;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener, EditDisplayName.OnClickListener {
    private static final String TAG = "ProfileFragment";
    private static final int PICK_IMAGE_REQUEST = 100;

    private RequestQueue requestQueue;
    private SharedPreferences user;
    private String userID;
    private TextView displayName,username,mob,email,name,country,currentPlan,totalBalance,walletAddressText;

    private ImageButton editProfile;
    private CircleImageView image;
    private Button addwalletAddress;
    private TextInputEditText walletAddress;
    private LinearLayout showAddress,addAddressView;
    private String imageString;
    private ProgressBar progressProfile;
    private View fragmentView;
    private ImageButton cameraButton;
    private String base64String = null;
    private NestedScrollView profileData;


    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        declarations(view);
        getSharedPreference();
        getProfileView();



        editProfile.setOnClickListener(this);
        addwalletAddress.setOnClickListener(this);
        cameraButton.setOnClickListener(this);

    }

    private void addAddress(final String walletAddress, final String userID) {

        progressProfile.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Insert_wallet_address", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response 0"+ response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {
                       progressProfile.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();

                        addAddressView.setVisibility(View.GONE);
                        showAddress.setVisibility(View.VISIBLE);
                        walletAddressText.setText(walletAddress);

                    } else {
                        // progress.setVisibility(View.GONE);
                       progressProfile.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
                map.put("wallet_address",walletAddress);
                return map;

            }
        };
        requestQueue.add(stringRequest);



    }

    private void declarations(View view) {
        requestQueue = Volley.newRequestQueue(getContext());


        this.fragmentView =view;
        totalBalance = (TextView) view.findViewById(R.id.totalBalance);
        currentPlan = (TextView) view.findViewById(R.id.currentPackage);
        displayName = (TextView) view.findViewById(R.id.displayName);
        username = (TextView) view.findViewById(R.id.userName);
        mob = (TextView) view.findViewById(R.id.mobileNo);
        email = (TextView) view.findViewById(R.id.emailID);
        name = (TextView) view.findViewById(R.id.name);
        country = (TextView) view.findViewById(R.id.country);
        editProfile = (ImageButton) view.findViewById(R.id.editProfile);
        walletAddressText = (TextView) view.findViewById(R.id.walletAddress);

        addwalletAddress = (Button) view.findViewById(R.id.addBitButton);
        walletAddress = (TextInputEditText) view.findViewById(R.id.senderAddress);
        image = (CircleImageView) view.findViewById(R.id.proPic);
        cameraButton = (ImageButton)view.findViewById(R.id.cameraButton);


        progressProfile = (ProgressBar) view.findViewById(R.id.progressProfile);
        addAddressView = (LinearLayout) view.findViewById(R.id.addAddress);
        showAddress = (LinearLayout) view.findViewById(R.id.showWalletAddress);

    }

    private void getSharedPreference() {

        user = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        userID = user.getString("user_id", "0");
        Log.e(TAG, userID);

    }

    private void getProfileView() {
        progressProfile.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
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

                       progressProfile.setVisibility(View.INVISIBLE);
                        username.setText(object.getString("username"));
                        displayName.setText(object.getString("display_name"));
                        mob.setText(object.getString("mobile"));
                        totalBalance.setText("$ "+object.getString("Total_balance"));
                        currentPlan.setText("$ "+object.getString("current_plan"));

//                        if (!object.getString("email").equalsIgnoreCase(null)){
//                            email.setText(object.getString("email"));
//                        }

                        if (!object.getString("email").equalsIgnoreCase(null)){
                            email.setText(object.getString("email"));
                        }

                        imageString = object.getString("image_url");

                        setImage(object.getString("image_url"),image);


                        name.setText(object.getString("name"));
                        country.setText(object.getString("country"));


                       if (!object.getString("wallet_address").equals("0")){

                           addAddressView.setVisibility(View.GONE);
                           showAddress.setVisibility(View.VISIBLE);
                           walletAddressText.setText(object.getString("wallet_address"));

                       }
                       else {
                           addAddressView.setVisibility(View.VISIBLE);
                       }


                    } else {
                        // progress.setVisibility(View.GONE);
                       progressProfile.setVisibility(View.INVISIBLE);

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


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.addBitButton:
                addAddress(walletAddress.getText().toString(),userID);
                break;
            case R.id.cameraButton:
                filePicker();
                break;
            case R.id.editProfile:
                callBottomSheet();
                break;
        }
    }

    private void callBottomSheet() {

        EditDisplayName editDisplayName = new EditDisplayName(getContext(), this,
                displayName.getText().toString());
        editDisplayName.show(getActivity().getSupportFragmentManager(), editDisplayName.getTag());
    }

    private void filePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri

                InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                
                // get File path
                //converting to base 64;
                base64String = convert(bitmap);

                if (!base64String.isEmpty()) {

                    updateProfile(displayName.getText().toString(),base64String,userID);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    public String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

    }

    private void updateProfile(final String display, final String base64String, final String user_id) {
        progressProfile.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Personal_details", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        progressProfile.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        //  finish();

                        if (!base64String.equals("0")){
                            setImage(base64String,image);
                        }else {

                        }

                        displayName.setText(display);




                    } else {
                        progressProfile.setVisibility(View.INVISIBLE);

                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Try after sometime....", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", user_id);
                map.put("profile", base64String);
                map.put("display_name", display);

                Log.e(TAG,map+"");

                return map;

            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public void onSave(String name) {
        updateProfile(name,"0",userID);
    }

    @Override
    public void onCancle() {

    }
}

