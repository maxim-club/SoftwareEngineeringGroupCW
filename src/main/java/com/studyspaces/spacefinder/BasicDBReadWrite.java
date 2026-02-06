package com.studyspaces.spacefinder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;


public class BasicDBReadWrite{
	private String uri;

	private MongoClient mongoClient;

	private MongoDatabase database;

	private boolean connectionStatus = false;

	public BasicDBReadWrite(String username, String password){

		uri = "mongodb+srv://" + username + ":" + password + "@datacluster.d2bhkt7.mongodb.net/?appName=DataCluster";
		mongoClient = MongoClients.create(uri);

	}
//IMPORTANT must always connect before using the functions of this class.
	public void Connect(String databaseName){
		
		if(!this.connectionStatus){
		database = mongoClient.getDatabase(databaseName);
		connectionStatus = true;
		}

	}

	public int InsertDocument(String collectionName, Document document){

		if(connectionStatus){
			MongoCollection<Document> collection = database.getCollection(collectionName);
			collection.insertOne(document.append("_id", new ObjectId()));
			return 1;
		}else{
			System.err.println("Database has not been connected!");
			return 0;
		}
	}
	//Deletes a socument if the field value in DB is == to value
	public int DeleteDocument(String collectionName, String field, Object value){
		if(connectionStatus){
			MongoCollection<Document> collection = database.getCollection(collectionName);
			collection.deleteOne(Filters.eq(field, value));
			return 1;
		}else{
			System.err.println("Database has not been connected!");
			return 0;
		}
	}
//overwrite old value with new vlue for the documnet with id.
	public int UpdateDocument(String collectionName, String id, String oldField, Object newValue ){
		if(connectionStatus){
			MongoCollection<Document> collection = database.getCollection(collectionName);
			    collection.updateOne(
				Filters.eq("id", id),      
				Updates.set(oldField, newValue)      
			);

			return 1;
		}else{
			System.err.println("Database has not been connected!");
			return 0;
		}
	}

	public int replaceDocumentById(String collectionName, String id, Document newDoc){
		if (!connectionStatus){return 0;}

		MongoCollection<Document> collection = database.getCollection(collectionName);

		collection.replaceOne(Filters.eq("id", id) , newDoc);
		return 1;
	}

	public Document GetFirstDocument(String collectionName, String field, Object value){
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		Document result = collection.find(Filters.eq(field, value)).first();
		return result;
	}

	public ArrayList<Document> GetDocuments(String collectionName, String field, Object value){
		
		System.out.println("GetDocuments: "+ collectionName + ", " + field + ", " + value +" (" + value.getClass().getSimpleName() + ")");
		
		MongoCollection<Document> collection = database.getCollection(collectionName);

		System.out.println(collection);

		ArrayList<Document> results = new ArrayList<Document>();


		for(Document doc : collection.find(Filters.eq(field, value))){
			results.add(doc);
		}
		
		System.out.println(results);

		return results;
		
	}

}
