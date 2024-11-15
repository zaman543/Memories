package com.instagramclone.memories.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.instagramclone.memories.R;

import java.io.File;

public class CameraFragment extends Fragment {
    private File photoFile;
    private ActivityResultLauncher<Uri> cameraResultLauncher;
    public static final String TAG = "Camera Fragment";


    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) {
        //    mParam1 = getArguments().getString(ARG_PARAM1);
        //    mParam2 = getArguments().getString(ARG_PARAM2);
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraResultLauncher = registerForActivityResult(pictureContract, result -> {
            if(result) {
                //camera photo is saved to storage
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                float aspectRatio = takenImage.getWidth() / (float) takenImage.getHeight();
                int width = 800;
                int height = Math.round(width/aspectRatio);
                Bitmap scaled = Bitmap.createScaledBitmap(takenImage, width, height, false);
                ImageView ivPreview = requireView().findViewById(R.id.ivPostImage);
                ivPreview.setImageBitmap(scaled);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        });
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
    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "Failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
    private void launchCamera() {
        String photoFileName = "photo.jpg";
        photoFile = getPhotoFileUri(photoFileName);
        Uri input = FileProvider.getUriForFile(requireContext(), "com.memories.fileprovider", photoFile);
        cameraResultLauncher.launch(input);
    }
}