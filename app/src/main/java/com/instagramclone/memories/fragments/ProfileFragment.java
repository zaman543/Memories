package com.instagramclone.memories.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.instagramclone.memories.LoginActivity;
import com.instagramclone.memories.ProfilePostsAdapter;
import com.instagramclone.memories.R;
import com.instagramclone.memories.models.Post;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends HomeFragment {
    public static final String TAG = "ProfileFragment";

    private ProfilePostsAdapter adapter;
    ImageView ivProfilePgPic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvUserPosts = view.findViewById(R.id.rvUserPosts);
        TextView tvUsernameProfile = view.findViewById(R.id.tvUsernameProfile);
        ImageButton btnLogout = view.findViewById(R.id.btnLogout);
        ivProfilePgPic = view.findViewById(R.id.ivProfilePgPic);

        allPosts = new ArrayList<>();
        adapter = new ProfilePostsAdapter(getContext(), allPosts);

        rvUserPosts.setAdapter(adapter);
        rvUserPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        btnLogout.setOnClickListener(e -> {
            ParseUser.logOut();
            goLoginActivity();
        });
        tvUsernameProfile.setText(ParseUser.getCurrentUser().getUsername());

        queryPosts();
        queryPicture();
    }

    private void queryPicture() {
        ParseQuery<ParseUser> currentUser = ParseUser.getQuery();
        currentUser.whereEqualTo("username", ParseUser.getCurrentUser().getUsername()).getFirstInBackground((user, e) -> {
            if(e == null) {
                String pic = Objects.requireNonNull(user.getParseFile("profilePicture")).getUrl();
                Glide.with(requireContext()).load(pic).into(ivProfilePgPic);
            } else {
                Log.e(TAG, "couldn't get user", e);
            }
        });
    }

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground((posts, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

   private void goLoginActivity() {
        Intent loginActivityIntent = new Intent(getContext(), LoginActivity.class);
        startActivity(loginActivityIntent);
        requireActivity().finish();
    }
}