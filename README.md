# SoftwareEngineeringGroupCW



Frontend to Backend connection:
1. Run 'npm start'
2. Run SpacefinderApplication
3. Navigate to 'http://localhost:3000/backendtest'


















## Study Space Profile
### StudySpaceProfile

This class stores data for a single room in a JSON like format.

Methods

| name                | parameters                 | return type         | description                                                                                                                 |
| ------------------- | -------------------------- | ------------------- | --------------------------------------------------------------------------------------------------------------------------- |
| `StudySpaceProfile` | `String id, Document doc`  | `StudySpaceProfile` | Constructor. Initializes a `StudySpaceProfile` with a permanent database ID and a BSON `Document` containing all room data. |
| `Add`               | `String key, Object value` | `int`               | Adds a new key-value pair to the BSON `Document`. Returns `1` on success. Throws an exception if the operation fails.       |
| `Get`               | `String key`               | `Object`            | Retrieves the value associated with the given key from the BSON `Document`. Returns `null` if the key does not exist.       |
| `Remove`            | `String key`               | `int`               | Removes the key-value pair from the BSON `Document`. Returns `1` on success. Throws an exception if the operation fails.    |
| `Update`            | `String key, Object value` | `int`               | Updates the value of an existing key in the BSON `Document` or adds it if the key does not exist. Returns `1` on success.   |
| `toJson`            | none                       | `String`            | Converts the BSON `Document` into a JSON string representation and returns it.                                              |

Fields

| name  | type       | description                                                   |
| ----- | ---------- | ------------------------------------------------------------- |
| `id`  | `String`   | The permanent ID of the room stored in the database.          |
| `doc` | `Document` | The BSON `Document` storing all room data as key-value pairs. |


### StudySpaceProfileManager

This class stores manages retrieving and writing room data to and from database,
Stores all retrieved content in a Map<String, StudySpaceProfile>.

Methods


| name                       | parameters                   | return type                    | description                                                                                                                                                                                                                                                                                   |
| -------------------------- | ---------------------------- | ------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `StudySpaceProfileManager` | none                         | `StudySpaceProfileManager`     | Constructor. Initializes the `StudySpaceProfileManager`, loads database credentials from environment variables, and sets up a connection client for MongoDB. Throws an exception if credentials are missing.                                                                                  |
| `fetch`                    | `String field, Object value` | `ArrayList<StudySpaceProfile>` | Queries the database for all documents in `"Room Data"` where the given field equals the specified value. Converts the documents into `StudySpaceProfile` objects, caches them in the `studySpaces` map, and returns the list of profiles. Skips documents that are missing the `"id"` field. |
| `Get`                      | `String id`                  | `StudySpaceProfile`            | Retrieves a `StudySpaceProfile` by its permanent `"id"`. First checks the cache map `studySpaces`. If not found, fetches it from the database using `fetch`. Returns `null` if the profile does not exist.                                                                                    |
| `write`                    | `String id`                  | `int`                          | Writes the cached `StudySpaceProfile` with the given `"id"` back to the database. Returns `1` if successfully written, `0` if the profile is not currently in memory. Throws an exception if database operations fail.                                                                        |



	

