package DataBase;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class database {



    private static final String URI = "mongodb://localhost:27017";

    static void main() {
        addPatient("1","abc",10,"male","abc");
    }

    public static void addPatient(String patientId, String name, int age, String gender, String disease) {

        MongoClient client = MongoClients.create(URI);
        MongoDatabase db = client.getDatabase("hospitalDB");
        MongoCollection<Document> patients = db.getCollection("patients");

        Document patient = new Document("patientId", patientId)
                .append("name", name)
                .append("age", age)
                .append("gender", gender)
                .append("disease", disease);

        patients.insertOne(patient);
        System.out.println("Patient added successfully 🏥");

        client.close();
    }

}
