package com.example.Social.controller;

public class CommentDTO {
    private Long commentID;
    private String commentBody;
    private UserController.UserDTO commentCreator;

    // Getters and setters

    public Long getCommentID() {
        return commentID;
    }

    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public UserController.UserDTO getCommentCreator() {
        return commentCreator;
    }

    public void setCommentCreator(UserController.UserDTO commentCreator) {
        this.commentCreator = commentCreator;
    }
}
