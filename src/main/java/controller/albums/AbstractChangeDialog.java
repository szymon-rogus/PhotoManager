package controller.albums;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public abstract class AbstractChangeDialog {

    protected Stage dialogStage;

    @FXML
    protected TextField textField;

    @FXML
    protected Button okButton;

    @FXML
    protected Button cancelButton;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    protected void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    protected abstract void handleOkAction(ActionEvent event);
}
