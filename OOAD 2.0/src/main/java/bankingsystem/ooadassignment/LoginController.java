package bankingsystem.ooadassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    @FXML private TextField idField;
    @FXML private TextField passwordField;

    @FXML
    private void loginCustomer(ActionEvent event) {
        String id = idField.getText().trim();
        String pass = passwordField.getText().trim();

        if(id.isEmpty() || pass.isEmpty()) {
            showAlert("Error", "Please enter ID and Password");
            return;
        }

        boolean found = false;

        // Check customer login
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                if(parts[0].equals(id) && parts[3].equals(pass)){
                    found = true;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankingsystem/ooadassignment/CustomerDashboard.fxml"));
                    Parent root = loader.load();
                    customerdashboard controller = loader.getController();
                    customer c = new customer(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                    controller.setCustomer(c);

                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                    break;
                }
            }
        } catch(IOException e) { e.printStackTrace(); }

        // TODO: add teller login check here similarly

        if(!found){
            showAlert("Error", "Invalid ID or Password");
        }
    }

    @FXML
    private void backToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bankingsystem/ooadassignment/MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(msg);
        alert.showAndWait();
    }
}
