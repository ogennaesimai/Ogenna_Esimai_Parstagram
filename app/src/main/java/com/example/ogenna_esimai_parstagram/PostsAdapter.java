package com.example.ogenna_esimai_parstagram;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.text.SimpleDateFormat;
import java.util.Date;
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
            Log.d("PostsAdapter", post.getKeyCreatedAt().toString());
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvCaption;
        private TextView tvCreatedAt;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }



        public void bind(Post post) {
            //bind the post data to the view elements
            tvCaption.setText(post.getCaption());
                Log.i("PostsAdapter_caption", post.getCaption());
            tvUsername.setText(post.getUser().getUsername());
                Log.i("PostsAdapter_getUser().getUsername()", post.getUser().getUsername());
            tvCreatedAt.setText(post.getKeyCreatedAt());
                //Log.i("PostsAdapter", post.getKeyCreatedAt().toString());
            //first check if post has a valid image
            ParseFile image = post.getImage();
            if (image != null) {
            Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }
        }
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        //items.clear();
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    //public void addAll(List<Tweet> list) {
    public void addAll(List<Post> list) {
        //items.addAll(list);
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
