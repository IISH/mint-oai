package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import gr.ntua.ivml.mint.oai.model.Project;
import gr.ntua.ivml.mint.oai.model.Report;
import gr.ntua.ivml.mint.oai.util.MongoDB;
import gr.ntua.ivml.mint.oai.util.StringUtils;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class Manager extends Controller {
	// views
	
	public static Result index() {
		return redirect("/manager/");
	}
	
	public static Result landingPage(){
		try {
			return ok(manager.render());
		} catch(Exception e) {
			return internalServerError(serverUnavailable.render(e));
		}
	}

	// json data
	
	public static Result getOverall() {
		response().setContentType("application/json");
		BasicDBObject result = new BasicDBObject();

		long unique = 0;
		int repositories = 0;
		int organizations = 0;
		for(String setName:MongoDB.getDB().getCollectionNames()){
			if(!setName.equals("reports") && !setName.equals("conflicts") && !setName.equals("system.indexes") 
						&& !setName.equals("fs.files") && !setName.equals("fs.chunks") && !setName.equals("metadata")){
							unique += MongoDB.getDB().getCollection(setName).count();
							repositories++;
							ArrayList<Object> dists = (ArrayList<Object>) MongoDB.getDB().getCollection(setName).distinct("orgId");
							organizations += dists.size();
			}
		}
		
		String duplicates = "" + MongoDB.getDB().getCollection("conflicts").count();
		
		result.put("repositories", repositories);
		result.put("organizations", organizations);
		result.put("unique", unique);
		result.put("duplicates", duplicates);
		
		return ok(com.mongodb.util.JSON.serialize(result));
	}

	public static Result getRecordsPerProject(){
		response().setContentType("application/json");

		String[] headers = new String[2];
		headers[0] = "projects";
		headers[1] = "records";
		
		
		ArrayList<Object[]> returnedVals = new ArrayList<Object[]>();
		returnedVals.add(headers);
		Object[] vals = new Object[2];
		for(String setName:MongoDB.getDB().getCollectionNames()){
			
			if(!setName.equals("reports") && !setName.equals("conflicts") && !setName.equals("system.indexes") 
					&& !setName.equals("fs.files") && !setName.equals("fs.chunks") && !setName.equals("metadata")){
				vals = new Object[2];
				vals[0] = setName;
				vals[1] = MongoDB.getDB().getCollection(setName).count();
				returnedVals.add(vals);
			}
		}
		vals = new Object[2];
		vals[0] = "conflicts";
		vals[1] = MongoDB.getDB().getCollection("conflicts").count();
		returnedVals.add(vals);
		BasicDBObject obj = new BasicDBObject();
		obj.put("vals", returnedVals);

		return ok(com.mongodb.util.JSON.serialize(obj));
	}
}
