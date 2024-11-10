package com.instagramclone.memories.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.instagramclone.memories.LoginActivity;
import com.instagramclone.memories.ProfilePostsAdapter;
import com.instagramclone.memories.R;
import com.instagramclone.memories.models.Post;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class ProfileFragment extends HomeFragment {
    public static final String TAG = "ProfileFragment";
    private ProfilePostsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvUserPosts = view.findViewById(R.id.rvUserPosts);
        //TextView tvUsernameProfile = view.findViewById(R.id.tvUsernameProfile);
        //Button btnLogout = view.findViewById(R.id.btnLogout);
        allPosts = new ArrayList<>();
        adapter = new ProfilePostsAdapter(getContext(), allPosts);

        rvUserPosts.setAdapter(adapter);
        rvUserPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        //btnLogout.setOnClickListener(e -> {
        //    ParseUser.logOut();
        //    goLoginActivity();
        //});
        //tvUsernameProfile.setText(ParseUser.getCurrentUser().getUsername());

        queryPosts();
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
                for (Post post : posts) {
                    Log.i(TAG, "Post " + post.getDescription() + ", Username: " + post.getUser().getUsername() + post.getCreatedAt());
                }
                allPosts.addAll(posts);
                Log.i(TAG, "added posts");
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