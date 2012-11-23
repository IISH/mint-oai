package controllers;

import gr.ntua.ivml.mint.oai.model.Organization;
import gr.ntua.ivml.mint.oai.model.Project;
import gr.ntua.ivml.mint.oai.model.Report;
import gr.ntua.ivml.mint.oai.util.MongoDB;
import gr.ntua.ivml.mint.oai.util.StringUtils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.project;
import views.html.serverUnavailable;

public class Projects extends Controller {
	// views
	
	public static Result landingPage(String projectId) {
		Project p = new Project(projectId);
		try {
			return ok(project.render(p));
		} catch(Exception e) {
			return internalServerError(serverUnavailable.render(e));
		}
	}
	
	//json data
	
	public static Result getReportsPerOrganization(String proj, String orgId){
		Report rep = new Report(proj);
		BasicDBObject result = new BasicDBObject();
		result.put("values", rep.getReports(orgId));
		return ok(com.mongodb.util.JSON.serialize(result));
	}
	
	public static Result getProjects() {
		response().setContentType("application/json");
		BasicDBObject projects = Project.getProjects();

		BasicDBObject list = new BasicDBObject();
		for(String setName: MongoDB.getDB().getCollectionNames()){
			
			if(!setName.equals("reports") && !setName.equals("conflicts") && !setName.equals("system.indexes") 
					&& !setName.equals("fs.files") && !setName.equals("fs.chunks") && !setName.equals("metadata")){

				BasicDBObject project = null;
				for(String key: projects.keySet()) {
					BasicDBObject p = (BasicDBObject) projects.get(key);
					if(p.containsField("projectName") && p.getString("projectName").equals(setName)) {
						project = p;
					}
				}
				
				if(project == null) {
					project = new BasicDBObject();
					project.append("projectName", setName);
				}
				
				list.append(setName, project);
			}
		}
		
		return ok(com.mongodb.util.JSON.serialize(list));
	}
		
	public static Result getOrganizations(String proj){
		Project project = new Project(proj);
		
		List<Integer> organizationIds =  project.getOrganizationIds();
		BasicDBObject result = new BasicDBObject();
		
		for(Integer organizationId: organizationIds){
			BasicDBObject organization = new BasicDBObject();
			
			Organization org = project.getOrganization(organizationId);
			organization.put("id", organizationId.toString());
			organization.put("metadata", org.getMetadata());
			organization.put("latestPublicationDate", StringUtils.formatDate(project.getLatestPublicationDate(organizationId)));
			organization.put("records", project.getRecordsCount(organizationId));
			
			result.put(organizationId.toString(), organization);
		}
		

		response().setContentType("application/json");
		return ok(com.mongodb.util.JSON.serialize(result));
	}
	
	public static Result getOverall(String proj){
		response().setContentType("application/json");
		Project project = new Project(proj);

		List<String> prefixes = project.getNamespaces();
		
		BasicDBObject res = new BasicDBObject();

		BasicDBList namespaces = new BasicDBList();
		for(String prefix: prefixes) {
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
		
		return ok(com.mongodb.util.JSON.serialize(res));
	}
	
	public static Result getRecordsPerOrganization(String proj, String namespace) {
		response().setContentType("application/json");
		Project project = new Project(proj);
		
		BasicDBObject result = new BasicDBObject();
		result.putAll(project.getRecordsCountPerOrganization(namespace));
		
		return ok(com.mongodb.util.JSON.serialize(result));
	}
	
	public static Result getOrgTimeLine(String proj, String org){
		BasicDBObject q = new BasicDBObject();
		q.put("projectName", proj);
		q.put("orgId", Integer.parseInt(org));
		DBCursor cur = MongoDB.getDB().getCollection("reports").find(q);
		
		if(cur.count() == 0){
			return notFound();
		}
		
		BasicDBObject res = new BasicDBObject();
		String headline = "Timeline of publications for project with ID " + org + " for project " + proj;
		res.put("headhline", headline);
		res.put("type", "default");
		ArrayList<BasicDBObject> events = new ArrayList<BasicDBObject>();
		DateFormat df = new SimpleDateFormat("yyyy,MM,dd");
		Calendar mydate = Calendar.getInstance();
		
		while(cur.hasNext()){
			BasicDBObject tmp = (BasicDBObject) cur.next();
			BasicDBObject event = new BasicDBObject();
			String text = "";
			text += "<p><h1>Inserted Records</h1>" + tmp.getInt("insertedRecords") + "</p>";
			text += "<p><h1>Conflicts Found</h1>" + tmp.getInt("conflictedRecords") + "</p>";
			mydate.setTimeInMillis(tmp.getLong("datestamp"));
			event.put("startDate", df.format(mydate.getTime()));
			mydate.add(Calendar.MINUTE, 5);
			event.put("endDate", df.format(mydate.getTime()));
			event.put("text", text);
			events.add(event);
		}
		
		res.put("date", events);
		BasicDBObject tm = new BasicDBObject();
		tm.put("timeline", res);
		return ok(com.mongodb.util.JSON.serialize(tm));
	}
}
