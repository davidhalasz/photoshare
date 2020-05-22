package com.swehd.controller;

import com.swehd.post.Post;
import com.swehd.post.PostDao;
import com.swehd.user.User;
import com.swehd.user.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;

public class Controller{
    public long id;
    private PostDao postDao;
    private UserDao userDao;

    /**
     * Get Current User's ID
     * @param id
     */
    public void initdata(Long id) {
        this.id = id;
        System.out.println("Current user ID is: " + this.id);
    }




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

    private String filename;

    public void initFile(String selectedFile) {
        this.filename = selectedFile;

    }

    /**
     * A Post builder. Get user's id from database. Set description and picture's name.
     * @return
     */
    private Post createPost() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("post-mysql");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();


        Post post = Post.builder()
                .description(postDesc.getText().trim())
                .picture(filename)
                .user(em.find(User.class, this.id))
                .build();

        em.close();
        emf.close();
        return post;
    }

    /**
     * Get selected picture's filename and description.
     * Save to database these data after clicking sendPostBtn.
     * @param e
     * @throws IOException
     */
    @FXML
    private void postForm(ActionEvent e) throws IOException {
        if (e.getSource().equals(choosePicBtn)){
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                initFile(selectedFile.getName());
            }

        } else if (e.getSource().equals(sendPostBtn)) {
            if(postDesc.getText() != null && filename != null) {
                postDao = PostDao.getInstance();
                postDao.persist(createPost());
            } else {
                System.out.println("Empty");
            }
        }

    }


}
