package com.instagramclone.memories.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_USER = "User";

    public Post() {
        super();
    }

    public String getCaption() {
        return getString(KEY_DESCRIPTION);
    }

    public void setCaption(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser author) {
        put(KEY_USER, author);
    }

}
