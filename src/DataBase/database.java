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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.ArrayList;





public class database {



    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "hospitalDB";

    private static final String PATIENTS_COL = "patients";
    private static final String DOCTORS_COL = "doctors";
    private static final String APPOINTMENTS_COL = "appointments";
    private static final String NOTIFICATIONS_COL = "notifications";


    public database(){

    }

    public static void main(String[] args) {

        //addPatient( "Nimal Perera", 32, "Male", "Fever");
        /*
        updatePatient("P001", "Nimal Perera", 33, "Male", "Recovered");
        deletePatient("P001");
*//*
        addDoctor( "Dr. Saman Jayawardena", "General Physician", true,
                List.of(
                        "Monday 09:00-11:00",
                        "Wednesday 14:00-16:00",
                        "Friday 10:00-12:00"
                )
        );*/
/*
        updateDoctor("D001", "Dr. Saman Jayawardena", "Internal Medicine", true,
                List.of(
                        "Tuesday 10:00-12:00",
                        "Thursday 15:00-17:00",
                        "Monday 09:00-11:00"
                )
        );


        List<Document> doctors = getAllDoctors();

        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            for (Document d : doctors) {
                System.out.println("Doctor ID   : " + d.getString("doctorId"));
                System.out.println("Name        : " + d.getString("name"));
                System.out.println("Specialty   : " + d.getString("specialty"));
                System.out.println("Available   : " + d.getBoolean("available"));
                System.out.println("Time Slots  : " + d.get("timeSlots"));
                System.out.println("----------------------------------");
            }
        }
















        */


//deleteDoctor("D001");
/*

        scheduleAppointment(
                "P001",
                "D001",
                LocalDate.now(),          // today
                "Monday 09:00-11:00",
                "General checkup"
        );
*/





/*
        Document info = trackAppointmentStatus("A001");
        if (info != null) {
            System.out.println("Appointment ID : " + info.getString("appointmentId"));
            System.out.println("Patient Name   : " + info.getString("patientName"));
            System.out.println("Doctor Name    : " + info.getString("doctorName"));
            System.out.println("Time Slot      : " + info.getString("timeSlot"));
            System.out.println("Reason         : " + info.getString("reason"));
            System.out.println("Status         : " + info.getString("status"));
            System.out.println("Created At     : " + info.getDate("createdAt"));
        }

        */
/*
        updateAppointment("A001", "Completed");
        updateAppointment("A002", "Cancelled");
*/


/*
        assignDoctor(
                "P001",
                "General Physician",
                LocalDate.of(2025, 1, 15), // future date
                "Wednesday 14:00-16:00",
                "Fever"
        );


*/


/*
        Document report = generateReportThisMonth();

        if (report != null) {
            System.out.println("Month: " + report.getString("month") + " " + report.getInteger("year"));
            System.out.println("Total Appointments     : " + report.getInteger("totalAppointments"));
            System.out.println("Completed Appointments : " + report.getInteger("completedAppointments"));
            System.out.println("Best Doctor            : " + report.getString("bestDoctorName"));

            System.out.println("\nDoctor Performance:");
            @SuppressWarnings("unchecked")
            Map<String, Integer> perf =
                    (Map<String, Integer>) report.get("doctorPerformance");

            for (Map.Entry<String, Integer> entry : perf.entrySet()) {
                System.out.println(entry.getKey() + " → " + entry.getValue() + " completed");
            }
        }
*/








/*
        List<Document> notifications = getPatientNotifications("P001");

        if (notifications.isEmpty()) {
            System.out.println("No notifications available.");
        } else {
            for (Document n : notifications) {
                System.out.println("Appointment ID : " + n.getString("appointmentId"));
                System.out.println("Doctor         : " + n.getString("doctorName"));
                System.out.println("Specialty      : " + n.getString("specialty"));
                System.out.println("Time Slot      : " + n.getString("timeSlot"));
                System.out.println("Status         : " + n.getString("status"));
                System.out.println("----------------------------------");
            }
        }
*/





/*
        List<Document> docnotifications = getDoctorNotifications("D001");

        if (docnotifications.isEmpty()) {
            System.out.println("No appointments assigned.");
        } else {
            for (Document n : docnotifications) {
                System.out.println("Appointment ID : " + n.getString("appointmentId"));
                System.out.println("Patient Name   : " + n.getString("patientName"));
                System.out.println("Time Slot      : " + n.getString("timeSlot"));
                System.out.println("Status         : " + n.getString("status"));
                System.out.println("----------------------------------");
            }
        }

*/



/*
        System.out.println("\n--- Today's Incomplete Appointments ---");

        List<Document> today = getAllAppointmentNotificationsToday();

        if (today.isEmpty()) {
            System.out.println("No pending appointments today 🎉");
        } else {
            for (Document d : today) {
                System.out.println("Appointment ID : " + d.getString("appointmentId"));
                System.out.println("Patient        : " + d.getString("patientName"));
                System.out.println("Doctor         : " + d.getString("doctorName"));
                System.out.println("Specialty      : " + d.getString("specialty"));
                System.out.println("Time Slot      : " + d.getString("timeSlot"));
                System.out.println("Status         : " + d.getString("status"));
                System.out.println("--------------------------------------");
            }
        }

*/




        List<Document> patients = getAllPatients();

        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            for (Document p : patients) {
                System.out.println("Patient ID : " + p.getString("patientId"));
                System.out.println("Name       : " + p.getString("name"));
                System.out.println("Age        : " + p.getInteger("age"));
                System.out.println("Gender     : " + p.getString("gender"));
                System.out.println("Disease    : " + p.getString("disease"));
                System.out.println("----------------------------------");
            }
        }


    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------
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


