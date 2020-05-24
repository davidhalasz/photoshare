package com.swehd.controller;

import com.swehd.App;
import com.swehd.post.Post;
import com.swehd.post.PostDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Optional;

public class EditController {
    private PostDao postDao;

    @FXML
    private TextArea editdescription;
    @FXML
    private Button saveBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Label errorLabel;

    private long pid;
    private String Description;

    @FXML
    public void initPost(long pid, String description)  {
        this.pid = pid;
        this.Description= description;
        editdescription.setText(description);
    }


    public void editWindow(ActionEvent e) throws IOException{
        if (e.getSource().equals(saveBtn)){
            if (editdescription.getText().isEmpty()) {
                errorLabel.setText("Text is empty!");
            } else {
                String editDescription = editdescription.getText().trim();

                postDao = PostDao.getInstance();
                Optional<Post> post = postDao.findPost(this.pid);
                Post editpost = post.get();
                editpost.setDescription(editDescription);
                postDao.update(editpost);
            }
        } else if (e.getSource().equals(backBtn)) {
            App.setRoot("mainwindow");
        } else if (e.getSource().equals(deleteBtn)) {
            postDao = PostDao.getInstance();
            Optional<Post> post = postDao.findPost(this.pid);
            postDao.remove(post);
        }

    }
}
