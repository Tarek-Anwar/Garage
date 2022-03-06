package com.HomeGarage.garage.sign;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeActivity;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {

    public static final String USER_NAME = "user name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    FirebaseUser firebaseUser;
    AwesomeValidation validation;

    TextInputLayout userNameET,emailET,phoneET,passwordET,confirmET ;
    Button creatBTN;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validation=new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_sign, container, false);
        initViews(rootView);
        validatET();
        creatBTN.setOnClickListener(v-> {

            String userName=userNameET.getEditText().getText().toString();
            String userEmail=emailET.getEditText().getText().toString();
            String userPhone=phoneET.getEditText().getText().toString();
            String userPass=passwordET.getEditText().getText().toString();
            String confirmPass=confirmET.getEditText().getText().toString();

            SharedPreferences preferences = requireActivity().getSharedPreferences(getString(R.string.file_info_user),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(USER_NAME, Objects.requireNonNull(userName));
            editor.putString(EMAIL, Objects.requireNonNull(userEmail));
            editor.putString(PHONE, Objects.requireNonNull(userPhone));
            editor.apply();


            if( validation.validate()) {
                FirebaseAuth firebaseAuth= FirebaseUtil.firebaseAuth;
                    firebaseAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference databaseReference = FirebaseUtil.databaseReference;
                                firebaseUser = task.getResult().getUser();
                                DatabaseReference reference=databaseReference.child(firebaseUser.getUid());
                                reference.child("Full Name").setValue(userName);
                                reference.child("Password").setValue(userPass);
                                reference.child("Email").setValue(userEmail);
                                reference.child("Phone").setValue(userPhone);
                                reference.child("id").setValue(firebaseUser.getUid());
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                Toast.makeText(getContext(),"you have account now",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            else
            {
                Toast.makeText(getContext(),"invalid register",Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void initViews(View rootView) {
        userNameET=rootView.findViewById(R.id.user_Name_ET);
        emailET=rootView.findViewById(R.id.cemail);
        phoneET=rootView.findViewById(R.id.cphone);
        passwordET=rootView.findViewById(R.id.cPassword_ET);
        confirmET=rootView.findViewById(R.id.Confirm_Password_ET);
        creatBTN=rootView.findViewById(R.id.register_btn);
    }

    private void validatET() {
        validation.addValidation(userNameET, RegexTemplate.NOT_EMPTY,"enter your name");
        validation.addValidation(emailET, Patterns.EMAIL_ADDRESS,"invalid e-mail");
        validation.addValidation(phoneET,"^01[0125][0-9]{8}$","invalid phone number");
        validation.addValidation(passwordET, "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}","password must be 6 digits or more");
        validation.addValidation(confirmET,passwordET,"mismatch password");
    }
}