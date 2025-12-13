package DataBase;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import java.util.List;
import java.util.Date;







public class database {



    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "hospitalDB";

    private static final String PATIENTS_COL = "patients";
    private static final String DOCTORS_COL = "doctors";
    private static final String APPOINTMENTS_COL = "appointments";
    private static final String NOTIFICATIONS_COL = "notifications";

    public static void main(String[] args) {

        addPatient( "Nimal Perera", 32, "Male", "Fever");
        updatePatient("P001", "Nimal Perera", 33, "Male", "Recovered");
        deletePatient("P001");

        addDoctor( "Dr. Saman Jayawardena", "General Physician", true,
                List.of(
                        "Monday 09:00-11:00",
                        "Wednesday 14:00-16:00",
                        "Friday 10:00-12:00"
                )
        );

        updateDoctor("D001", "Dr. Saman Jayawardena", "Internal Medicine", false,
                List.of(
                        "Tuesday 10:00-12:00",
                        "Thursday 15:00-17:00"
                )
        );


    }

    public static void addPatient(String name, int age, String gender, String disease) {

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);
            MongoCollection<Document> patients = db.getCollection(PATIENTS_COL);

            String patientId = generatePatientId(patients);

            Document patient = new Document("patientId", patientId)
                    .append("name", name)
                    .append("age", age)
                    .append("gender", gender)
                    .append("disease", disease);

            patients.insertOne(patient);
            System.out.println("Patient added successfully with ID: " + patientId);
        }
    }
    public static void updatePatient(String patientId, String name, int age, String gender, String disease) {

        MongoClient client = MongoClients.create(URI);
        MongoDatabase db = client.getDatabase("hospitalDB");
        MongoCollection<Document> patients = db.getCollection("patients");

        Document filter = new Document("patientId", patientId);

        Document updateFields = new Document()
                .append("name", name)
                .append("age", age)
                .append("gender", gender)
                .append("disease", disease);

        Document update = new Document("$set", updateFields);

        UpdateResult result = patients.updateOne(filter, update);

        if (result.getMatchedCount() == 0) {
            System.out.println("No patient found with ID: " + patientId);
        } else {
            System.out.println("Patient updated successfully");
        }

    }

    public static void deletePatient(String patientId) {

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase("hospitalDB");
            MongoCollection<Document> patients = db.getCollection("patients");

            Document filter = new Document("patientId", patientId);

            DeleteResult result = patients.deleteOne(filter);

            if (result.getDeletedCount() == 0) {
                System.out.println("No patient found with ID: " + patientId);
            } else {
                System.out.println("Patient deleted successfully");
            }
        }
    }
//    ------------------------------------------------------------------------------

    public static void addDoctor(String name, String specialty, boolean available, List<String> timeSlots) {

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            String doctorId = generateDoctorId(doctors);

            Document doctor = new Document("doctorId", doctorId)
                    .append("name", name)
                    .append("specialty", specialty)
                    .append("available", available)
                    .append("timeSlots", timeSlots)
                    .append("createdAt", new java.util.Date());

            doctors.insertOne(doctor);
            System.out.println("Doctor added successfully with ID: " + doctorId);
        }
    }





    public static void updateDoctor(String doctorId, String name, String specialty, boolean available, List<String> timeSlots) {

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            // Find doctor by primary key
            Document filter = new Document("doctorId", doctorId);

            // Fields to update
            Document updateFields = new Document()
                    .append("name", name)
                    .append("specialty", specialty)
                    .append("available", available)
                    .append("timeSlots", timeSlots);

            Document update = new Document("$set", updateFields);

            UpdateResult result = doctors.updateOne(filter, update);

            if (result.getMatchedCount() == 0) {
                System.out.println("No doctor found with ID: " + doctorId);
            } else {
                System.out.println("Doctor updated successfully 🩺");
            }
        }
    }















    private static String generatePatientId(MongoCollection<Document> patients) {

        Document lastPatient = patients.find()
                .sort(new Document("patientId", -1))
                .limit(1)
                .first();

        if (lastPatient == null) {
            return "P001";
        }

        String lastId = lastPatient.getString("patientId"); // e.g. P007
        int num = Integer.parseInt(lastId.substring(1));
        return String.format("P%03d", num + 1);
    }



    private static String generateDoctorId(MongoCollection<Document> doctors) {

        Document lastDoctor = doctors.find()
                .sort(new Document("doctorId", -1))
                .limit(1)
                .first();

        if (lastDoctor == null) {
            return "D001";
        }

        String lastId = lastDoctor.getString("doctorId"); // e.g. D012
        int num = Integer.parseInt(lastId.substring(1));
        return String.format("D%03d", num + 1);
    }





}
