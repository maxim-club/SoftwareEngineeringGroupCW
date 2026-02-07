package com.studyspaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;

import io.github.cdimascio.dotenv.Dotenv;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.studyspaces.StudySpaceProfile;
import com.studyspaces.spacefinder.BasicDBReadWrite;




//	This class is for handling the interface between Room data database and backend code
//	This contains a map of (int)ava Space ID

public class StudySpaceProfileManager{
	
	private BasicDBReadWrite client;
	static private HashMap<String, StudySpaceProfile> studySpaces = new HashMap<String, StudySpaceProfile>();

	
	public StudySpaceProfileManager() throws Exception{
		//this will be the same in all instances of profile manager
			
		String username;
		String password;

		try{

			Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

			username = dotenv.get("BOT_USERNAME");
			password = dotenv.get("BOT_PASSWORD");

			if (username == null || password == null) {
				throw new IllegalStateException("db.username or db.password is missing in application.properties.");
			}
			}catch (IllegalStateException e){
				throw e;
			}

			client = new BasicDBReadWrite(username, password);
		}


//Find all instances of the field having value in database and return list of them. Will also cache in memory 
	public ArrayList<StudySpaceProfile> fetch(String field, Object value){ 
		ArrayList<Document> documents = new ArrayList<Document>();
		//System.out.println("Fetching: " + field + " , " +value+ " (" + value.getClass().getSimpleName() + ")");

		try{
			client.Connect("Data");
			documents = this.client.GetDocuments("Room Data", field, value);
		}catch(Exception e){
			throw e;
		}
		
		ArrayList<StudySpaceProfile> profiles = new ArrayList<>();

		//get the keys as string and add to the studySpaces map
		for(Document doc : documents){
			Object idObj = doc.get("id");

			if (idObj == null) {
				System.err.println("Warning: document missing 'id' field, skipping: " + doc);
				continue; // skip this document
			}

			String id = idObj.toString();

			StudySpaceProfile profile = new StudySpaceProfile(id, doc); 
			this.studySpaces.put(id, profile); //cache
														  
			profiles.add(profile);
		}

		return profiles;
	}
	
	//With this function you can retrieve the study space profile using the ID.
	public StudySpaceProfile Get(String id){

		StudySpaceProfile room = this.studySpaces.get(id);

		if(room != null){	// exists in the map and is in database
			return this.studySpaces.get(id);

		}else{
			ArrayList<StudySpaceProfile> fetchedProfiles = this.fetch("id", id);

			if(fetchedProfiles.size() == 0){ //not in database
				return null;
			}
			StudySpaceProfile profile = fetchedProfiles.get(0);
			studySpaces.put(id, profile);   // cache it
			return profile;
			
		}
	}

//With this function you can write the profile back to the database by selecting with id.
	public int write(String id){
		StudySpaceProfile target = this.studySpaces.get(id);

		if (target == null){
			System.out.println("Profile with id " + id + " is not currently in memory. Not written");
			return 0;
		}

		try{
			client.Connect("Data");
			
			int res = client.replaceDocumentById("Room Data", id, target.doc);

			//remove from cache
			if (res == 1){ //it successfully wrote to DB
				this.studySpaces.remove(id);	
				return 1;
			}else{
				System.err.println("Error, study space with ID " + id + " has not been written to DB!");
				return 0;
			}

		}catch(Exception e){
			throw e;
		}



	}

}
