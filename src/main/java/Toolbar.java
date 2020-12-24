import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Toolbar extends VBox implements IView {
    enum Tool {
        NONE("None"),
        SELECT("Select"),
        ERASE("Erase"),
        LINE("Line"),
        CIRCLE("Circle"),
        RECTANGLE("Rectangle"),
        FILL("Fill");

        private final String label;
        Tool(String label) {
            this.label = label;
        }
        public String getLabel() {
            return label;
        }
    }

    enum LineStyle {
        SOLID("Solid"),
        DOTTED("Dotted"),
        ALTERNATE("Alternate");

        private final String label;
        private final ArrayList<Double> style;
        LineStyle(String label) {
            this.label = label;
            this.style = new ArrayList<>();
            if (label.equals("Dotted")) {
                style.add(5d);
                style.add(10d);
            } else if (label.equals("Alternate")) {
                style.add(15d);
                style.add(20d);
                style.add(5d);
                style.add(20d);
            }
        }

        static public ArrayList<Double> getStyle(int size) {
            if (size == 0) {
                return SOLID.style;
            } else if (size == 2) {
                return DOTTED.style;
            } else {
                return ALTERNATE.style;
            }
        }
    }

    private Model model;

    private VBox tools, colors, lineStyles;
    private ToggleGroup toolsToggleGroup = new ToggleGroup();

    Toolbar(Model model) {
        this.model = model;
        // Set size
        this.setPrefSize(100, SketchIt.PREF_WINDOW_HEIGHT);
        this.setBackground(new Background(
                new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setPadding(new Insets(10, 5, 10, 5));
        this.setSpacing(10);

        createTools(model);
        createColors(model);
        createLineStyles(model);

        this.getChildren().add(tools);
        this.getChildren().add(new Separator(Orientation.HORIZONTAL));
        this.getChildren().add(colors);
        this.getChildren().add(new Separator(Orientation.HORIZONTAL));
        this.getChildren().add(lineStyles);

        model.addToolbar(this);

        updateView();
    }

    public void updateView() {
        ((ColorPicker)colors.getChildren().get(1)).setValue(model.getLineColor());
        if (model.getFillColor() != null) {
            ((ColorPicker)colors.getChildren().get(3)).setValue(model.getFillColor());
        }

        if (model.getLineThickness() == 2) {
            ((ChoiceBox)lineStyles.getChildren().get(1)).setValue("Thin");
        } else if (model.getLineThickness() == 4) {
            ((ChoiceBox)lineStyles.getChildren().get(1)).setValue("Medium");
        } else {
            ((ChoiceBox)lineStyles.getChildren().get(1)).setValue("Thick");
        }

        if (model.getLineStyle().size() == 0) {
            ((ChoiceBox)lineStyles.getChildren().get(3)).setValue("Solid");
        } else if (model.getLineStyle().size() == 2) {
            ((ChoiceBox)lineStyles.getChildren().get(3)).setValue("Dotted");
        } else {
            ((ChoiceBox)lineStyles.getChildren().get(3)).setValue("Alternate");
        }

        // Disable all by default
        colors.getChildren().get(1).setDisable(true);
        colors.getChildren().get(3).setDisable(true);
        lineStyles.getChildren().get(1).setDisable(true);
        lineStyles.getChildren().get(3).setDisable(true);

        if (model.getSelectedShape() != null) {
            colors.getChildren().get(1).setDisable(false);
            lineStyles.getChildren().get(1).setDisable(false);
            lineStyles.getChildren().get(3).setDisable(false);
            if (!(((CSelectBox)model.getSelectedShape()).getSelectedShape() instanceof CLine)) {
                colors.getChildren().get(3).setDisable(false);
            }
        } else if (model.getSelectedTool() == Tool.LINE) {
            colors.getChildren().get(1).setDisable(false);
            lineStyles.getChildren().get(1).setDisable(false);
            lineStyles.getChildren().get(3).setDisable(false);
        } else if (model.getSelectedTool() == Tool.CIRCLE ||
                model.getSelectedTool() == Tool.RECTANGLE) {
            colors.getChildren().get(1).setDisable(false);
            colors.getChildren().get(3).setDisable(false);
            lineStyles.getChildren().get(1).setDisable(false);
            lineStyles.getChildren().get(3).setDisable(false);
        } else if (model.getSelectedTool() == Tool.FILL) {
            colors.getChildren().get(3).setDisable(false);
        }
    }

    private void createTools(Model model) {
        VBox tools = new VBox(5);

        tools.getChildren().add(new Label("Tools"));

        for (Tool tool : Tool.values()) {
            if (tool == Tool.NONE) continue;
            RadioButton rb = new RadioButton(tool.getLabel());
            rb.setOnAction(event -> {
               model.setSelectedTool(tool);
            });

            tools.getChildren().add(rb);
            rb.setToggleGroup(toolsToggleGroup);
        }

        this.tools = tools;
    }

    private void createColors(Model model) {
        VBox colors = new VBox();

        colors.getChildren().add(new Label("Line Colour"));
        ColorPicker lineColorButton = new ColorPicker(Color.BLACK);
        lineColorButton.setOnAction(event -> {
           model.setLineColor(lineColorButton.getValue());
        });
        colors.getChildren().add(lineColorButton);

        colors.getChildren().add(new Label("Fill Colour"));
        ColorPicker fillColorButton = new ColorPicker(Color.WHITE);
        fillColorButton.setOnAction(event -> {
            model.setFillColor(fillColorButton.getValue());
        });
        colors.getChildren().add(fillColorButton);

        this.colors = colors;
    }

    private void createLineStyles(Model model) {
        VBox lineStyles = new VBox();

        lineStyles.getChildren().add(new Label("Line Thickness"));
        ChoiceBox thicknessChoice = new ChoiceBox();
        thicknessChoice.getItems().addAll("Thin", "Medium", "Thick");
        thicknessChoice.setValue("Thin");
        thicknessChoice.setOnAction(event -> {
           switch((String)thicknessChoice.getValue()) {
               case "Thin":
                   model.setLineThickness(2);
                   break;
               case "Medium":
                   model.setLineThickness(4);
                   break;
               case "Thick":
                   model.setLineThickness(6);
                   break;
           }
        });
        lineStyles.getChildren().add(thicknessChoice);

        lineStyles.getChildren().add(new Label("Line Style"));
        ChoiceBox stylesChoice = new ChoiceBox();
        stylesChoice.getItems().addAll("Solid", "Dotted", "Alternate");
        stylesChoice.setValue("Solid");
        stylesChoice.setOnAction(event -> {
            ArrayList<Double> style = new ArrayList<>();
            switch((String)(stylesChoice.getValue())) {
                case "Dotted":
                    style.add(5d);
                    style.add(10d);
                    break;
                case "Alternate":
                    style.add(15d);
                    style.add(20d);
                    style.add(5d);
                    style.add(20d);
                    break;
            }
            model.setLineStyle(style);
        });
        lineStyles.getChildren().add(stylesChoice);

        this.lineStyles = lineStyles;
    }

}
