package com.example.demo8;

public class MonthlyRegistrations {
    private String sse;
    private String vehicle_plate;
    private String vehicle_type;
    private String ticket;
    private String start_date;
    private String end_date;
    private String add_by;
    private String total_payment;

    public MonthlyRegistrations(String sse, String vehicle_plate, String vehicle_type, String ticket, String start_date, String end_date, String add_by, String total_payment) {
        this.sse = sse;
        this.vehicle_plate = vehicle_plate;
        this.vehicle_type = vehicle_type;
        this.ticket = ticket;
        this.start_date = start_date;
        this.end_date = end_date;
        this.add_by = add_by;
        this.total_payment = total_payment;
    }

    public String getSse() {
        return sse;
    }

    public String getVehicle_plate() {
        return vehicle_plate;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public String getTicket() {
        return ticket;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getAdd_by() {
        return add_by;
    }

    public String getTotal_payment() {
        return total_payment;
    }
}
