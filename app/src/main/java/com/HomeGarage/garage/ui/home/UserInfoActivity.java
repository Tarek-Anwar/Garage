package com.HomeGarage.garage.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.HomeGarage.garage.util.FirebaseUtil;
import com.HomeGarage.garage.databinding.ActivityUserInfoBinding;
import com.HomeGarage.garage.modules.CarInfoModule;
import com.HomeGarage.garage.ui.sign.NewPasswordActivity;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

public class UserInfoActivity extends AppCompatActivity {


    Uri filePath;
    private ActivityUserInfoBinding binding;
    private ActivityResultLauncher<Object> launcher;
    private CarInfoModule carInfoModule = FirebaseUtil.carInfoModuleLogin.get(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(carInfoModule.getImageUrl()!=null){
            showImage(carInfoModule.getImageUrl());
        }
        ActivityResultContract<Object, Uri> contract = new ActivityResultContract<Object, Uri>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Object input) {
                return CropImage.activity()
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setAspectRatio(1,1)
                        .getIntent(getApplicationContext());
            }
            @Override
            public Uri parseResult(int resultCode, @Nullable Intent intent) {
                if(CropImage.getActivityResult(intent)!=null){
                    return CropImage.getActivityResult(intent).getUri();
                }else {
                    return null;
                }
            }
        };

        launcher=registerForActivityResult(contract,uri->{
            if (uri!=null){
                binding.profileImage.setImageURI(uri);
                filePath = uri;
                uploadImage();
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

        binding.newPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this , NewPasswordActivity.class);
                startActivity(intent);
            }
        });
        setInfo();

        binding.layoutEditInfo.setOnClickListener(v->{
            binding.editUserName.setEnabled(true);
            binding.editPhone.setEnabled(true);
        });

        binding.btnSave.setOnClickListener(v -> {
            carInfoModule.setName(binding.editUserName.getText().toString());
            carInfoModule.setPhone(binding.editPhone.getText().toString());
            FirebaseUtil.databaseReference.child(carInfoModule.getId()).setValue(carInfoModule);
        });
        binding.profileImage.setOnClickListener(v->launcher.launch(null));
    }

    void setInfo() {
        if (carInfoModule.getId() != null) {
            binding.editUserName.setText(carInfoModule.getName());
            binding.editPhone.setText(carInfoModule.getPhone());
            binding.editEmail.setText(carInfoModule.getEmail());
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = FirebaseUtil.storageReference.child(UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                String nameImage  = taskSnapshot.getStorage().getPath();
                carInfoModule.setImageName(nameImage);
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri ->{
                    carInfoModule.setImageUrl(uri.toString());
                    FirebaseUtil.databaseReference.child(carInfoModule.getId()).setValue(carInfoModule);
                });
                Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show(); }).addOnFailureListener(e -> { progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int)progress + "%"); });

        }
    }

    private void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width, width)
                    .centerCrop()
                    .into(binding.profileImage);
        }
    }
}