    public static List<Document> getAllPatients() {

        List<Document> patientsList = new ArrayList<>();

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);
            MongoCollection<Document> patients = db.getCollection(PATIENTS_COL);

            patients.find().into(patientsList);
        }

        return patientsList;
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

    public static void deleteDoctor(String doctorId) {

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            Document filter = new Document("doctorId", doctorId);

            DeleteResult result = doctors.deleteOne(filter);

            if (result.getDeletedCount() == 0) {
                System.out.println("No doctor found with ID: " + doctorId);
            } else {
                System.out.println("Doctor deleted successfully 🗑️");
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








    public static void scheduleAppointment(String patientId, String doctorId,LocalDate appointmentDate, String timeSlot, String reason) {

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);

            MongoCollection<Document> patients = db.getCollection(PATIENTS_COL);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);
            MongoCollection<Document> appointments = db.getCollection(APPOINTMENTS_COL);

            // 1) Check patient exists
            Document patient = patients.find(new Document("patientId", patientId)).first();
            if (patient == null) {
                System.out.println("Cannot schedule: No patient found with ID: " + patientId);
                return;
            }

            // 2) Check doctor exists
            Document doctor = doctors.find(new Document("doctorId", doctorId)).first();
            if (doctor == null) {
                System.out.println("Cannot schedule: No doctor found with ID: " + doctorId);
                return;
            }

            // 3) Check doctor availability
            Boolean available = doctor.getBoolean("available", false);
            if (!available) {
                System.out.println("Cannot schedule: Doctor is not available right now.");
                return;
            }

            // 4) (Optional but useful) Check doctor has this timeslot
            //    This works because your timeSlots are stored as List<String>.
            if (timeSlot != null && !timeSlot.isBlank()) {
                Object slotsObj = doctor.get("timeSlots");
                if (slotsObj instanceof java.util.List) {
                    @SuppressWarnings("unchecked")
                    java.util.List<String> slots = (java.util.List<String>) slotsObj;

                    if (!slots.contains(timeSlot)) {
                        System.out.println("Cannot schedule: Doctor does not have this time slot: " + timeSlot);
                        return;
                    }
                }
            }

            // 5) Generate appointment ID like A001, A002...
            String appointmentId = generateAppointmentId(appointments);

            // 6) Create appointment document
            Date apptDate = Date.from(
                    appointmentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
            );

            Document appointment = new Document("appointmentId", appointmentId)
                    .append("patientId", patientId)
                    .append("doctorId", doctorId)
                    .append("appointmentDate", apptDate)   // ✅ REAL appointment date
                    .append("timeSlot", timeSlot)
                    .append("reason", reason)
                    .append("status", "Scheduled")
                    .append("createdAt", new Date());

            appointments.insertOne(appointment);

            System.out.println("Appointment scheduled successfully ✅ ID: " + appointmentId);
        }
    }






    private static String generateAppointmentId(MongoCollection<Document> appointments) {

        Document lastAppointment = appointments.find()
                .sort(new Document("appointmentId", -1))
                .limit(1)
                .first();

        if (lastAppointment == null) {
            return "A001";
        }

        String lastId = lastAppointment.getString("appointmentId"); // e.g. A012
        int num = Integer.parseInt(lastId.substring(1));
        return String.format("A%03d", num + 1);
    }





    public static Document trackAppointmentStatus(String appointmentId) {

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);

            MongoCollection<Document> appointments = db.getCollection(APPOINTMENTS_COL);
            MongoCollection<Document> patients = db.getCollection(PATIENTS_COL);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            // 1) Find appointment
            Document appointment = appointments.find(
                    new Document("appointmentId", appointmentId)
            ).first();

            if (appointment == null) {
                System.out.println("No appointment found with ID: " + appointmentId);
                return null;
            }

            String patientId = appointment.getString("patientId");
            String doctorId = appointment.getString("doctorId");

            // 2) Find patient
            Document patient = patients.find(
                    new Document("patientId", patientId)
            ).first();

            // 3) Find doctor
            Document doctor = doctors.find(
                    new Document("doctorId", doctorId)
            ).first();

            String patientName = (patient != null) ? patient.getString("name") : "Unknown Patient";
            String doctorName = (doctor != null) ? doctor.getString("name") : "Unknown Doctor";

            // 4) Build result document (ONLY essential display data)
            Document result = new Document()
                    .append("appointmentId", appointment.getString("appointmentId"))
                    .append("patientName", patientName)
                    .append("doctorName", doctorName)
                    .append("timeSlot", appointment.getString("timeSlot"))
                    .append("reason", appointment.getString("reason"))
                    .append("status", appointment.getString("status"))
                    .append("createdAt", appointment.getDate("createdAt"));

            return result;
        }
    }

    public static void updateAppointment(String appointmentId, String newStatus) {

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);
            MongoCollection<Document> appointments = db.getCollection(APPOINTMENTS_COL);

            Document filter = new Document("appointmentId", appointmentId);

            Document update = new Document("$set",
                    new Document("status", newStatus)
                            .append("updatedAt", new java.util.Date())
            );

            UpdateResult result = appointments.updateOne(filter, update);

            if (result.getMatchedCount() == 0) {
                System.out.println("No appointment found with ID: " + appointmentId);
            } else {
                System.out.println("Appointment status updated to '" + newStatus + "' ✅");
            }
        }
    }






    public static void assignDoctor(String patientId, String specialty, LocalDate appointmentDate, String timeSlot, String reason) {
        System.out.println(patientId+specialty+appointmentDate+timeSlot+reason);
        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);

            MongoCollection<Document> patients = db.getCollection(PATIENTS_COL);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);
            MongoCollection<Document> appointments = db.getCollection(APPOINTMENTS_COL);

            // 1) Check patient exists
            Document patient = patients.find(new Document("patientId", patientId)).first();
            if (patient == null) {
                System.out.println("Cannot assign doctor: Patient not found (" + patientId + ")");
                return;
            }

            // 2) Find available doctor with given specialty
            Document doctor = doctors.find(
                    new Document("specialty", specialty)
                            .append("available", true)
            ).first();

            if (doctor == null) {
                System.out.println("No available doctor found for specialty: " + specialty);
                return;
            }

            // 3) Check requested time slot is available for this doctor
            Object slotsObj = doctor.get("timeSlots");
            if (!(slotsObj instanceof List)) {
                System.out.println("Doctor has no available time slots.");
                return;
            }

            @SuppressWarnings("unchecked")
            List<String> slots = (List<String>) slotsObj;

            if (!slots.contains(timeSlot)) {
                System.out.println("Doctor is not available at time slot: " + timeSlot);
                return;
            }

            String doctorId = doctor.getString("doctorId");

            // 4) Generate appointment ID
            String appointmentId = generateAppointmentId(appointments);

            // 5) Create appointment
            Date apptDate = Date.from(
                    appointmentDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
            );

            Document appointment = new Document("appointmentId", appointmentId)
                    .append("patientId", patientId)
                    .append("doctorId", doctorId)
                    .append("appointmentDate", apptDate)   // ✅ REAL appointment date
                    .append("timeSlot", timeSlot)
                    .append("reason", reason)
                    .append("status", "Scheduled")
                    .append("createdAt", new Date());

            appointments.insertOne(appointment);

            System.out.println(
                    "Doctor assigned successfully ✅\n" + "Appointment ID: " + appointmentId + "\n" + "Doctor ID     : " + doctorId + "\n" + "Specialty     : " + specialty
            );
        }
    }




    public static List<Document> getDoctorsBySpecialty(String specialty) {

        List<Document> doctorsList = new ArrayList<>();

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            Document filter = new Document("specialty", specialty);

            doctors.find(filter).into(doctorsList);
        }

        return doctorsList;
    }




    public static List<String> getAvailableTimeSlots(String doctorId) {

        List<String> timeSlots = new ArrayList<>();

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            Document doctor = doctors.find(
                    new Document("doctorId", doctorId)
            ).first();

            if (doctor == null) {
                System.out.println("No doctor found with ID: " + doctorId);
                return timeSlots;
            }

            Object slotsObj = doctor.get("timeSlots");

            if (slotsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> slots = (List<String>) slotsObj;
                timeSlots.addAll(slots);
            }
        }

        return timeSlots;
    }




    public static Document generateReportThisMonth() {

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);

            MongoCollection<Document> appointments = db.getCollection(APPOINTMENTS_COL);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            // 1) Calculate start and end of current month
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = startOfMonth.plusMonths(1);

            Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // 2) Query appointments for this month
            List<Document> monthlyAppointments = appointments.find(
                    new Document("createdAt",
                            new Document("$gte", startDate)
                                    .append("$lt", endDate))
            ).into(new ArrayList<>());

            int totalAppointments = monthlyAppointments.size();
            int completedAppointments = 0;

            // doctorId -> completed count
            Map<String, Integer> doctorCompletedCount = new HashMap<>();

            for (Document appt : monthlyAppointments) {
                String status = appt.getString("status");
                String doctorId = appt.getString("doctorId");

                if ("Completed".equalsIgnoreCase(status)) {
                    completedAppointments++;

                    doctorCompletedCount.put(
                            doctorId,
                            doctorCompletedCount.getOrDefault(doctorId, 0) + 1
                    );
                }
            }

            // 3) Find best performed doctor
            String bestDoctorId = null;
            int maxCompleted = 0;

            for (Map.Entry<String, Integer> entry : doctorCompletedCount.entrySet()) {
                if (entry.getValue() > maxCompleted) {
                    maxCompleted = entry.getValue();
                    bestDoctorId = entry.getKey();
                }
            }

            String bestDoctorName = "N/A";

            if (bestDoctorId != null) {
                Document bestDoctor = doctors.find(
                        new Document("doctorId", bestDoctorId)
                ).first();

                if (bestDoctor != null) {
                    bestDoctorName = bestDoctor.getString("name");
                }
            }

            // 4) Build summary result
            Document report = new Document()
                    .append("month", startOfMonth.getMonth().toString())
                    .append("year", startOfMonth.getYear())
                    .append("totalAppointments", totalAppointments)
                    .append("completedAppointments", completedAppointments)
                    .append("bestDoctorId", bestDoctorId)
                    .append("bestDoctorName", bestDoctorName)
                    .append("doctorPerformance", doctorCompletedCount);

            return report;
        }
    }





    public static List<Document> getPatientNotifications(String patientId) {

        List<Document> notifications = new ArrayList<>();

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);

            MongoCollection<Document> appointments = db.getCollection(APPOINTMENTS_COL);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            // 1) Get all appointments for this patient
            List<Document> patientAppointments = appointments.find(
                    new Document("patientId", patientId)
            ).into(new ArrayList<>());

            // 2) Build notification data for each appointment
            for (Document appt : patientAppointments) {

                String doctorId = appt.getString("doctorId");

                Document doctor = doctors.find(
                        new Document("doctorId", doctorId)
                ).first();

                String doctorName = (doctor != null) ? doctor.getString("name") : "Unknown Doctor";
                String specialty = (doctor != null) ? doctor.getString("specialty") : "N/A";

                Document notification = new Document()
                        .append("appointmentId", appt.getString("appointmentId"))
                        .append("doctorName", doctorName)
                        .append("specialty", specialty)
                        .append("timeSlot", appt.getString("timeSlot"))
                        .append("reason", appt.getString("reason"))
                        .append("status", appt.getString("status"))
                        .append("createdAt", appt.getDate("createdAt"));

                notifications.add(notification);
            }
        }

        return notifications;
    }






    public static List<Document> getDoctorNotifications(String doctorId) {

        List<Document> notifications = new ArrayList<>();

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);

            MongoCollection<Document> appointments = db.getCollection(APPOINTMENTS_COL);
            MongoCollection<Document> patients = db.getCollection(PATIENTS_COL);

            // 1) Get all appointments for this doctor
            List<Document> doctorAppointments = appointments.find(
                    new Document("doctorId", doctorId)
            ).into(new ArrayList<>());

            // 2) Build notification data for each appointment
            for (Document appt : doctorAppointments) {

                String patientId = appt.getString("patientId");

                Document patient = patients.find(
                        new Document("patientId", patientId)
                ).first();

                String patientName = (patient != null) ? patient.getString("name") : "Unknown Patient";

                Document notification = new Document()
                        .append("appointmentId", appt.getString("appointmentId"))
                        .append("patientName", patientName)
                        .append("timeSlot", appt.getString("timeSlot"))
                        .append("reason", appt.getString("reason"))
                        .append("status", appt.getString("status"))
                        .append("createdAt", appt.getDate("createdAt"));

                notifications.add(notification);
            }
        }

        return notifications;
    }


    public static List<Document> getAllAppointmentNotificationsToday() {

        List<Document> notifications = new ArrayList<>();

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);

            MongoCollection<Document> appointments = db.getCollection(APPOINTMENTS_COL);
            MongoCollection<Document> patients = db.getCollection(PATIENTS_COL);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            // 1) Calculate today's date range (00:00 → tomorrow 00:00)
            LocalDate today = LocalDate.now(ZoneId.systemDefault());

            Date startOfToday = Date.from(
                    today.atStartOfDay(ZoneId.systemDefault()).toInstant()
            );

            Date startOfTomorrow = Date.from(
                    today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
            );

            // 2) Find today's incomplete appointments
            Document filter = new Document("appointmentDate",
                    new Document("$gte", startOfToday).append("$lt", startOfTomorrow))
                    .append("status", new Document("$ne", "Completed"));

            List<Document> todaysAppointments =
                    appointments.find(filter).into(new ArrayList<>());

            // 3) Build notification data
            for (Document appt : todaysAppointments) {

                String patientId = appt.getString("patientId");
                String doctorId = appt.getString("doctorId");

                Document patient = patients.find(
                        new Document("patientId", patientId)
                ).first();

                Document doctor = doctors.find(
                        new Document("doctorId", doctorId)
                ).first();

                String patientName =
                        (patient != null) ? patient.getString("name") : "Unknown Patient";

                String doctorName =
                        (doctor != null) ? doctor.getString("name") : "Unknown Doctor";

                String specialty =
                        (doctor != null) ? doctor.getString("specialty") : "N/A";

                Document notification = new Document()
                        .append("appointmentId", appt.getString("appointmentId"))
                        .append("patientName", patientName)
                        .append("doctorName", doctorName)
                        .append("specialty", specialty)
                        .append("timeSlot", appt.getString("timeSlot"))
                        .append("reason", appt.getString("reason"))
                        .append("status", appt.getString("status"))
                        .append("appointmentDate", appt.getDate("appointmentDate"));

                notifications.add(notification);
            }
        }

        return notifications;
    }



    public static List<Document> getAllDoctors() {

        List<Document> doctorsList = new ArrayList<>();

        try (MongoClient client = MongoClients.create(URI)) {

            MongoDatabase db = client.getDatabase(DB_NAME);
            MongoCollection<Document> doctors = db.getCollection(DOCTORS_COL);

            doctors.find().into(doctorsList);
        }

        return doctorsList;
    }



}
