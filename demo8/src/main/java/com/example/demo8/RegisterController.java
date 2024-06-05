package com.example.demo8;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;


public class RegisterController {
    @FXML
    private Text txtHello;
    @FXML
    private TextField txtPlate;
    @FXML
    private TextField txtSSE;
    @FXML
    private ComboBox cboType;
    @FXML
    private TableView tbMontlyTicket;
    @FXML
    private TextField txtSearch;
    @FXML
    private Text textTicket;
    @FXML
    private TextField carFee;
    @FXML
    private TextField bikeFee;

    @FXML
    void initialize() throws SQLException {
        String username = User.getUsername();
        txtHello.setText("Hello "+ username);

        String type[] = {"Car","Motorbike"};

        cboType.getItems().addAll(type);

        Connect_View();
        carFee.setText(String.valueOf(ParkingLot.getMonthlyCarFee()));
        bikeFee.setText(String.valueOf(ParkingLot.getMonthlyBikeFee()));

    }
    @FXML
    void DefineTableView(){
        tbMontlyTicket.setEditable(true);
        tbMontlyTicket.getColumns().clear();
        tbMontlyTicket.getItems().clear();

        TableColumn<MonthlyRegistrations, String> sse = new TableColumn<>("SSE");
        sse.setCellValueFactory(new PropertyValueFactory<>("sse"));

        TableColumn<MonthlyRegistrations, String> plate = new TableColumn<>("Plate #");
        plate.setCellValueFactory(new PropertyValueFactory<>("vehicle_plate"));

        TableColumn<MonthlyRegistrations, String> type = new TableColumn<>("Type");
        type.setCellValueFactory(new PropertyValueFactory<>("vehicle_type"));

        TableColumn<MonthlyRegistrations, String> ticket = new TableColumn<>("Ticket");
        ticket.setCellValueFactory(new PropertyValueFactory<>("ticket"));

        TableColumn<MonthlyRegistrations, String> startDate = new TableColumn<>("Start At");
        startDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));

        TableColumn<MonthlyRegistrations, String> endDate = new TableColumn<>("Expired At");
        endDate.setCellValueFactory(new PropertyValueFactory<>("end_date"));

        TableColumn<MonthlyRegistrations, String> addBy = new TableColumn<>("Add By");
        addBy.setCellValueFactory(new PropertyValueFactory<>("add_by"));

        TableColumn<MonthlyRegistrations, String> totalPayment = new TableColumn<>("Total Payment");
        totalPayment.setCellValueFactory(new PropertyValueFactory<>("total_payment"));

        tbMontlyTicket.getColumns().addAll(sse,plate,type,ticket,startDate,endDate,addBy,totalPayment);

        sse.setPrefWidth(100);
        plate.setPrefWidth(100);
        type.setPrefWidth(100);
        ticket.setPrefWidth(150);
        startDate.setPrefWidth(100);
        endDate.setPrefWidth(100);
        addBy.setPrefWidth(100);
        totalPayment.setPrefWidth(100);
    }
    @FXML
    void Connect_View() throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                DefineTableView();
                Statement st = conn.createStatement();
                String query = "SELECT * FROM registedVehicle";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    MonthlyRegistrations mt = new MonthlyRegistrations(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9));
                    tbMontlyTicket.getItems().add(mt);
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    @FXML
    void tbViewClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            int row = tbMontlyTicket.getFocusModel().getFocusedCell().getRow();
            Object ob = tbMontlyTicket.getItems().get(row);
            MonthlyRegistrations mt = (MonthlyRegistrations) ob;

            txtPlate.setText(mt.getVehicle_plate());
            cboType.setValue(mt.getVehicle_type());
            textTicket.setText(mt.getTicket());
            txtSSE.setText(mt.getSse());
        }
    }
    @FXML
    void addClick(MouseEvent event) throws SQLException {
        Connection conn = null;
        String type = cboType.getSelectionModel().getSelectedItem().toString();
        String ticket = textTicket.getText();
        int available = ParkingLot.getAvailableMTickets(type);
        float parkingFee = (type.equals("Car")) ? ParkingLot.getMonthlyCarFee() : ParkingLot.getMonthlyBikeFee();
        if(available>0) {
            try {
                conn = Database.getConnection();
                if (conn != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.FINISH, ButtonType.CANCEL);
                    alert.setTitle("Parking Fee");
                    alert.setContentText("Vehicle Type: " + type + "\n" +
                            "Parking Fee: " + parkingFee);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent()) {
                        if (result.get() == ButtonType.FINISH) {
                            Statement st = conn.createStatement();
                            String query2 = "INSERT INTO registedVehicle(ssen,vehicle_plate, vehicle_type,ticket_id,add_by,total_fee) VALUES (" +
                                    "'" + txtSSE.getText() + "',"+
                                    "'" + txtPlate.getText() + "'," +
                                    "'" + type + "'," +
                                    "'" + ticket + "'," +
                                    "'" + User.getUsername() + "', "+
                                    "'" + parkingFee + "')";
                            st.executeUpdate(query2);
                            conn.close();
                        } else {
                            alert.setContentText("Cancellation");
                        }
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Error: "+ex.getMessage());
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"",ButtonType.OK);
            alert.setContentText("Not enough ticket!" + type );
            alert.showAndWait();
        }
        cboType.getSelectionModel().clearSelection();
        txtPlate.clear();
        textTicket.setText("");
        txtSSE.clear();
        Connect_View();
    }
    @FXML
    public void updateClick(MouseEvent event) throws SQLException {
        int row = tbMontlyTicket.getFocusModel().getFocusedCell().getRow();
        Object ob = tbMontlyTicket.getItems().get(row);
        MonthlyRegistrations mt = (MonthlyRegistrations) ob;
        String oldPlate = mt.getVehicle_plate();
        Connection conn = null;
        String type = cboType.getSelectionModel().getSelectedItem().toString();
        String newPlate = txtPlate.getText();
        Alert alter = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        alter.setTitle("UPDATE?");
        alter.showAndWait();
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "UPDATE registedVehicle SET " +
                        "ssen = '"+txtSSE.getText()+"',"+
                        "vehicle_plate = '" + newPlate + "', " +
                        "vehicle_type = '" + type + "', " +
                        "ticket_id = '" + textTicket.getText() + "'" +
                        "WHERE vehicle_plate = '" + oldPlate + "'";
                st.executeUpdate(query);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        cboType.getSelectionModel().clearSelection();
        txtPlate.clear();
        textTicket.setText("");
        txtSSE.clear();
        Connect_View();
    }
    @FXML
    void deleteClick(MouseEvent event) throws SQLException {
        Alert alter = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        alter.setTitle("Confirmation Dialog");
        alter.setContentText("Are you sure you want to delete?");
        alter.showAndWait();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "DELETE FROM registedVehicle WHERE vehicle_plate = '" + txtPlate.getText() + "'";
                st.executeUpdate(query);
            }
        } catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        cboType.getSelectionModel().clearSelection();
        txtPlate.clear();
        textTicket.setText("");
        txtSSE.clear();
        Connect_View();
    }
    @FXML
    void carFeeChange(MouseEvent event){
        String newCarFee = carFee.getText();
        Connection conn = null;
        try{
            conn = Database.getConnection();
            if(conn != null){
                Statement st = conn.createStatement();
                String query = "update parkingLot set carMonthlyFee = "+ newCarFee +" where lot_id =1";
                int rowsAffected = st.executeUpdate(query);
                if(rowsAffected>0){
                    carFee.setText(newCarFee);
                }else {
                    System.out.println("Update failed. No rows affected");
                }
                conn.close();
            }
        }catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
    }
    @FXML
    void bikeFeeChange(MouseEvent event){
        String newBikeFee = bikeFee.getText();
        Connection conn = null;
        try{
            conn = Database.getConnection();
            if(conn != null){
                Statement st = conn.createStatement();
                String query = "update parkingLot set bikeMonthlyFee = "+ newBikeFee +" where lot_id =1";
                int rowsAffected = st.executeUpdate(query);
                if(rowsAffected>0){
                    bikeFee.setText(newBikeFee);
                }else {
                    System.out.println("Update failed. No rows affected");
                }
                conn.close();
            }
        }catch (Exception ex){
            System.out.println("Error: "+ex.getMessage());
        }
    }
    @FXML
    void getTicket(MouseEvent event) {
        Connection conn = null;
        String type = cboType.getSelectionModel().getSelectedItem().toString();
        try {
            conn = Database.getConnection();
            if (conn != null) {
                String query = "";
                if (type.equals("Car")) {
                    query = "SELECT TOP 1 * FROM ticket WHERE ticket_id LIKE 'MONTHLYCAR%' AND be_registed = 0";
                } else if (type.equals("Motorbike")) {
                    query = "SELECT TOP 1 * FROM ticket WHERE ticket_id LIKE 'MONTHLYBIKE%' AND be_registed = 0";
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
                    alert.setContentText("Vehicle Type can't be null");
                    alert.showAndWait();
                    return;
                }
                try (Statement st = conn.createStatement()) {
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        textTicket.setText(rs.getString("ticket_id"));
                    }
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    @FXML
    void clearClick(MouseEvent event){
        cboType.getSelectionModel().clearSelection();
        txtPlate.clear();
        textTicket.setText("");
        txtSSE.clear();
    }
    @FXML
    void searchClick2(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            getSearchInformation();
        }
    }
    @FXML
    void searchClick(MouseEvent event){
        getSearchInformation();
    }
    @FXML
    void getSearchInformation() {
        String searchText = txtSearch.getText();
        tbMontlyTicket.getItems().clear();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "SELECT * FROM registedVehicle WHERE ssen LIKE '%" + searchText + "%' OR vehicle_plate LIKE '%" + searchText + "%' OR " +
                        "ticket_id LIKE '%" + searchText + "%'";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    MonthlyRegistrations mt = new MonthlyRegistrations(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9));
                    tbMontlyTicket.getItems().add(mt);
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    @FXML
    void logoutClick(MouseEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.WARNING,"", ButtonType.OK,ButtonType.CANCEL);
        alert.setTitle("Log out confirm");
        alert.setContentText("Do you really want to log out?");
        alert.showAndWait();

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                Parent my_root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(my_root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    @FXML
    void parkingClick(MouseEvent event) throws IOException {
        Parent my_root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Parking-View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(my_root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void homeClick(MouseEvent event) throws IOException {
        Parent my_root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ParkingLot-View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(my_root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void empClick(MouseEvent event) throws IOException {
        Parent my_root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Employee-View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(my_root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void montlyTicketClick(MouseEvent event) throws IOException {
        Parent my_root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Register-View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(my_root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void historyClick(MouseEvent event) throws IOException {
        Parent my_root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("History-View.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(my_root);
        stage.setScene(scene);
        stage.show();
    }

}
