package com.example.myapplication;

public class Post {
    private String postId;
    private String title;
    private String description;
    private String userId;
    private String creatorUserId;
    private String post_name;
    public String post_type;
    private String post_presentation;
    public String post_amount;
    public String post_status;
    private String post_number;

    public String post_;


    public Post() {
    }


    public Post(String title, String description, String userId, String creatorUserId, String post_type, String post_number, String post_amount, String post_status) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.post_status = post_status;
        this.creatorUserId = creatorUserId;
        this.post_type = post_type;
        this.post_amount = post_amount;
        this.post_number = post_number;
        this.post_ = "close";
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }
    public String get_post() {
        return post_;
    }

    public String getpost_type() {
        return post_type;
    }


    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return post_status;
    }
    public String get_type() {
        return post_type;
    }

    public String get_amount() {
        return post_amount;
    }

    public String get_number() {
        return post_number;
    }

    public String getUserId() {
        return userId;
    }

}
