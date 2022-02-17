package com.HomeGarage.garage.sign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.HomeGarage.garage.R;
import com.HomeGarage.garage.databinding.FragmentUserInfoBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;




public class UserInfoFragment extends Fragment {
    public static final String IMAGE_PRFILE = "IMAGE_PRFILE";
    FragmentUserInfoBinding binding;
    ActivityResultLauncher<Object> launcher;
    SharedPreferences preferences ;
    public UserInfoFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getContext().getSharedPreferences(getString(R.string.file_info_user),Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActivityResultContract<Object, Uri> contract = new ActivityResultContract<Object, Uri>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Object input) {
                return CropImage.activity()
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setAspectRatio(1,1)
                        .getIntent(getContext());
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
        binding = FragmentUserInfoBinding.inflate(getLayoutInflater());

        launcher=registerForActivityResult(contract,uri->{
            if (uri!=null){
                binding.imgProfileUpdate.setImageURI(uri);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(IMAGE_PRFILE,uri.getPath());
                editor.apply();
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

        if(preferences.getString(IMAGE_PRFILE,null)!=null){
            binding.imgProfileUpdate.setImageURI(Uri.parse(preferences.getString(IMAGE_PRFILE,null)));
        }
        binding.imgProfileUpdate.setOnClickListener(v->launcher.launch(null));

        return binding.getRoot();
    }

}