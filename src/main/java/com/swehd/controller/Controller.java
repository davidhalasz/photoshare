package com.swehd.controller;

import com.swehd.post.Post;
import com.swehd.post.PostDao;
import com.swehd.user.User;
import com.swehd.user.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.text.html.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

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


    @FXML
    private AnchorPane postWall;
    @FXML
    private ImageView pimage;
    @FXML
    private Label pname;
    @FXML
    private Label pdescription;



    public void initialize() {
        postDao = PostDao.getInstance();

        List<Post> postWall = postDao.findAll();
        pdescription.setLabelFor(new PropertyValueFactory<>("description"));
        

    }



    @FXML
    private Button choosePicBtn;
    @FXML
    private Button sendPostBtn;
    @FXML
    private TextArea postDesc;

    private String filename;
    private String path;

    public void initFile(String selectedFile) {
        this.filename = selectedFile;
    }

    public void absolutePath(String path) {
        this.path = path;
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
        if (e.getSource().equals(choosePicBtn)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                initFile(selectedFile.getName());
                absolutePath(selectedFile.getAbsolutePath());
            }

        } else if (e.getSource().equals(sendPostBtn)) {
            if (postDesc.getText() != null && filename != null) {
                postDao = PostDao.getInstance();
                postDao.persist(createPost());

                File folder = new File("src/main/resources/pictures");
                boolean success = folder.mkdir();
                String destination = folder.getAbsolutePath() + File.separator + filename;

                try {
                    // Copy file from source to destination
                    FileChannel source = new FileInputStream(path).getChannel();
                    FileChannel dest = new FileOutputStream(destination).getChannel();
                    dest.transferFrom(source, 0, source.size());

                    // Close
                    source.close();
                    dest.close();

                    System.out.println("Done");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                System.out.println("Empty");
            }
        }
    }
}
