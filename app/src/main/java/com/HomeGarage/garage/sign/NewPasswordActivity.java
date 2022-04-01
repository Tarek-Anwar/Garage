package com.HomeGarage.garage.sign;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.MainActivity;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.home.HomeActivity;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;

public class NewPasswordActivity extends AppCompatActivity {

    TextInputEditText oldPass , newPass , conPass;
    Button savePass;
    AwesomeValidation validation  = new AwesomeValidation(ValidationStyle.BASIC);;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        initUI();
        validatET();

        FirebaseUser user = FirebaseUtil.firebaseAuth.getCurrentUser();
        String email = user.getEmail();
        String password_Successfully = getString(R.string.Password_Successfully_Modified);
        String some_error = getString(R.string.something_went_wrong);
        String old_pass_error  = getString(R.string.old_pass_worng);

        savePass.setOnClickListener(v -> {
            if(validation.validate()){
                String oldpassText = oldPass.getText().toString();
                String newPassText = newPass.getText().toString();
                AuthCredential credential = EmailAuthProvider.getCredential(email,oldpassText);
                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        user.updatePassword(newPassText).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(getApplicationContext(), password_Successfully , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }else{ Toast.makeText(getApplicationContext(), some_error , Toast.LENGTH_SHORT).show(); }
                        });
                    }else { Toast.makeText(getApplicationContext(), old_pass_error , Toast.LENGTH_SHORT).show(); }
                });
            }
        });


    }

    private void initUI(){
        oldPass = findViewById(R.id.et_old_pass);
        newPass = findViewById(R.id.et_new_pass);
        conPass = findViewById(R.id.et_con_new_pass);
        savePass = findViewById(R.id.btn_new_pass_save);
    }

    private void validatET() {
        validation.addValidation(oldPass, RegexTemplate.NOT_EMPTY,getString(R.string.ake_sure_password));
        validation.addValidation(newPass, "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}",getString(R.string.invalid_password));
        validation.addValidation(conPass,newPass,getString(R.string.invalid_conform));
    }

}