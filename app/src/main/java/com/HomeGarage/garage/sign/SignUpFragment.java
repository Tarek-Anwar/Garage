package com.HomeGarage.garage.sign;

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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeActivity;
import com.HomeGarage.garage.home.models.CarInfo;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class SignUpFragment extends Fragment {

    public static final String USER_NAME = "user name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ID_USER = "ID_USER";
    public static final String BALANCE = "BALANCE";
    AwesomeValidation validation;

    CarInfo model = new CarInfo();

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
        SharedPreferences preferences = requireActivity().getSharedPreferences(getString(R.string.file_info_user),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        creatBTN.setOnClickListener(v-> {

            model.setName(userNameET.getEditText().getText().toString());
            model.setEmail(emailET.getEditText().getText().toString());
            model.setPhone(phoneET.getEditText().getText().toString());

            String userPass=passwordET.getEditText().getText().toString();

            editor.putString(USER_NAME, model.getName());
            editor.putString(EMAIL, model.getEmail());
            editor.putString(PHONE, model.getPhone());
            editor.apply();

            if(validation.validate()) {
                FirebaseAuth firebaseAuth= FirebaseUtil.firebaseAuth;
                 firebaseAuth.createUserWithEmailAndPassword(model.getEmail(),userPass).addOnCompleteListener(task -> {
                     if (task.isSuccessful()) {
                            DatabaseReference databaseReference = FirebaseUtil.databaseReference;
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            DatabaseReference reference =databaseReference.child(firebaseUser.getUid());

                            model.setId(firebaseUser.getUid());
                            model.setBalance(0.0f);

                            editor.putString(ID_USER, model.getId());
                            editor.putFloat(BALANCE , model.getBalance());
                            editor.commit();

                            reference.setValue(model);
                            Toast.makeText(getContext(),"you have account now",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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