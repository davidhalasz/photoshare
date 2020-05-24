package com.swehd.controller;

import com.swehd.post.Post;
import com.swehd.post.PostDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.Optional;


public class DialogController {
    private PostDao postDao;

    @FXML
    private TextArea editdescription;

    private long pid;
    private String Description;

    public void initPost(long pid, String description) {
        this.pid = pid;
        this.Description= description;
        editdescription.setText(description);
    }

    @FXML
    public void updatePost() {
        String editDescription = editdescription.getText().trim();

        postDao = PostDao.getInstance();
        Optional<Post> post = postDao.findPost(this.pid);
        Post editpost = post.get();
        editpost.setDescription(editDescription);
        postDao.update(editpost);
    }
}

