package com.HomeGarage.garage.sign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.HomeGarage.garage.R;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpFragment extends Fragment {
    TextInputEditText userNameET,emailET,phoneET,passwordET,confirmET;
    Button creatBTN;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_sign, container, false);
        initViews(rootView);
        return rootView;
    }
    private void initViews(View rootView)
    {
        userNameET=(TextInputEditText) rootView.findViewById(R.id.user_name_TF);
        emailET=(TextInputEditText) rootView.findViewById(R.id.cemail_TF);
        phoneET=(TextInputEditText) rootView.findViewById(R.id.phone_TF);
        passwordET=(TextInputEditText) rootView.findViewById(R.id.cpassword_TF);
        confirmET=(TextInputEditText) rootView.findViewById(R.id.Confirm_Password_TF);
        creatBTN=(Button) rootView.findViewById(R.id.register_btn);
    }
}