package com.example.demo8;

public class Employee {
    private String id;
    private String first_name;
    private String last_name;
    private String emp_gender;
    private String emp_address;
    private String phone_number;
    private String emp_position;
    private String emp_shift;
    private String manager_id;
    private String emp_avatar;
    private String emp_pass;

    public Employee(String id, String first_name, String last_name, String emp_gender, String emp_address, String phone_number, String emp_position, String emp_shift, String manager_id, String emp_avatar, String emp_pass) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.emp_gender = emp_gender;
        this.emp_address = emp_address;
        this.phone_number = phone_number;
        this.emp_position = emp_position;
        this.emp_shift = emp_shift;
        this.manager_id = manager_id;
        this.emp_avatar = emp_avatar;
        this.emp_pass = emp_pass;
    }

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmp_gender() {
        return emp_gender;
    }

    public String getEmp_address() {
        return emp_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmp_position() {
        return emp_position;
    }

    public String getEmp_shift() {
        return emp_shift;
    }

    public String getManager_id() {
        return manager_id;
    }

    public String getEmp_avatar() {
        return emp_avatar;
    }

    public String getEmp_pass() {
        return emp_pass;
    }
}
