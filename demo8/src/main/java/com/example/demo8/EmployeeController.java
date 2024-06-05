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

public class EmployeeController {
    @FXML
    TableView tbEmployee;
    @FXML
    ComboBox cboGender;
    @FXML
    ComboBox cboPosition;
    @FXML
    ComboBox cboShift;

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtFname;
    @FXML
    private TextField txtLname;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtManagerID;
    @FXML
    private TextField txtPass;
    @FXML
    private Text txtHello;

    public void initialize() {
        String username = User.getUsername();
        txtHello.setText("Hello "+ username);
        //for comboBox
        String[] genderArr = {"Male", "Female", "Other"};
        cboGender.getItems().addAll(genderArr);
        String[] positionArr = {"Parking Checker", "Parking Manager"};
        cboPosition.getItems().addAll(positionArr);
        String[] shiftArr = {"6:00 - 14:00", "14:00 - 22:00", "22:00 - 6:00"};
        cboShift.getItems().addAll(shiftArr);

        Connect_View();
    }

    @FXML
    void DefineTableView() {
        // define table
        tbEmployee.setEditable(true);
        tbEmployee.getColumns().clear();
        tbEmployee.getItems().clear();

        TableColumn<Employee, String> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Employee, String> fname = new TableColumn<>("First Name");
        fname.setCellValueFactory(new PropertyValueFactory<>("first_name"));

        TableColumn<Employee, String> lname = new TableColumn<>("Last Name");
        lname.setCellValueFactory(new PropertyValueFactory<>("last_name"));

        TableColumn<Employee, String> gender = new TableColumn<>("Gender");
        gender.setCellValueFactory(new PropertyValueFactory<>("emp_gender"));

        TableColumn<Employee, String> address = new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<>("emp_address"));

        TableColumn<Employee, String> phone = new TableColumn<>("Phone #");
        phone.setCellValueFactory(new PropertyValueFactory<>("phone_number"));

        TableColumn<Employee, String> position = new TableColumn<>("Position");
        position.setCellValueFactory(new PropertyValueFactory<>("emp_position"));

        TableColumn<Employee, String> shift = new TableColumn<>("Shift");
        shift.setCellValueFactory(new PropertyValueFactory<>("emp_shift"));

        TableColumn<Employee, String> managerid = new TableColumn<>("Manager ID");
        managerid.setCellValueFactory(new PropertyValueFactory<>("manager_id"));

        TableColumn<Employee, String> password = new TableColumn<>("Password");
        password.setCellValueFactory(new PropertyValueFactory<>("emp_pass"));

        TableColumn<Employee, String> avatar = new TableColumn<>("Avatar");
        avatar.setCellValueFactory(new PropertyValueFactory<>("emp_avatar"));

        tbEmployee.getColumns().addAll(id, fname, lname, gender, address, phone, position, shift, managerid,password, avatar);


