package lazyblogger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Lazyblogger extends Application {

    public static TextArea textArea;

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(new Controller(), 500, 600);

        primaryStage.setTitle("lazyblogger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
