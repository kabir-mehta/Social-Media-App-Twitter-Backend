package com.example.Social.controller;

import com.example.Social.model.Comment;
import com.example.Social.model.Post;
import com.example.Social.model.User;
import com.example.Social.repositories.CommentRepository;
import com.example.Social.repositories.PostRepository;
import com.example.Social.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request) {
        // Extract comment details from the request
        String commentBody = request.getCommentBody();
        Long postId = request.getPostID();
        Long userId = request.getUserID();

        // Check if the user exists
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        // Check if the post exists
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        // Create a new comment
        Comment comment = new Comment();
        comment.setCommentBody(commentBody);
        comment.setPost(optionalPost.get());
        comment.setUser(optionalUser.get());

        // Save the comment
        commentRepository.save(comment);

        return ResponseEntity.ok("Comment created successfully");
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteCommentById(@RequestParam("commentID") Long commentID) {
        if (commentRepository.existsById(commentID)) {
            commentRepository.deleteById(commentID);
            return ResponseEntity.ok("Comment deleted");
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PatchMapping("/comment")
    public ResponseEntity<?> editComment(@RequestBody EditCommentRequest editCommentRequest) {
        Long commentId = editCommentRequest.getCommentID();
        String newCommentBody = editCommentRequest.getCommentBody();

        Comment existingComment = commentRepository.findById(commentId).orElse(null);
        if (existingComment != null) {
            existingComment.setCommentBody(newCommentBody);
            commentRepository.save(existingComment);
            return ResponseEntity.ok("Comment edited successfully");
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/comment")
    public ResponseEntity<Object> getCommentByID(@RequestParam("commentID") Long commentID) {
        Comment comment = commentRepository.findById(commentID).orElse(null);
        if (comment != null) {
            // Construct response object
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setCommentID(comment.getCommentID());
            commentResponse.setCommentBody(comment.getCommentBody());
            commentResponse.setCommentCreator(comment.getUser());

            return ResponseEntity.ok(commentResponse);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Comment does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // Inner class representing the response body for retrieving a comment
    static class CommentResponse {
        private Long commentID;
        private String commentBody;
        private UserResponse commentCreator;

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

        public UserResponse getCommentCreator() {
            return commentCreator;
        }

        public void setCommentCreator(User commentCreator) {
            this.commentCreator = new UserResponse(commentCreator.getID(), commentCreator.getName());
        }
    }

    // Inner class representing the response body for the user
    static class UserResponse {
        private Long userID;
        private String name;

        // Constructor
        public UserResponse(Long userID, String name) {
            this.userID = userID;
            this.name = name;
        }

        // Getters and setters
        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // Inner class representing the request body for editing a comment
    static class EditCommentRequest {
        private Long commentID;
        private String commentBody;

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
    }

    static class CreateCommentRequest {
        private String commentBody;
        private Long postID;
        private Long userID;

        // Constructors, getters, and setters

        public CreateCommentRequest(String commentBody, Long postId, Long userId) {
            this.commentBody = commentBody;
            this.postID = postId;
            this.userID = userId;
        }

        public String getCommentBody() {
            return commentBody;
        }

        public void setCommentBody(String commentBody) {
            this.commentBody = commentBody;
        }

        public Long getPostID() {
            return postID;
        }

        public void setPostID(Long postID) {
            this.postID = postID;
        }

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }
    }
}
