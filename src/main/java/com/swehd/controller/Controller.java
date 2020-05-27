package com.swehd.controller;

import com.swehd.app.App;
import com.swehd.post.Post;
import com.swehd.post.PostDao;
import com.swehd.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

@Slf4j
public class Controller {
    private User user;
    private PostDao postDao;
    private static final double IMAGE_WIDTH = 350;
    /**
     * Get Current User.
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

    public static <T,U> void refreshTableView(TableView<T> tableView, List<TableColumn<T,U>> columns, List<T> rows) {
        tableView.getColumns().clear();
        tableView.getColumns().addAll(columns);

        ObservableList<T> list = FXCollections.observableArrayList(rows);
        tableView.setItems(list);
    }

    /**
     * Display images in picture cell.
     * Show edit buttons in editButton cell.
     * After clicking show a dialog window with selected description to edit.
     */
    @FXML
    public void initialize() {
        postDao = PostDao.getInstance();
        List<Post> postList = postDao.findAllPost();

        picture.setCellValueFactory(new PropertyValueFactory<>("picture"));
        editButton.setCellValueFactory(new PropertyValueFactory<>("dummy"));

        picture.setCellFactory(column -> {
            TableCell<Post, String> cell = new TableCell<Post, String>() {
                ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        VBox box = new VBox();
                        Image img = new Image(getClass().getResource("/pictures/" + item).toExternalForm());
                        imageView.setImage(img);
                        imageView.setPreserveRatio(true);
                        imageView.setFitWidth(IMAGE_WIDTH);
                        imageView.setPreserveRatio(true);

                        box.getChildren().add(imageView);

                        Post post = getTableView().getItems().get(getIndex());
                        Label labeledImage = new Label(post.getDescription() + "\nPosted by: " + post.getUser().getName());
                        labeledImage.setFont(new Font("Arial", 15));
                        box.setAlignment(Pos.TOP_CENTER);
                        box.getChildren().add(labeledImage);
                        box.setPadding(new Insets(10));
                        setGraphic(box);
                    }
                }
            };
            return cell;
        });

        editButton.setCellFactory(column -> {
            TableCell<Post, String> cell = new TableCell<>() {
                private Button btn = new Button("Edit");

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
                                ((Node) event.getSource()).getScene().getWindow();
                                stage.showAndWait();
                                initialize();

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
    private boolean downloaded;

    public void initFile(String selectedFile) {
        this.filename = selectedFile;
    }

    public void absolutePath(String path) {
        this.path = path;
    }

    /**
     *
     * @return the created post.
     */
    public Post createPost() {
        Post post = Post.builder()
                .description(postDesc.getText())
                .picture(filename)
                .user(user)
                .build();
        return post;
    }

    /**
     * Get the selected picture's filename and description.
     * Save to database after clicking sendPostBtn.
     * @param e the selected button.
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

                log.info("User created a post and refreshed table.");


                File folder = new File("src/main/resources/pictures");
                String destination = folder.getAbsolutePath() + File.separator + filename;

                try {
                    // Copy file from source to destination
                    FileChannel source = new FileInputStream(path).getChannel();
                    FileChannel dest = new FileOutputStream(destination).getChannel();
                    dest.transferFrom(source, 0, source.size());

                    // Close
                    source.close();
                    dest.close();
                    log.info("User selected a picture.");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                initialize();
            }
        }
    }
}
