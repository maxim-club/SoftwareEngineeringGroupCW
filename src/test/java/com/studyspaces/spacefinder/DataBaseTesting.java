package com.studyspaces.spacefinder;

import com.studyspaces.spacefinder.BasicDBReadWrite;
import org.junit.jupiter.api.Test;
import org.bson.Document;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;


class DataBaseTesting{
	private BasicDBReadWrite client;


	@BeforeEach
    void setUp() throws Exception { // Ask maxim for help with connectiong to DB
        
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            
            if (input == null) {
                throw new FileNotFoundException("application.properties not found. Check src/test/resources.");
            }
            props.load(input);

            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");
            
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
			.append("utilities", Arrays.asList("sockets", "food"));

		int res = client.InsertDocument("Room Data", testDoc);
		assertEquals(1, res);
	}

	@Test
	void retrieveDataFromDataBase(){
		client.Connect("Data");
		Document testDoc = new Document()
			.append("room", "-1")
			.append("name", "test")
			.append("utilities", Arrays.asList("sockets", "food"));
		Document fetchedDoc = client.GetDocument("Room Data", "room", "-1");
		assertEquals(testDoc.get("name"), fetchedDoc.get("name"));
	}

}
