package bankingsystem.ooadassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private void openRegistration(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bankingsystem/ooadassignment/CustomerRegistration.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void openLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bankingsystem/ooadassignment/Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void exitApp(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}
