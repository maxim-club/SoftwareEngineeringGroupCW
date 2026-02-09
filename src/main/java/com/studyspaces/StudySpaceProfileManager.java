package com.studyspaces;

import java.util.List;
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

//TODO. Add specific function like get all rooms from one building to avoid more interaction with mongoDB from outside this class.


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
	
	//put a profile inside. Will add to cache and will write to memory
	public int create(StudySpaceProfile profile){
	
		if (this.studySpaces.get(profile.id) != null){
			return 0; //Item already exists in cache
		}

		profile.dirty = true;

		studySpaces.put(profile.id, profile);   // cache it
		int res = this.write(profile.id);

		if (res == 1){
			profile.dirty = false;
			return 1;
		}else{
			return 0;
		}
	}

	//replace the profile in cache. will make that entry dirty.
	public int update(StudySpaceProfile profile){
		studySpaces.put(profile.id, profile);   // cache it
		profile.dirty = true;

		int res = this.write(profile.id);

		if (res == 1){
			profile.dirty = false;
			return 1;
		}else{
			return 0;
		}
	
	}
	
	//delete from cache AND DATABASE
	public int delete(String id){
		client.Connect("Data");

		int res = client.DeleteDocument("Room Data", "id", id);
		if (res == 1){
			this.studySpaces.remove(id);
			return 1;
		}else{
			return 0;
		}
	}

	// Cache focused methods:
	
	//Remove everything from cache and write back any dirty entries. Returns an array of ids that where unsuccesful
	public List<String> clearCache(){
		List<String> errorIds = new ArrayList<String> ();
		
		for (String key : new ArrayList<>(this.studySpaces.keySet())){
			
			try{
				this.evict_write(key);
			}catch(Exception e){
				errorIds.add(key);
				e.printStackTrace();
			}

		}
		return errorIds;
	}
	
	//Forcibly remove an item in cache. Write back if dirty. 
	public int evict_write(String id) throws Exception{
		StudySpaceProfile profile = this.studySpaces.get(id);
		int res = 1;


		if( profile.dirty ){
			res = this.write(id);
		}

		if (res == 0){ //the write failed so don't evict. Throw error
			throw new Exception("Write has failed for Profile with id: " + id);
		}
		
		this.studySpaces.remove(id);
		return 1;
	}
	
	//Forcibly remove an item in cache. DOESNT WRITE BACK DIRTY ENTRIES
	public void evict(String id){
		this.studySpaces.remove(id);
	}

	//Fetch this item from database again
	public StudySpaceProfile refresh(String id) throws Exception{
		StudySpaceProfile profile = this.studySpaces.get(id);
		
		if (profile == null){
			throw new Exception("Profile with id: " + id + " does not exist in Database"); // TODO add better exception handling. Profile doesnt exist in DB error.
		}

		this.studySpaces.put(id, profile);
		profile.dirty = false; //its false because its exactly the same as in DB.
		return profile;
	}	

	//check if item exists in cache
	public boolean isCached(String id){
		return this.studySpaces.containsKey(id);

	}
	
	//return the number of items in cache.
	public int getCacheSize(){
		return this.studySpaces.size();

	}
}
