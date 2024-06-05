package com.example.demo8;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;

public class LotController {
    @FXML
    private Text carSlot_num;
    @FXML
    private Text bikeSlot_num;
    @FXML
    private TextField textField_CarSlot;
    @FXML
    private TextField textField_BikeSlot;
    @FXML
    private TextField textField_AddFee;
    @FXML
    private Text carFee;
    @FXML
    private Text bikeFee;
    @FXML
    private Text addFee;
    @FXML
    private TextField textField_CarFee;
    @FXML
    private TextField textField_BikeFee;
    @FXML
    private Text txtHello;

    @FXML
    void initialize(){
        String username = User.getUsername();
        txtHello.setText("Hello "+ username);
        carSlot_num.setText(String.valueOf(ParkingLot.getMaxSpotsforCar()));
        bikeSlot_num.setText(String.valueOf(ParkingLot.getMaxSpotsforBike()));
        carFee.setText(String.valueOf(ParkingLot.getFeeCar()));
        bikeFee.setText(String.valueOf(ParkingLot.getFeeBike()));
        addFee.setText(String.valueOf(ParkingLot.getAdditionFee()));
    }

    @FXML
    void bikeSlotChange(MouseEvent event) throws IOException {
        int newBikeNum = Integer.parseInt(textField_BikeSlot.getText());
        Connection conn = null;
        try{
            conn = Database.getConnection();
            if(conn != null){
                Statement st = conn.createStatement();
                String query =  "update parkingLot set car_num = "+ newBikeNum +" where lot_id =1";
                int rowsAffected = st.executeUpdate(query);
                if (rowsAffected > 0) {
                    bikeSlot_num.setText(String.valueOf(newBikeNum));
                } else {
                    System.out.println("Update failed. No rows affected.");
                }
                conn.close();
            }
        }catch (Exception ex){
            System.out.println("Error: "+ ex.getMessage());
        }
    }

    @FXML
    void carSlotChange(MouseEvent event) throws IOException{
        int newCarNum = Integer.parseInt(textField_CarSlot.getText());
        Connection conn = null;
        try{
            conn = Database.getConnection();
            if(conn != null){
                Statement st = conn.createStatement();
                String query =  "update parkingLot set car_num = "+ newCarNum +" where lot_id =1";
                int rowsAffected = st.executeUpdate(query);
                if (rowsAffected > 0) {
                    carSlot_num.setText(String.valueOf(newCarNum));
                } else {
                    System.out.println("Update failed. No rows affected.");
                }
                conn.close();
            }
        }catch (Exception ex){
            System.out.println("Error: "+ ex.getMessage());
        }
    }

    @FXML
    void carFeeChange(MouseEvent event) throws IOException{
        String newCarFee = textField_CarFee.getText();
        Connection conn = null;
        try{
            conn = Database.getConnection();
            if(conn != null){
                Statement st = conn.createStatement();
                String query = "update parkingLot set car_fee = "+ newCarFee +" where lot_id =1";
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
    void addFeeChange(MouseEvent event) throws IOException{
        String newAddFee = textField_AddFee.getText();
        Connection conn = null;
        try{
            conn = Database.getConnection();
            if(conn != null){
                Statement st = conn.createStatement();
                String query = "update parkingLot set addition_fee = "+ newAddFee +" where lot_id =1";
                int rowsAffected = st.executeUpdate(query);
                if(rowsAffected>0){
                    addFee.setText(newAddFee);
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
    void bikeFeeChange(MouseEvent event) throws IOException{
        String newBikeFee = textField_BikeFee.getText();
        Connection conn = null;
        try{
            conn = Database.getConnection();
            if(conn != null){
                Statement st = conn.createStatement();
                String query = "update parkingLot set bike_fee = "+ newBikeFee +" where lot_id =1";
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
