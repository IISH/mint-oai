package gr.ntua.ivml.mint.oai.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.ntua.ivml.mint.oai.util.MongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;

public class Project {
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
	public List<Integer> getOrganizationIds() {
		List<Object> objects =  (List<Object>) MongoDB.getDB().getCollection(this.project).distinct("orgId");
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(Object o: objects) {
			ids.add(new Integer(o.toString()));
		}
		
		return ids;
	}
	
	/**
	 * Get number of organizations in OAI for this project.
	 * @return
	 */
	public int getOrganizationCount() {
		return this.getOrganizationIds().size();
	}
	
	/**
	 * Get number of unique published records for this project.
	 */
	public long getUniqueRecordsCount() {
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
		
		for(Integer id: this.getOrganizationIds()){
			BasicDBObject q1 = new BasicDBObject();
			q1.put("orgId", id);
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
	 * Get number of records published in this project that belong to the specified namespace.
	 * @param namespace
	 * @return
	 */
	public int getRecordsCount(String namespace) {
		BasicDBObject qN = new BasicDBObject();
		qN.put("namespace.prefix", namespace);
		int size = MongoDB.getDB().getCollection(this.project).find(qN).count();
		
		return size;
	}

	/**
	 * Get number of records published by this organization that belong to the specified namespace.
	 * @param namespace
	 * @param organizationId
	 * @return
	 */
	public int getRecordsCount(String namespace, Integer organizationId) {
		BasicDBObject qN = new BasicDBObject();
		qN.put("orgId", organizationId);
		qN.put("namespace.prefix", namespace);
		int size = MongoDB.getDB().getCollection(this.project).find(qN).count();
		
		return size;
	}

	/**
	 * Get a map with organization id as the key and the number of records for the specified namespace as the value.
	 * @param namespace
	 * @return
	 */
	public Map<String, Integer> getRecordsCountPerOrganization(String namespace) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		for(Integer organizationId: this.getOrganizationIds()) {
			map.put(organizationId.toString(), new Integer(this.getRecordsCount(namespace, organizationId)));
		}
		
		return map;
	}
	
	/**
	 * Get string representation of OAI url for this project
	 */
	public String getOAI(String namespace) {
		return "/" + this.project + "/oai?verb=ListIdentifiers&metadataPrefix=" + namespace;
	}
}
