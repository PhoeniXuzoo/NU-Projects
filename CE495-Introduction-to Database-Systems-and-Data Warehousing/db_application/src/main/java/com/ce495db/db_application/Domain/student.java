package com.ce495db.db_application.Domain;

public class student {
    private String Address;
    private int Id;
    private String Name;
    private String Password;

    public student () { }

    public student (String Address, int Id, String Name, String Password) {
        this.Address = Address;
        this.Id = Id;
        this.Name = Name;
        this.Password = Password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
