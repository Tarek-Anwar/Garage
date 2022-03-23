package com.HomeGarage.garage.sign;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeActivity;
import com.HomeGarage.garage.home.navfragment.OnSwipeTouchListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {


     EditText emailEditText,passwordEditText;
     TextView sign_in ;
     FloatingActionButton login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(rootView);

        rootView.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                opneSign();
            }
        });

        sign_in.setOnClickListener(V-> {
           opneSign();
        });

        login.setOnClickListener(v-> {
            String email=emailEditText.getText().toString();
            String pass= passwordEditText.getText().toString();

            if(!email.isEmpty()&&!pass.isEmpty()) {
                if (!(pass.length()<6)) {
                    FirebaseAuth firebaseAuth= FirebaseUtil.firebaseAuth;
                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        { Toast.makeText(getContext(),"Welcom!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else { Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show(); }
                    });
                }
                else { Toast.makeText(getContext(), "password should be 6 digits or more" , Toast.LENGTH_SHORT).show(); }
            } else { Toast.makeText(getContext(), "please insert email and password", Toast.LENGTH_SHORT).show(); }
        });

        return rootView;
    }

    private void initViews(View rootView) {
        emailEditText=rootView.findViewById(R.id.et_email_address);
        passwordEditText=rootView.findViewById(R.id.password_TF);
        sign_in = rootView.findViewById(R.id.txt_sign_up_log);
        login = rootView.findViewById(R.id.login_float);
    }

    private void opneSign(){
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                R.anim.enter_left_to_righ,R.anim.exit_left_to_righ)
        .replace(R.id.fragmentContainer_main, new SignUpFragment())
        .commit();
    }
    @Override
    public void onResume() {
        super.onResume();
        FirebaseUtil.attachListner();
    }

    @Override
    public void onPause() {
        super.onPause();
        FirebaseUtil.detachListner();
    }
}