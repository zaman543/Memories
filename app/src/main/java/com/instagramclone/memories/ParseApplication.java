package com.instagramclone.memories;

import android.app.Application;

import com.instagramclone.memories.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    String clientKey = BuildConfig.CLIENT_KEY;
    String applicationID = BuildConfig.APPLICATION_ID;
    String server = BuildConfig.SERVER;

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(applicationID)
                .clientKey(clientKey)
                .server(server)
                .build()
        );
    }
}
