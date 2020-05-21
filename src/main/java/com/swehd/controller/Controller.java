package com.swehd.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class Controller {
    /*
        private List<PhotoSharing> photoSharings;

        @FXML
        private ListView<PhotoSharing> postWall;


        public void initialize() {
            photoSharings = new ArrayList<PhotoSharing>();
            postWall.getItems().setAll(photoSharings);
            postWall.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }

        @FXML
        private TextArea photoDesc;

        @FXML
        private Label deadlineLabel;


        @FXML
        public void handleClickPost() {
            PhotoSharing post = postWall.getSelectionModel().getSelectedItem();
            photoDesc.setText(post.getDescription());
            deadlineLabel.setText(post.getDeadLine().toString());
        }

    */
    @FXML
    private Button choosePicBtn;
    @FXML
    private Button sendPostBtn;
    @FXML
    private TextArea postDesc;

    @FXML
    private void postForm(ActionEvent e) throws IOException {
        if (e.getSource().equals(choosePicBtn)){
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                System.out.println(selectedFile.getName());
            }

        } else if (e.getSource().equals(sendPostBtn)) {
            if(postDesc.getText() != null) {
                System.out.println(postDesc.getText().trim());
            } else {
                System.out.println("Empty");
            }
        }
    }


}
