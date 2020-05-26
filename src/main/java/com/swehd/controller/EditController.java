package com.swehd.controller;

import com.swehd.post.Post;
import com.swehd.post.PostDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class EditController{
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
    private String description;

    /**
     * Get selected post's id and description to edit.
     * @param pid the post's id.
     * @param description the post's description.
     */
    @FXML
    public void initPost(long pid, String description)  {
        this.pid = pid;
        this.description = description;
        editdescription.setText(description);
    }

    /**
     * Update post or delete it.
     * @param e button event.
     */
    @FXML
    public void editWindow(ActionEvent e) {
        if (e.getSource().equals(saveBtn)) {
            if (editdescription.getText().isEmpty()) {
                errorLabel.setText("Text is empty!");
            } else {
                String editDescription = editdescription.getText().trim();

                postDao = PostDao.getInstance();
                Optional<Post> post = postDao.findPost(this.pid);
                Post editpost = post.get();
                editpost.setDescription(editDescription);
                postDao.update(editpost);
                Stage stage = (Stage) saveBtn.getScene().getWindow();
                stage.close();
                log.info("User edited the post.");

            }
        } else if (e.getSource().equals(backBtn)) {
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.close();
        } else if (e.getSource().equals(deleteBtn)) {
            postDao = PostDao.getInstance();
            Optional<Post> post = postDao.findPost(this.pid);
            postDao.remove(post.get());
            Stage stage = (Stage) deleteBtn.getScene().getWindow();
            stage.close();
            log.info("User deleted the post.");
        }

    }
}
