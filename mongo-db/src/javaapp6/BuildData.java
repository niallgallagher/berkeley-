package javaapp6;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Updates.addEachToSet;
import static com.mongodb.client.model.Updates.addToSet;
import com.mongodb.client.result.InsertOneResult;
import java.util.Arrays;
import org.bson.Document;

/**
 *
 * @author davidw
 */
public class BuildData {
    
    private static MongoCollection<Document> students = null, courses = null;
    
    public BuildData(){}
    
    public static void AddDataToMongoDB(ClientSession clientSession, MongoDatabase mongodb) {
                
        MongoIterable<String> list = mongodb.listCollectionNames();
            
            int i = 1;
            System.out.println("Current collections in the database. If nothing listed, I will create for you\n");
            for(String name: list) {
                System.out.println("[" + i + "] " + name);
                i++;
            }
            
            if(i==1) {
                System.out.println("Database contains no collections. Creating collections for you\n");
                mongodb.createCollection("courses");
                mongodb.createCollection("students");
            } else {
                System.out.println("\nCollections exists. Dropping and re-creating to keep things clean and idempotent\n");
                mongodb.getCollection("courses").drop();
                mongodb.getCollection("students").drop();            

                mongodb.createCollection("courses");
                mongodb.createCollection("students");
            }
            
            students = mongodb.getCollection("students");
            courses = mongodb.getCollection("courses");
            
            clientSession.startTransaction();  
                      
            InsertOneResult courseResult1 = courses.insertOne(new Document()
                        .append("_id", "A1")
                        .append("courseName", "Data Science")
                        .append("credits", 30));
            System.out.println("Success! Inserted document id: " + courseResult1.getInsertedId());

            
            InsertOneResult courseResult2 = courses.insertOne(new Document()
                        .append("_id", "A2")
                        .append("courseName", "Computing 101")
                        .append("credits", 40));
            System.out.println("Success! Inserted document id: " + courseResult2.getInsertedId());


            InsertOneResult studentResult1 = students.insertOne(new Document()
                        .append("_id", 001)
                        .append("name", "Niall Gallagher")
                        .append("DOB", "16-10-2000")
                        .append("nationality", "Irish"));
            
                // Prints the ID of the inserted document
            System.out.println("Success! Inserted document id: " + studentResult1.getInsertedId());
                
            InsertOneResult studentResult2 = students.insertOne(new Document()
                        .append("_id", 2)
                        .append("name", "Adam Wall")
                        .append("DOB", "17-05-2017")
                        .append("nationality", "Irish"));
            
                // Prints the ID of the inserted document
            System.out.println("Success! Inserted document id: " + studentResult2.getInsertedId() + "\n");
                
            students.updateOne(clientSession,
                    eq("_id", 1),
                    addEachToSet("courses", Arrays.asList("A1", "A2"))
            );
            students.updateOne(clientSession,
                    eq("_id", 2),
                    addEachToSet("courses", Arrays.asList("A1", "A2"))
            );
            
            courses.updateMany(clientSession,
                    or(eq("_id", "A1"), eq("_id", "A2")),
                    addToSet("students", 1)
            );
            courses.updateMany(clientSession,
                    or(eq("_id", "A1"), eq("_id", "A2")),
                    addToSet("students", 2)
            );
    }
           
}
