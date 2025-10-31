package bankingsystem.ooadassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class tellerdashboardcontroller {

    @FXML private Label welcomeLabel;
    @FXML private ListView<String> customersListView;

    private teller loggedInTeller;
    private List<customer> customers = new ArrayList<>();

    public void setTeller(teller t) {
        this.loggedInTeller = t;
        welcomeLabel.setText("Welcome, " + t.getFirstname() + "!");
        loadCustomers();
    }

    private void loadCustomers() {
        customers.clear();
        customersListView.getItems().clear();

        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                customer c = new customer(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                customers.add(c);
                customersListView.getItems().add(c.getCustomerID() + " - " + c.getFirstname() + " " + c.getSurname());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteCustomer(ActionEvent event) {
        int index = customersListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            customer c = customers.get(index);
            try {
                List<String> lines = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (!line.startsWith(c.getCustomerID() + ",")) {
                            lines.add(line);
                        }
                    }
                }

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("customers.txt"))) {
                    for (String l : lines) {
                        bw.write(l);
                        bw.newLine();
                    }
                }

                loadCustomers();
                showAlert("Deleted", "Customer deleted successfully!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bankingsystem/ooadassignment/MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(msg);
        alert.showAndWait();
    }
}
