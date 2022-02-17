package com.HomeGarage.garage.sign;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.R;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpFragment extends Fragment {

    public static final String USER_NAME = "user name";
    public static final String EMAIL = "emaiil";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";

    TextInputEditText userNameET,emailET,phoneET,passwordET,confirmET , addressET;
    Button creatBTN;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_sign, container, false);
        initViews(rootView);

        creatBTN.setOnClickListener(v-> {

            SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.file_info_user),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(USER_NAME,userNameET.getText().toString());
            editor.putString(EMAIL,emailET.getText().toString());
            editor.putString(PHONE,phoneET.getText().toString());
            editor.putString(ADDRESS,addressET.getText().toString());
            Toast.makeText(getContext(), "Sussess", Toast.LENGTH_SHORT).show();
            editor.commit();
        });

        return rootView;
    }

    private void initViews(View rootView)
    {
        userNameET=rootView.findViewById(R.id.user_name_TF);
        emailET=rootView.findViewById(R.id.cemail_TF);
        phoneET=rootView.findViewById(R.id.phone_TF);
        passwordET=rootView.findViewById(R.id.password_TF);
        confirmET=rootView.findViewById(R.id.Confirm_Password_TF);
        creatBTN=rootView.findViewById(R.id.register_btn);
        addressET = rootView.findViewById(R.id.address_TF);
    }
}