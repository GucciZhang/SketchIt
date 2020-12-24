import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Layout extends BorderPane {

    Layout(Model model, Stage stage) {
        // Set size
        this.setPrefSize(SketchIt.PREF_WINDOW_WIDTH, SketchIt.PREF_WINDOW_HEIGHT);
        this.setBackground(new Background(
                new BackgroundFill(Color.web("#b2d4ed"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Add view elements
        ViewCanvas viewCanvas = new ViewCanvas(model);
        this.setCenter(viewCanvas);

        VMenu menu = new VMenu(model, stage);
        this.setTop(menu);

        Toolbar toolbar = new Toolbar(model);
        this.setLeft(toolbar);
    }

}
