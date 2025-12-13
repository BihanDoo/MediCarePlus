package DataBase;

import java.util.List;
import DataBase.database;

public class testDBvalues2 {
    static void main() {

        database DB = new database();
        DB.addPatient("Nimal Perera", 32, "Male", "Fever");
        DB.addPatient("Kumari Silva", 45, "Female", "Diabetes");
        DB.addPatient("Suresh Kumar", 28, "Male", "Migraine");
        DB.addPatient("Anusha Fernando", 60, "Female", "Blood Pressure");
        DB.addPatient("Ravi Jayasinghe", 40, "Male", "Back Pain");
        DB.addPatient("Sanduni Perera", 25, "Female", "Allergy");
        DB.addPatient("Kamal Fernando", 55, "Male", "Chest Pain");
        DB.addPatient("Nadeesha Perera", 22, "Female", "Skin Allergy");
        DB.addPatient("Sunil Jayawardena", 67, "Male", "Heart Condition");
        DB.addPatient("Dilani Samarasinghe", 35, "Female", "Back Pain");
        DB.addPatient("Pradeep Silva", 48, "Male", "High Cholesterol");
        DB.addPatient("Tharindu Wickramasinghe", 30, "Male", "Headache");
        DB.addPatient("Ishara Abeysekera", 27, "Female", "Acne");
        DB.addPatient("Mahesh Gunawardena", 42, "Male", "Joint Pain");




DB.addDoctor("Dr. Saman Jayawardena","General Physician",true,List.of("Monday 09:00-11:00","Wednesday 14:00-16:00","Friday 10:00-12:00"));DB.addDoctor("Dr. Ruwan Silva","Neurologist",true,List.of("Monday 13:00-15:00","Friday 09:00-11:00"));DB.addDoctor("Dr. Malini Perera","Cardiologist",true,List.of("Tuesday 10:00-12:00","Thursday 15:00-17:00"));DB.addDoctor("Dr. Nadeesha Fernando","Dermatologist",true,List.of("Monday 11:00-13:00","Wednesday 09:00-11:00"));DB.addDoctor("Dr. Chathura Gunasekara","Orthopedic",true,List.of("Monday 10:00-12:00","Thursday 14:00-16:00"));DB.addDoctor("Dr. Sanduni Jayalath","Dermatologist",true,List.of("Tuesday 09:00-11:00","Friday 13:00-15:00"));DB.addDoctor("Dr. Anura Wijesinghe","Cardiologist",true,List.of("Monday 09:00-11:00","Wednesday 15:00-17:00"));DB.addDoctor("Dr. Priyanka Rathnayake","General Physician",true,List.of("Tuesday 11:00-13:00","Thursday 09:00-11:00","Saturday 10:00-12:00"));DB.addDoctor("Dr. Hashini Karunaratne","Neurologist",true,List.of("Wednesday 10:00-12:00","Friday 14:00-16:00"));    }}
