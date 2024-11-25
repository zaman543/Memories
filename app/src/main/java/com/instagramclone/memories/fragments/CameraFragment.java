package com.instagramclone.memories.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.instagramclone.memories.R;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraFragment extends Fragment {
    private ActivityResultLauncher<Uri> cameraResultLauncher;
    public static final String TAG = "Camera Fragment";

    private Uri tempPhotoUri;

    public CameraFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requireActivity().setTitle("Camera");
        return inflater.inflate(R.layout.fragment_compose, container, false); //TODO update
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraResultLauncher = registerForActivityResult(pictureContract, result -> {
            if(result) {
                Bitmap takenImage;
                try {
                    takenImage = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), tempPhotoUri);
                    float aspectRatio = takenImage.getWidth() / (float) takenImage.getHeight();
                    int width = 800;
                    int height = Math.round(width/aspectRatio);
                    Bitmap scaled = Bitmap.createScaledBitmap(takenImage, width, height, false);
                    Bundle photo = new Bundle();
                    photo.putParcelable("photoFile", scaled);
                    getParentFragmentManager().setFragmentResult("photoFile", photo);
                    getParentFragmentManager().popBackStack();
                } catch (IOException e) {
                    Log.e(TAG, "Unable to create temporary image bitmap", e);
                    getParentFragmentManager().popBackStack();
                } catch (Exception e) {
                    Log.e(TAG, "unspecified error", e);
                    getParentFragmentManager().popBackStack();
                }
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });
        launchCamera();
    }

    ActivityResultContracts.TakePicture pictureContract = new ActivityResultContracts.TakePicture() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, @NonNull Uri input) {
            super.createIntent(context, input);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, input);
            return intent;
        }
    };

    private void launchCamera() {
        tempPhotoUri = createTempImageUri();
        cameraResultLauncher.launch(tempPhotoUri);
    }

    private Uri createTempImageUri() {
        String imageFileName = "TEMP_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        File storageDir = requireContext().getCacheDir();

        try {
            File tempFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            return FileProvider.getUriForFile(requireContext(), "com.memories.fileprovider", tempFile);
        } catch (IOException e) {
            Log.e(TAG, "Unable to create a temp file", e);
            getParentFragmentManager().popBackStack();
        }

        return null;
    }
}