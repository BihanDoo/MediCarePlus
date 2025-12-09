package model;

public class Patient {
    private int patientId;
    private String name;
    private String dateOfBirth; // Format: YYYY-MM-DD
    private String gender;
    private String phoneNumber;
    private String email;
    private String address;
    private String medicalHistory;

    // Constructor
    public Patient(int patientId, String name, String dateOfBirth, String gender,
                   String phoneNumber, String email, String address, String medicalHistory) {
        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.medicalHistory = medicalHistory;
    }

    // Default
    public Patient() {
    }

    // Getter and Setter methods for each field

    // Patient ID
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Date of Birth
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Gender
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Phone Number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Medical History
    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    @Override
    public String toString() {
        return "Patient ID: " + patientId + " | Name: " + name + " | Phone: " + phoneNumber;
    }
}