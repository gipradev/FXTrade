package com.example.fxcaptrade.BottomSheet;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fxcaptrade.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditDisplayName extends BottomSheetDialogFragment implements View.OnClickListener{
    private static final String TAG = "EditDisplayName";


    private final Context context;
    private final OnClickListener onListiner;
    private final String name;
    private TextView saveButton,cancleButton;
    private EditText display_name;

    public EditDisplayName(Context applicationContext, OnClickListener listener,
                           String name) {


        this.context = applicationContext;
        this.onListiner = listener;
        this.name = name;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_display_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        declarations(view);


    }

    private void declarations(View view) {

        display_name = (EditText) view.findViewById(R.id.display_name);
        saveButton = (TextView) view.findViewById(R.id.save_action);
        cancleButton = (TextView) view.findViewById(R.id.cancel_action);
        
        display_name.setText(name);

        saveButton.setOnClickListener(this);
        cancleButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Log.e(TAG,"here");
        switch (v.getId()) {
            case R.id.save_action:

                onListiner.onSave(display_name.getText().toString());

                break;
            case R.id.cancel_action:

                EditDisplayName.this.dismiss();

                break;
            default:
                break;
        }
        EditDisplayName.this.dismiss();

    }

    public interface OnClickListener {

        void onSave(String name);

        void onCancle();
    }
}

