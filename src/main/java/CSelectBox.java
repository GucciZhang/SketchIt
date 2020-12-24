import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class CSelectBox extends Rectangle {
    Model model;
    private Shape selectedShape;

    private double startX;
    private double startY;

    public CSelectBox(Shape shape, Model model) {
        this.selectedShape = shape;
        this.model = model;

        Bounds bounds = selectedShape.getBoundsInParent();
        setX(bounds.getMinX());
        setY(bounds.getMinY());
        setWidth(bounds.getWidth());
        setHeight(bounds.getHeight());

        setFill(Color.web("#333333", 0.3));

        setStroke(Color.BLACK);
        setStrokeWidth(1);
        ArrayList<Double> style = new ArrayList<>();
        style.add(5d);
        style.add(5d);
        getStrokeDashArray().setAll(style);

        addHandlers();
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    private void addHandlers() {
        setOnMousePressed(mouseEvent -> {
            switch(model.getSelectedTool()) {
                case SELECT: {
                    startX = mouseEvent.getX();
                    startY = mouseEvent.getY();
                    break;
                }
            }
        });

        setOnMouseDragged(mouseEvent -> {
            switch(model.getSelectedTool()) {
                case SELECT: {
                    double dx = mouseEvent.getX() - startX;
                    double dy = mouseEvent.getY() - startY;

                    setTranslateX(getTranslateX() + dx);
                    setTranslateY(getTranslateY() + dy);

                    model.moveShape(selectedShape, dx, dy);
                    break;
                }
            }
        });

        setOnMouseClicked(mouseEvent -> {
            mouseEvent.consume();
        });

        this.setOnKeyPressed(keyEvent -> {
            switch(model.getSelectedTool()) {
                case SELECT: {
                    if (model.getSelectedShape() == this) {
                        if (keyEvent.getCode() == KeyCode.ESCAPE) {
                            model.deselect();
                        } else if (keyEvent.getCode() == KeyCode.DELETE) {
                            model.deselect();
                            model.eraseShape(selectedShape);
                            model.eraseShape(this);
                        }
                    }
                }
            }
        });
    }
}
