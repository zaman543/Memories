package com.instagramclone.memories.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.instagramclone.memories.R;
import com.instagramclone.memories.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ComposeFragment extends Fragment {
    public static final String TAG = "ComposeFragment";
    private EditText etCaption;
    private ImageView ivPostImage;
    Button btnSubmit;
    private File photoFile;

    // Required empty public constructor
    public ComposeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //called when fragment should create its view object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("New Post");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    //triggered after onCreateView; any view setup happens here - view lookups + attaching listeners
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCaption = view.findViewById(R.id.etCaption);
        Button btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        getParentFragmentManager().setFragmentResultListener("photoFile", getViewLifecycleOwner(), (requestKey, result) -> {
            Bitmap photo = result.getParcelable("photoFile");
            ivPostImage.setImageBitmap(photo);
            try {
                if (photo != null) {
                    File mediaStorageDir = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
                    if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                        Log.d(TAG, "Failed to create directory");
                    }
                    String name = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".jpg";
                    photoFile = new File(mediaStorageDir.getPath() + File.separator + name);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(photoFile));
                } else {
                    photoFile = null;
                    Log.e(TAG, "unable to compress photo");
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Unable to convert image into JPG, try again", e);
            }
        });

        btnCaptureImage.setOnClickListener(v -> goFragment(new CameraFragment()));

        btnSubmit.setOnClickListener(v -> {
            String description = etCaption.getText().toString();
            if(description.isEmpty()) {
                Toast.makeText(getContext(), "Caption can't be blank", Toast.LENGTH_SHORT).show();
                return;
            }
            if(photoFile == null || ivPostImage.getDrawable() == null) {
                Toast.makeText(getContext(), "Can't submit without image", Toast.LENGTH_SHORT).show();
                return;
            }
            btnSubmit.setText(R.string.saving);
            savePost(description, ParseUser.getCurrentUser(), photoFile);
        });
    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setUser(currentUser);
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(e -> {
            if(e == null) {
                Toast.makeText(requireContext(), "Posted!", Toast.LENGTH_SHORT).show();
                etCaption.setText("");
                ivPostImage.setImageResource(0);
                btnSubmit.setText(R.string.submit);
                goFragment(new HomeFragment());
            } else {
                Log.e("Failed to save post", Objects.requireNonNull(e.getMessage()));
            }
        });
    }
    private void goFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) requireContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();
    }
}