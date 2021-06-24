# GameFinderAPI
RESTful API for creating and accessing information on volleyball events

#Endpoints

###Players

**GET** /players
+ Returns an array with JSON objects representing all players 
  - Status Code 200: Successful retrieval of all players
    
**GET** /players/id
+ Returns the Player object with the given id
  - Status Code 200: Successful retrieval of specified player
  - Status Code 404: No player object with the given id exists
    
**POST** /players
+ Creates a new player object and adds it to database
  - Status Code 201: Succesfully added new player to database
    
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