        id.setPrefWidth(30);
        fname.setPrefWidth(80);
        lname.setPrefWidth(80);
        gender.setPrefWidth(50);
        address.setPrefWidth(60);
        phone.setPrefWidth(60);
        position.setPrefWidth(80);
        shift.setPrefWidth(50);
        managerid.setPrefWidth(70);
        password.setPrefWidth(60);
        avatar.setPrefWidth(50);

    }


    @FXML
    ImageView img;
    @FXML
    private String uploadedImagePath;

    @FXML
    void uploadImage(MouseEvent event) {
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                Image image = new Image(new FileInputStream(file.getPath()));
                img.setImage(image);
                img.setPreserveRatio(true);

                uploadedImagePath = file.getPath();

            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    @FXML
    void clearClick(MouseEvent event) {
        txtID.clear();
        txtFname.clear();
        txtLname.clear();
        cboGender.getSelectionModel().clearSelection();
        txtAddress.clear();
        txtPhone.clear();
        cboPosition.getSelectionModel().clearSelection();
        cboShift.getSelectionModel().clearSelection();
        txtManagerID.clear();
        txtPass.clear();
        img.setImage(null);
    }


    @FXML
    void Connect_View() {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                DefineTableView();

                Statement m_Statement = conn.createStatement();
                String query = "SELECT * FROM employee";
                ResultSet rs = m_Statement.executeQuery(query);

                while (rs.next()) {
                    Employee emp = new Employee(rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9),
                            rs.getString(10),
                            rs.getString(11));
                    tbEmployee.getItems().add(emp);
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    @FXML
    void addClick(MouseEvent event) {
        Connection conn = null;
        String gender = cboGender.getSelectionModel().getSelectedItem().toString();
        String position = cboPosition.getSelectionModel().getSelectedItem().toString();
        String shift = cboShift.getSelectionModel().getSelectedItem().toString();
        String avatar = uploadedImagePath;

        Alert alter = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);

        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                Statement st2 = conn.createStatement();
                String checkManagerIdQuery = "SELECT emp_id FROM employee WHERE emp_id = '" + txtManagerID.getText() + "'";
                ResultSet resultSet = st.executeQuery(checkManagerIdQuery);
                String checkEmployeeID = "SELECT emp_id FROM employee WHERE emp_id = '" + txtID.getText() + "'";
                ResultSet resultSet1 = st2.executeQuery(checkEmployeeID);
                if (resultSet1.next()) {
                    alter.setTitle("ERROR!!!");
                    alter.setContentText("Error: " + txtID.getText() + " has been ID of other");
                    alter.showAndWait();
                } else if (position.equals("Parking Manager")) {
                    txtManagerID.setText(null);
                    String query = "INSERT INTO employee (emp_id, first_name, last_name, emp_gender, emp_address, emp_phone, emp_position, emp_shift, password_login, emp_avatar) " +
                            "VALUES ('" + txtID.getText() + "', '" + txtFname.getText() + "','" + txtLname.getText() + "','" + gender + "', '" +
                            txtAddress.getText() + "', '" + txtPhone.getText() + "', '" + position + "', '" + shift + "', '" +
                            txtPass.getText() + "','" + avatar + "')";
                    st.executeUpdate(query);
                } else if (resultSet.next()) {
                    String query = "INSERT INTO employee (emp_id, first_name, last_name, emp_gender, emp_address, emp_phone, emp_position, emp_shift, manager_id, password_login, emp_avatar) " +
                            "VALUES ('" + txtID.getText() + "', '" + txtFname.getText() + "','" + txtLname.getText() + "','" + gender + "', '" +
                            txtAddress.getText() + "', '" + txtPhone.getText() + "', '" + position + "', '" + shift + "', '" +
                            txtManagerID.getText() + "', '" + txtPass.getText() + "','" + avatar + "')";
                    st.executeUpdate(query);
                } else {
                    alter.setTitle("ERROR!!!");
                    alter.setContentText("Error: ManagerID " + txtManagerID.getText() + " does not exit!");
                    alter.showAndWait();
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("error connections " + ex);
        }
        Connect_View();
    }

    @FXML
    public void updateClick(MouseEvent event) {
        Connection conn = null;
        String gender = cboGender.getSelectionModel().getSelectedItem().toString();
        String position = cboPosition.getSelectionModel().getSelectedItem().toString();
        String shift = cboShift.getSelectionModel().getSelectedItem().toString();
        String avatar = uploadedImagePath;

        Alert alter = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        alter.setTitle("UPDATE?");
        alter.setContentText("Are you really want to update?");
        alter.showAndWait();

        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String managerId = txtManagerID.getText();
                String checkManagerIdQuery = "SELECT emp_id FROM employee WHERE emp_id = '" + managerId + "'";
                ResultSet resultSet = st.executeQuery(checkManagerIdQuery);

                if (resultSet.next()) {

                    String query = "UPDATE employee SET " +
                            "first_name = '" + txtFname.getText() + "', " +
                            "last_name = '" + txtLname.getText() + "', " +
                            "emp_gender = '" + gender + "', " +
                            "emp_address = '" + txtAddress.getText() + "', " +
                            "emp_phone = '" + txtPhone.getText() + "', " +
                            "emp_position = '" + position + "', " +
                            "emp_shift = '" + shift + "', " +
                            "manager_id = '" + txtManagerID.getText() + "', " +
                            "password_login = '" + txtPass.getText() + "' , " +
                            "emp_avatar = '" + avatar + "' " +
                            "WHERE emp_id = '" + txtID.getText() + "'";
                    st.executeUpdate(query);
                } else {
                    alter.setTitle("ERROR!!!");
                    alter.setContentText("Error: ManagerID " + txtManagerID.getText() + " does not exit!");
                    alter.showAndWait();
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        Connect_View();
    }

    @FXML
    void deleteClick(MouseEvent event) {
        Alert alter = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        alter.setTitle("Confirmation Dialog");
        alter.setContentText("Are you sure you want to delete this employee?");
        alter.showAndWait();
        Connection conn = null;

        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            String query = "DELETE FROM employee WHERE emp_id = '" + txtID.getText() + "'";
            st.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        Connect_View();
    }

    @FXML
    void tbViewClick(MouseEvent event) {

        if (event.getClickCount() == 2) {
            int row = tbEmployee.getFocusModel().getFocusedCell().getRow();
            Object ob = tbEmployee.getItems().get(row);
            Employee emp = (Employee) ob;

            txtID.setText(emp.getId());
            txtID.setEditable(false);
            txtFname.setText(emp.getFirst_name());
            txtLname.setText(emp.getLast_name());
            cboGender.setValue(emp.getEmp_gender());
            txtAddress.setText(emp.getEmp_address());
            txtPhone.setText(emp.getPhone_number());
            cboPosition.setValue(emp.getEmp_position());
            cboShift.setValue(emp.getEmp_shift());
            txtManagerID.setText(emp.getManager_id());
            txtPass.setText(emp.getEmp_pass());

            try{
                Image image = new Image(new FileInputStream(emp.getEmp_avatar()));
                img.setImage(image);
                img.setPreserveRatio(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    TextField txtSearch;
    @FXML
    void getSearchInformation(){
        String searchText = txtSearch.getText();
        tbEmployee.getItems().clear();
        Connection conn = null;

        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "SELECT * FROM employee WHERE emp_id LIKE '%" + searchText + "%' OR first_name LIKE '%" + searchText + "%' OR last_name LIKE '%" + searchText + "%'";
                ResultSet resultSet = st.executeQuery(query);
                while (resultSet.next()) {
                    Employee employee = new Employee(
                            resultSet.getString("emp_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("emp_gender"),
                            resultSet.getString("emp_address"),
                            resultSet.getString("emp_phone"),
                            resultSet.getString("emp_position"),
                            resultSet.getString("emp_shift"),
                            resultSet.getString("manager_id"),
                            resultSet.getString("password_login"),
                            resultSet.getString("emp_avatar")
                    );
                    tbEmployee.getItems().add(employee);
                }
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    @FXML
    void searchClick(MouseEvent event) {
        getSearchInformation();
    }

    @FXML
    void searchClick2(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            getSearchInformation();
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
