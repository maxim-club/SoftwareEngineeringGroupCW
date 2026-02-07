package com.studyspaces;
import com.studyspaces.StudySpaceProfile;
import com.studyspaces.StudySpaceProfileManager;
import org.junit.jupiter.api.Test;

import io.github.cdimascio.dotenv.Dotenv;

import org.bson.Document;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;

class StudySpaceProfileTesting{

	private StudySpaceProfileManager manager;

    @BeforeEach
    void setup() throws Exception {
        manager = new StudySpaceProfileManager();
    }

	//	Manager Tests


	//Fetching test values check if it recieved everything
	@Test
	void fetchTest(){
		ArrayList<StudySpaceProfile> profiles = this.manager.fetch("room", "-1");
		assertNotEquals(profiles.size(), 0);
	}	
	
	//Get a value and check if everything is there
	@Test
	void getValueInMap(){
		ArrayList<StudySpaceProfile> profiles = this.manager.fetch("room", "-1");
		StudySpaceProfile profile = profiles.get(0);
		assertEquals(profile.Get("name"), "test");


	}
	
	//Get a value not already in and check if it is there
	@Test
	void getValueNotInMap(){
		StudySpaceProfile profile = this.manager.Get("-1");
		assertEquals(profile.Get("name"), "test");
	}

	//Get a value not already in and not in database and check if it can handle it
	@Test
	void getNonExistantValue(){
		StudySpaceProfile profile = this.manager.Get("ajsduwbnjiduniqonioenqiwneoiendonsdmwdiowndionweidmioamwd");
	}
	
	@Test
	void reWriteProfileTest(){
		StudySpaceProfile profile = this.manager.Get("-1");
		profile.Add("CurrentlyTested?", "True");
		this.manager.write("-1");

		//fetch to test if worked
		
		profile = this.manager.Get("-1");
		assertEquals(profile.Get("CurrentlyTested?"), "True");

		//remove the new field
		
		profile.Remove("CurrentlyTested?");
		this.manager.write("-1");

		//test if its been removed.
	
		assertNull(profile.Get("CurrentlyTested?"));
		
	}

	@Test
	void WriteRougeValue(){
		StudySpaceProfile profile = this.manager.Get("aaaaaaaaaaaaaaaaaaaaaaaaa");
		this.manager.write("aaaaaaaaaaaaaaaaaaaaaaaaa");
	}



	//fetch somehting that doesnt exist in DB
	@Test
	void fetchNothing(){
		ArrayList<StudySpaceProfile> profiles = this.manager.fetch("unefujwejandujnwdjamsdqwlkdn", "hello");
		assertEquals(profiles.size(), 0);
		
	}

	//	---------------- Single Profile test ----------------------------
	
    /* ---------------- ADD TESTS ---------------- */

    @Test
    void addNewKeyStoresValue() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        Object result = profile.Add("room", "C3");

        assertEquals("C3", profile.Get("room"));
        assertSame(doc, result); // append returns the document
    }

    @Test
    void addMultipleKeysStoresAllValues() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        profile.Add("room", "C3");
        profile.Add("capacity", 20);
        profile.Add("quiet", true);

        assertEquals("C3", profile.Get("room"));
        assertEquals(20, profile.Get("capacity"));
        assertEquals(true, profile.Get("quiet"));
    }

    @Test
    void addOverwritesExistingKeyValue() {
        Document doc = new Document();
        doc.put("room", "A1");
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        profile.Add("room", "D4");

        assertEquals("D4", profile.Get("room"));
    }

    @Test
    void addAllowsNullValue() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        profile.Add("notes", null);

        assertTrue(doc.containsKey("notes"));
        assertNull(profile.Get("notes"));
    }

    @Test
    void addWithNullKeyThrowsException() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        assertThrows(IllegalArgumentException.class, () ->
            profile.Add(null, "value")
        );
    }

    @Test
    void addWithEmptyKeyIsStored() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        profile.Add("", "emptyKey");

        assertEquals("emptyKey", profile.Get(""));
    }

    @Test
    void addDoesNotRemoveExistingKeys() {
        Document doc = new Document();
        doc.put("existing", "keep");
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        profile.Add("newKey", "newValue");

        assertEquals("keep", profile.Get("existing"));
        assertEquals("newValue", profile.Get("newKey"));
    }



	//	------------------ get tests -----------------
	
	@Test
	void getReturnsValueForExistingKey() {
		Document doc = new Document();
		doc.put("room", "A1");
		StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

		Object result = profile.Get("room");

		assertEquals("A1", result);
	}

	@Test
	void getReturnsNullForMissingKey() {
		Document doc = new Document();
		StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

		Object result = profile.Get("missing");

		assertNull(result);
	}

	@Test
	void getThrowsExceptionForNullKey() {
		Document doc = new Document();
		StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

		assertThrows(IllegalArgumentException.class, () ->
			profile.Get(null)
		);
	}

	@Test
	void getReturnsCorrectObjectType() {
		Document doc = new Document();
		doc.put("capacity", 12);
		StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

		Object result = profile.Get("capacity");

		assertTrue(result instanceof Integer);
		assertEquals(12, result);
	}


	    /* ---------------- REMOVE TESTS ---------------- */

    @Test
    void removeExistingKeyReturnsPreviousValue() {
        Document doc = new Document();
        doc.put("room", "A1");
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        Object removed = profile.Remove("room");

        assertEquals("A1", removed);
        assertNull(profile.Get("room"));
    }

    @Test
    void removeMissingKeyReturnsNull() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        Object removed = profile.Remove("missing");

        assertNull(removed);
    }

    @Test
    void removeNullKeyThrowsException() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        assertThrows(IllegalArgumentException.class, () ->
            profile.Remove(null)
        );
    }

    /* ---------------- UPDATE TESTS ---------------- */

    @Test
    void updateExistingKeyReturnsOldValueAndReplacesIt() {
        Document doc = new Document();
        doc.put("capacity", 10);
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        Object oldValue = profile.Update("capacity", 20);

        assertEquals(10, oldValue);
        assertEquals(20, profile.Get("capacity"));
    }

    @Test
    void updateNewKeyReturnsNullAndAddsValue() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        Object oldValue = profile.Update("building", "Main");

        assertNull(oldValue);
        assertEquals("Main", profile.Get("building"));
    }

    @Test
    void updateWithNullKeyThrowsException() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        assertThrows(IllegalArgumentException.class, () ->
            profile.Update(null, "value")
        );
    }

    @Test
    void updateAllowsNullValue() {
        Document doc = new Document();
        doc.put("notes", "quiet");
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        Object oldValue = profile.Update("notes", null);

        assertEquals("quiet", oldValue);
        assertNull(profile.Get("notes"));
    }

    /* ---------------- TO JSON TESTS ---------------- */

    @Test
    void toJsonReturnsValidJsonString() {
        Document doc = new Document();
        doc.put("room", "B2");
        doc.put("capacity", 15);
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        String json = profile.toJson();

        assertNotNull(json);
        assertTrue(json.contains("\"room\""));
        assertTrue(json.contains("\"B2\""));
        assertTrue(json.contains("\"capacity\""));
        assertTrue(json.contains("15"));
    }

    @Test
    void toJsonReturnsEmptyJsonForEmptyDocument() {
        Document doc = new Document();
        StudySpaceProfile profile = new StudySpaceProfile("-1", doc);

        String json = profile.toJson();

        assertEquals("{}", json);
    }
	
	



}

