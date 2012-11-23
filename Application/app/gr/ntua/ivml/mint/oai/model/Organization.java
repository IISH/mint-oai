package gr.ntua.ivml.mint.oai.model;

import gr.ntua.ivml.mint.oai.util.MongoDB;

import java.util.Calendar;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class Organization {
	Project project = null;
	Integer organizationId = null;
	
	public Organization(Project project, Integer organizationId) {
		this.project = project;
		this.organizationId = organizationId;
	}
	
	/**
	 * Get the project that this organization belongs to. 
	 * @return
	 */
	public Project getProject() {
		return this.project;
	}
	
	/**
	 * Get the organization's id.
	 * @return
	 */
	public Integer getOrganizationId() {
		return this.organizationId;
	}

	/**
	 * Get latest publication date for an organization.
	 * @organizationId
	 * @return
	 */
	public Date getLatestPublicationDate() {
		Calendar calendar = Calendar.getInstance();

		BasicDBObject q = new BasicDBObject("orgId", organizationId);
		DBCursor cur = MongoDB.getDB().getCollection("reports").find(q).sort(new BasicDBObject("datestamp", -1));
		BasicDBObject latestReport = (BasicDBObject) cur.next();

		calendar.setTimeInMillis(latestReport.getLong("datestamp"));
		Date date = calendar.getTime();
		
		return date; 
	}
	

	/**
	 * Get number of records of all namespaces published by this organization.
	 * @param namespace
	 * @param organizationId
	 * @return
	 */
	public int getRecordsCount() {
		BasicDBObject qN = new BasicDBObject();
		qN.put("orgId", organizationId);
		int size = MongoDB.getDB().getCollection(this.getProject().getId()).find(qN).count();
		
		return size;
	}

	/**
	 * Get number of records published by this organization that belong to the specified namespace.
	 * @param namespace
	 * @param organizationId
	 * @return
	 */
	public int getRecordsCount(String namespace) {
		BasicDBObject qN = new BasicDBObject();
		qN.put("orgId", organizationId);
		qN.put("namespace.prefix", namespace);
		int size = MongoDB.getDB().getCollection(this.getProject().getId()).find(qN).count();
		
		return size;
	}

	/**
	 * Get metadata for this organization
	 * @return
	 */
	public BasicDBObject getMetadata() {
		BasicDBObject metadata = this.project.getOrganizationsMetadata();
		
		if(metadata.containsField(this.organizationId.toString())) {
			return (BasicDBObject) metadata.get(this.organizationId.toString());
		}
		
		return new BasicDBObject();
	}

}
