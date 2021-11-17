package com.instagramclone.memories;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.instagramclone.memories.models.Post;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private EditText etCaption;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private File photoFile;
    private String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCaption = findViewById(R.id.etCaption);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnCaptureImage.setOnClickListener(v -> launchCamera());

       // queryPosts();

        btnSubmit.setOnClickListener(v -> {
            String caption = etCaption.getText().toString();
            if(caption.isEmpty()) {
                Toast.makeText(MainActivity.this, "Description can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if(photoFile == null || ivPostImage.getDrawable() == null) {
                Toast.makeText(MainActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                return;
            }
            ParseUser currentUser = ParseUser.getCurrentUser();
            savePost(caption, currentUser, photoFile);
        });
    }

    ActivityResultContracts.TakePicture pictureContract = new ActivityResultContracts.TakePicture() {
        @NonNull
        @CallSuper
        @Override
        public Intent createIntent(@NonNull Context context, @NonNull Uri input) {
            super.createIntent(context, input);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, input);
            return intent;
        }
    };

    ActivityResultLauncher<Uri> cameraResultLauncher = registerForActivityResult(pictureContract, new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result) {
                //camera photo is saved to storage
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                //resize bitmap
                //load the taken image into a preview
                //you might run into out of memory
                //store / resize bitmap to a smaller one
                ImageView ivPreview = findViewById(R.id.ivPostImage);
                ivPreview.setImageBitmap(takenImage);
            } else {
                Toast.makeText(MainActivity.this, "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    });

    public void launchCamera() {
        //intent will take a picture and return control to app
        //we are making an intent for an action - image capture
        //we want to tell the app we are launching where to put the output

        //input: (uri) pointer to file to put image into
        photoFile = getPhotoFileUri(photoFileName);
        Uri input = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
        cameraResultLauncher.launch(input);
    }

    //get photo file uniform file identifier - string identifying file - the img captured
    //sharing files with api 24 or higher: extra security restriction
    //just specifying a file to write to is bad
    //what if camera can't access location?
    //solution: wrap file object in a content provider
    //(content://)
    //using the FileProvider class
    //we are saying: application launched from our app has access to our external storage directory
    //now camera will be able to access file provider
    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "getPhotoFileUri: failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    private void savePost(String caption, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setCaption(caption);
        post.setImage(new ParseFile(photoFile));
        Log.i(TAG, "savePost: caption is " + post.getCaption());
        post.setAuthor(currentUser);
        post.saveInBackground(e -> {
            if(e != null) {
                Log.e(TAG, "error while saving", e);
                Toast.makeText(MainActivity.this, "Error saving! " + e, Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, "successfully saved post!");
            Toast.makeText(MainActivity.this, "successfully saved post!", Toast.LENGTH_SHORT).show();
            etCaption.setText("");
            ivPostImage.setImageResource(0);
        });
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());

        query.findInBackground((posts, e) -> {
            if(e != null) {
                Log.e(TAG, "Issue with getting posts", e);
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            for(Post post1 : posts) {
                Log.i(TAG, "Post: " + post1.getCaption() + ", username: " + post1.getAuthor().getUsername());
                Toast.makeText(MainActivity.this, post1.getCaption() + " ", Toast.LENGTH_LONG).show();
            }
        });
    }
}