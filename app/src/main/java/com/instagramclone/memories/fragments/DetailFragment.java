package com.instagramclone.memories.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.instagramclone.memories.R;
import com.instagramclone.memories.models.Post;

import java.text.DateFormat;
import java.util.Objects;

public class DetailFragment extends Fragment {
    public DetailFragment() {}
    Post post;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        post = getArguments().getSerializable("post", Post.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_post, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvUsername = view.findViewById(R.id.tvUsername);
        TextView tvTimestamp = view.findViewById(R.id.tvTimestamp);
        TextView tvPostDescription = view.findViewById(R.id.tvPostDescription);
        ImageView ivImage = view.findViewById(R.id.ivImage);
        ImageView ivPostProfilePic = view.findViewById(R.id.ivPostProfilePic);

        tvUsername.setText(post.getUser().getUsername());
        tvPostDescription.setText(post.getDescription());
        tvTimestamp.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(post.getCreatedAt()));

        Glide.with(requireContext()).load(post.getImage().getUrl()).into(ivImage);
        Glide.with(requireContext()).load(Objects.requireNonNull(post.getUser().getParseFile("profilePicture")).getUrl()).into(ivPostProfilePic);

    }
}
