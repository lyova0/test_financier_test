package com.example.myapplication;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class plus extends AppCompatActivity {

    EditText project_name, project_amount, full_presentation_of_the_project, project_user, your_number, my_type_ed;

    final String[] moneys = {"$", "֏", "₽", "€"};

    String i = "";

    String status = "none";
    String project_type = "none";
    String c = "close";
    String g = "close";

    public static int my_post_count, oll_post_count, save_post = 0;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private boolean isPosting = false;


    Button financier, entrepreneur, business, tourism, solar_energy, education, agriculture, ecology, new_post, my_type, my_type_ok;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //project type
        financier = findViewById(R.id.financier);
        entrepreneur = findViewById(R.id.entrepreneur);

        // edit texts
        project_name = findViewById(R.id.project_name);
        project_amount = findViewById(R.id.project_amount);
        full_presentation_of_the_project = findViewById(R.id.full_idea);
        project_user = findViewById(R.id.project_user);
        your_number = findViewById(R.id.my_number);
        my_type_ed = findViewById(R.id.my_type_ed);

        //project types
        business = findViewById(R.id.business_type);
        tourism = findViewById(R.id.tourism_type);
        solar_energy = findViewById(R.id.solar_energy_type);
        education = findViewById(R.id.education_type);
        agriculture = findViewById(R.id.agriculture_type);
        ecology = findViewById(R.id.ecology_type);
        my_type = findViewById(R.id.my_type);
        my_type_ok = findViewById(R.id.my_type_btn);


        //new post btn
        new_post = findViewById(R.id.new_post_btn);

        Button money = findViewById(R.id.money);

        my_type_ed.setVisibility(View.INVISIBLE);
        my_type_ok.setVisibility(View.INVISIBLE);

        financier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                financier.setBackgroundColor(getResources().getColor(R.color.blue));
                entrepreneur.setBackgroundColor(getResources().getColor(R.color.black));
                status = "Financier";
            }
        });

        entrepreneur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrepreneur.setBackgroundColor(getResources().getColor(R.color.blue));
                financier.setBackgroundColor(getResources().getColor(R.color.black));
                status = "Innovator";
            }
        });


        //set project type

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                project_type = " Business";
                business.setBackgroundColor(getResources().getColor(R.color.blue));
                tourism.setBackgroundColor(getResources().getColor(R.color.black));
                solar_energy.setBackgroundColor(getResources().getColor(R.color.black));
                education.setBackgroundColor(getResources().getColor(R.color.black));
                agriculture.setBackgroundColor(getResources().getColor(R.color.black));
                ecology.setBackgroundColor(getResources().getColor(R.color.black));
                my_type.setBackgroundColor(getResources().getColor(R.color.black));
            }
        });

        my_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (g.equals("close")) {
                    g = "open";
                    my_type.setBackgroundColor(getResources().getColor(R.color.blue));
                    tourism.setBackgroundColor(getResources().getColor(R.color.black));
                    solar_energy.setBackgroundColor(getResources().getColor(R.color.black));
                    education.setBackgroundColor(getResources().getColor(R.color.black));
                    agriculture.setBackgroundColor(getResources().getColor(R.color.black));
                    ecology.setBackgroundColor(getResources().getColor(R.color.black));
                    business.setBackgroundColor(getResources().getColor(R.color.black));
                    my_type_ed.setVisibility(View.VISIBLE);
                    my_type_ok.setVisibility(View.VISIBLE);

                    my_type_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!my_type_ed.getText().toString().isEmpty()){
                                project_type = my_type_ed.getText().toString();
                                g = "close";
                                my_type.setText(my_type_ed.getText().toString());
                                my_type_ed.setVisibility(View.INVISIBLE);
                                my_type_ok.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

        tourism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                project_type = " Tourism";
                tourism.setBackgroundColor(getResources().getColor(R.color.blue));
                business.setBackgroundColor(getResources().getColor(R.color.black));
                solar_energy.setBackgroundColor(getResources().getColor(R.color.black));
                education.setBackgroundColor(getResources().getColor(R.color.black));
                agriculture.setBackgroundColor(getResources().getColor(R.color.black));
                ecology.setBackgroundColor(getResources().getColor(R.color.black));
                my_type.setBackgroundColor(getResources().getColor(R.color.black));
            }
        });

        solar_energy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                project_type = "Solar Energy";
                solar_energy.setBackgroundColor(getResources().getColor(R.color.blue));
                tourism.setBackgroundColor(getResources().getColor(R.color.black));
                business.setBackgroundColor(getResources().getColor(R.color.black));
                education.setBackgroundColor(getResources().getColor(R.color.black));
                agriculture.setBackgroundColor(getResources().getColor(R.color.black));
                ecology.setBackgroundColor(getResources().getColor(R.color.black));
                my_type.setBackgroundColor(getResources().getColor(R.color.black));
            }
        });

        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                project_type = "Education";
                education.setBackgroundColor(getResources().getColor(R.color.blue));
                tourism.setBackgroundColor(getResources().getColor(R.color.black));
                solar_energy.setBackgroundColor(getResources().getColor(R.color.black));
                business.setBackgroundColor(getResources().getColor(R.color.black));
                agriculture.setBackgroundColor(getResources().getColor(R.color.black));
                ecology.setBackgroundColor(getResources().getColor(R.color.black));
                my_type.setBackgroundColor(getResources().getColor(R.color.black));
            }
        });

        agriculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                project_type = "Agriculture";
                agriculture.setBackgroundColor(getResources().getColor(R.color.blue));
                tourism.setBackgroundColor(getResources().getColor(R.color.black));
                solar_energy.setBackgroundColor(getResources().getColor(R.color.black));
                education.setBackgroundColor(getResources().getColor(R.color.black));
                business.setBackgroundColor(getResources().getColor(R.color.black));
                ecology.setBackgroundColor(getResources().getColor(R.color.black));
                my_type.setBackgroundColor(getResources().getColor(R.color.black));
            }
        });

        ecology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                project_type = "Ecology";
                ecology.setBackgroundColor(getResources().getColor(R.color.blue));
                tourism.setBackgroundColor(getResources().getColor(R.color.black));
                solar_energy.setBackgroundColor(getResources().getColor(R.color.black));
                education.setBackgroundColor(getResources().getColor(R.color.black));
                agriculture.setBackgroundColor(getResources().getColor(R.color.black));
                business.setBackgroundColor(getResources().getColor(R.color.black));
                my_type.setBackgroundColor(getResources().getColor(R.color.black));
            }
        });

        new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPosting) {
                    return;
                }
                isPosting = true;
                postQuestion();
            }
        });

        money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c.equals("close")) {
                    c = "open";
                    LinearLayout l = findViewById(R.id.mon);
                    View v = getLayoutInflater().inflate(R.layout.type_mony, null);
                    l.addView(v);


                    Button dl = v.findViewById(R.id.dollar);
                    Button eu = v.findViewById(R.id.euro);
                    Button dr = v.findViewById(R.id.dram);
                    Button rb = v.findViewById(R.id.rub);
                    Button btn = findViewById(R.id.money);

                    dl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            i = "$";
                            l.removeAllViews();
                            l.addView(btn);
                            btn.setText(i);
                            c = "close";
                        }
                    });
                    eu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            i = "€";
                            l.removeAllViews();
                            l.addView(btn);
                            btn.setText(i);
                            c = "close";
                        }
                    });

                    dr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            i = "֏";
                            l.removeAllViews();
                            l.addView(btn);
                            btn.setText(i);
                            c = "close";
                        }
                    });

                    rb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            i = "₽";
                            l.removeAllViews();
                            l.addView(btn);
                            btn.setText(i);
                            c = "close";
                        }
                    });
                }
            }
        });
    }

    private void postQuestion() {
        String title = project_name.getText().toString().trim();
        EditText description = full_presentation_of_the_project;
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String post_number = your_number.getText().toString();
        String post_amount = project_amount.getText().toString() + i;
        String post_status = status;
        String post_type = project_type;


        if (currentUser == null) {
            isPosting = false;
            return;
        }
        String userId = currentUser.getUid();
        if (title.isEmpty() || title.length() < 2) {
            project_name.setError("Title must be at least 2 characters long");
            isPosting = false;
            return;
        }

        if (i.isEmpty()){
            Toast.makeText(this, "Set money type",Toast.LENGTH_LONG).show();
            isPosting = false;
            return;
        }
        if (description.getText().toString().isEmpty() || description.length() < 5) {
            full_presentation_of_the_project.setError("Description must be at least 5 characters long");
            isPosting = false;
            return;
        }
        if (post_number.isEmpty() || post_number.length() != 9) {
            your_number.setError("Number must be at least 9 characters long");
            isPosting = false;
            return;
        }
        if (post_amount.isEmpty()) {
            project_amount.setError("Number must be at least 9 characters long");
            isPosting = false;
            return;
        }
        if (post_status.isEmpty()) {
            isPosting = false;
            return;
        }

        String creatorUserId = currentUser.getUid();
        String postId = firestore.collection("close").document().getId();
        Post post = new Post(title, description.getText().toString(), currentUser.getEmail(), creatorUserId, post_type, post_number, post_amount, post_status);
        post.setPostId(postId);
        firestore.collection("close").document(postId).set(post)
                .addOnSuccessListener(documentReference -> {
                    if (getContext() != null) {
                        Toast.makeText(plus.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(plus.this, "Admin reviewing your post", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(plus.this, Home.class));
                    }
                    isPosting = false;
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null) {
                        Toast.makeText(plus.this, "Failed to add post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    isPosting = false;
                });
    }
}