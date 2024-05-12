package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Home extends AppCompatActivity {

    private ImageButton search;
    private FirebaseFirestore db;
    public static final String SHARED_PREFS = "sharedPrefs";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        ImageButton btn = findViewById(R.id.profile_btn);
        ImageButton btn1 = findViewById(R.id.plus_btn);
        search = findViewById(R.id.search_btn);
        Button logout = findViewById(R.id.id);

        getPostsAndAddToParentLayout();

        LinearLayout send = findViewById(R.id.send_layout);
        send.setVisibility(View.INVISIBLE);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        getPostsAndAddToParentLayout();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                signOutUser();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, profile.class));
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, plus.class));
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, search.class));
            }
        });
    }

    private void getPostsAndAddToParentLayout() {
        LinearLayout allPostsLayout = findViewById(R.id.fragment_home);
        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allPostsLayout.removeAllViews();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        addPostToParentLayout(allPostsLayout, post);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(Home.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());
    }

    private void addPostToParentLayout(LinearLayout parentLayout, Post post) {
        parentLayout.addView(createPostLayout(post.getTitle(), post.getDescription(), post.getUserId(), post.post_status, post.post_amount, post.getStatus(), post));
    }

    private LinearLayout createPostLayout(String title, String description, String userId, String postType, String postAmount, String postStatus, Post post) {
        // Inflate the layout for the post item
        View view = getLayoutInflater().inflate(R.layout.item_post, null);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                920, // Width (match parent in this case)
                ViewGroup.LayoutParams.WRAP_CONTENT // Height in pixels, you can change this value as needed
        );

        // Find views inside the inflated layout
        TextView titleTextView = view.findViewById(R.id.project_name);
        TextView descriptionTextView = view.findViewById(R.id.project_presentation);
        TextView postTypeTextView = view.findViewById(R.id.project_type);
        TextView userIdTextView = view.findViewById(R.id.project_user);
        TextView postStatusTextView = view.findViewById(R.id.project_status_t);
        TextView postAmountTextView = view.findViewById(R.id.project_amount);
        Button moreButton = view.findViewById(R.id.more_btn);
        ImageView save_btn = view.findViewById(R.id.save_btn);

        // Set data to the views
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        postTypeTextView.setText(post.getStatus());
        userIdTextView.setText(userId);
        postStatusTextView.setText(post.post_type);
        postAmountTextView.setText(postAmount);

        if (post.getStatus().equals("Financier")){
            view.setBackgroundResource(R.drawable.finiancier_post_orange);
            view.setLayoutParams(layoutParams);
        } else {
            view.setBackgroundResource(R.drawable.financier_post_green);
            view.setLayoutParams(layoutParams);
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String postId = firestore.collection(currentUser.getEmail()).document().getId();
        Post post1 = post;


        db.collection(currentUser.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post2 = documentSnapshot.toObject(Post.class);
                        if (post2.getTitle().equals(post1.getTitle()) && post2.getDescription().equals(post1.getDescription()) && post2.getUserId().equals(post1.getUserId())){
                            save_btn.setBackgroundResource(R.drawable.save);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(Home.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());


        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout send = findViewById(R.id.send_layout);
                send.setVisibility(View.VISIBLE);
                EditText text = findViewById(R.id.send_edit);
                Button btn = findViewById(R.id.send_btn);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendEmail("Financier", text.getText().toString(), post.getUserId());
                        text.setText("");
                        send.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                String postId = firestore.collection(currentUser.getEmail()).document().getId();
                Post post1 = post;

                final Post[] post2 = new Post[1];
                final int[] count = {0};
                db.collection(currentUser.getEmail())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Post post = documentSnapshot.toObject(Post.class);
                                if (post.getTitle().equals(post1.getTitle()) && post.getDescription().equals(post1.getDescription()) && post.getUserId().equals(post1.getUserId())){
                                    count[0]++;
                                    post2[0] = post;
                                    break;
                                }
                            }
                            if (count[0] == 0){
                                firestore.collection(currentUser.getEmail()).document(post.getPostId()).set(post)
                                        .addOnSuccessListener(documentReference -> {
                                        })
                                        .addOnFailureListener(e -> {
                                        });
                                save_btn.setBackgroundResource(R.drawable.save);
                            } else {
                                save_btn.setBackgroundResource(R.drawable.d_save);
                                deleteDocument(currentUser.getEmail(), post1.getPostId());
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(Home.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());

            }

            private void deleteDocument(String email, String postId) {
                DocumentReference docRef = db.collection(email).document(postId);

                // Delete the document
                docRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Document successfully deleted
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        });

        // Create a new LinearLayout to hold the inflated view
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams1.setMargins(10, 50, 0, 0); // left, top, right, bottom
        LinearLayout outerLinearLayout = new LinearLayout(Home.this);
        outerLinearLayout.setLayoutParams(layoutParams1);
        outerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        outerLinearLayout.setPadding(12, 12, 12, 12); // left, top, right, bottom

        // Add the inflated view to the parent LinearLayout
        outerLinearLayout.addView(view);

        return outerLinearLayout;
    }


    private void signOutUser() {
        Intent mainActivity = new Intent(Home.this, MainActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "false");
        editor.apply();
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();
    }

    public void sendEmail(String subject, String content, String to_email){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to_email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject + "from " + currentUser.getEmail());
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose email client: "));
    }
}
