package com.HomeGarage.garage.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

     TextInputEditText emailEditText,passwordEditText;
     Button loginBTN,registerButton ;
     TextView forgotPassTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_login, container, false);
        initViews(rootView);

        registerButton.setOnClickListener(V-> {

                SignUpFragment newFragment = new SignUpFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer_main, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
        });

        loginBTN.setOnClickListener(v-> {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
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

    }
}