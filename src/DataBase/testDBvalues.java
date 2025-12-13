package DataBase;


//thisssssss
import DataBase.database;



import org.bson.Document;

import java.time.LocalDate;
import java.util.List;

public class testDBvalues {
    static void main() {


        //---------
        database DB = new database();
        //---------




        DB.addPatient("Nimal Perera", 32, "Male", "Fever");
        DB.addPatient("Kumari Silva", 45, "Female", "Diabetes");
        DB.addPatient("Suresh Kumar", 28, "Male", "Headache");
        DB.addPatient("Anusha Fernando", 60, "Female", "Blood Pressure");


        DB.addDoctor(
                "Dr. Saman Jayawardena",
                "General Physician",
                true,
                List.of("Monday 09:00-11:00", "Wednesday 14:00-16:00")
        );

        DB.addDoctor(
                "Dr. Malini Perera",
                "Cardiologist",
                true,
                List.of("Tuesday 10:00-12:00", "Thursday 15:00-17:00")
        );

        DB.addDoctor(
                "Dr. Ruwan Silva",
                "Neurologist",
                true,
                List.of("Monday 13:00-15:00", "Friday 09:00-11:00")
        );


        DB.scheduleAppointment(
                "P001",
                "D001",
                LocalDate.now(),
                "Monday 09:00-11:00",
                "General checkup"
        );

        DB.scheduleAppointment(
                "P002",
                "D002",
                LocalDate.now(),
                "Tuesday 10:00-12:00",
                "Heart checkup"
        );

        DB.assignDoctor(
                "P003",
                "Neurologist",
                LocalDate.now(),
                "Monday 13:00-15:00",
                "Migraine problem"
        );

        DB.assignDoctor(
                "P004",
                "General Physician",
                LocalDate.now().plusDays(1),
                "Wednesday 14:00-16:00",
                "Routine checkup"
        );

        DB.assignDoctor(
                "P003",
                "Neurologist",
                LocalDate.now(),
                "Monday 13:00-15:00",
                "Migraine problem"
        );

        DB.assignDoctor(
                "P004",
                "General Physician",
                LocalDate.now().plusDays(1),
                "Wednesday 14:00-16:00",
                "Routine checkup"
        );

        DB.updateAppointment("A001", "Completed");
        DB.updateAppointment("A002", "Completed");
        DB.updateAppointment("A003", "Scheduled");
        DB.updateAppointment("A004", "Scheduled");

        Document info = DB.trackAppointmentStatus("A001");
        if (info != null) {
            System.out.println("Appointment ID : " + info.getString("appointmentId"));
            System.out.println("Patient Name   : " + info.getString("patientName"));
            System.out.println("Doctor Name    : " + info.getString("doctorName"));
            System.out.println("Status         : " + info.getString("status"));
        }


        System.out.println("\n--- Patient Notifications (P001) ---");
        for (Document d : DB.getPatientNotifications("P001")) {
            System.out.println(d.toJson());
        }

        System.out.println("\n--- Doctor Notifications (D001) ---");
        for (Document d : DB.getDoctorNotifications("D001")) {
            System.out.println(d.toJson());
        }


        System.out.println("\n--- Today's Incomplete Appointments ---");

        List<Document> today = DB.getAllAppointmentNotificationsToday();

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



        Document report = DB.generateReportThisMonth();

        System.out.println("\n--- Monthly Report ---");
        System.out.println("Total Appointments     : " + report.getInteger("totalAppointments"));
        System.out.println("Completed Appointments : " + report.getInteger("completedAppointments"));
        System.out.println("Best Doctor            : " + report.getString("bestDoctorName"));



    }
}
