package com.example.Social.controller;

import java.util.Date;
import java.util.List;

public class PostDTO {
    private Long postID;
    private String postBody;
    private Date postDate;
    private List<CommentDTO> comments;

    // Getters and setters

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostdate(Date postDate) {
        this.postDate = postDate;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}
