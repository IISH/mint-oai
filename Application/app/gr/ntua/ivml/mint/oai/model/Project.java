package gr.ntua.ivml.mint.oai.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	private String projectId = null;
	
	public Project(String projectId) {
		this.projectId = projectId;
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
	 * Get the project id which corresponds to the project mongo collection.
	 * @return
	 */
	public String getProjectId() {
		return this.projectId;
	}
	
	/**
	 * Get list of namespaces related to this project.
	 * @return
	 */
	public List<String> getNamespaces() {
		ArrayList<Object> ns = (ArrayList<Object>) MongoDB.getDB().getCollection(this.projectId).distinct("namespace.prefix");
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
		List<Object> objects =  (List<Object>) MongoDB.getDB().getCollection(this.projectId).distinct("orgId");
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
		 return MongoDB.getDB().getCollection(this.projectId).count();
	}
	
	/**
	 * Get number of reports for this project.
	 * @return
	 */
	public int getReportsCount() {
		BasicDBObject q = new BasicDBObject();
		q.put("projectName", this.projectId);
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

		DBCursor cur = MongoDB.getDB().getCollection(this.projectId).find().sort(new BasicDBObject("datestamp", -1));
		BasicDBObject latestReport = (BasicDBObject) cur.next();

		calendar.setTimeInMillis(latestReport.getLong("datestamp"));
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
		int size = MongoDB.getDB().getCollection(this.projectId).find(qN).count();
		
		return size;
	}
	
	public Organization getOrganization(Integer organizationId) {
		return new Organization(this, organizationId);
	}
	
	/**
	 * Get latest publication date for an organization of this project
	 * @organizationId
	 * @return
	 */
	public Date getLatestPublicationDate(Integer organizationId) {
		return this.getOrganization(organizationId).getLatestPublicationDate();
	}

	/**
	 * Get number of records published by this organization that belong to the specified namespace.
	 * @param namespace
	 * @param organizationId
	 * @return
	 */
	public int getRecordsCount(String namespace, Integer organizationId) {
		return this.getOrganization(organizationId).getRecordsCount(namespace);
	}

	/**
	 * Get number of records of all namespaces published by this organization.
	 * @param namespace
	 * @param organizationId
	 * @return
	 */
	public int getRecordsCount(Integer organizationId) {
		return this.getOrganization(organizationId).getRecordsCount();
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
		return "/" + this.projectId + "/oai?verb=ListIdentifiers&metadataPrefix=" + namespace;
	}
}
