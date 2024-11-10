package com.instagramclone.memories.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.instagramclone.memories.PostsAdapter;
import com.instagramclone.memories.R;
import com.instagramclone.memories.models.Post;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    // Required empty public constructor
    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Steps to use recycler view:
        //1. create layout for a row of the view: item_post.xml
        RecyclerView rvHome = view.findViewById(R.id.rvHome);
        //2. the data source: Post model
        allPosts = new ArrayList<>();
        //3. create adapter: PostsAdapter
        adapter = new PostsAdapter(getContext(), allPosts);
        //4. set the adapter on the recycler view
        rvHome.setAdapter(adapter);
        //5. set the layout manager on the recycler view
        rvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
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
}