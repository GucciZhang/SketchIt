import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SketchIt extends Application {
    static final double PREF_WINDOW_WIDTH = 1200;
    static final double PREF_WINDOW_HEIGHT = 600;
    static final double MIN_WINDOW_WIDTH = 600;
    static final double MIN_WINDOW_HEIGHT = 500;
    static final double MAX_WINDOW_WIDTH = 1900;
    static final double MAX_WINDOW_HEIGHT = 1400;

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model();

        Layout layout = new Layout(model, stage);

        // Add grid to a scene (and the scene to the stage)
        Scene scene = new Scene(layout, PREF_WINDOW_WIDTH, PREF_WINDOW_HEIGHT);

        // Window limits
        stage.setMaxWidth(MAX_WINDOW_WIDTH);
        stage.setMaxHeight(MAX_WINDOW_HEIGHT);
        stage.setMinWidth(MIN_WINDOW_WIDTH);
        stage.setMinHeight(MIN_WINDOW_HEIGHT);

        stage.setTitle("SketchIt!");
        stage.setScene(scene);
        stage.show();

        //layout.getCenter().requestFocus(); // Keyboard input goes to canvas
    }
}
