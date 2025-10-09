package com.zuehlke.securesoftwaredevelopment.controller;

import com.zuehlke.securesoftwaredevelopment.domain.Comment;
import com.zuehlke.securesoftwaredevelopment.domain.Person;
import com.zuehlke.securesoftwaredevelopment.domain.User;
import com.zuehlke.securesoftwaredevelopment.repository.CommentRepository;
import com.zuehlke.securesoftwaredevelopment.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CommentController {
    private static final Logger LOG = LoggerFactory.getLogger(CommentController.class);

    private CommentRepository commentRepository;
    private PersonRepository personRepository;

    public CommentController(CommentRepository commentRepository, PersonRepository personRepository) {
        this.commentRepository = commentRepository;
        this.personRepository = personRepository;
    }

    @PostMapping(value = "/comments", consumes = "application/json")
    public ResponseEntity<Void> createComment(@RequestBody Comment comment, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        comment.setUserId(user.getId());
        commentRepository.create(comment);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/comments/edit", consumes = "application/json")
    public ResponseEntity<Void> editComment(@RequestBody Comment updatedComment, Authentication authentication) {
        Comment existing = commentRepository.findById(updatedComment.getId());

        if (existing == null || !existing.getUserId().equals(getCurrentUserId(authentication))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentRepository.update(updatedComment);

        return ResponseEntity.noContent().build();
    }

    private Integer getCurrentUserId(Authentication authentication) {
        String username = authentication.getName();
        Person person = personRepository.findByUsername(username);

        return person.getId();
    }

}
