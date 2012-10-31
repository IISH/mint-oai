package gr.ntua.ivml.mint.oai.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gr.ntua.ivml.mint.oai.util.MongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;

public class Project {
	public class Statistics {
		public int publications = 0;
		public int organizations = 0;
		public int duplicates = 0;
		public long records = 0;
	}
	
	private String project = null;
	
	public Project(String project) {
		this.project = project;
	}
	
	/**
	 * Get json object with metadata for all projects in OAI.
	 * @return
	 */
	public static BasicDBList getAll() {
		BasicDBObject q = new BasicDBObject();
		q.put("id", "projects");
		BasicDBObject projects = (BasicDBObject) MongoDB.getDB().getCollection("metadata").findOne(q);

		return (BasicDBList) projects.get("projects");
	}
	
	/**
	 * Get list of namespaces related to this project.
	 * @return
	 */
	public List<String> getNamespaces() {
		ArrayList<Object> ns = (ArrayList<Object>) MongoDB.getDB().getCollection(this.project).distinct("namespace.prefix");
		ArrayList<String> namespaces = new ArrayList<String>();
		for(Object n:ns){
			String prefix = (String) n;
			namespaces.add(prefix);
		}
		
		return namespaces;
	}
	
	/**
	 * Get a list of organization ids in OAI for this project.
	 * @return
	 */
	public List<Object> getOrganizationIds() {
		return (List<Object>) MongoDB.getDB().getCollection(this.project).distinct("orgId");
	}
	
	/**
	 * Get number of organizations in OAI for this project.
	 * @return
	 */
	public int getOrganizationCount() {
		return this.getOrganizationIds().size();
	}
	
	/**
	 * Get number of published records for this project.
	 */
	public long getRecordsCount() {
		 return MongoDB.getDB().getCollection(this.project).count();
	}
	
	/**
	 * Get number of reports for this project.
	 * @return
	 */
	public int getReportsCount() {
		BasicDBObject q = new BasicDBObject();
		q.put("projectName", this.project);
		int publications = MongoDB.getDB().getCollection("reports").find(q).count(); //total number of reports

		return publications;
	}

	/**
	 * Get number of conflicts for this project.
	 * @return
	 */
	public int getConflicts() {
		int conflicts = 0;
		
		for(Object obj : this.getOrganizationIds()){
			Integer in = (Integer)obj;
			BasicDBObject q1 = new BasicDBObject();
			q1.put("orgId", in.intValue());
			DBCursor cur = MongoDB.getDB().getCollection("reports").find(q1);
			while(cur.hasNext()){
				BasicDBObject tmpQ = (BasicDBObject) cur.next();
				if(tmpQ.get("conflictedRecords") != null){
					conflicts += tmpQ.getInt("conflictedRecords");	
				}
			}
				
		}	

		return conflicts;
	}
	
	/**
	 * Get latest publication date for this project
	 * @return
	 */
	public Date getLatestPublicationDate() {
		Calendar calendar = Calendar.getInstance();

		DBCursor cur1 = MongoDB.getDB().getCollection(this.project).find().sort(new BasicDBObject("datestamp", -1));
		BasicDBObject latest = (BasicDBObject) cur1.next();

		calendar.setTimeInMillis(latest.getLong("datestamp"));
		Date date = calendar.getTime();
		
		return date; 
	}
	
	/**
	 * Get aggregated statistics for this project.
	 * @return
	 */
	public Project.Statistics getStatistics() {
		Project.Statistics statistics = new Project.Statistics();
		
		statistics.records = this.getRecordsCount();
		statistics.duplicates = this.getConflicts();
		statistics.organizations = this.getOrganizationCount();
		statistics.publications = this.getReportsCount();
		
		return statistics;
	}
}
