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

		manager = Mockito.spy(new StudySpaceProfileManager());

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

	
    /* ---------------- CREATE ---------------- */

    @Test
    void createCachesProfileAndWritesSuccessfully() {
        StudySpaceProfile profile =
                new StudySpaceProfile("room1", new org.bson.Document("id", "room1"));

        doReturn(1).when(manager).write("room1");

        int result = manager.create(profile);

        assertEquals(1, result);
        assertTrue(manager.isCached("room1"));
        assertFalse(profile.dirty);
    }

    @Test
    void createReturnsZeroIfAlreadyCached() {
        StudySpaceProfile profile =
                new StudySpaceProfile("room1", new org.bson.Document("id", "room1"));

        manager.create(profile);

        int result = manager.create(profile);

        assertEquals(0, result);
    }

    /* ---------------- UPDATE ---------------- */

    @Test
    void updateMarksDirtyAndWritesSuccessfully() {
        StudySpaceProfile profile =
                new StudySpaceProfile("room1", new org.bson.Document("id", "room1"));

        doReturn(1).when(manager).write("room1");

        int result = manager.update(profile);

        assertEquals(1, result);
        assertFalse(profile.dirty);
        assertTrue(manager.isCached("room1"));
    }

    /* ---------------- DELETE ---------------- */

    @Test
    void deleteRemovesFromCacheOnSuccess() {
        StudySpaceProfile profile =
                new StudySpaceProfile("room1", new org.bson.Document("id", "room1"));

        manager.update(profile);

        when(mockClient.DeleteDocument("Room Data", "id", "room1"))
                .thenReturn(1);

        int result = manager.delete("room1");

        assertEquals(1, result);
        assertFalse(manager.isCached("room1"));
    }

    @Test
    void deleteFailureDoesNotRemoveFromCache() {
        StudySpaceProfile profile =
                new StudySpaceProfile("room1", new org.bson.Document("id", "room1"));

        manager.update(profile);

        when(mockClient.DeleteDocument("Room Data", "id", "room1"))
                .thenReturn(0);

        int result = manager.delete("room1");

        assertEquals(0, result);
        assertTrue(manager.isCached("room1"));
    }

    /* ---------------- EVICT_WRITE ---------------- */

    @Test
    void evictWriteWritesDirtyProfileAndRemovesIt() throws Exception {
        StudySpaceProfile profile =
                new StudySpaceProfile("room1", new org.bson.Document("id", "room1"));
        profile.dirty = true;

        manager.update(profile);

        doReturn(1).when(manager).write("room1");

        int result = manager.evict_write("room1");

        assertEquals(1, result);
        assertFalse(manager.isCached("room1"));
    }

    @Test
    void evictWriteThrowsIfWriteFails() {
        StudySpaceProfile profile =
                new StudySpaceProfile("room1", new org.bson.Document("id", "room1"));
        profile.dirty = true;

        manager.update(profile);

        doReturn(0).when(manager).write("room1");

        assertThrows(Exception.class,
                () -> manager.evict_write("room1"));

        assertTrue(manager.isCached("room1"));
    }

    /* ---------------- CLEAR CACHE ---------------- */

	@Test
	void clearCacheEvictsAllEntries() throws Exception {
		StudySpaceProfile p1 =
				new StudySpaceProfile("1", new Document("id", "1"));
		StudySpaceProfile p2 =
				new StudySpaceProfile("2", new Document("id", "2"));

		manager.update(p1);
		manager.update(p2);

		// Both writes succeed
		when(mockClient.replaceDocumentById(
				eq("Room Data"), anyString(), any()))
				.thenReturn(1);

		List<String> errors = manager.clearCache();

		assertTrue(errors.isEmpty());      // no stragglers
		assertEquals(0, manager.getCacheSize()); // cache fully cleared
	}

	@Test
	void clearCacheReturnsIdsThatFailEviction() throws Exception {
		StudySpaceProfile p1 =
				new StudySpaceProfile("1", new Document("id", "1"));
		StudySpaceProfile p2 =
				new StudySpaceProfile("2", new Document("id", "2"));

		manager.update(p1);
		manager.update(p2);

		// Write succeeds for "1"
		when(mockClient.replaceDocumentById(
				eq("Room Data"), eq("1"), any()))
				.thenReturn(1);

		// Write fails for "2"
		when(mockClient.replaceDocumentById(
				eq("Room Data"), eq("2"), any()))
				.thenReturn(0);

		List<String> errors = manager.clearCache();

		assertEquals(1, errors.size());
		assertEquals("2", errors.get(0));
		assertTrue(manager.isCached("2"));   // eviction failed
		assertFalse(manager.isCached("1"));  // eviction succeeded
	}

    /* ---------------- REFRESH ---------------- */

    @Test
    void refreshClearsDirtyFlag() throws Exception {
        StudySpaceProfile profile =
                new StudySpaceProfile("room1", new org.bson.Document("id", "room1"));
        profile.dirty = true;

        manager.update(profile);

        StudySpaceProfile refreshed = manager.refresh("room1");

        assertFalse(refreshed.dirty);
    }

    @Test
    void refreshThrowsIfProfileNotCached() {
        assertThrows(Exception.class,
                () -> manager.refresh("missing"));
    }

    /* ---------------- CACHE HELPERS ---------------- */

    @Test
    void cacheSizeIsAccurate() {
        manager.update(new StudySpaceProfile("1", new org.bson.Document("id", "1")));
        manager.update(new StudySpaceProfile("2", new org.bson.Document("id", "2")));

        assertEquals(2, manager.getCacheSize());
    }


}
