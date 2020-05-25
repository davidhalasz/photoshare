package com.swehd.controller;

import com.swehd.App;
import com.swehd.post.Post;
import com.swehd.post.PostDao;
import com.swehd.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class Controller{
    private User user;
    private PostDao postDao;

    /**
     * Get Current User
     * @param user
     */
    public void initdata(User user) {
        this.user = user;
    }


    @FXML
    private TableView<Post> postWall;

    @FXML
    private TableColumn<Post, String> picture;

    @FXML
    private TableColumn<Post, String> description;

    @FXML
    private TableColumn<Post, String> editButton;


    /**
     * Display images in picture cell.
     * Show edit buttons in editButton cell. After clicking show a dialog window with selected description to edit.
     */

    @FXML
    public void initialize() {
        postDao = PostDao.getInstance();
        List<Post> postList = postDao.findAllPost();

        picture.setCellValueFactory(new PropertyValueFactory<>("picture"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        editButton.setCellValueFactory(new PropertyValueFactory<>("dummy"));


        picture.setCellFactory(column -> {
            TableCell<Post, String> cell = new TableCell<Post, String>() {
                ImageView imageView = new ImageView();

                /**
                 * Show posted images.
                 * @param item
                 * @param empty
                 */
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if(empty) {
                        setText(null);
                        setGraphic(null);
                    }
                    else {
                        imageView.setFitHeight(150);
                        imageView.setPreserveRatio(true);
                        imageView.setImage(new Image(getClass().getResource("/pictures/" + item).toExternalForm()));

                        setGraphic(imageView);
                    }
                }
            };
            return cell;
        });

        editButton.setCellFactory(column -> {
            TableCell<Post, String> cell = new TableCell<>() {
                final Button btn = new Button("Edit");

                /**
                 * If User's id equal to post's user id, then show edit button.
                 * Show an edit window after clicking edit button.
                 * @param item
                 * @param empty
                 */
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {

                            try {
                                Post post = getTableView().getItems().get(getIndex());
                                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("editwindow.fxml"));
                                Parent root = fxmlLoader.load();
                                fxmlLoader.<EditController>getController().initPost(post.getPid(), post.getDescription());
                                Stage stage = new Stage();
                                stage.setTitle("Edit your post");
                                stage.setScene(new Scene(root));
                                stage.showAndWait();
                                initialize();
                               ((Node) event.getSource()).getScene().getWindow();

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
                        Post post = getTableView().getItems().get(getIndex());
                        if (user.getId() == post.getUser().getId()) {
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                }

            };
            return cell;
        });

        ObservableList<Post> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(postList);

        postWall.setItems(observableResult);
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
     * Returns a created post.
     * @return
     */
    public Post createPost() {
        Post post = Post.builder()
                .description(postDesc.getText().trim())
                .picture(filename)
                .user(user)
                .build();

        return post;
    }

    /**
     * Get the selected picture's filename and description.
     * Save to database after clicking sendPostBtn.
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
                initialize();

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
