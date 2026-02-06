package com.studyspaces.spacefinder;
import com.studyspaces.spacefinder.BasicDBReadWrite;
import org.junit.jupiter.api.Test;

import io.github.cdimascio.dotenv.Dotenv;

import org.bson.Document;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
//Testing to check if database works. Feel free to use this code as a guide to connecting to the DB.
class DataBaseTesting{
	private BasicDBReadWrite client;


	@BeforeEach
    void setUp() throws Exception {         
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
			
			Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();
		
			String username = dotenv.get("BOT_USERNAME");
			String password = dotenv.get("BOT_PASSWORD");

            
            if (username == null || password == null) {
                throw new IllegalStateException("db.username or db.password is missing in application.properties.");
            }

            client = new BasicDBReadWrite(username, password);
        } 
    }


	@Test
	void establishDataBaseConnectionTest(){
		client.Connect("Data");
	}
	@Test
	void writeDataToDataBaseTest(){
		client.Connect("Data");
		Document testDoc = new Document()
			.append("room", "-1")
			.append("name", "test")
			.append("utilities", Arrays.asList("sockets", "food"))
			.append("id", "-1");

		int res = client.InsertDocument("Room Data", testDoc);
		assertEquals(1, res);
	}

	@Test
	void retrieveDataFromDataBase(){
		client.Connect("Data");
		Document testDoc = new Document()
			.append("room", "-1")
			.append("name", "test")
			.append("utilities", Arrays.asList("sockets", "food"))
			.append("id", "-1");
		Document fetchedDoc = client.GetDocuments("Room Data", "room", "-1").get(0);
		assertEquals(testDoc.get("name"), fetchedDoc.get("name"));
	}
	@Test
	void deleteDataFromDataBase(){
		client.Connect("Data");
		int result = client.DeleteDocument("Room Data", "room", "-1");

		assertEquals(result, 1);


	}
}
