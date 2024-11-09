package com.instagramclone.memories;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.instagramclone.memories.models.Post;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

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

        private TextView tvUsername;
        private TextView tvPostDescription;
        private ImageView ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPostDescription = itemView.findViewById(R.id.tvPostDescription);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            Log.i("PostsAdapter", "created viewholder for posts adapter");
        }

        public void bind(Post post) {
            tvPostDescription.setText(post.getDescription());
            Log.i("PostsAdapter", "binded description");
            tvUsername.setText(post.getUser().getUsername());
            if(post.getImage() != null)
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
        }
    }
}
