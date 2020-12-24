import javafx.scene.input.KeyCode;

import javafx.scene.shape.Rectangle;

public class CRectangle extends Rectangle {
    private Model model;

    public CRectangle(double x, double y, Model model) {
        super(x, y, 0, 0);
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

    public CRectangle getCopy() {
        CRectangle copy = new CRectangle(0, 0, model);
        copy.setWidth(getWidth());
        copy.setHeight(getHeight());
        copy.setStroke(getStroke());
        copy.setFill(getFill());
        copy.setStrokeWidth(getStrokeWidth());
        copy.getStrokeDashArray().setAll(getStrokeDashArray());

        return copy;
    }
}
