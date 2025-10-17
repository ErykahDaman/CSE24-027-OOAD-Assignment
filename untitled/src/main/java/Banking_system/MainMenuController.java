package bankingsystem.ooadassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Switch to Customer Registration screen
    @FXML
    public void openRegistration(ActionEvent event) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/CustomerRegistration.fxml")); // make sure this path is correct
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Switch to Login screen
    @FXML
    public void openLogin(ActionEvent event) throws Exception {
        root = FXMLLoader.load(getClass().getResource("/Login.fxml")); // added "/" to be consistent
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Exit application
    public void exitApp(ActionEvent event) {
        System.exit(0);
    }
}
