package controllers;

import java.io.IOException;
import java.util.Map;

import org.json.JSONException;
import com.mongodb.BasicDBObject;
import gr.ntua.ivml.mint.oai.model.Project;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Administration extends Controller {
    public static Result index() {
        return redirect("/admin/");
    }

    public Result landingPage() {
        try {
            return ok(administration.render());
        } catch (Exception e) {
            return internalServerError(serverUnavailable.render(e));
        }
    }

    public Result projects() {
        return ok(com.mongodb.util.JSON.serialize(Project.getProjects())).as("application/json");
    }

    public Result organizations(String projectName) throws JSONException {
        Project project = new Project(projectName);

        if (request().method() == "POST") {
            Map<String, String[]> parameters = request().body().asFormUrlEncoded();
            if (parameters.containsKey("refresh"))
                try {
                    project.fetchOrganizationsMetadata();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        BasicDBObject organizations = project.getOrganizationsMetadata();
        return ok(com.mongodb.util.JSON.serialize(organizations)).as("application/json");
    }

    public Result updateDetails(String projectName) {
        Project project = new Project(projectName);

        Map<String, String[]> parameters = request().body().asFormUrlEncoded();
        String title = parameters.get("title") != null ? parameters.get("title")[0] : null;
        String description = parameters.get("description") != null ? parameters.get("description")[0] : null;
        String version = parameters.get("mintVersion") != null ? parameters.get("mintVersion")[0] : null;
        String url = parameters.get("mintURL") != null ? parameters.get("mintURL")[0] : null;
        String mintId = parameters.get("mintId") != null ? parameters.get("mintId")[0] : null;


        BasicDBObject result = project.getMetadata();
        result.put("projectName", projectName);
        if (title != null) result.put("title", title);
        if (description != null) result.put("description", description);
        if (version != null) result.put("mintVersion", version);
        if (url != null) result.put("mintURL", url);
        if (mintId != null) result.put("mintId", mintId);

        Project.saveProjectMetadata(result);

        return ok(com.mongodb.util.JSON.serialize(project.getMetadata())).as("application/json");
    }
}