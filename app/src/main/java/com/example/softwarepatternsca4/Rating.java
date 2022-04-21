package com.example.softwarepatternsca4;

public class Rating {

    private String itemId;
    private String email;
    private String comment;
    private String rating;

    public Rating(){}

    public Rating(String itemId, String email, String comment, String rating){
        this.itemId = itemId;
        this.email = email;
        this.comment = comment;
        this.rating = rating;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
