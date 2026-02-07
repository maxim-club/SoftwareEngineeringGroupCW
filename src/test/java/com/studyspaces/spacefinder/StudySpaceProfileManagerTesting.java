package com.studyspaces;
import com.studyspaces.StudySpaceProfile;
import com.studyspaces.StudySpaceProfileManager;
import com.studyspaces.spacefinder.BasicDBReadWrite;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.bson.Document;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.*;

class StudySpaceProfileManagerTest {

    private StudySpaceProfileManager manager;
    private BasicDBReadWrite mockClient;

    @BeforeEach
    void setup() throws Exception {
        manager = new StudySpaceProfileManager();

        mockClient = mock(BasicDBReadWrite.class);

        // Inject mock client using reflection
        Field clientField = StudySpaceProfileManager.class.getDeclaredField("client");
        clientField.setAccessible(true);
        clientField.set(manager, mockClient);

        // Clear static cache
        Field cacheField = StudySpaceProfileManager.class.getDeclaredField("studySpaces");
        cacheField.setAccessible(true);
        ((Map<?, ?>) cacheField.get(null)).clear();
    }

    /* ---------------- FETCH ---------------- */

    @Test
    void fetchReturnsProfilesAndCachesThem() throws Exception {
        Document doc = new Document()
                .append("id", "room1")
                .append("name", "Quiet Room");

        when(mockClient.GetDocuments("Room Data", "name", "Quiet Room"))
                .thenReturn(new ArrayList<>(List.of(doc)));

        ArrayList<StudySpaceProfile> profiles =
                manager.fetch("name", "Quiet Room");

        assertEquals(1, profiles.size());
        assertEquals("room1", profiles.get(0).Get("id"));

        // cached
        assertNotNull(manager.Get("room1"));
    }

    @Test
    void fetchSkipsDocumentsWithoutId() throws Exception {
        Document doc = new Document().append("name", "Broken");

        when(mockClient.GetDocuments("Room Data", "name", "Broken"))
                .thenReturn(new ArrayList<>(List.of(doc)));

        ArrayList<StudySpaceProfile> profiles =
                manager.fetch("name", "Broken");

        assertTrue(profiles.isEmpty());
    }

    /* ---------------- GET ---------------- */

	@Test
	void getReturnsCachedProfileWithoutFetching() throws Exception {
		StudySpaceProfile cached =
				new StudySpaceProfile("cached", new Document("id", "cached"));

		// Manually insert into static cache (NO fetch)
		Field cacheField = StudySpaceProfileManager.class
				.getDeclaredField("studySpaces");
		cacheField.setAccessible(true);

		@SuppressWarnings("unchecked")
		Map<String, StudySpaceProfile> cache =
				(Map<String, StudySpaceProfile>) cacheField.get(null);

		cache.put("cached", cached);

		StudySpaceProfile result = manager.Get("cached");

		assertSame(cached, result);

		// Now this assertion is valid
		verify(mockClient, never())
				.GetDocuments(any(), any(), any());
	}

    @Test
    void getFetchesFromDatabaseIfNotCached() throws Exception {
        Document doc = new Document().append("id", "dbRoom");

        when(mockClient.GetDocuments("Room Data", "id", "dbRoom"))
                .thenReturn(new ArrayList<>(List.of(doc)));

        StudySpaceProfile result = manager.Get("dbRoom");

        assertNotNull(result);
        assertEquals("dbRoom", result.Get("id"));
    }

    @Test
    void getReturnsNullIfNotFound() throws Exception {
        when(mockClient.GetDocuments("Room Data", "id", "missing"))
                .thenReturn(new ArrayList<Document>());

        StudySpaceProfile result = manager.Get("missing");

        assertNull(result);
    }

    /* ---------------- WRITE ---------------- */

    @Test
    void writeReturnsZeroIfProfileNotCached() {
        int result = manager.write("missing");

        assertEquals(0, result);
    }

    @Test
    void writeWritesProfileAndRemovesFromCacheOnSuccess() throws Exception {
        Document doc = new Document().append("id", "room1");
        StudySpaceProfile profile = new StudySpaceProfile("room1", doc);

        // cache it manually
        Field cache = StudySpaceProfileManager.class.getDeclaredField("studySpaces");
        cache.setAccessible(true);
        ((Map<String, StudySpaceProfile>) cache.get(null))
                .put("room1", profile);

        when(mockClient.replaceDocumentById("Room Data", "room1", doc))
                .thenReturn(1);

        int result = manager.write("room1");

        assertEquals(1, result);
        assertNull(manager.Get("room1")); // removed from cache
    }

    @Test
    void writeDoesNotRemoveFromCacheOnFailure() throws Exception {
        Document doc = new Document().append("id", "room1");
        StudySpaceProfile profile = new StudySpaceProfile("room1", doc);

        Field cache = StudySpaceProfileManager.class.getDeclaredField("studySpaces");
        cache.setAccessible(true);
        ((Map<String, StudySpaceProfile>) cache.get(null))
                .put("room1", profile);

        when(mockClient.replaceDocumentById("Room Data", "room1", doc))
                .thenReturn(0);

        int result = manager.write("room1");

        assertEquals(0, result);
        assertNotNull(manager.Get("room1")); // still cached
    }
}
