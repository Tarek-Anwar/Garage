package com.HomeGarage.garage.ui.sign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.HomeGarage.garage.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPasswordFragment extends Fragment {

    private TextInputEditText inputEmail;
    private Button btnReset;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    public ForgetPasswordFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_forget_password, container, false);
        initUI(view);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        auth = FirebaseAuth.getInstance();
        btnReset.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Enter your registered email", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(),"Please check your email history", Toast.LENGTH_SHORT).show();
                    while (fm.getBackStackEntryCount() != 0) {
                        fm.popBackStackImmediate();
                    }
                } else Toast.makeText(getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            });
        });
        return view;
    }

    private void initUI(View view) {
        inputEmail = view.findViewById(R.id.email_sent_rest);
        btnReset = view.findViewById(R.id.btn_reset_password);
        progressBar = view.findViewById(R.id.progressBar_rest);

    }
}