package controllers;

import java.util.Map;

import com.mongodb.BasicDBObject;

import gr.ntua.ivml.mint.oai.model.Project;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Administration extends Controller {
	public static Result index() {
		return redirect("/admin/");
	}
	
	public static Result landingPage(){
		try {
			return ok(administration.render());
		} catch(Exception e) {
			return internalServerError(serverUnavailable.render(e));
		}
	}
	
	public static Result projects() {
		response().setContentType("application/json");
		return ok(com.mongodb.util.JSON.serialize(Project.getProjects()));
	}
	
	public static Result updateDetails(String projectName) {
		response().setContentType("application/json");
		
		Map<String, String[]> parameters = request().body().asFormUrlEncoded();
	    String title = parameters.get("title")!=null?parameters.get("title")[0]:"";
	    String description = parameters.get("description")!=null?parameters.get("description")[0]:"";
	    String version = parameters.get("mintVersion")!=null?parameters.get("mintVersion")[0]:"";
	    String url = parameters.get("mintURL")!=null?parameters.get("mintURL")[0]:"";
		
	    Project project = new Project(projectName);
		BasicDBObject result = project.getMetadata();
		result.put("projectName", projectName);
		result.put("title", title);
		result.put("description", description);
		result.put("mintVersion", version);
		result.put("mintURL", url);
		Project.saveProjectMetadata(result);
		
		return ok(com.mongodb.util.JSON.serialize(project.getMetadata()));
	}
}