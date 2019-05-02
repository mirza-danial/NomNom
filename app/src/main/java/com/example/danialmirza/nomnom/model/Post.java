package com.example.danialmirza.nomnom.model;

import java.util.ArrayList;

public class Post {
    private String post_id;
    private String rest_id;
    private String user_id;
    private String photo_path;
    private Float rating;
    private String review;
    private ArrayList<String> upvoters;
    private ArrayList<String> downvoters;
    private Integer upvotes;
    private Integer downvotes;

    public Post() {
    }

    public Post(String post_id, String rest_id, String user_id, String photo_path, Float rating, String review, ArrayList<String> upvoters, ArrayList<String> downvoters, Integer upvotes, Integer downvotes) {
        this.post_id = post_id;
        this.rest_id = rest_id;
        this.user_id = user_id;
        this.photo_path = photo_path;
        this.rating = rating;
        this.review = review;
        this.upvoters = upvoters;
        this.downvoters = downvoters;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id='" + post_id + '\'' +
                ", rest_id='" + rest_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", photo_path='" + photo_path + '\'' +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                ", upvoters=" + upvoters +
                ", downvoters=" + downvoters +
                ", upvotes=" + upvotes +
                ", downvotes=" + downvotes +
                '}';
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getRest_id() {
        return rest_id;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public ArrayList<String> getUpvoters() {
        return upvoters;
    }

    public void setUpvoters(ArrayList<String> upvoters) {
        this.upvoters = upvoters;
    }

    public ArrayList<String> getDownvoters() {
        return downvoters;
    }

    public void setDownvoters(ArrayList<String> downvoters) {
        this.downvoters = downvoters;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Integer getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(Integer downvotes) {
        this.downvotes = downvotes;
    }
}
