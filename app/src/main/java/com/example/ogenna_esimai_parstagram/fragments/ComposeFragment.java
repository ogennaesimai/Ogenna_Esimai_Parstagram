package com.example.ogenna_esimai_parstagram.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.ogenna_esimai_parstagram.LoginActivity;
import com.example.ogenna_esimai_parstagram.MainActivity;
import com.example.ogenna_esimai_parstagram.Post;
import com.example.ogenna_esimai_parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

//import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;
import static android.app.Activity.RESULT_OK;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String TAG = "ComposeFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;


    //private Toolbar abActionbar;
    private Toolbar tbToolbar;
    //private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private EditText etCaption;
    private Button btnSubmit;
    private File photoFile;
    private String photoFileName = "photo.jpg";
    //private Button btnLogOut;

    public ComposeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComposeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeFragment newInstance(String param1, String param2) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    //https://stackoverflow.com/questions/38189198/how-to-use-setsupportactionbar-in-fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
        //return inflater.inflate(R.layout.tabbar_layout, container, false);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //abActionbar = findViewById(R.id.abActionbar);
        tbToolbar = view.findViewById(R.id.tbToolbar);

        //etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        etCaption = view.findViewById(R.id.etCaption);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        //btnLogOut = view.findViewById(R.id.btnLogOut);

        //setSupportActionBar(tbToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tbToolbar);

        //setSupportActionBar(abActionbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(null);

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        //queryPosts();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String description = etDescription.getText().toString();
                String caption = etCaption.getText().toString();
                //if (description.isEmpty()) {
                if (caption.isEmpty()) {
                    //Toast.makeText(MainActivity.this, "Description cannot be empty", Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "Caption cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image!", Toast.LENGTH_LONG).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                //savePost(description, currentUser, photoFile);
                savePost(caption, currentUser, photoFile);
            }
        });

        /*btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent startLoginActivityIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(startLoginActivityIntent);
            }
        });*/

        /*etCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCaption.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });*/
    }

    // Menu icons are inflated just as they were with actionbar

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    //this method will be called when the child app returns to the parent app which is the Ogenna_Esimai_Parstagram app
    //@SuppressLint("RestrictedApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) { //Uri = Uniform Resource Identifier
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    //private void savePost(String description, ParseUser currentUser, File photoFile) {
    private void savePost(String caption, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        //post.setDescription(description);
        post.setCaption(caption);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, "Post save was successful!");
                Toast.makeText(getContext(), "Post save was successful!", Toast.LENGTH_LONG).show();

                //to make sure they don't save the same post twice
                //etDescription.setText("");
                etCaption.setText("");
                ivPostImage.setImageResource(0); //empty resource id
            }
        });
    }

    //public static void hideKeyboard(Activity activity) {
    //public void hideKeyboard(Activity activity) {
        /*InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View v = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (v == null) {
            //v = new View(activity);
            v = findViewById(R.id.etCaption);
        }
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        v.clearFocus();*/

        /*InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //to get what is definitely the currently focused view,
        View focused = getCurrentFocus();
        focused.clearFocus();
        //inputMethodManager.hideSoftInputFromWindow(focused.getWindowToken(), 0)
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);*/

    /*protected void hideSoftKeyboard(EditText etCaption) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etCaption.getWindowToken(), 0);

    }*/

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
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
            }
        });
    }

}

