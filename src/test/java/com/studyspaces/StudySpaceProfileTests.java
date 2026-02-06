package com.studyspaces;
import com.studyspaces.StudySpaceProfile;
import com.studyspaces.StudySpaceProfileManager;
import org.junit.jupiter.api.Test;

import io.github.cdimascio.dotenv.Dotenv;

import org.bson.Document;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import org.junit.jupiter.api.BeforeEach;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.util.ArrayList;

class StudySpaceProfileTesting{

	private StudySpaceProfileManager manager;

    @BeforeEach
    void setup() throws Exception {
        manager = new StudySpaceProfileManager();
    }


	//Fetching test values check if it recieved everything
	@Test
	void fetchTest(){
		ArrayList<StudySpaceProfile> profiles = this.manager.fetch("room", "-1");
		System.err.println(profiles);
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
	



}

