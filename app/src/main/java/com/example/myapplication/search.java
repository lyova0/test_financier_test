package com.example.myapplication;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplication.Home.SHARED_PREFS;

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


public class search extends AppCompatActivity {

    ImageButton home, profile, plus;

    private FirebaseFirestore db;

    String status = "NONE";
    public final int[] b = {0};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        Button financier = findViewById(R.id.project_financier);
        Button innovator = findViewById(R.id.project_innovator);
        db = FirebaseFirestore.getInstance();

        home = findViewById(R.id.home_btn);
        profile = findViewById(R.id.profile_btn);
        plus = findViewById(R.id.plus_btn);

        financier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "Financier";
                getPostsAndAddToParentLayout();
            }
        });

        innovator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "Innovator";
                getPostsAndAddToParentLayout();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(search.this, Home.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(search.this, profile.class));
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(search.this, plus.class));
            }
        });

    }

    private void getPostsAndAddToParentLayout() {
        LinearLayout allPostsLayout = findViewById(R.id.oll_posts);
        allPostsLayout.removeAllViews();
        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allPostsLayout.removeAllViews();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        if (post.post_status.equals(status)) {
                            addPostToParentLayout(allPostsLayout, post);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(search.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());
    }

    private void addPostToParentLayout(LinearLayout parentLayout, Post post) {
        parentLayout.addView(createPostLayout(post.getTitle(), post.getDescription(), post.getUserId(), post.post_status, post.post_amount, post.getStatus(), post));
    }

    private LinearLayout createPostLayout(String title, String description, String userId, String postType, String postAmount, String postStatus, Post post) {
        // Inflate the layout for the post item
        View view = getLayoutInflater().inflate(R.layout.item_post, null);

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
                .addOnFailureListener(e -> Toast.makeText(search.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());


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
                        .addOnFailureListener(e -> Toast.makeText(search.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());

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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10, 50, 0, 0); // left, top, right, bottom
        LinearLayout outerLinearLayout = new LinearLayout(search.this);
        outerLinearLayout.setLayoutParams(layoutParams);
        outerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        outerLinearLayout.setPadding(12, 12, 12, 12); // left, top, right, bottom

        // Add the inflated view to the parent LinearLayout
        outerLinearLayout.addView(view);

        return outerLinearLayout;
    }

    private void signOutUser() {
        Intent mainActivity = new Intent(search.this, MainActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "false");
        editor.apply();
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();
    }

    public void sendEmail(String subject, String content, String to_email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to_email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose email client: "));
    }
}