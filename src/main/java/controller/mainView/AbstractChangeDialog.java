package controller.mainView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class AbstractChangeDialog {

    protected Stage dialogStage;

    @FXML
    protected Button okButton;

    @FXML
    protected Button cancelButton;

    @FXML
    protected void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    protected abstract void handleOkAction(ActionEvent event);
}
