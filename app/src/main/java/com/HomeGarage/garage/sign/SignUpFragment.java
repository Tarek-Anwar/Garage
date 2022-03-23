package com.HomeGarage.garage.sign;

import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SignUpFragment extends Fragment {


    AwesomeValidation validation;
    CarInfo model = new CarInfo();
    TextInputEditText userNameET,emailET,phoneET,passwordET,confirmET ;
    FloatingActionButton creatBTN;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validation= new AwesomeValidation(ValidationStyle.BASIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_sign, container, false);
        initViews(rootView);

        validatET();

        creatBTN.setOnClickListener(v-> {

            model.setName(userNameET.getText().toString());
            model.setEmail(emailET.getText().toString());
            model.setPhone(phoneET.getText().toString());
            String userPass=passwordET.getText().toString();

            if(validation.validate()) {
               FirebaseAuth firebaseAuth= FirebaseUtil.firebaseAuth;
                 firebaseAuth.createUserWithEmailAndPassword(model.getEmail(),userPass).addOnCompleteListener(task -> {
                     if (task.isSuccessful()) {
                            DatabaseReference databaseReference = FirebaseUtil.databaseReference;
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            DatabaseReference reference =databaseReference.child(firebaseUser.getUid());

                            model.setId(firebaseUser.getUid());
                            model.setBalance(0.0f);

                            reference.setValue(model);
                            Toast.makeText(getContext(),"you have account now",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        { Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show(); }
                    });
                Toast.makeText(getContext(), "Goog", Toast.LENGTH_SHORT).show();
                }
            else
            { Toast.makeText(getContext(),"invalid register",Toast.LENGTH_SHORT).show(); }
        });
        return rootView;
    }

    private void initViews(View rootView) {
        userNameET=rootView.findViewById(R.id.user_name_TF);
        emailET=rootView.findViewById(R.id.et_email_address);
        phoneET=rootView.findViewById(R.id.edit_phone);
        passwordET=rootView.findViewById(R.id.password_sign);
        confirmET=rootView.findViewById(R.id.Confirm_Password_TF);
        creatBTN = rootView.findViewById(R.id.sign_float);
    }

    private void validatET() {
        validation.addValidation(userNameET, RegexTemplate.NOT_EMPTY,getString(R.string.invalid_username));
        validation.addValidation(emailET, Patterns.EMAIL_ADDRESS,getString(R.string.invalid_email));
         validation.addValidation(phoneET,"^01[0125][0-9]{8}$",getString(R.string.invalid_phone));
        validation.addValidation(passwordET, "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}",getString(R.string.invalid_password));
        validation.addValidation(confirmET,passwordET,getString(R.string.invalid_conform));
    }
}