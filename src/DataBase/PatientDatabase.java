package DataBase;

import model.Patient;
import java.util.ArrayList;
import java.util.List;

public class PatientDatabase {
    // List
    private List<Patient> patientList;
    private int nextPatientId = 1;

    // Constructor
    public PatientDatabase() {
        patientList = new ArrayList<>();

        addPatient(new Patient(nextPatientId++, "Alice Johnson", "1990-05-15",
                "Female", "555-0101", "alice@email.com",
                "123 Main Street", "Allergy to penicillin"));

        addPatient(new Patient(nextPatientId++, "Bob Williams", "1985-11-22",
                "Male", "555-0102", "bob@email.com",
                "456 Oak Avenue", "Diabetes type 2"));

        addPatient(new Patient(nextPatientId++, "Charlie Brown", "1978-03-30",
                "Male", "555-0103", "charlie@email.com",
                "789 Pine Road", "High blood pressure"));
    }



    // CREATE
    public boolean addPatient(Patient patient) {
        try {
            patient.setPatientId(nextPatientId++);
            patientList.add(patient);
            System.out.println("Patient added: " + patient.getName() + " (ID: " + patient.getPatientId() + ")");
            return true;
        } catch (Exception e) {
            System.out.println("Error adding patient: " + e.getMessage());
            return false;
        }
    }

    // READ
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientList); // Return copy
    }

    // READ
    public Patient getPatientById(int patientId) {
        for (Patient patient : patientList) {
            if (patient.getPatientId() == patientId) {
                return patient;
            }
        }
        return null; // Not found
    }

    // READ
    public List<Patient> searchPatientsByName(String name) {
        List<Patient> results = new ArrayList<>();
        String searchName = name.toLowerCase();

        for (Patient patient : patientList) {
            if (patient.getName().toLowerCase().contains(searchName)) {
                results.add(patient);
            }
        }
        return results;
    }

    // UPDATE
    public boolean updatePatient(Patient updatedPatient) {
        for (int i = 0; i < patientList.size(); i++) {
            Patient existing = patientList.get(i);
            if (existing.getPatientId() == updatedPatient.getPatientId()) {
                patientList.set(i, updatedPatient);
                System.out.println("Patient updated: " + updatedPatient.getName());
                return true;
            }
        }
        System.out.println("Patient not found for update (ID: " + updatedPatient.getPatientId() + ")");
        return false;
    }

    // DELETE
    public boolean deletePatient(int patientId) {
        for (int i = 0; i < patientList.size(); i++) {
            if (patientList.get(i).getPatientId() == patientId) {
                Patient removed = patientList.remove(i);
                System.out.println("Patient deleted: " + removed.getName() + " (ID: " + patientId + ")");
                return true;
            }
        }
        System.out.println("Patient not found for deletion (ID: " + patientId + ")");
        return false;
    }

    // GET
    public int getPatientCount() {
        return patientList.size();
    }

    // GET
    public int getNextAvailableId() {
        return nextPatientId;
    }

    // PRINT
    public void printAllPatients() {
        System.out.println("\n=== ALL PATIENTS ===");
        if (patientList.isEmpty()) {
            System.out.println("No patients in database.");
        } else {
            for (Patient patient : patientList) {
                System.out.println(patient.toString());
            }
        }
        System.out.println("Total patients: " + patientList.size());
    }
}