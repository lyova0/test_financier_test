package com.example.myapplication;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class profile extends AppCompatActivity {

    String status_btn = "hide";
    String status_save_btn = "hide";
    TextView user, post_count;

    ImageButton home, plus_btn;

    private FirebaseFirestore db;

    ImageButton search_btn;
    private FirebaseAuth auth;

    public final int[] b = {0};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        Button save_btn = findViewById(R.id.save_btn);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        home = findViewById(R.id.home_btn);
        plus_btn = findViewById(R.id.plus_btn);
        search_btn = findViewById(R.id.search_btn);

        user = findViewById(R.id.username);

        user.setText(currentUser.getEmail());

        getCount();

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        if (!currentUser.getEmail().equals("ad.financier.mobile@gmail.com")) {
                            getPostsAndAddToParentLayout();
                        } else {
                            getPostsAndAddToParentLayoutf();
                        }

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        if (!currentUser.getEmail().equals("ad.financier.mobile@gmail.com")) {
            getPostsAndAddToParentLayout();
        } else {
            getPostsAndAddToParentLayoutf();
        }

            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(profile.this, save_posts.class));
                }
            });

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(profile.this, Home.class));
                }
            });


            plus_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(profile.this, plus.class));
                }
            });

            search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(profile.this, search.class));
                }
            });

        }

    private void getPostsAndAddToParentLayoutf() {
        LinearLayout allPostsLayout = findViewById(R.id.oll_posts);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        db.collection("close")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allPostsLayout.removeAllViews();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                            addPostToParentLayout(allPostsLayout, post);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(profile.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());
    }

    private void getCount() {
        final int[] i = {0};
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        TextView postCountTextView = findViewById(R.id.my_post_count);

        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        if (post.getUserId().equals(currentUser.getEmail())) {
                            i[0]++;
                        }
                    }
                    runOnUiThread(() -> postCountTextView.setText(String.valueOf(i[0])));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(profile.this, "Failed to retrieve posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", "Failed to retrieve posts", e);
                });
    }

    private void getPostsAndAddToParentLayout() {
        LinearLayout allPostsLayout = findViewById(R.id.oll_posts);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allPostsLayout.removeAllViews();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        if (post.getUserId().equals(currentUser.getEmail())) {
                            addPostToParentLayout(allPostsLayout, post);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(profile.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());
    }

    private void addPostToParentLayout(LinearLayout parentLayout, Post post) {
        parentLayout.addView(createPostLayout(post.getTitle(), post.getDescription(), post.getUserId(), post.post_status, post.post_amount, post.getStatus(), post));


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser.getEmail().equals("ad.financier.mobile@gmail.com")) {
            Button btn_o = new Button(this);
            btn_o.setText("open");
            Button btn_c = new Button(this);
            btn_c.setText("close");
            parentLayout.addView(btn_o);
            parentLayout.addView(btn_c);
            btn_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDocumentf(post.getUserId(), post.getPostId());
                }
            });

            btn_o.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    deleteDocumentf(post.getUserId(), post.getPostId());

                    String creatorUserId = currentUser.getUid();
                    String postId = firestore.collection("posts").document().getId();
                    Post post1 = post;
                    post1.post_ = "open";
                    post1.setPostId(postId);
                    firestore.collection("posts").document(postId).set(post1)
                            .addOnSuccessListener(documentReference -> {
                                if (getContext() != null) {
                                }
                            })
                            .addOnFailureListener(e -> {
                                if (getContext() != null) {
                                }
                            });
                }
            });
        } else {
            Button btn_o = new Button(this);
            btn_o.setText("delete");
            parentLayout.addView(btn_o);
            btn_o.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteDocument("posts", post.getPostId());
                }
            });
        }
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
                .addOnFailureListener(e -> Toast.makeText(profile.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());


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
                        .addOnFailureListener(e -> Toast.makeText(profile.this, "Failed to retrieve posts", Toast.LENGTH_SHORT).show());

            }

        });

        // Create a new LinearLayout to hold the inflated view
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10, 50, 0, 0); // left, top, right, bottom
        LinearLayout outerLinearLayout = new LinearLayout(profile.this);
        outerLinearLayout.setLayoutParams(layoutParams);
        outerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        outerLinearLayout.setPadding(12, 12, 12, 12); // left, top, right, bottom

        // Add the inflated view to the parent LinearLayout
        outerLinearLayout.addView(view);

        return outerLinearLayout;
    }

    public void deleteDocument(String email, String postId) {
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
    public void deleteDocumentf(String email, String postId) {
        DocumentReference docRef = db.collection("close").document(postId);

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

    public void sendEmail(String subject, String content, String to_email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to_email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose email client: "));
    }

}