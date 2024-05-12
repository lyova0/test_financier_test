package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter {

    private List<Post> postList;
    private String currentUserId;
    private OnPostOptionsClickListener optionsClickListener;

    public PostAdapter(List<Post> postList, String currentUserId) {
        this.postList = postList;
        this.currentUserId = currentUserId;
        this.optionsClickListener = optionsClickListener;
    }

    @NonNull
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bind(post);
    }

    public int getItemCount() {
        return postList.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView post_type;
        private TextView post_email;
        private TextView post_status;
        private TextView post_presentation;
        private TextView post_amount;
        private Button more_button, save_btn;
        private TextView descriptionTextView;
        private ImageView threeDots;
        public String post_;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.project_name);
            descriptionTextView = itemView.findViewById(R.id.project_presentation);
            post_type = itemView.findViewById(R.id.project_type);
            post_email = itemView.findViewById(R.id.project_user);
            post_status = itemView.findViewById(R.id.project_status_t);
            post_presentation = itemView.findViewById(R.id.project_presentation);
            post_amount = itemView.findViewById(R.id.project_amount);
            more_button = itemView.findViewById(R.id.more_btn);
            save_btn = itemView.findViewById(R.id.save_btn);

            threeDots.setOnClickListener(v -> {
                if (optionsClickListener != null) {
                    optionsClickListener.onPostOptionsClicked(v, getAdapterPosition(), postList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            post_type.setText(post.get_type());
            post_amount.setText(post.get_amount());
            post_status.setText(post.getStatus());
            threeDots.setVisibility(View.VISIBLE);
            post_ = "close";
        }
    }


    public interface OnPostOptionsClickListener {
        void onPostOptionsClicked(View view, int position, Post post);
    }

}
