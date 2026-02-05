# SoftwareEngineeringGroupCW



Frontend to Backend connection:
1. Run 'npm start'
2. Run SpacefinderApplication
3. Navigate to 'http://localhost:3000/backendtest'


















## Study Space Profile
### StudySpaceProfile

This class stores data for a single room in a JSON like format.

Methods

| name | parameters | return type | description|
| ---  |       ---  |        ---  |        --- |
| Add | String key, Object value | int | Adds the value to the key field|
| Get | String key | Object | get the specific value from a field|
| Remove | String key| int | Delete the key value pair|
| Update | String key, Object value | int | Replace the value to the key field|
| toJson | void | String | Outputs the full JSON as a string|

### StudySpaceProfileManager

This class stores manages retrieving and writing room data to and from database,
Stores all retrieved content in a Map<String, StudySpaceProfile>.

Methods

| name | parameters | return type | description|
| ---  |       ---  |        ---  |        --- |
| fetchFromDB| String field, Object value | String[] | Finds all instances of a room where field value == search value and stores in its own map. Returns a list of IDs that are related to the rooms.|
| Get | String id | StudySpaceProfile | get the studyspace profile from the map and then return it. Will search if not in current map|



	

