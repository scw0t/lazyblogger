package lazyblogger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Lazyblogger extends Application {
    
    public static final String APP_NAME = "LSDinc-TestApp/0.1";

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new Controller(), 500, 600);

        primaryStage.setTitle("lazyblogger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
