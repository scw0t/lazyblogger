package lazyblogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lazyblogger.db.dbController;

public class Controller extends BorderPane {

    private TextArea textArea;
    private TextField titleTextField;
    private TextField linkTextField;
    private String imageLink;
    private AudioFactory audioFactory;
    private String downloadLink;
    private PostData bloggerPost;

    private final String[] SUPPORTED_LINKS
            = {"http://www.discogs.com/",
                "http://musicbrainz.org/",
                "http://rateyourmusic.com/",
                "http://www.youtube.com/",
                "https://mega.co.nz/",
                "http://uloz.to/",
                "http://tny.cz/"};

    public Controller() {
        setPadding(new Insets(10));
        initComponents();
    }

    private void initComponents() {
        textArea = new TextArea();

        setTop(createTopBox());
        setCenter(textArea);
        setBottom(createBottomBox());

        initDragNdropHandler();
    }

    private HBox createTopBox() {
        HBox hBox = new HBox(10);

        titleTextField = new TextField();
        HBox.setHgrow(titleTextField, Priority.ALWAYS);
        hBox.getChildren().addAll(new Label("Title"), titleTextField);
        hBox.setAlignment(Pos.CENTER_LEFT);

        setMargin(hBox, new Insets(0, 0, 10, 0));
        return hBox;
    }

    private HBox createBottomBox() {
        linkTextField = new TextField();

        Button btn = new Button("Submit post");
        //btn.disableProperty().set(true);
        btn.setOnAction((ActionEvent event) -> {
            pushPost();
        });

        /*linkTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.contains("http://") || newValue.contains("https://")) {
                btn.disableProperty().set(false);
            } else {
                btn.disableProperty().set(true);
            }
        });*/

        HBox hBox = new HBox(10);
        HBox.setHgrow(linkTextField, Priority.ALWAYS);
        hBox.getChildren().addAll(new Label("Link"), linkTextField, btn);
        hBox.setAlignment(Pos.CENTER_LEFT);

        setMargin(hBox, new Insets(10, 0, 0, 0));

        return hBox;
    }

    private void pushPost() {
        Content content = new Content();

        if (audioFactory != null) {
            PostData postData = new PostData();
            postData.setArtist(audioFactory.getArtist());
            postData.setAlbum(audioFactory.getAlbum());
            postData.setYear(audioFactory.getYear());
            postData.setCountry(audioFactory.getCountry());
            postData.setGenre(audioFactory.getGenres());
            postData.setLabel(audioFactory.getLabel());
            postData.setCatNumber(audioFactory.getCatNumber());
            postData.setDiscogsLink(parseLinks(textArea.getText(), "discogs.com"));
            postData.setMbLink(parseLinks(textArea.getText().replaceFirst("\\?tport=8000", ""), "musicbrainz.org"));
            postData.setRymLink(parseLinks(textArea.getText(), "rateyourmusic.com"));
            postData.setDl1Link(parseLinks(textArea.getText(), "mega.co.nz"));
            postData.setDl2Link(parseLinks(textArea.getText(), "uloz.to"));
            postData.setTxtLink(parseLinks(textArea.getText(), "tny.cz"));
            postData.setYoutubeLink(parseLinks(textArea.getText(), "youtube.com"));
            postData.setThumbLink(imageLink);

            content.setPostData(postData);
            content.setTracklist(audioFactory.getTrackList());
            content.setTitle(titleTextField.getText());

            content.constructPost();
            postData.setPostBody(content.getPostBody());

            ExecutePost executePost = new ExecutePost();
            if (content.getPost() != null) {
                try {
                    executePost.execute(content.getPost());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            addInfoToDatabase(postData);
        }

    }

    private String parseLinks(String source, String required) {
        BufferedReader bufReader = new BufferedReader(new StringReader(source));
        String line = "";
        String result = "";
        try {
            while ((line = bufReader.readLine()) != null) {

                for (String link : SUPPORTED_LINKS) {
                    if (line.startsWith(link) && line.contains(required)) {
                        result = line;
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Content.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!result.equals("")) {
            return result;
        } else {
            return "none";
        }
    }

    private String constructTitle() {
        audioFactory.getTrackList();
        StringBuilder title = new StringBuilder();
        title.append(audioFactory.getArtist());
        title.append(" - ");
        title.append(audioFactory.getYear());
        title.append(" - ");
        title.append(audioFactory.getAlbum());
        title.append(" (");
        title.append(audioFactory.getCountry());
        title.append(", ");
        title.append(audioFactory.getGenres());
        title.append(")");
        return title.toString();
    }
    
    private void addInfoToDatabase(PostData postData){
        dbController db = new dbController();
        db.createTableIfNotExists();
        db.addRecord(postData.getArtist(), 
                postData.getAlbum(), 
                postData.getYear(), 
                postData.getLabel(), 
                postData.getCatNumber(), 
                postData.getGenre(), 
                postData.getCountry(), 
                postData.getDiscogsLink(), 
                postData.getMbLink(), 
                postData.getRymLink(), 
                postData.getYoutubeLink(), 
                postData.getDl1Link(), 
                postData.getDl2Link(), 
                postData.getTxtLink(), 
                postData.getThumbLink(), 
                postData.getPostBody());
        db.outputLastRecord();
        db.closeConnection();
    }

    private void initDragNdropHandler() {
        setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.LINK);
            } else {
                event.consume();
            }
        });

        setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                if (!"".equals(textArea.getText())) {
                    textArea.clear();
                }
                success = true;
                for (File folder : db.getFiles()) {
                    if (folder.isDirectory()) {

                        System.out.println("Processing directory: " + folder.getAbsolutePath());

                        Thread mainProcessThread = new Thread(new ScanTask(folder));
                        mainProcessThread.setDaemon(true);
                        mainProcessThread.start();

                        /*LinkedList<File> folderList = (LinkedList) FileUtils.listFilesAndDirs(file,
                         new NotFileFilter(TrueFileFilter.INSTANCE),
                         DirectoryFileFilter.DIRECTORY);
                         for (File folder : folderList) {
                         initialDirectoryList.add(new IncomingDirectory(folder));
                         }*/
                    }
                }
            }
            event.setDropCompleted(success);
            if (event.isDropCompleted()) {

            }
            event.consume();
        });

    }

    class ScanTask extends Task {

        private File folder;

        public ScanTask(File folder) {
            this.folder = folder;
        }

        @Override
        protected Object call() throws Exception {
            Platform.runLater(() -> {
                audioFactory = new AudioFactory(folder);
                LinkedList<File> audioList = audioFactory.getAudio();
                titleTextField.setText(constructTitle());

                for (File audioList1 : audioList) {
                    System.out.println(audioList1.getName());
                }

                File image = audioFactory.getFolderImage();
                if (image != null) {
                    System.out.println("Folder: " + image.getName());
                    try {
                        DriveUploader uploader = new DriveUploader();
                        uploader.uploadImage(image);
                        imageLink = uploader.getImageLink();

                    } catch (IOException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("There is no image in folder: " + folder.getAbsolutePath());
                }
            });
            return null;
        }
    }

}
