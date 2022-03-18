package com.HomeGarage.garage.home;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.HomeGarage.garage.FirebaseUtil;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.databinding.ActivityUserInfoBinding;
import com.HomeGarage.garage.home.models.CarInfo;
import com.HomeGarage.garage.sign.SignUpFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;
import java.util.UUID;

public class UserInfoActivity extends AppCompatActivity {


    private ActivityUserInfoBinding binding;
    private ActivityResultLauncher<Object> launcher;
    private CarInfo carInfo = FirebaseUtil.carInfoLogin.get(0);
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(carInfo.getImageUrl()!=null){
            showImage(carInfo.getImageUrl());
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

        setInfo();

        binding.layoutEditInfo.setOnClickListener(v->{
            binding.editUserName.setEnabled(true);
            binding.editPhone.setEnabled(true);
        });

        binding.btnSave.setOnClickListener(v -> {
            carInfo.setName(binding.editUserName.getText().toString());
            carInfo.setPhone(binding.editPhone.getText().toString());
            FirebaseUtil.databaseReference.child(carInfo.getId()).setValue(carInfo);
        });
        binding.profileImage.setOnClickListener(v->launcher.launch(null));
    }

    void setInfo() {
        if (carInfo.getId() != null) {
            binding.editUserName.setText(carInfo.getName());
            binding.editPhone.setText(carInfo.getPhone());
            binding.editEmail.setText(carInfo.getEmail());
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
                carInfo.setImageName(nameImage);
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri ->{
                    carInfo.setImageUrl(uri.toString());
                    FirebaseUtil.databaseReference.child(carInfo.getId()).setValue(carInfo);
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