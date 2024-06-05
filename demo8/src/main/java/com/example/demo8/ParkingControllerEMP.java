package com.example.demo8;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;

public class ParkingControllerEMP {
    @FXML
    private Text txtHello;
    @FXML
    private TextField txtPlate;
    @FXML
    private TextField monthyTicket;
    @FXML
    private ComboBox cboType;
    @FXML
    private TableView tbParking;
    @FXML
    private Text car_left;
    @FXML
    private Text bike_left;
    @FXML
    private TextField txtSearch;
    @FXML
    private Text textTicket;
    @FXML
    private ImageView img;
    @FXML
    private String uploadedPicturePath;

    @FXML
    void initialize() throws SQLException {
        String username = User.getUsername();
        txtHello.setText("Hello "+ username);

        String type[] = {"Car","Motorbike"};

        cboType.getItems().addAll(type);

        Connect_View();

    }

    @FXML
    void uploadImage(MouseEvent event){
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                Image image = new Image(new FileInputStream(file.getPath()));
                img.setImage(image);
                img.setPreserveRatio(true);

                uploadedPicturePath = file.getPath();

            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    @FXML
    void getTicket(MouseEvent event) {
        if(Objects.equals(monthyTicket.getText(), "")) {
            Connection conn = null;
            String type = cboType.getSelectionModel().getSelectedItem().toString();
            try {
                conn = Database.getConnection();
                if (conn != null) {
                    String query = "";
                    if (type.equals("Car")) {
                        query = "SELECT TOP 1 * FROM ticket WHERE ticket_id LIKE 'DAILYCAR%' AND be_used = 0";
                    } else if (type.equals("Motorbike")) {
                        query = "SELECT TOP 1 * FROM ticket WHERE ticket_id LIKE 'DAILYBIKE%' AND be_used = 0";
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
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"",ButtonType.OK);
            alert.setContentText("Just choose 1 of 2: Daily Ticket or Monthly Ticket" );
            alert.showAndWait();
        }
    }

    @FXML
    void DefineTableView(){
        tbParking.setEditable(true);
        tbParking.getColumns().clear();
        tbParking.getItems().clear();

        TableColumn<ParkingVehicle, String> plate = new TableColumn<>("Plate #");
        plate.setCellValueFactory(new PropertyValueFactory<>("plate_num"));

        TableColumn<ParkingVehicle, String> type = new TableColumn<>("Type");
        type.setCellValueFactory(new PropertyValueFactory<>("vehicle_type"));

        TableColumn<ParkingVehicle, String> ticket = new TableColumn<>("Ticket");
        ticket.setCellValueFactory(new PropertyValueFactory<>("ticket"));

        TableColumn<ParkingVehicle, String> entryTime = new TableColumn<>("Entry");
        entryTime.setCellValueFactory(new PropertyValueFactory<>("entry_time"));

        TableColumn<ParkingVehicle, String> addBy = new TableColumn<>("Add by");
        addBy.setCellValueFactory(new PropertyValueFactory<>("add_by"));

        TableColumn<ParkingVehicle, String> vehiclePicture = new TableColumn<>("Picture");
        vehiclePicture.setCellValueFactory(new PropertyValueFactory<>("vehicle_picture"));

        tbParking.getColumns().addAll(plate,type,ticket,entryTime,addBy,vehiclePicture);

        plate.setPrefWidth(100);
        type.setPrefWidth(100);
        ticket.setPrefWidth(100);
        entryTime.setPrefWidth(100);
        addBy.setPrefWidth(100);
        vehiclePicture.setPrefWidth(100);

    }
    @FXML
    void Connect_View() throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                DefineTableView();

                Statement st = conn.createStatement();
                String query = "SELECT * FROM parkingVehicle";
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    ParkingVehicle park = new ParkingVehicle(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7));
                    tbParking.getItems().add(park);
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        int availableSpotsforCar = ParkingLot.getAvailableSpots("Car");
        int availableSpotsforBike = ParkingLot.getAvailableSpots("Motorbike");

        car_left.setText(String.valueOf(availableSpotsforCar));
        bike_left.setText(String.valueOf(availableSpotsforBike));
    }
    @FXML
    void tbViewClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            int row = tbParking.getFocusModel().getFocusedCell().getRow();
            Object ob = tbParking.getItems().get(row);
            ParkingVehicle park = (ParkingVehicle) ob;

            String ticket = park.getTicket();

            if(ticket.startsWith("MONTHLY")){
                monthyTicket.setText(ticket);
            }else {
                textTicket.setText(park.getTicket());
            }
            txtPlate.setText(park.getPlate_num());
            cboType.setValue(park.getVehicle_type());

            try {
                if (park.getVehicle_picture() != null) {
                    Image image = new Image(new FileInputStream(park.getVehicle_picture()));
                    img.setImage(image);
                    img.setPreserveRatio(true);
                } else {
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
    // them xe vao bai
    @FXML
    void addClick(MouseEvent event) throws SQLException {
        Connection conn = null;
        String type = cboType.getSelectionModel().getSelectedItem().toString();
        String addBy = User.getUsername();
        String picture = uploadedPicturePath;
        String ticket = textTicket.getText();
        String mTicket = monthyTicket.getText();
        int availableSpots = ParkingLot.getAvailableSpots(type);
        if (availableSpots > 0) {
            try {
                conn = Database.getConnection();
                if (conn != null) {
                    if (!mTicket.isEmpty()) { // nguoi dung nhap ve thang
                        boolean check = isValidMonthly(txtPlate.getText(), mTicket, type);
                        if (check) {
                            // them vao (su dung ve thang)
                            Statement st1 = conn.createStatement();
                            String query1 = "INSERT INTO parkingVehicle (vehicle_plate, vehicle_type, ticket_id, add_by, picture) VALUES (" +
                                    "'" + txtPlate.getText() + "'," +
                                    "'" + type + "'," +
                                    "'" + mTicket + "'," +
                                    "'" + addBy + "'," +
                                    "'" + picture + "')";
                            st1.executeUpdate(query1);
                            conn.close();
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                            alert.setContentText("Monthly Ticket");
                            alert.showAndWait();
                        } else {
                            // ve khong hop le
                            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                            alert.setContentText("Invalid Monthly Ticket");
                            alert.showAndWait();
                        }
                    } else if (!ticket.isEmpty()) { // nguoi dung nhap ve ngay
                        // them vao (su dung ve ngay)
                        Statement st2 = conn.createStatement();
                        String query2 = "INSERT INTO parkingVehicle(vehicle_plate, vehicle_type,ticket_id, add_by, picture) VALUES (" +
                                "'" + txtPlate.getText() + "'," +
                                "'" + type + "'," +
                                "'" + ticket + "'," +
                                "'" + addBy + "'," +
                                "'" + picture + "')";
                        st2.executeUpdate(query2);
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
            alert.setContentText("Parking Lot is full! No available spots for" + type);
            alert.showAndWait();
        }

        Connect_View();
        cboType.getSelectionModel().clearSelection();
        txtPlate.clear();
        textTicket.setText("");
        monthyTicket.clear();
        img.setImage(null);
    }


    // tinh phi do xe, rule: duration> 24 --> thu them phu phi
    float calculateParkingFee(String vehicle_type, int duration_hour){
        float parkingFee = 0;
        float additionFee = ParkingLot.getAdditionFee();
        float carFee = ParkingLot.getFeeCar();
        float bikeFee = ParkingLot.getFeeBike();
        if(vehicle_type.equals("Car")){
            parkingFee = (duration_hour ==0 )? carFee*1 : carFee*duration_hour;
        }else {
            parkingFee = (duration_hour == 0)? bikeFee*1 : bikeFee*duration_hour;
        }

        if(duration_hour>24)
            return (parkingFee + additionFee);

        return parkingFee;
    }

    private boolean isValidMonthly(String plate,String ticket, String type) throws SQLException {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                DefineTableView();
                Statement st = conn.createStatement();
                String query = "SELECT * FROM registedVehicle where end_at > getdate()";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    String Plate = rs.getString("vehicle_plate");
                    String Ticket = rs.getString("ticket_id");
                    String Type = rs.getString("vehicle_type");
                    if(plate.equals(Plate) && ticket.equals(Ticket) && type.equals(Type)){
                        return true;
                    }
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return false;
    }
    private void deleteVehicle() {
        try {
            Connection conn = Database.getConnection();
            Statement st = conn.createStatement();
            String query1 = "DELETE FROM parkingVehicle WHERE vehicle_plate = '" + txtPlate.getText() + "'";
            st.executeUpdate(query1);
            conn.close();

            Connect_View();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"",ButtonType.OK);
            alert.setContentText("Delete Successful");
            alert.showAndWait();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    @FXML
    void releaseClick(MouseEvent event) throws SQLException {
        Connection conn = null;
        int duration_hours = 0;
        String vehicle_type = cboType.getSelectionModel().getSelectedItem().toString();
        boolean check2 = isValidMonthly(txtPlate.getText(),monthyTicket.getText(),vehicle_type);
        if(check2){
            conn = Database.getConnection();
            deleteVehicle();
            Statement st0 = conn.createStatement();
            String query0 = "UPDATE parkingHistory " +
                    "SET exit_time = GETDATE(), " +
                    "    release_by = '" + User.getUsername() + "' " +
                    "WHERE vehicle_plate = '" + txtPlate.getText() + "'";
            st0.executeUpdate(query0);
            conn.close();
            Connect_View();
            cboType.getSelectionModel().clearSelection();
            txtPlate.clear();
            textTicket.setText("");
            monthyTicket.clear();
            img.setImage(null);
            return;
        }
        try {
            conn = Database.getConnection();
            Statement st1 = conn.createStatement();
            Statement st = conn.createStatement();
            String query2 = "SELECT " +
                    "    DATEDIFF(HOUR, entry_time, GETDATE()) AS duration_hours " +
                    "FROM parkingVehicle where vehicle_plate = '" + txtPlate.getText() + "'";
            ResultSet rs = st.executeQuery(query2);
            while (rs.next()) {
                duration_hours = rs.getInt("duration_hours");
            }
            float parkingFee = calculateParkingFee(vehicle_type, duration_hours);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.FINISH, ButtonType.CANCEL);
            alert.setTitle("Parking Fee");
            alert.setContentText("ParkingHour: " + duration_hours + "\n" +
                    "Parking Fee: " + parkingFee);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.FINISH) {
                    conn = Database.getConnection();
                    deleteVehicle();
                    st = conn.createStatement();
                    String query = "UPDATE parkingHistory " +
                            "SET exit_time = GETDATE(), " +
                            "    release_by = '" + User.getUsername() + "', " +
                            "    total_fee = " + parkingFee + " " +
                            "WHERE vehicle_plate = '" + txtPlate.getText() + "'";
                    st.executeUpdate(query);
                    conn.close();
                } else {
                    alert.setContentText("Release Cancellation");
                }
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.CANCEL);
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.showAndWait();
        }
        Connect_View();
        cboType.getSelectionModel().clearSelection();
        txtPlate.clear();
        textTicket.setText("");
        monthyTicket.clear();
        img.setImage(null);
    }

    @FXML
    void clearClick(MouseEvent event){
        cboType.getSelectionModel().clearSelection();
        txtPlate.clear();
        textTicket.setText("");
        monthyTicket.clear();
        img.setImage(null);
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
        tbParking.getItems().clear();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select * from parkingVehicle where vehicle_plate like '%" + searchText + "%' or" +
                        " ticket_id like '%" +searchText +"%'";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    ParkingVehicle park = new ParkingVehicle(
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7));
                    tbParking.getItems().add(park);
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
