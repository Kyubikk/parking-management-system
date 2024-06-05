package com.example.demo8;

import java.sql.*;

public class ParkingLot {
    public static int getMaxSpotsforCar() {
        int car_num = 0;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select car_num from parkingLot";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    car_num = rs.getInt("car_num");
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return car_num;
    }

    public static int getMaxSpotsforBike() {
        int bike_num = 0;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select bike_num from parkingLot";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    bike_num = rs.getInt("bike_num");
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return bike_num;
    }


    public static float getFeeCar() {
        float car_fee = 0;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select car_fee from parkingLot";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    car_fee = rs.getFloat("car_fee");
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return car_fee;
    }

    public static float getFeeBike() {
        float bike_fee = 0;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select bike_fee from parkingLot";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    bike_fee = rs.getFloat("bike_fee");
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return bike_fee;
    }

    public static float getAdditionFee() {
        float additionFee = 0;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select addition_fee from parkingLot";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    additionFee = rs.getFloat("addition_fee");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return additionFee;
    }

    public static float getMonthlyCarFee() {
        float monthlyCarFee = 0;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select carMonthlyFee from parkingLot";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    monthlyCarFee = rs.getFloat("carMonthlyFee");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return monthlyCarFee;
    }

    public static float getMonthlyBikeFee() {
        float monthlyBikeFee = 0;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select bikeMonthlyFee from parkingLot";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    monthlyBikeFee = rs.getFloat("bikeMonthlyFee");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return monthlyBikeFee;
    }

    public static int carParkingNum() {
        Connection conn = null;
        int carParkingNum = 0;
        String query = "select count(*)as 'car_count' from parkingVehicle where vehicle_type = 'Car'";
        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                carParkingNum = rs.getInt("car_count");
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return carParkingNum;
    }

    public static int bikeParkingNum() {
        Connection conn = null;
        int bikeParkingNum = 0;
        String query = "select count(*)as 'bike_count' from parkingVehicle where vehicle_type = 'Motorbike'";
        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                bikeParkingNum = rs.getInt("bike_count");
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return bikeParkingNum;
    }

    public static int getAvailableSpots(String vehicle_type) {
        int availableSpots = 0;
        if (vehicle_type.equals("Car")) {
            availableSpots = getMaxSpotsforCar() - carParkingNum();
        } else if (vehicle_type.equals("Motorbike")) {
            availableSpots = getMaxSpotsforBike() - bikeParkingNum();
        } else {
            System.out.println("Invalid vehicle type!");
        }
        return availableSpots;
    }

    public static int totalCarMTicket() {
        int totalCarMTicket = 0;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select totalCarMTicket from parkingLot";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    totalCarMTicket = rs.getInt("totalCarMTicket");
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return totalCarMTicket;
    }

    public static int totalBikeMTicket() {
        int totalBikeMTicket = 0;
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn != null) {
                Statement st = conn.createStatement();
                String query = "select totalBikeMTicket from parkingLot";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    totalBikeMTicket = rs.getInt("totalBikeMTicket");
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return totalBikeMTicket;
    }

    public static int carMonthyUsed() {
        Connection conn = null;
        int carMonthyUsed = 0;
        String query = "select count(*) as 'car_count' from registedVehicle where vehicle_type = 'Car'";
        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                carMonthyUsed = rs.getInt("car_count");
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return carMonthyUsed;
    }
    public static int bikeMonthyUsed() {
        Connection conn = null;
        int bikeMonthyUsed = 0;
        String query = "select count(*) as 'bike_count' from registedVehicle where vehicle_type = 'Motorbike'";
        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                bikeMonthyUsed = rs.getInt("bike_count");
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return bikeMonthyUsed;
    }
    public static int getAvailableMTickets(String vehicle_type) {
        int availableMTickets = 0;
        if (vehicle_type.equals("Car")) {
            availableMTickets = totalCarMTicket() - carMonthyUsed();
        } else if (vehicle_type.equals("Motorbike")) {
            availableMTickets = totalBikeMTicket() - bikeMonthyUsed();
        } else {
            System.out.println("Invalid vehicle type!");
        }
        return availableMTickets;
    }

    public static int getTotalParkedCar(){
        int totalParkedCar =0;
        Connection conn =null;
        String query = "select count(*) as 'car_count' from parkingHistory where vehicle_type = 'Car'";
        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                totalParkedCar = rs.getInt("car_count");
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return totalParkedCar;
    }
    public static int getTotalParkedBike(){
        int totalParkedBike =0;
        Connection conn =null;
        String query = "select count(*) as 'bike_count' from parkingHistory where vehicle_type = 'Motorbike'";
        try {
            conn = Database.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                totalParkedBike = rs.getInt("bike_count");
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return totalParkedBike;
    }

}
