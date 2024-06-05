package com.example.demo8;

import com.example.demo8.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class LoginController {

    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPass;
    private boolean isManager(String username, String password) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select emp_id, password_login from employee where manager_id is null";
                ResultSet rs = st.executeQuery(query);

                while(rs.next()) {
                    String mn_user = rs.getString("emp_id");
                    String mn_password = rs.getString("password_login");
                    if (username.equals(mn_user) && password.equals(mn_password)){
                        return true;
                    }
                }
                conn.close();
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
        return false;
    }

    public boolean isEmployee(String username, String password){
        Connection conn = null;
        try{
            conn = Database.getConnection();
            if(conn != null){
                Statement st = conn.createStatement();
                String query =  "select emp_id, password_login from employee where manager_id is not null";
                ResultSet rs = st.executeQuery(query);

                while (rs.next()){
                    String emp_user = rs.getString("emp_id");
                    String emp_password = rs.getString("password_login");
                    if(username.equals(emp_user) && password.equals(emp_password)){
                        return true;
                    }
                }
                conn.close();
            }
        }catch (Exception ex){
            System.out.println("Error: "+ ex.getMessage());
            return false;
        }
        return false;
    }

    public void loginClick(MouseEvent event) throws IOException {
        String username = txtUser.getText();
        String password = txtPass.getText();

        Alert alter = new Alert(Alert.AlertType.WARNING,"", ButtonType.OK);

        boolean checkMN = isManager(username,password);
        boolean checkEMP = isEmployee(username,password);

        if(checkMN){
            try{
                User.setUsername(username);
                Parent my_root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Parking-View.fxml")));
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(my_root);
                stage.setScene(scene);
                stage.setTitle("Parking management system - Manager");
                stage.show();
            }catch (Exception e){
                System.out.println("Cant load scene!");
            }
        }else if(checkEMP) {
            try {

                User.setUsername(username);

                Parent my_root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Parking-EmpView.fxml")));
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(my_root);
                stage.setScene(scene);
                stage.setTitle("Parking management system - Employee");
                stage.show();
            }catch (Exception ex){
                System.out.println("cant load scene!");
            }
        }else{
            alter.setTitle("ERROR!!!");
            alter.setContentText("Wrong login information!");
            alter.showAndWait();
        }
    }
}