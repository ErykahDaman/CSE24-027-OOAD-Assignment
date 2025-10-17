package bankingsystem.ooadassignment;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;

public class AccountDashboardController {

    private customer loggedInCustomer;

    // You can pass the logged-in customer from the login controller
    public void setCustomer(customer customer) {
        this.loggedInCustomer = customer;
    }

    @FXML
    private void viewAccounts(ActionEvent event) throws Exception {
        // Load the ViewAccounts.fxml page
        Parent root = FXMLLoader.load(getClass().getResource("/ViewAccounts.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void deposit(ActionEvent event) throws Exception {
        // Load Deposit.fxml page
        Parent root = FXMLLoader.load(getClass().getResource("/Deposit.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void withdraw(ActionEvent event) throws Exception {
        // Load Withdraw.fxml page
        Parent root = FXMLLoader.load(getClass().getResource("/Withdraw.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void transfer(ActionEvent event) throws Exception {
        // Load Transfer.fxml page
        Parent root = FXMLLoader.load(getClass().getResource("/Transfer.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {
        // Return to Main Menu
        Parent root = FXMLLoader.load(getClass().getResource("/MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        showAlert("Logout", "You have successfully logged out.");
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
