# GameFinderAPI
RESTful API for creating and accessing information on volleyball events

# Endpoints

### Players

**GET** /players
+ Returns an array with JSON objects representing all players 
  - Status Code 200: Successful retrieval of all players
    
**GET** /players?eventId=X
+ Returns an array with JSON objects representing all players registered for event with ID X
  - Status Code 200: Successful retrieval of said players
  
**GET** /players?name=X
+ Returns an array with JSON objects representing all players with names who start with X
  Status Code 200: Successful retrieval of said players

**GET** /players/id
+ Returns the Player object with the given id
  - Status Code 200: Successful retrieval of specified player
  - Status Code 404: No player object with the given id exists
    

**POST** /players
+ Creates a new player object and adds it to database
  - Status Code 201: Succesfully added new player to database
  - Status Code 422: Failed to add player because username was a duplicate

**POST** /players/login
+ Requires a body containing "username" and "password" attributes.  Checks the database for a player with the given credentials and returns a JSON object if found.
  - Status Code 200: Succesfully retrieved player with provided credentials
  - Status Code 400: JSON Body does not contain "username" and "password" fields
  - Status Code 422: No player with the provided credentials could be found
    

**PUT** /players/id
+ Updates the player with given id and returns a representation of the new object
  - Status Code 200: Successfully updated the given object
  - Status Code 404: No object with the given ID could be found
    

**DELETE** /players/id
+ Deletes the player with the given id
  - Status Code 205: Succesfully deleted the object
  - Status Code 404: Object with the given ID could not be found   

### Events

**GET** /events
+ Returns an array with JSON objects representing all events 
  - Status Code 200: Successful retrieval of all events
  - Status Code 400: Failed to retrieve event due to malformed JSON
  - Use query parameter playerID=X to get all events player with ID X is registered for
    
**GET** /events/id
+ Returns the Event object with the given id
  - Status Code 200: Successful retrieval of specified event
  - Status Code 400: Failed to retrieve event due to malformed JSON
  - Status Code 400: Failed to retrieve event due to inputing a String as id
  - Status Code 404: No event with that id could be found

**GET** /events?title=${title}
+ Returns the Event with a title containing the text provided
  - Status Code 200: Successful retrieval of specified event
  - Status Code 400: Failed to retrieve event due to malformed JSON
  - Status Code 400: Failed to retrieve event due to inputing a String as id
  - Status Code 404: No event with that id could be found
    
**POST** /events
+ Creates a new Event object and adds it to database
  - Status Code 201: Succesfully added new event to database
  - Status Code 400: Failed to retrieve event due to malformed JSON
    
**PUT** /events/id
+ Updates the event with given id and returns a representation of the new object
  - Status Code 200: Successful retrieval of specified event
  - Status Code 400: Failed to retrieve event due to malformed JSON
  - Status Code 400: Failed to retrieve event due to inputing a String as id
  - Status Code 404: No event with that id could be found
    
**DELETE** /events/id
+ Deletes the evemtnt with the given id
  - Status Code 205: Succesfully deleted the object
  - Status Code 400: Failed to retrieve event due to malformed JSON
  - Status Code 400: Failed to retrieve event due to inputing a String as id
  - Status Code 404: No event with that id could be found
  
### Registrations
**POST** /registration
+ Creates a new registration to be added to the database
  - Status Code 201: Succesfully added the object
  - Status Code 400: Invalid JSON Body
  - Status Code 422: Either the given parent or event ID do not exist
  
**GET** /registration/playerId/eventId
+ Returns a JSON indicating whether the player with the given ID is registered for the event with the given ID
  - Status Code 200: Successfully returned a JSON indicating registration status
  - Status Code 400: Invalid pathParameters provided
  - Status Code 404: The given IDs could not be found

**DELETE** /registration/playerId/eventId
+ Deletes the registration connected to the given player and event id
  - Status Code 205: Succesfully deleted the object
  - Status Code 404: No object with the given contents exists
  - Status Code 422: A non-numeric path parameter was provided