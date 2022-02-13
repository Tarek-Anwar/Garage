package com.HomeGarage.garage.sign;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;

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

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void initViews(View rootView)
    {
        emailEditText=rootView.findViewById(R.id.email_TF);
        passwordEditText=rootView.findViewById(R.id.password_TF);
        loginBTN=rootView.findViewById(R.id.login_BTN);
        registerButton=rootView.findViewById(R.id.creat_account_btn);
        forgotPassTV= rootView.findViewById(R.id.forgetPass_TV);
        frameLayout=rootView.findViewById(R.id.framelayout);
    }
}