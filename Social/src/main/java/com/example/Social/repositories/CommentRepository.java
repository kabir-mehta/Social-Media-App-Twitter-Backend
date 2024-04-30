package com.example.Social.repositories;

import com.example.Social.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //Comment getCommentByID();
    // You can add custom query methods if needed
}
