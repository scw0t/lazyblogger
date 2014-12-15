package lazyblogger;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Lazyblogger extends Application {

    public static TextArea textArea;

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Submit post");
        btn.setOnAction((ActionEvent event) -> {
            Content content = new Content(textArea.getText());
            ExecutePost executePost = new ExecutePost();
            if (content.getPost() != null) {
                try {
                    executePost.execute(content.getPost());
                } catch (Exception ex) {
                    Logger.getLogger(Lazyblogger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        textArea = new TextArea();

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));
        pane.setMargin(btn, new Insets(10, 0, 0, 0));
        pane.setCenter(textArea);
        pane.setBottom(btn);

        Scene scene = new Scene(pane, 500, 600);

        primaryStage.setTitle("lazyblogger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
