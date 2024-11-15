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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
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
import java.util.Objects;

public class ComposeFragment extends Fragment {
    public static final String TAG = "ComposeFragment";
    private EditText etCaption;
    private ImageView ivPostImage;
    Button btnSubmit;
    private File photoFile;
   // ActivityResultLauncher<Uri> cameraResultLauncher;

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

        //moved here for problem: You must ensure the ActivityResultLauncher is registered before calling launch().
        /*cameraResultLauncher = registerForActivityResult(pictureContract, result -> {
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
        });*/

        btnCaptureImage.setOnClickListener(v -> {
            goFragment(new CameraFragment());
            //required: camera fragment must return a photofile that we can use to save post
            //figure out how to go back to a fragment and send that file
        });

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

    //replacement for deprecated StartActivityForResult
    /*ActivityResultContracts.TakePicture pictureContract = new ActivityResultContracts.TakePicture() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, @NonNull Uri input) {
            super.createIntent(context, input);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, input);
            return intent;
        }
    };*/

    //intent will take a picture and return control to app
    //we are making an intent for an action - image capture
    //we want to tell the app we are launching where to put the output
    //input: (uri) pointer to file to put image into
    /*private void launchCamera() {
        String photoFileName = "photo.jpg";
        photoFile = getPhotoFileUri(photoFileName);
        Uri input = FileProvider.getUriForFile(requireContext(), "com.memories.fileprovider", photoFile);
        cameraResultLauncher.launch(input);
    }*/

    //get photo file uniform file identifier - string identifying file - the img captured
    //sharing files with api 24 or higher: extra security restriction
    //just specifying a file to write to is bad
    //what if camera can't access location?
    //solution: wrap file object in a content provider
    //(content://)
    //using the FileProvider class
    //we are saying: application launched from our app has access to our external storage directory
    //now camera will be able to access file provider
    /*private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "Failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }*/

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