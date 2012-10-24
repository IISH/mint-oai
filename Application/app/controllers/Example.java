package controllers;

import com.mongodb.BasicDBObject;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.example;

public class Example extends Controller{

	public static Result getRecord(){
		BasicDBObject obj = new BasicDBObject();
		obj.put("title", "Record Example");
		obj.put("description", "This is an example of a record described in JSON with just a title and description fields");
		response().setContentType("application/json");
		return ok(com.mongodb.util.JSON.serialize(obj));
	}
	
	public static Result getExample(){
		return ok(example.render());
	}
}
