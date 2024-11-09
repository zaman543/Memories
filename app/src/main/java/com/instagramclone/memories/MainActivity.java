package com.instagramclone.memories;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.instagramclone.memories.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ParseQuery;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCaption = findViewById(R.id.etCaption);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnCaptureImage.setOnClickListener(v -> launchCamera());

        //queryPosts();

        btnSubmit.setOnClickListener(v -> {
            String description = etCaption.getText().toString();
            if(description.isEmpty()) {
                Toast.makeText(MainActivity.this, "Caption can't be blank", Toast.LENGTH_SHORT).show();
                return;
            }
            if(photoFile == null || ivPostImage.getDrawable() == null) {
                Toast.makeText(MainActivity.this, "Can't submit without image", Toast.LENGTH_SHORT).show();
                return;
            }
            savePost(description, ParseUser.getCurrentUser(), photoFile);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //we want to inflate menu to add action bar if present
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void goLoginActivity() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.log_out) {
            ParseUser.logOut();
            goLoginActivity();
        }
        return true;
    }

    //replacement for deprecated StartActivityForResult
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

    ActivityResultLauncher<Uri> cameraResultLauncher = registerForActivityResult(pictureContract, new ActivityResultCallback<>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result) {
                //camera photo is saved to storage
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                float aspectRatio = takenImage.getWidth() / (float) takenImage.getHeight();
                int width = 800;
                int height = Math.round(width/aspectRatio);
                Bitmap scaled = Bitmap.createScaledBitmap(takenImage, width, height, false);
                ImageView ivPreview = findViewById(R.id.ivPostImage);
                ivPreview.setImageBitmap(scaled);
            } else {
                Toast.makeText(MainActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    });

    //intent will take a picture and return control to app
    //we are making an intent for an action - image capture
    //we want to tell the app we are launching where to put the output
    //input: (uri) pointer to file to put image into
    private void launchCamera() {
        photoFile = getPhotoFileUri(photoFileName);
        Uri input = FileProvider.getUriForFile(MainActivity.this, "com.memories.fileprovider", photoFile);
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
    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "Failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);

    }

    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setUser(currentUser);
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.saveInBackground(e -> {
            if(e == null) {
                Log.i(TAG,"Saved post");
                etCaption.setText("");
                ivPostImage.setImageResource(0);
            } else {
                Log.e("Failed to save post", Objects.requireNonNull(e.getMessage()));
            }
        });
    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);

        query.findInBackground((posts, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e);
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            } else {
                for (Post post : posts) {
                    Log.i(TAG, "Post " + post.getDescription() + ", Username: " + post.getUser().getUsername());
                }
            }
        });
    }
}