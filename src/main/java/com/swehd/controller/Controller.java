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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class Controller{
    public long id;
    private PostDao postDao;

    /**
     * Get Current User's ID
     * @param id
     */
    public void initdata(Long id) {
        this.id = id;
        System.out.println("Current user ID is: " + this.id);
    }

    @FXML
    private SplitPane sp;

    @FXML
    private DialogPane dialogContent;

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

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if(empty) {
                        setText(null);
                        setGraphic(null);
                    }
                    else {
                        imageView.setFitHeight(50);
                        imageView.setPreserveRatio(true);
                        imageView.setImage(new Image(getClass().getResource("/pictures/" + item).toExternalForm()));

                        setGraphic(imageView);
                    }
                }
            };
            return cell;
        });

        editButton.setCellFactory(column -> {
            TableCell<Post, String> cell = new TableCell<Post, String>() {
                final Button btn = new Button("Edit");

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
                                stage.setScene(new Scene(root));
                                stage.show();
                               ((Node) event.getSource()).getScene().getWindow();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
                        setGraphic(btn);
                        setText(null);

                    }
//                        Dialog<ButtonType> dialog = new Dialog<>();
//                        dialog.initOwner(sp.getScene().getWindow());
//                        dialog.setTitle("Edit your photo description");
//                        btn.setOnAction(event -> {
//                            Post post = getTableView().getItems().get(getIndex());
//
//                            try {
//                                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("editdialog.fxml"));
//                                Parent root = fxmlLoader.load();
//                                fxmlLoader.<DialogController>getController().initPost(post.getPid(), post.getDescription());
//
//                                dialog.getDialogPane().setContent(root);
//                            } catch (IOException ex) {
//                                ex.printStackTrace();
//                            }
//                            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//                            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//
//                            Optional<ButtonType> result = dialog.showAndWait();
//                            if(result.isPresent() && result.get() == ButtonType.OK) {
//
//                            }
//
//                        });
//                        setGraphic(btn);
//                        setText(null);
//                    }
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
     * A Post builder. Get logged user's id. Set description and picture's name.
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
