package lazyblogger;

import java.io.File;
import java.util.LinkedList;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import static lazyblogger.Lazyblogger.textArea;

public class Controller extends BorderPane {

    public Controller() {
        init();
    }

    public void init() {
        Button btn = new Button();
        btn.setText("Submit post");
        btn.setOnAction((ActionEvent event) -> {
            /*Content content = new Content(textArea.getText());
             ExecutePost executePost = new ExecutePost();
             if (content.getPost() != null) {
             try {
             executePost.execute(content.getPost());
             } catch (Exception ex) {
             Logger.getLogger(Lazyblogger.class.getName()).log(Level.SEVERE, null, ex);
             }
             }*/
        });

        textArea = new TextArea();

        setPadding(new Insets(10));
        setMargin(btn, new Insets(10, 0, 0, 0));
        setCenter(textArea);
        setBottom(btn);

        initDragNdropHandler();
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

                        System.out.println(folder.getAbsolutePath());

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
                new AudioFactory(folder);
            });
            return null;
        }
    }

}
