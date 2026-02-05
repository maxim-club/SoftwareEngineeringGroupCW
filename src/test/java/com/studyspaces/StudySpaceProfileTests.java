package com.studyspaces;
import com.studyspaces.StudySpaceProfile;
import com.studyspaces.StudySpaceProfilieManager;
import org.junit.jupiter.api.Test;

import io.github.cdimascio.dotenv.Dotenv;

import org.bson.Document;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileNotFoundException;

class DataBaseTesting{
	private StudySpaceProfile studySpaceProfileManager = new StudySpaceProfileManager();


	//Fetching test values check if it recieved everything
	@Test
	void fetchTest(){
		ArrayList<String> ids = this.studySpaceProfileManager.fetchFromDB("room", "-1");
		assertNotEquals(ids.length(), 0);
	}	
	
	//Get a value and check if everything is there
	@Test
	void getValueInMap(){
		ArrayList<String> ids = this.studySpaceProfileManager.fetchFromDB("room", "-1");
		StudySpaceProfile profile = this.studySpaceProfileManager.get(ids.at(0));
		assertEquals(profile.get("name"), "test");


	}
	
	//Get a value not already in and check if it is there
	@Test
	void getValueNotInMap(){
		StudySpaceProfile profile = this.studySpaceProfileManager.get(ids.at(0));
		assertEquals(profile.get("name"), "test");
	}

	//Get a value not already in and not in database and check if it can handle it
	@Test
	void getNonExistantValue(){
		StudySpaceProfile profile = this.studySpaceProfileManager.get("ajsduwbnjiduniqonioenqiwneoiendonsdmwdiowndionweidmioamwd");
	}
	



}

