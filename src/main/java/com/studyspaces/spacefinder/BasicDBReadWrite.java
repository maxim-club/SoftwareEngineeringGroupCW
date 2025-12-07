package com.studyspaces.spacefinder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;


public class BasicDBReadWrite{
	private String uri;

	private MongoClient mongoClient;

	private MongoDatabase database;

	private boolean connectionStatus = false;

	BasicDBReadWrite(String username, String password){

		uri = "mongodb+srv://" + username + ":" + password + "@datacluster.d2bhkt7.mongodb.net/?appName=DataCluster";
		mongoClient = MongoClients.create(uri);

	}

	public void Connect(String databaseName){

		database = mongoClient.getDatabase(databaseName);
		connectionStatus = true;

	}



	public int InsertDocument(String collectionName, Document document){

		if(connectionStatus){
			MongoCollection<Document> collection = database.getCollection(collectionName);
			collection.insertOne(document.append("_id", new ObjectId()));
			return 1;
		}else{
			return 0;
		}
	}



	public Document GetDocument(String collectionName, String field, String value){
		
		
		MongoCollection<Document> collection = database.getCollection(collectionName);
		Document result = collection.find(eq(field, value)).first();
		return result;
		
	}




}
