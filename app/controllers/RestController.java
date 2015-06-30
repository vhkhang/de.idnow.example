package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.mvc.*;

public class RestController extends Controller {

    public Result startIdentification() {
    	//Get the parsed JSON data
    	JsonNode json = request().body().asJson();
    	
    	//Do something with the identification
    	
        return ok();
    }

    public Result addCompany() {
    	//Get the parsed JSON data
    	JsonNode json = request().body().asJson();
    	
    	//Do something with the company
    	
        return ok();
    }

    public Result identifications() {
    	JsonNode identifications = Json.newArray();
    	
    	//Get the current identification
    	//Compute correct order
    	//Create new identification JSON with JsonNode identification = Json.newObject();
    	//Add identification to identifications list 
    	
        return ok(identifications);
    }

}
