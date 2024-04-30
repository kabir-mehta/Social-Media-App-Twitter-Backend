package com.example.Social.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentID;

    @Column(nullable=false)
    private String commentBody;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY) // Lazily fetch the associated user
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) // Lazily fetch the associated comment creator
    @JoinColumn(name = "comment_creator_id")
    @JsonIgnore
    private User commentCreator;

    // Constructors, getters, and setters

    public Comment(Long commentID, String commentBody, Date date, User user, Post post, User commentCreator) {
        this.commentID = commentID;
        this.commentBody = commentBody;
        this.date = date;
        this.user = user;
        this.post = post;
        this.commentCreator = commentCreator;
    }

    public Comment() {

    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getCommentCreator() {
        return commentCreator;
    }

    public void setCommentCreator(User commentCreator) {
        this.commentCreator = commentCreator;
    }
}
