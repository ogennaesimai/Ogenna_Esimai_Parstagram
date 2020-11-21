package com.example.ogenna_esimai_parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ogenna_esimai_parstagram.LoginActivity;
import com.example.ogenna_esimai_parstagram.Post;
import com.example.ogenna_esimai_parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    //private Toolbar abActionbar;
    private Toolbar tbToolbar;
    private Button btnLogOut;


    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
        //return inflater.inflate(R.layout.tabbar_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //abActionbar = findViewById(R.id.abActionbar);
        //tbToolbar = view.findViewById(R.id.tbToolbar);

        btnLogOut = view.findViewById(R.id.btnLogOutProfileInNestedLinearLayout);

        //setSupportActionBar(tbToolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(tbToolbar);

        //setSupportActionBar(abActionbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(null);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent startLoginActivityIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(startLoginActivityIntent);
            }
        });

        /*etCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCaption.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });*/
    }

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    //Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                    Log.i(TAG, "Post: " + post.getCaption() + ", username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
