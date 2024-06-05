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

public class historyController {
    @FXML
    private Text txtHello;
    @FXML
    private TableView tbHistory;
    @FXML
    private TextField txtSearch;
    @FXML
    private Text parkedCar;
    @FXML
    private Text parkedBike;
    @FXML
    private TextField inputDay;
    @FXML
    private TextField inputMonth;
    @FXML
    private TextField inputYear;
    @FXML
    private Text txtRevenue;
    @FXML
    void initialize() throws SQLException {
        String username = User.getUsername();
        txtHello.setText("Hello "+ username);

        parkedCar.setText(String.valueOf(getTotalBike()));
        parkedBike.setText(String.valueOf(getTotalCar()));

        txtRevenue.setText(String.valueOf(getMonthlyRevenue()));

        Connect_View();
    }
    public float getMonthlyRevenue() {
        float revenue = 0;
        Connection conn = null;
        String query = "SELECT dbo.calculateTotalRevenue(GETDATE()) AS TotalRevenue";
        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                revenue = rs.getFloat("TotalRevenue");
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return revenue;
    }
    public int getTotalBike(){
        int totalBike =0;
        Connection conn =null;
        String query = "select count(*) as 'bike_count' from parkingHistory where vehicle_type = 'Motorbike'";
        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                totalBike = rs.getInt("bike_count");
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return totalBike;
    }
    public int getTotalCar(){
        int totalCar =0;
        Connection conn =null;
        String query = "select count(*) as 'car_count' from parkingHistory where vehicle_type = 'Car'";
        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                totalCar = rs.getInt("car_count");
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return totalCar;
    }
    @FXML
    void Connect_View() throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                DefineTableView();
                Statement st = conn.createStatement();
                String query = "SELECT * FROM parkingHistory";
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    parkedVehicle park = new parkedVehicle(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(10),
                            rs.getString(9));
                    tbHistory.getItems().add(park);
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    @FXML
    void DefineTableView(){
        tbHistory.setEditable(true);
        tbHistory.getColumns().clear();
        tbHistory.getItems().clear();

        TableColumn<ParkingVehicle, String> plate = new TableColumn<>("Plate #");
        plate.setCellValueFactory(new PropertyValueFactory<>("plate_num"));

        TableColumn<ParkingVehicle, String> type = new TableColumn<>("Type");
        type.setCellValueFactory(new PropertyValueFactory<>("vehicle_type"));

        TableColumn<ParkingVehicle, String> ticket = new TableColumn<>("Ticket");
        ticket.setCellValueFactory(new PropertyValueFactory<>("ticket"));

        TableColumn<ParkingVehicle, String> entryTime = new TableColumn<>("Entry");
        entryTime.setCellValueFactory(new PropertyValueFactory<>("entry_time"));

        TableColumn<ParkingVehicle, String> releaseTime = new TableColumn<>("Release");
        releaseTime.setCellValueFactory(new PropertyValueFactory<>("release_time"));

        TableColumn<ParkingVehicle, String> addBy = new TableColumn<>("Add by");
        addBy.setCellValueFactory(new PropertyValueFactory<>("add_by"));

        TableColumn<ParkingVehicle, String> releaseBy = new TableColumn<>("Release by");
        releaseBy.setCellValueFactory(new PropertyValueFactory<>("release_by"));

        TableColumn<ParkingVehicle, String> totalPayment = new TableColumn<>("Total Payment");
        totalPayment.setCellValueFactory(new PropertyValueFactory<>("total_payment"));

        TableColumn<ParkingVehicle, String> vehiclePicture = new TableColumn<>("Picture");
        vehiclePicture.setCellValueFactory(new PropertyValueFactory<>("vehicle_picture"));

        tbHistory.getColumns().addAll(plate,type,ticket,entryTime,releaseTime,addBy,releaseBy,totalPayment,vehiclePicture);

        plate.setPrefWidth(100);
        type.setPrefWidth(100);
        ticket.setPrefWidth(100);
        entryTime.setPrefWidth(100);
        addBy.setPrefWidth(100);
        releaseTime.setPrefWidth(100);
        releaseBy.setPrefWidth(100);
        totalPayment.setPrefWidth(100);
        vehiclePicture.setPrefWidth(100);

    }
    @FXML
    void findClick(MouseEvent event){
        String day = inputDay.getText();
        String month = inputMonth.getText();
        String year = inputYear.getText();
        tbHistory.getItems().clear();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "SELECT * FROM parkingHistory WHERE DAY(entry_time) = '" + day + "' OR MONTH(entry_time) = '" + month + "' OR YEAR(entry_time) = '" + year + "'";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    parkedVehicle park = new parkedVehicle(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(10),
                            rs.getString(9));
                    tbHistory.getItems().add(park);
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    @FXML
    void okClick(MouseEvent event) throws SQLException {
        Connect_View();
        inputDay.clear();
        inputMonth.clear();
        inputYear.clear();
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
        tbHistory.getItems().clear();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select * from parkingHistory where vehicle_plate like '%" + searchText + "%' or" +
                        " ticket_id like '%" +searchText +"%'";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    parkedVehicle park = new parkedVehicle(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(10),
                            rs.getString(9));
                    tbHistory.getItems().add(park);
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    // left button
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
