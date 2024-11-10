package com.instagramclone.memories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.instagramclone.memories.fragments.DetailFragment;
import com.instagramclone.memories.models.Post;

import java.text.DateFormat;
import java.util.List;
import java.util.Objects;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private final Context context;
    private final List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvUsername;
        private final TextView tvPostDescription;
        private final ImageView ivImage;
        private final TextView tvTimestamp;
        private final ImageView ivPostProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivPostProfilePic = itemView.findViewById(R.id.ivPostProfilePic);
        }

        public void bind(Post post) {
            tvPostDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            tvTimestamp.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(post.getCreatedAt()));
            Glide.with(context).load(Objects.requireNonNull(post.getUser().getParseFile("profilePicture")).getUrl()).into(ivPostProfilePic);

            ivImage.setOnClickListener(e -> launchDetailFragment(post));
            tvPostDescription.setOnClickListener(e -> launchDetailFragment(post));
        }

        private void launchDetailFragment(Post post) {
            Fragment f = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("post", post);
            f.setArguments(bundle);

            AppCompatActivity activity = (AppCompatActivity) context;
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, f).addToBackStack(null).commit();
        }
    }
}
