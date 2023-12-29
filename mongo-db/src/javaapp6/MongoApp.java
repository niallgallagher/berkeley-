package javaapp6;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

public class MongoApp {
    private static MongoClient mongoClient = null;
    private static MongoDatabase mongodb = null;
    private static ClientSession clientSession = null;
    
    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        
        try {
            // NOTE : Setup !
            //mongoClient = MongoClients.create("mongodb+srv://Cluster33172:Q2lddGV8XndS@cluster33172.fj0jgm5.mongodb.net/?retryWrites=true&w=majority");   
            mongoClient = MongoClients.create("mongodb+srv://ngall:Testing123!@cluster0.ru1ueu9.mongodb.net/?retryWrites=true&w=majority"); 
                                                

            mongodb = mongoClient.getDatabase("mongodbapp");          
            clientSession = mongoClient.startSession();
            
            BuildData.AddDataToMongoDB(clientSession, mongodb);
            
            clientSession.commitTransaction();
            
            System.out.println("\nCompleted Data Load to your " + mongodb.getName() + " database\n");
            
            MongoIterable<String> collectionList = mongodb.listCollectionNames();
                   
            for(String collection: collectionList) {
                getAllDocuments(mongodb.getCollection(collection));
            }

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            clientSession.abortTransaction();         
        }
        finally {
            clientSession.close();
            mongoClient.close();
        }
    }
    
    private static void getAllDocuments(MongoCollection<Document> col) {
        System.out.println("\nFetching all documents from the collection[" + col.getNamespace().getCollectionName() + "]:\n");
 
        // Performing a read operation on the collection.
        FindIterable<Document> fi = col.find();
        try (MongoCursor<Document> cursor = fi.iterator()) {
            while(cursor.hasNext()) {               
                System.out.println(cursor.next().toJson());
            }
        }
    }
    
}
