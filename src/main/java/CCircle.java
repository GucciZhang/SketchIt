import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;

public class CCircle extends Circle {
    private Model model;

    CCircle(double x, double y, Model model ) {
        super(x, y, 0);
        this.model = model;

        this.setOnMouseClicked(mouseEvent -> {
            switch(model.getSelectedTool()) {
                case FILL: {
                    model.fillShape(this);
                    break;
                } case ERASE: {
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

    public CCircle getCopy() {
        CCircle copy = new CCircle(getRadius(), getRadius(), model);
        copy.setRadius(getRadius());
        copy.setStroke(getStroke());
        copy.setFill(getFill());
        copy.setStrokeWidth(getStrokeWidth());
        copy.getStrokeDashArray().setAll(getStrokeDashArray());

        return copy;
    }

}
