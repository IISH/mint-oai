package controllers;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import gr.ntua.ivml.mint.oai.model.Project;
import gr.ntua.ivml.mint.oai.util.MongoDB;
import gr.ntua.ivml.mint.oai.util.StringUtils;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.project;
import views.html.serverUnavailable;

import java.util.List;

public class Projects extends Controller {
    // views

    public Result landingPage(String projectId) {
        Project p = new Project(projectId);
        try {
            return ok(project.render(p));
        } catch (Exception e) {
            return internalServerError(serverUnavailable.render(e));
        }
    }

    //json data

    public Result getProjects() {
        BasicDBObject projects = Project.getProjects();

        BasicDBObject list = new BasicDBObject();
        for (String setName : MongoDB.getDB().getCollectionNames()) {

            if (!setName.equals("reports") && !setName.equals("conflicts") && !setName.equals("system.indexes")
                    && !setName.equals("fs.files") && !setName.equals("fs.chunks") && !setName.equals("metadata")) {

                BasicDBObject project = null;
                for (String key : projects.keySet()) {
                    BasicDBObject p = (BasicDBObject) projects.get(key);
                    if (p.containsField("projectName") && p.getString("projectName").equals(setName)) {
                        project = p;
                    }
                }

                if (project == null) {
                    project = new BasicDBObject();
                    project.append("projectName", setName);
                    project.append("metadata", "");
                }

                list.append(setName, project);
            }
        }

        return ok(com.mongodb.util.JSON.serialize(list)).as("application/json");
    }

    public Result getOverall(String proj) {
        Project project = new Project(proj);

        List<String> prefixes = project.getNamespaces();

        BasicDBObject res = new BasicDBObject();

        BasicDBList namespaces = new BasicDBList();
        for (String prefix : prefixes) {
            BasicDBObject namespace = new BasicDBObject();
            namespace.put("prefix", prefix);
            namespace.put("oai", project.getOAIUrl(prefix));
            namespaces.add(namespace);
        }
        res.put("namespaces", namespaces);
        res.put("organizations", project.getOrganizationCount());
        res.put("unique", project.getUniqueRecordsCount());
        res.put("publications", project.getReportsCount());
        res.put("duplicates", project.getConflicts());
        res.put("latestPublicationDate", StringUtils.formatDate(project.getLatestPublicationDate()));

        return ok(com.mongodb.util.JSON.serialize(res)).as("application/json");
    }

    public Result getRecordsPerOrganization(String proj, String namespace) {
        Project project = new Project(proj);

        BasicDBObject result = new BasicDBObject();
        result.putAll(project.getRecordsCountPerOrganization(namespace));

        return ok(com.mongodb.util.JSON.serialize(result)).as("application/json");
    }
}
