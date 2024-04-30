package com.example.Social.controller;

import com.example.Social.model.Comment;
import com.example.Social.model.Post;
import com.example.Social.model.User;
import com.example.Social.repositories.UserRepository;
import com.example.Social.repositories.PostRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // DTO class for returning user details without password
    static class UserDTO {
        private String name;
        private Long id;
        private String email;

        // Constructor, getters, and setters
        public UserDTO(String name, Long id, String email) {
            this.name = name;
            this.id = id;
            this.email = email;
        }

        public UserDTO() {
            
        }

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getID() {
            return id;
        }

        public void setID(Long id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestParam("userID") Long userId) {
        // Find the user by userID
        User user = userRepository.findById(userId).orElse(null);

        // If user does not exist, return relevant error
        if (user == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Error", "User does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        // Map user entity to UserDTO to exclude password
        UserDTO userDTO = new UserDTO(user.getName(), user.getID(), user.getEmail());

        // If user exists, return user details
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        // Find all users from the repository
        List<User> users = userRepository.findAll();

        // Map users to UserDTO objects
        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.getName(), user.getID(), user.getEmail()))
                .collect(Collectors.toList());

        // Return the list of UserDTOs in the response body
        return ResponseEntity.ok(userDTOs);
    }

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public ResponseEntity<List<Map<String, Object>>> getUserFeed() {
        List<Post> posts = postRepository.findAllByOrderByPostDateDesc();
        List<Map<String, Object>> postDTOs = new ArrayList<>();

        for (Post post : posts) {
            Map<String, Object> postDTO = new HashMap<>();
            postDTO.put("postID", post.getPostID());
            postDTO.put("postBody", post.getPostBody());
            postDTO.put("date", post.getPostDate());

            List<Map<String, Object>> commentDTOs = new ArrayList<>();
            for (Comment comment : post.getComments()) {
                Map<String, Object> commentDTO = new HashMap<>();
                commentDTO.put("commentID", comment.getCommentID());
                commentDTO.put("commentBody", comment.getCommentBody());

                Map<String, Object> commentCreatorDTO = new HashMap<>();
                User user = comment.getUser();
                commentCreatorDTO.put("userID", user.getID());
                commentCreatorDTO.put("name", user.getName());
                commentDTO.put("commentCreator", commentCreatorDTO);

                commentDTOs.add(commentDTO);
            }

            postDTO.put("comments", commentDTOs);
            postDTOs.add(postDTO);
        }

        return ResponseEntity.ok(postDTOs);
    }
}
