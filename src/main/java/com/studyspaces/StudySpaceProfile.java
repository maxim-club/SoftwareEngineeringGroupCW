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

public class StudySpaceProfile(ObjectId, id, Document doc){

	public ObjectId mongoID = id; //This is the database ID that this room is linked to

	public Document doc = doc; //This is a BSON document which basically contains all data. Its like a Map with a String key.
	

	public void Add(String key, Object value){ //Add the object to the JSON with the key as key
		try{
			this.doc.append(key, value);
		}catch (Exception e){
			throw e;
			return 0;
		}
		return 1;
	}

	public Object Get(String key){ //fetch the value with the key
		val = null;
		try{
			val = this.doc.get(key);
		}catch(Exception e){
			throw e;
		}
		
		return val;
	}

	public int Remove(String key){ //remove the key and value pair
		try{
			val = this.doc.remove(key);
		}catch(Exception e){
			throw e;
			return 0;
		}
		return 1;
	}
	
	public int Update(String key, Object value){ //Like the Add function but you replace the value.
		try{
			this.doc.put(key, value);
		}catch (Exception e){
			throw e;
			return 0;
		}
		return 1;		
	}

	public String toJson(){	//Return the contents of document as a single string.
		return this.doc.toJson();
	}



}
	
	


