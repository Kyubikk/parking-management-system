package com.example.demo8;

public class ParkingVehicle {
    private String plate_num;
    private String vehicle_type;
    private String ticket;
    private String entry_time;
    private String add_by;
    private String vehicle_picture;

    public ParkingVehicle(String plate_num, String vehicle_type, String ticket, String entry_time, String add_by, String vehicle_picture) {
        this.plate_num = plate_num;
        this.vehicle_type = vehicle_type;
        this.ticket = ticket;
        this.entry_time = entry_time;
        this.add_by = add_by;
        this.vehicle_picture = vehicle_picture;
    }

    public String getPlate_num() {
        return plate_num;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public String getTicket() {
        return ticket;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public String getAdd_by() {
        return add_by;
    }

    public String getVehicle_picture() {
        return vehicle_picture;
    }
}
