package javaapp6;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.TransactionBody;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Updates.addToSet;
import static com.mongodb.client.model.Updates.addEachToSet;

import org.bson.Document;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoApp {
    private static MongoClient c = null;
    private static MongoDatabase mongodb = null;
    private static ClientSession s = null;
    private static MongoCollection<Document> students = null, courses = null;

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        
        try {
            // NOTE : Setup !
            c = MongoClients.create("mongodb://localhost:27017");          
            mongodb = c.getDatabase("mongodbapp");          
            s = c.startSession();

            students = mongodb.getCollection("students");
            courses = mongodb.getCollection("courses");

            s.startTransaction();

            students.updateOne(s,
                    eq("_id", 9),
                    addEachToSet("courses", Arrays.asList(1, 2))
            );

            courses.updateMany(s,
                    or(eq("_id", 1), eq("_id", 2)),
                    addToSet("students", 9)
            );

            s.commitTransaction();
            //s.abortTransaction();         

            
            TransactionBody tB = new TransactionBody<String>() {
                @Override
                public String execute() {
                    students.updateOne(s,
                        eq("_id", 9),
                        addEachToSet("courses", Arrays.asList(1, 2))
                    );

                    courses.updateMany(s,
                        or(eq("_id", 1), eq("_id", 2)),
                        addToSet("students", 9)
                    );

                    return ":)";
                }
            };

            s.withTransaction(tB); 

            System.out.println("Bye Bye :)");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        finally {
            s.close();
            c.close();
        }
    }
}
