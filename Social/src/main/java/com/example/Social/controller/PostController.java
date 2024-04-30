package com.example.Social.controller;

import com.example.Social.model.Comment;
import com.example.Social.model.Post;
import com.example.Social.model.User;
import com.example.Social.repositories.PostRepository;
import com.example.Social.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Social.model.ErrorResponse;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/post")
    public ResponseEntity<?> createPost(@RequestBody CommentCreator.PostRequest request) {
        // Check if userID is null
        if (request.getUserID() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse.toString());
        }

        // Proceed with the logic to create post
        Optional<User> optionalUser = userRepository.findById(request.getUserID());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Post post = new Post();
            post.setPostBody(request.getPostBody());
            post.setPostDate(new Date()); // Set the current date and time
            post.setUser(user);

            postRepository.save(post);

            return ResponseEntity.ok("Post created successfully");
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/post")
    public ResponseEntity<Object> getPostByID(@RequestParam Long postID) {
        Optional<Post> optionalPost = postRepository.findById(postID);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            // Create a list to hold comment details
            List<CommentResponse> comments = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                // Create a CommentResponse object for each comment
                CommentResponse commentResponse = new CommentResponse();
                commentResponse.setCommentID(comment.getCommentID());
                commentResponse.setCommentBody(comment.getCommentBody());

                // Create a CommentCreator object for the comment's user
                User user = comment.getUser();
                CommentCreator commentCreator = new CommentCreator();
                commentCreator.setUserID(user.getID());
                commentCreator.setName(user.getName());

                commentResponse.setCommentCreator(commentCreator);

                comments.add(commentResponse);
            }

            // Create the PostResponse object
            PostResponse postResponse = new PostResponse();
            postResponse.setPostID(post.getPostID());
            postResponse.setPostBody(post.getPostBody());
            postResponse.setDate(post.getPostDate());
            postResponse.setComments(comments);

            return ResponseEntity.ok(postResponse);
        } else {
            // If the post does not exist, return a 404 Not Found response
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PatchMapping("/post")
    public ResponseEntity<?> editPost(@RequestBody Map<String, Object> request) {
        // Extract postBody and postID from the request object
        String postBody = (String) request.get("postBody");
        Long postID = Long.parseLong(request.get("postID").toString());

        // Check if the post exists
        Optional<Post> optionalPost = postRepository.findById(postID);
        if (optionalPost.isPresent()) {
            // If the post exists, update its body
            Post post = optionalPost.get();
            post.setPostBody(postBody);
            postRepository.save(post); // Save the updated post
            return ResponseEntity.ok("Post edited successfully");
        } else {
            // If the post does not exist, return an error response
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }




    @DeleteMapping("/post")
    public ResponseEntity<?> deletePost(@RequestParam Long postID) {
        // Check if the post exists
        if (postRepository.existsById(postID)) {
            // If the post exists, delete it
            postRepository.deleteById(postID);
            return ResponseEntity.ok("Post deleted");
        } else {
            // If the post does not exist, return an error response
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "Post does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    // Class representing the response body for a post
    class PostResponse {
        private Long postID;
        private String postBody;
        private Date date;
        private List<CommentResponse> comments;

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

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public List<CommentResponse> getComments() {
            return comments;
        }

        public void setComments(List<CommentResponse> comments) {
            this.comments = comments;
        }
    }

    // Class representing the response body for a comment
    class CommentResponse {
        private Long commentID;
        private String commentBody;
        private CommentCreator commentCreator;

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

        public CommentCreator getCommentCreator() {
            return commentCreator;
        }

        public void setCommentCreator(CommentCreator commentCreator) {
            this.commentCreator = commentCreator;
        }
    }

    // Class representing the creator of a comment
    class CommentCreator {
        private Long userID;
        private String name;

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

        static class PostRequest {
            private String postBody;
            private Long userID;

            public String getPostBody() {
                return postBody;
            }

            public void setPostBody(String postBody) {
                this.postBody = postBody;
            }

            public Long getUserID() {
                return userID;
            }

            public void setUserID(Long userId) {
                this.userID = userId;
            }
        }
    }
}