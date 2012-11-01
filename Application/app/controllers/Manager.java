package controllers;

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

import gr.ntua.ivml.mint.oai.model.Project;
import gr.ntua.ivml.mint.oai.util.MongoDB;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.*;

public class Manager extends Controller {
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
	
	
	public static Result getOrgStats(String proj){
		ArrayList<Object> dists = (ArrayList<Object>) MongoDB.getDB().getCollection(proj).distinct("orgId");
		
		BasicDBObject q = null;
		Calendar mydate = Calendar.getInstance();
		Object[] vals = null;
		ArrayList<Object[]> returnedVals = new ArrayList<Object[]>();
		for(Object obj : dists){
			Integer in = (Integer) obj;
			q = new BasicDBObject("orgId", in);
			DBCursor cur = MongoDB.getDB().getCollection("reports").find(q).sort(new BasicDBObject("datestamp", -1));
			BasicDBObject latestReport = (BasicDBObject) cur.next();
			mydate.setTimeInMillis(latestReport.getLong("datestamp"));
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String date =  df.format(mydate.getTime());
			int size = MongoDB.getDB().getCollection(proj).find(q).count();
			vals = new Object[3];
			vals[0] = "" + in;
			BasicDBObject tmp = new BasicDBObject();
			tmp.put("v", size);
			tmp.put("f", NumberFormat.getNumberInstance(Locale.US).format(size));
			vals[1] = tmp;
			vals[2] = date;
			returnedVals.add(vals);
		}
		BasicDBObject res = new BasicDBObject();
		res.put("values", returnedVals);
		response().setContentType("application/json");
		return ok(com.mongodb.util.JSON.serialize(res));
	}
	
	public static Result getProjects() {
		response().setContentType("application/json");
		BasicDBList projects = Project.getAll();

		BasicDBObject list = new BasicDBObject();
		for(String setName: MongoDB.getDB().getCollectionNames()){
			
			if(!setName.equals("reports") && !setName.equals("conflicts") && !setName.equals("system.indexes") 
					&& !setName.equals("fs.files") && !setName.equals("fs.chunks") && !setName.equals("metadata")){

				BasicDBObject project = null;
				for(Object o: projects) {
					BasicDBObject p = ((BasicDBObject) o);
					if(p.getString("projectName").equals(setName)) {
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

	public static Result getOverallForProject(String proj){
		response().setContentType("application/json");
		Project project = new Project(proj);

		List<String> namespaces = project.getNamespaces();
		
		BasicDBObject res = new BasicDBObject();

		res.put("namespaces", namespaces);
		res.put("organizations", project.getOrganizationCount());
		res.put("unique", project.getUniqueRecordsCount());
		res.put("publications", project.getReportsCount());
		res.put("duplicates", project.getConflicts());
		res.put("latestPublicationDate", Manager.formatDate(project.getLatestPublicationDate()));
		
		return ok(com.mongodb.util.JSON.serialize(res));
	}
	
	public static Result loadStats(String proj){
		return ok(ManagerLoadDetailedStats.render(proj));
	}
	
	public static Result landingPage(){
		try {
			return ok(managerLandingPage.render());
		} catch(Exception e) {
			return internalServerError(serverUnavailable.render(e));
		}
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

	public static Result getRecordsPerOrganization(String proj, String namespace) {
		response().setContentType("application/json");
		Project project = new Project(proj);
		
		BasicDBObject result = new BasicDBObject();
		result.putAll(project.getRecordsCountPerOrganization(namespace));
		
		return ok(com.mongodb.util.JSON.serialize(result));
	}

	// utility
	
	private static String formatDate(Date date) {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
	}
}
