import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ViewCanvas extends Pane implements IView {
    static final double WINDOW_WIDTH = 900;
    static final double WINDOW_HEIGHT = 400;

    private Model model;

    ViewCanvas(Model model) {
        this.model = model;

        // Style canvas
        this.setBackground(new Background(
                new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        addHandlers();

        // Register with model
        model.addCanvas(this);
    }

    public void updateView() {
        this.getChildren().setAll(model.getShapes());
        //System.out.println("View: updateView");
    }

    private void addHandlers() {
        this.setOnMousePressed(mouseEvent -> {
            switch(model.getSelectedTool()) {
                case LINE: {
                    model.createLine(mouseEvent.getX(), mouseEvent.getY());
                    break;
                } case RECTANGLE: {
                    model.createRect(mouseEvent.getX(), mouseEvent.getY());
                    break;
                } case CIRCLE: {
                    model.createCircle(mouseEvent.getX(), mouseEvent.getY());
                }
            }
        });

        this.setOnMouseDragged(mouseEvent -> {
            switch(model.getSelectedTool()) {
                case LINE: {
                    // Only draw if within canvas
                    if (this.getLayoutBounds().contains(mouseEvent.getX(), mouseEvent.getY())) {
                        //System.out.printf("%f, %f\n", mouseEvent.getX(), mouseEvent.getY());
                        model.drawLine(mouseEvent.getX(), mouseEvent.getY());
                    }
                    break;
                } case RECTANGLE: {
                    if (this.getLayoutBounds().contains(mouseEvent.getX(), mouseEvent.getY())) {
                        model.drawRect(mouseEvent.getX(), mouseEvent.getY());
                    }
                    break;
                } case CIRCLE: {
                    if (this.getLayoutBounds().contains(mouseEvent.getX(), mouseEvent.getY())) {
                        model.drawCircle(mouseEvent.getX(), mouseEvent.getY());
                    }
                }
            }
        });

        this.setOnMouseReleased(mouseEvent -> {
            switch(model.getSelectedTool()) {
                case LINE:
                case RECTANGLE:
                case CIRCLE: {
                    model.deselect();
                    break;
                }
            }
        });

        setOnMouseClicked(mouseEvent -> {
           if (model.getSelectedTool() == Toolbar.Tool.SELECT) {
               model.deselect();
           }
        });
    }

}
