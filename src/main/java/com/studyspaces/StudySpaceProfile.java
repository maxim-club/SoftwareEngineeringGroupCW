package com.studyspaces;

import org.bson.Document;

import java.util.Arrays;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;


//	This class is for a single room profile.
//	It holds the data fetched from the database related to a single room.

public class StudySpaceProfile{	
	public String id; //This is the database ID as a string that this room is linked to

	public Document doc; //This is a BSON document which basically contains all data. Its like a Map with a String key.
	
	public boolean dirty = false; //When in SSPManager cache, this will be true if it has changes that need to be written back to DB.
	


	public StudySpaceProfile(String id, Document doc){

		this.id = id; 
		this.doc = doc; 
	}
	
	public Document Add(String key, Object value){ //Add the object to the JSON with the key as key
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null");
		}
		return this.doc.append(key, value);
	}
	//fetch value that has this key
	public Object Get(String key){
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null");
		}

		return this.doc.get(key);
	}

	public Object Remove(String key){ //remove the key and value pair
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null");
		}
		return this.doc.remove(key);
	}
	
	public Object Update(String key, Object value){ //Like the Add function but you replace the value.
		if (key == null) {
			throw new IllegalArgumentException("Key cannot be null");
		}
		return this.doc.put(key, value);
	
	}

	public String toJson(){	//Return the contents of document as a single string.
		return this.doc.toJson();
	}




}
	
	


