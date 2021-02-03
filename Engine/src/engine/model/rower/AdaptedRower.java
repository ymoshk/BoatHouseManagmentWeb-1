package engine.model.rower;

import engine.model.Model;

import java.util.List;


public class AdaptedRower extends Model {
    private String serialNumber;
    private String name;
    private int age;
    private List<String> notes;
    private String phoneNumber;
    private String email;
    private String password;
    private Rower.eRowerRank rank;
    private String joinDate;
    private String expirationDate;
    private String[] privateBoatsSerialNumbers;
    private boolean isAdmin;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rower.eRowerRank getRank() {
        return rank;
    }

    public void setRank(Rower.eRowerRank rank) {
        this.rank = rank;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String[] getPrivateBoatsSerialNumbers() {
        return privateBoatsSerialNumbers;
    }

    public void setPrivateBoatsSerialNumbers(String[] privateBoatsSerialNumbers) {
        this.privateBoatsSerialNumbers = privateBoatsSerialNumbers;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
