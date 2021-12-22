package com.HomeGarage.garage.sign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.HomeGarage.garage.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class LoginFragment extends Fragment {
     TextInputEditText emailEditText,passwordEditText;
     Button loginBTN,registerButton;
     TextView forgotPassTV;
     FrameLayout frameLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_login, container, false);
        initViews(rootView);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout=(FrameLayout) getActivity().findViewById(R.id.framelayout);
                SignUpFragment signUpFragment=new SignUpFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(frameLayout.getId(),signUpFragment,"creat new account")
                        .addToBackStack(null).commit();
            }
        });
        return rootView;
    }
    private void initViews(View rootView)
    {
        emailEditText=(TextInputEditText) rootView.findViewById(R.id.email_TF);
        passwordEditText=(TextInputEditText) rootView.findViewById(R.id.password_TF);
        loginBTN=(Button) rootView.findViewById(R.id.login_BTN);
        registerButton=(Button) rootView.findViewById(R.id.creat_account_btn);
        forgotPassTV=(TextView) rootView.findViewById(R.id.forgetPass_TV);
        frameLayout=(FrameLayout) rootView.findViewById(R.id.framelayout);
    }
}