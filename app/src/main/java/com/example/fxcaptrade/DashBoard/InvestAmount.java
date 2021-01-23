package com.example.fxcaptrade.DashBoard;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvestAmount extends Fragment implements View.OnClickListener {
    private static final String TAG = "InvestAmount";
    private final List<String> amountList = new ArrayList<>();
    private Spinner spinnerAmount;
    private TextInputEditText hashcode, companyAddress, senderAddress, transferBitcoin;
    private Button chooseFile;
    private ExtendedFloatingActionButton reset, send;
    private int PICK_IMAGE_REQUEST = 100;
    private View activityView;
    private String base64String= "";
    private TextView filename;
    private RequestQueue requestQueue;
    private ImageButton copyButton;
    final String walletAddress = "1G3Cu82d1R5y9KA2rW5RVb8JRnaUkDCbif";
    private String packageId;
    private SharedPreferences user;
    private String username;

    private ImageView screenShotImg;

    public InvestAmount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invest_amount, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        declaration(view);
        getSharedPreference();
        getPackageList();
        getCompanyAddress();

        chooseFile.setOnClickListener(this);
        copyButton.setOnClickListener(this);
        reset.setOnClickListener(this);
        send.setOnClickListener(this);

        transferBitcoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                checkValidation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void checkValidation(String string) {

        if (string.isEmpty()){
            transferBitcoin.setError("Required");
        }
        else {
            transferBitcoin.setError(null);
        }
    }

    private void getCompanyAddress() {
        //progress.setVisibility(View.VISIBLE);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Company_bitcoin_address", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        companyAddress.setText(object.getString("bitcoin_address"));

                    } else {
                        // progress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No data Found", Toast.LENGTH_SHORT).show();
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
                map.put("user_id", username);
                return map;

            }
        };
        requestQueue.add(stringRequest);

    }

    private void getSharedPreference() {
        user = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        username = user.getString("user_id", "0");
    }

    //********************************************************************************************************************************

    //********************************************************************************************************************************

    private void getPackageList() {
        final ProgressDialog dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...",
                true);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Getpackage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {
                        dialog.dismiss();
                        // Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONArray array = object.getJSONArray("data");


                        ArrayList<String> list = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {
                            try {
                                String packageName = array.getJSONObject(i).getString("package_name");
                                if (packageName.equalsIgnoreCase("Select Amount")) {
                                    list.add(packageName);
                                } else {

                                    list.add("$ " + packageName);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setAmountSpinner(list, array);


                    } else {
                        dialog.dismiss();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Exception" + e);
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError" + error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", username);
                Log.e(TAG,username);
                return  map;
            }
        }
                ;
        requestQueue.add(stringRequest);

    }
    //********************************************************************************************************************************
    //********************************************************************************************************************************

    private void declaration(View view) {

        activityView = view;
        requestQueue = Volley.newRequestQueue(getActivity());
        companyAddress = (TextInputEditText) view.findViewById(R.id.companyAddress);
        senderAddress = (TextInputEditText) view.findViewById(R.id.senderAddress);
        transferBitcoin = (TextInputEditText) view.findViewById(R.id.transferBit);
        hashcode = (TextInputEditText) view.findViewById(R.id.hashCode);
        spinnerAmount = (Spinner) view.findViewById(R.id.amountSpinner);
        chooseFile = (Button) view.findViewById(R.id.chooseButton);
        copyButton = (ImageButton) view.findViewById(R.id.copyButton);
        filename = (TextView) view.findViewById(R.id.fileName);
        reset = (ExtendedFloatingActionButton) view.findViewById(R.id.fabReset);
        send = (ExtendedFloatingActionButton) view.findViewById(R.id.fabSend);
        screenShotImg = (ImageView) view.findViewById(R.id.screenShot);

        companyAddress.setKeyListener(null);


    }


    private void setAmountSpinner(ArrayList<String> packageList, final JSONArray jsonArray) {

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, packageList);
        spinnerAmount.setAdapter(itemsAdapter);

        spinnerAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spinnerAmount.getSelectedItem().toString().equalsIgnoreCase("Select Amount")) {
                    try {
                        packageId = jsonArray.getJSONObject(position).getString("package_id");
                        Log.e(TAG, packageId);

                        getBTC(spinnerAmount.getSelectedItem().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

    }

    private void getBTC(final String amount) {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());
            Log.e(TAG, "Setting a new request queue");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Converter", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response" + response);
                try {

                    JSONObject object = new JSONObject(response.trim());
                    String status = object.getString("status");
                    if (status.equals("1")) {

                        String btc = object.getString("result");
                        transferBitcoin.setText(btc + "  BTC");
                        transferBitcoin.setError(null);
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
                map.put("amount", amount);
                return map;

            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.copyButton:
                ClipboardManager cManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText("text", companyAddress.getText());
                cManager.setPrimaryClip(cData);
                Toast.makeText(getContext(), "Address copied...!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.chooseButton:
                filePicker(filename);
                break;
            case R.id.fabReset:

                resetAllValues();
                break;
            case R.id.fabSend:
                validateForm();
                break;
        }

    }

    //*****************************************************************************************************************************
    private void validateForm() {

        boolean stat = false;
        if (senderAddress.getText().toString().isEmpty()) {
            senderAddress.setError("required");
            stat = true;
        }
        if (transferBitcoin.getText().toString().isEmpty()) {
            transferBitcoin.setError("required");
            stat = true;
        }
        if (base64String.equals("")) {
            Toast.makeText(getContext(), "no File found", Toast.LENGTH_SHORT).show();
        }
        if (filename.getText().toString().equalsIgnoreCase("No file selected")) {
            filename.setError("required");
            stat = true;
        }
        if (!stat) {

            alertDialogue();

        }
    }

    private void alertDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Alert");
        builder.setMessage("Do you really want to proceed....?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{


                submitAmount(packageId, transferBitcoin.getText().toString(),
                        senderAddress.getText().toString(), base64String);
                }catch(Exception e){
                    Log.e(TAG,e+"");
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }
    //*****************************************************************************************************************************

    private void submitAmount(final String package_id, final String transferBitcoin,
                              final String sendAddress, final String imageString) {

        final ProgressDialog dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                WebServices.BaseUrl + "Invest_amount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"Response"+response);

                JSONObject object = null;
                try {
                    object = new JSONObject(response.trim());

                String status = object.getString("status");
                    if (status.equals("1")) {

                        Log.e(TAG, "ONRes" );

                        dialog.dismiss();

                        //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        Toast toast = Toast.makeText(getActivity(), object.getString("message"),
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        resetAllValues();
                        getPackageList();


                    } else {

                        Log.e(TAG, "O" );
                        dialog.dismiss();

                        Toast toast = Toast.makeText(getActivity(), object.getString("message"),
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        //Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "ONs" );
            }
        }, new Response.ErrorListener()  {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "es" );
                dialog.dismiss();
                Toast.makeText(getActivity(), "Try after some times.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "VolleyError" + error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", username);
                map.put("package_id", package_id);
                map.put("btc", transferBitcoin);
                map.put("s_address", sendAddress);
                map.put("hashcode", "0");
                map.put("image_string", imageString);

                Log.e(TAG,username+" "+package_id+" "+transferBitcoin+" "+sendAddress);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity());
            Log.e(TAG, "Setting a new request queue");
        }

        requestQueue.add(stringRequest);

    }

    //*****************************************************************************************************************************
    //*****************************************************************************************************************************
    private void resetAllValues() {

        getPackageList();

        transferBitcoin.setText("");
        senderAddress.setText("");
        hashcode.setText("");
        filename.setText("");
        base64String = "";
        transferBitcoin.setError(null);
        screenShotImg.setImageDrawable(null);
        screenShotImg.setVisibility(View.GONE);
    }

    //*****************************************************************************************************************************
    //*****************************************************************************************************************************
    private void filePicker(TextView filename) {
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

                InputStream is = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                screenShotImg.setVisibility(View.VISIBLE);
                screenShotImg.setImageBitmap(bitmap);
                // get File path

//                File file = new File(imageUri.getPath());//create path from uri
//                final String[] split = file.getPath().split(":");//split the path.
               // String filePath = split[1];
                //converting to base 64;
                base64String = convert(bitmap);
                // get file name
                String fileName = getFileName(imageUri);
                filename.setText(fileName);
                filename.setError(null);

                // Log.e(TAG,"Base\n "+fileName);


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

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null,
                    null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
//*****************************************************************************************************************************



}
