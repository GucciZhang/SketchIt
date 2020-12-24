import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class VMenu extends MenuBar implements IView {
    private Model model;
    private Stage stage;

    VMenu(Model model, Stage stage) {
        this.model = model;
        this.stage = stage;
        // Set size
        this.setPrefSize(SketchIt.PREF_WINDOW_WIDTH, 30);
        this.setBackground(new Background(
                new BackgroundFill(Color.web("#92a1fc"), CornerRadii.EMPTY, Insets.EMPTY)));

        addMenus();
        model.addMenu(this);
        updateView();
    }

    private void addMenus() {
        Menu menuFile = new Menu("File");
        MenuItem mNew = new MenuItem("New");
        MenuItem mLoad = new MenuItem("Load");
        MenuItem mSave = new MenuItem("Save");
        MenuItem mQuit = new MenuItem("Quit");

        mNew.setOnAction(event -> {
            if (model.getSaved()) {
                model.clearCanvas();
            } else {
                unsavedChanges(mSave);
            }
        });
        mLoad.setOnAction(event -> {
            if (!model.getSaved()) {
                if(!unsavedChanges(mSave)) {
                    return;
                }
            }

            model.clearCanvas();

            FileChooser fileChooser = new FileChooser();

            // Save as skt (Faker) file
            FileChooser.ExtensionFilter sktFilter = new FileChooser.ExtensionFilter("SketchIt file (*.skt)", "*.skt");
            fileChooser.getExtensionFilters().add(sktFilter);

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                try {
                    model.load(file);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("SketchIt!");
                    alert.setContentText("Load file failed");
                }
            }

        });
        mSave.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            // Save as skt (Faker) file
            FileChooser.ExtensionFilter sktFilter = new FileChooser.ExtensionFilter("SketchIt file (*.skt)", "*.skt");
            fileChooser.getExtensionFilters().add(sktFilter);

            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                try {
                    model.save(file);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("SketchIt!");
                    alert.setContentText("Save file failed");
                }

            }
        });
        mQuit.setOnAction(event -> {
            if (!model.getSaved()) {
                if(unsavedChanges(mSave)) {
                    stage.close();
                }
            } else {
                stage.close();
            }
        });

        menuFile.getItems().addAll(mNew, mLoad, mSave, mQuit);

        Menu menuEdit = new Menu("Edit");
        MenuItem mCut = new MenuItem("Cut");
        MenuItem mCopy = new MenuItem("Copy");
        MenuItem mPaste = new MenuItem("Paste");

        mCut.setOnAction(event -> {
            model.cut();
        });
        mCopy.setOnAction(event -> {
            model.copy();
        });
        mPaste.setOnAction(event -> {
            model.paste();
        });

        menuEdit.getItems().addAll(mCut, mCopy, mPaste);

        Menu menuHelp = new Menu("Help");
        MenuItem mAbout = new MenuItem("About");

        mAbout.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("SketchIt!");
            alert.setHeaderText("About");
            alert.setContentText("Application: SketchIt\nBy: Jeff Zhang (j927zhan)");

            alert.showAndWait();
        });

        menuHelp.getItems().add(mAbout);

        getMenus().addAll(menuFile, menuEdit, menuHelp);
    }

    private boolean unsavedChanges(MenuItem mSave) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SketchIt!");
        alert.setHeaderText("You have unsaved changes");
        alert.setContentText("Would you like to save?");

        ButtonType bYes = new ButtonType("Yes");
        ButtonType bNo = new ButtonType("No");
        ButtonType bCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(bYes, bNo, bCancel);

        boolean ret = false;
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.get() == bYes) {
            mSave.fire(); // Trigger same operation as saving
            model.clearCanvas();
            ret = true;
        } else if (choice.get() == bNo) {
            model.clearCanvas();
            ret = true;
        }
        alert.close();
        return ret;
    }

    public void updateView() {

    }
}
