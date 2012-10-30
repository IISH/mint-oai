package controllers;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
	
	
	public static Result getQuickStats(String proj){
		ArrayList<Object> dists = (ArrayList<Object>) MongoDB.getDB().getCollection(proj).distinct("orgId");
		
		int totalConflicts = 0;
		for(Object obj : dists){
			Integer in = (Integer)obj;
			BasicDBObject q1 = new BasicDBObject();
			q1.put("orgId", in.intValue());
			DBCursor cur = MongoDB.getDB().getCollection("reports").find(q1);
			while(cur.hasNext()){
				BasicDBObject tmpQ = (BasicDBObject) cur.next();
				if(tmpQ.get("conflictedRecords") != null){
					totalConflicts += tmpQ.getInt("conflictedRecords");	
				}
			}
				
		}
		
		ArrayList<Object> ns = (ArrayList<Object>) MongoDB.getDB().getCollection(proj).distinct("namespace.prefix");
		ArrayList<String> namespaces = new ArrayList<String>();
		for(Object n:ns){
			String prefix = (String) n;
			namespaces.add(prefix);
		}
		
		
		
		int counter1 = dists.size(); // number of organizations.
		long counter2 = MongoDB.getDB().getCollection(proj).count(); //total records
		BasicDBObject q = new BasicDBObject();
		q.put("projectName", proj);
		response().setContentType("application/json");
		int counter3 = MongoDB.getDB().getCollection("reports").find(q).count(); //total number of reports
		BasicDBObject res = new BasicDBObject();
		res.put("OrgsNumber", NumberFormat.getNumberInstance(Locale.US).format(counter1));
		res.put("RecordsNumber", NumberFormat.getNumberInstance(Locale.US).format(counter2));
		res.put("ReportsNumber", NumberFormat.getNumberInstance(Locale.US).format(counter3));
		res.put("ConflictsNumber", NumberFormat.getNumberInstance(Locale.US).format(totalConflicts));
		
		res.put("prefixes", namespaces);
		DBCursor cur1 = MongoDB.getDB().getCollection(proj).find().sort(new BasicDBObject("datestamp", -1));
		BasicDBObject latest = (BasicDBObject) cur1.next();
		Calendar mydate = Calendar.getInstance();
		mydate.setTimeInMillis(latest.getLong("datestamp"));
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		res.put("latestDate", df.format(mydate.getTime()));
		
		
		ArrayList<BasicDBObject> recordCountsPerNS = new ArrayList<BasicDBObject>();
		
		for(String pre:namespaces){
			
			String[] headers = new String[2];
			headers[0] = "Organization IDs";
			headers[1] = "records";
			ArrayList<Object[]> returnedVals = new ArrayList<Object[]>();
			returnedVals.add(headers);
			Object[] vals;
			BasicDBObject tmpObj = new BasicDBObject();
			for(Object obj:dists){
				Integer in = (Integer)obj;
				vals = new Object[2];
				BasicDBObject qN = new BasicDBObject();
				qN.put("orgId", in);
				qN.put("namespace.prefix", pre);
				int size = MongoDB.getDB().getCollection(proj).find(qN).count();
				vals[0] = "" + in;
				vals[1] = size;
				returnedVals.add(vals);
			}
			tmpObj.put("namespace", pre);
			tmpObj.put("values", returnedVals);
			recordCountsPerNS.add(tmpObj);
		}
		res.put("orgsRecordsCount", recordCountsPerNS);
		
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

	public static Result getOverall(){
		BasicDBObject result = new BasicDBObject();
		response().setContentType("application/json");

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
	
	public static Result getProjects(){
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
	
	public static Result getRecordsPerProject(){
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
		response().setContentType("application/json");
		return ok(com.mongodb.util.JSON.serialize(obj));
	}
}
