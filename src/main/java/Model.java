import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {
    private IView viewCanvas;
    private IView toolbar;
    private IView vMenu;

    // Selected
    private Toolbar.Tool selectedTool = Toolbar.Tool.NONE;
    private Shape selectedShape = null;

    // Properties
    private Color lineColor = Color.BLACK;
    private Color fillColor = Color.WHITE;
    private double lineThickness = 2;
    private ArrayList<Double> lineStyle = new ArrayList<Double>();

    private Color oldLC;
    private Color oldFC;
    private double oldLT;
    private ArrayList<Double> oldLS;

    private boolean saved = false;

    // all shapes
    private ArrayList<Node> shapes = new ArrayList<Node>();

    // For rect
    private double initialX;
    private double initialY;

    // Copy Paste
    private Shape copy = null;

    public void copy() {
        if (selectedShape != null) {
            Shape shape = ((CSelectBox) selectedShape).getSelectedShape();
            copy = shape;
        }
    }
    public void cut() {
        if (selectedShape != null) {
            copy();
            eraseShape(((CSelectBox)selectedShape).getSelectedShape());
            eraseShape(selectedShape);
        }
    }
    public void paste() {
        if (copy != null) {
            Shape cop;
            if (copy instanceof CCircle) {
                cop = ((CCircle) copy).getCopy();
            } else if (copy instanceof CLine) {
                cop = ((CLine) copy).getCopy();
            } else {
                cop = ((CRectangle) copy).getCopy();
            }
            shapes.add(cop);
            notifyCanvas();
        }
    }

    public void addCanvas(ViewCanvas viewCanvas) {
        this.viewCanvas = viewCanvas;
    }

    public void addToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public void addMenu(VMenu menu) {
        this.vMenu = menu;
    }

    public void deselect() {
        if (selectedShape != null) {
            if (selectedTool == Toolbar.Tool.SELECT) {
                eraseShape(selectedShape);
                selectedShape = null;

                notifyCanvas();

                lineColor = oldLC;
                fillColor = oldFC;
                lineThickness = oldLT;
                lineStyle = oldLS;
                notifyToolbar();
            } else {
                selectedShape = null;
            }
        }
    }

    public void save(File file) throws IOException {
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file.getAbsolutePath());

        fileWriter.write(String.format("%d\n", shapes.size()));
        for (Node node: shapes) {
            if (node instanceof CLine) {
                CLine shape = (CLine) node;
                Color lc = (Color)shape.getStroke();

                fileWriter.write(String.format(
                        "1 %f %f %f %f %f %f %f %f %d\n",
                        shape.getStartX()+shape.getTranslateX(), shape.getStartY()+shape.getTranslateY(),
                        shape.getEndX()+shape.getTranslateX(), shape.getEndY()+shape.getTranslateY(),
                        lc.getRed(), lc.getGreen(), lc.getBlue(),
                        shape.getStrokeWidth(), shape.getStrokeDashArray().size()
                ));
            } else if (node instanceof CRectangle) {
                CRectangle shape = (CRectangle) node;
                Color lc = (Color)shape.getStroke();
                Color fc = (Color)shape.getFill();

                fileWriter.write(String.format(
                        "2 %f %f %f %f %f %f %f %f %f %f %f %d\n",
                        shape.getX()+shape.getTranslateX(), shape.getY()+shape.getTranslateY(),
                        shape.getWidth(), shape.getHeight(),
                        lc.getRed(), lc.getGreen(), lc.getBlue(),
                        fc.getRed(), fc.getGreen(), fc.getBlue(),
                        shape.getStrokeWidth(), shape.getStrokeDashArray().size()
                ));
            } else if (node instanceof CCircle) {
                CCircle shape = (CCircle) node;
                Color lc = (Color)shape.getStroke();
                Color fc = (Color)shape.getFill();

                fileWriter.write(String.format(
                        "3 %f %f %f %f %f %f %f %f %f %f %d\n",
                        shape.getCenterX()+shape.getTranslateX(), shape.getCenterY()+shape.getTranslateY(),
                        shape.getRadius(),
                        lc.getRed(), lc.getGreen(), lc.getBlue(),
                        fc.getRed(), fc.getGreen(), fc.getBlue(),
                        shape.getStrokeWidth(), shape.getStrokeDashArray().size()
                ));
            }
        }
        fileWriter.close();
        saved = true;
    }

    public void load(File file) throws IOException {
        Scanner in = new Scanner(file);
        int num = in.nextInt();
        for (int i = 0; i < num; ++i) {
            int type = in.nextInt();
            if (type == 1) { // Line
                CLine line = new CLine(in.nextDouble(), in.nextDouble(), this);
                line.setEndX(in.nextDouble());
                line.setEndY(in.nextDouble());
                line.setStroke(Color.color(in.nextDouble(), in.nextDouble(), in.nextDouble()));
                line.setStrokeWidth(in.nextDouble());
                line.getStrokeDashArray().setAll(Toolbar.LineStyle.getStyle(in.nextInt()));
                shapes.add(line);
            } else if (type == 2) { // Rectangle
                CRectangle rect = new CRectangle(in.nextDouble(), in.nextDouble(), this);
                rect.setWidth(in.nextDouble());
                rect.setHeight(in.nextDouble());
                rect.setStroke(Color.color(in.nextDouble(), in.nextDouble(), in.nextDouble()));
                rect.setFill(Color.color(in.nextDouble(), in.nextDouble(), in.nextDouble()));
                rect.setStrokeWidth(in.nextDouble());
                rect.getStrokeDashArray().setAll(Toolbar.LineStyle.getStyle(in.nextInt()));
                shapes.add(rect);
            } else { // Circle
                CCircle circle = new CCircle(in.nextDouble(), in.nextDouble(), this);
                circle.setRadius(in.nextDouble());
                circle.setStroke(Color.color(in.nextDouble(), in.nextDouble(), in.nextDouble()));
                circle.setFill(Color.color(in.nextDouble(), in.nextDouble(), in.nextDouble()));
                circle.setStrokeWidth(in.nextDouble());
                circle.getStrokeDashArray().setAll(Toolbar.LineStyle.getStyle(in.nextInt()));
                shapes.add(circle);
            }
        }
        notifyCanvas();
        saved = true;
    }

    private void unsave() {
        saved = false;
    }

    public boolean getSaved() {
        return saved;
    }

    public void createLine(double startX, double startY) {
        CLine line = new CLine(startX, startY, this);
        line.setStroke(lineColor);
        line.setStrokeWidth(lineThickness);
        line.getStrokeDashArray().setAll(lineStyle);
        shapes.add(line);
        selectedShape = line;
        notifyCanvas();

        unsave();
    }

    public void drawLine(double endX, double endY) {
        if (selectedShape != null) {
            CLine line = (CLine) selectedShape;
            line.setEndX(endX);
            line.setEndY(endY);
            notifyCanvas();
        }
    }

    public void createRect(double startX, double startY) {
        initialX = startX;
        initialY = startY;

        CRectangle rect = new CRectangle(startX, startY, this);
        rect.setStroke(lineColor);
        rect.setFill(fillColor);
        rect.setStrokeWidth(lineThickness);
        rect.getStrokeDashArray().setAll(lineStyle);
        shapes.add(rect);
        selectedShape = rect;
        notifyCanvas();

        unsave();
    }

    public void drawRect(double endX, double endY) {
        if (selectedShape != null) {
            CRectangle rect = (CRectangle) selectedShape;

            double dx = endX - initialX;
            double dy = endY - initialY;

            if (dx >= 0) {
                rect.setX(initialX);
                rect.setWidth(dx);
            } else {
                rect.setX(initialX + dx);
                rect.setWidth(-dx);
            }

            if (dy >= 0) {
                rect.setY(initialY);
                rect.setHeight(dy);
            } else {
                rect.setY(initialY + dy);
                rect.setHeight(-dy);
            }

            notifyCanvas();
        }
    }

    public void createCircle(double startX, double startY) {
        CCircle circle = new CCircle(startX, startY, this);
        circle.setStroke(lineColor);
        circle.setFill(fillColor);
        circle.setStrokeWidth(lineThickness);
        circle.getStrokeDashArray().setAll(lineStyle);
        shapes.add(circle);
        selectedShape = circle;
        notifyCanvas();

        unsave();
    }

    public void drawCircle(double endX, double endY) {
        if (selectedShape != null) {
            CCircle circle = (CCircle) selectedShape;
            Point2D center = new Point2D(circle.getCenterX(), circle.getCenterY());
            double dist = center.distance(endX, endY);
            circle.setRadius(dist);

            notifyCanvas();
        }
    }

    public void moveShape(Shape shape, double dx, double dy) {
        shape.setTranslateX(shape.getTranslateX() + dx);
        shape.setTranslateY(shape.getTranslateY() + dy);
        // Shouldn't need to call notify
        unsave();
    }

    public void fillShape(Shape shape) {
        shape.setFill(fillColor);
        /* notifyCanvas();  Technically don't need, since canvas has reference to shape */
        unsave();
    }

    public void eraseShape(Shape shape) {
        shapes.remove(shape);
        notifyCanvas();

        unsave();
    }

    public Toolbar.Tool getSelectedTool() {
        return selectedTool;
    }

    public void setSelectedTool(Toolbar.Tool tool) {
        if (tool != Toolbar.Tool.SELECT)
            deselect();
        this.selectedTool = tool;
        notifyToolbar();
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape shape) {
        CSelectBox box = new CSelectBox(shape, this);
        this.selectedShape = box;
        shapes.add(box);
        notifyCanvas();
        box.requestFocus();

        oldLC = lineColor;
        lineColor = (Color) shape.getStroke();
        oldFC = fillColor;
        fillColor = (Color) shape.getFill();
        oldLT = lineThickness;
        lineThickness = shape.getStrokeWidth();
        oldLS = lineStyle;
        lineStyle = new ArrayList<>(shape.getStrokeDashArray());
        notifyToolbar();
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color color) {
        this.lineColor = color;
        if (selectedShape != null) {
            ((CSelectBox) selectedShape).getSelectedShape().setStroke(lineColor);
            unsave();
        }
        notifyCanvas();
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
        if (selectedShape != null) {
            ((CSelectBox) selectedShape).getSelectedShape().setFill(fillColor);
            unsave();
        }
        notifyCanvas();
    }

    public double getLineThickness() {
        return lineThickness;
    }

    public void setLineThickness(double thickness) {
        this.lineThickness = thickness;
        if (selectedShape != null) {
            ((CSelectBox) selectedShape).getSelectedShape().setStrokeWidth(lineThickness);
            unsave();
        }
        notifyCanvas();
    }

    public ArrayList<Double> getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(ArrayList<Double> style) {
        this.lineStyle = style;
        if (selectedShape != null) {
            ((CSelectBox) selectedShape).getSelectedShape().getStrokeDashArray().setAll(lineStyle);
            unsave();
        }
        notifyCanvas();
    }

    public ArrayList<Node> getShapes() {
        return shapes;
    }

    public void clearCanvas() {
        shapes.clear();
        selectedShape = null;
        unsave();
        notifyCanvas();
    }

    private void notifyCanvas() {
        //System.out.println("Model: notify View");
        viewCanvas.updateView();
        if (selectedShape != null) {
            selectedShape.requestFocus();
        }
    }

    private void notifyToolbar() {
        toolbar.updateView();
    }
}
