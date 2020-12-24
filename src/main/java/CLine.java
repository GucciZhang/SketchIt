import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Line;

public class CLine extends Line {
    private Model model;

    public CLine(double x, double y, Model model) {
        super(x, y, x, y);
        this.model = model;

        this.setOnMouseClicked(mouseEvent -> {
            switch(model.getSelectedTool()) {
                case ERASE: {
                    model.eraseShape(this);
                    break;
                } case SELECT: {
                    model.deselect();
                    model.setSelectedShape(this);
                    mouseEvent.consume();
                    break;
                }
            }
        });
    }

    public CLine getCopy() {
        CLine copy = new CLine(getStartX(), getStartY(), model);
        copy.setEndX(getEndX());
        copy.setEndY(getEndY());
        copy.setStroke(getStroke());
        copy.setStrokeWidth(getStrokeWidth());
        copy.getStrokeDashArray().setAll(getStrokeDashArray());

        Bounds bounds = copy.getBoundsInParent();
        copy.setTranslateX(-bounds.getMinX());
        copy.setTranslateY(-bounds.getMinY());

        return copy;
    }
}

