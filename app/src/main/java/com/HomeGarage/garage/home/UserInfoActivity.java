package com.HomeGarage.garage.home;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.HomeGarage.garage.R;
import com.HomeGarage.garage.databinding.ActivityUserInfoBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UserInfoActivity extends AppCompatActivity {

    public static final String IMAGE_PROFILE = "IMAGE_PROFILE";
    private ActivityUserInfoBinding binding;
    private ActivityResultLauncher<Object> launcher;
    private SharedPreferences preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //define binding and set ContentView
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // define file preferences
        preferences = getSharedPreferences(getString(R.string.file_info_user), Context.MODE_PRIVATE);


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
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(IMAGE_PROFILE,uri.getPath());
                editor.apply();
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

        if(preferences.getString(IMAGE_PROFILE,null)!=null){
            binding.profileImage.setImageURI(Uri.parse(preferences.getString(IMAGE_PROFILE,null)));
        }
        binding.profileImage.setOnClickListener(v->launcher.launch(null));
    }
}