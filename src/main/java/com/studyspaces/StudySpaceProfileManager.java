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

package com.studyspaces;
import com.studyspaces.StudySpaceProfile;



//	This class is for handling the interface between Room data database and backend code
//	This contains a map of (int)ava Space ID

public class StudySpaceProfileManager{

	//this will be the same in all instances of profile manager
	static public HashMap<String, StudySpaceProfile> studySpaces = new HashMap<String, StudySpaceProfile>();

	Dotenv dotenv = Dotenv.configure()
		.ignoreIfMissing()
		.load();

	String username = dotenv.get("BOT_USERNAME");
	String password = dotenv.get("BOT_PASSWORD");

	if (username == null || password == null) {
		throw new IllegalStateException("db.username or db.password is missing in application.properties.");
	}

	private BasicDBReadWrite client = new BasicDBReadWrite(username, password);


	public String[] fetchFromDB(String field, Object value){ //Find all instances of the field having value in database and return a list of java space ids to access them 
		Document[] documents = new ArrayList<Document>();

		try{
			client.Connect("Data");
			documents = this.client.GetDocuments("Room Data", field, value);
		}catch (Exception e){
			throw e;
		}
		
		String[] ids = new ArrayList<String>();

		//get the keys as string and add to the studySpaces map
		for(Document doc : documents){
			ObjectId id = doc.get("_id");
			this.studySpaces.append(id.toString(), new StudySpaceProfile(id, doc));
			ids.append(id.toString());
		}

		return ids;
	}
	
	//With this function you can retrieve the study space profile using the ID.
	public StudySpaceProfile Get(String id){
		StudySpaceProfile room = this.studySpaces.get(id);

		if(room != null){
			return this.studySpaces.get(id);
		}else{
			//search the DB
			
			String[] fetchedIds = this.fetchFromDB("_id", new ObjectID(id));
			if(fetchedIds.length() == 0){
				return null;
			}else{
				return this.studySpaces.get(id);
			}
		}
	}











}
