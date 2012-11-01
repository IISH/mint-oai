package gr.ntua.ivml.mint.oai.model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

import gr.ntua.ivml.mint.oai.util.MongoDB;

public class Report {

	private String projectName;
	
	public Report(String projectName){
		this.projectName = projectName;
	}
	
	/**
	 * Get the number of reports for the current project
	 * @return
	 */
	public int getReportsCount(){
		int res = 0;
		BasicDBObject q = new BasicDBObject();
		q.put("projectName",this.projectName);
		res = MongoDB.getDB().getCollection("reports").find(q).count();
		return res;
	}
	
	/**
	 * Get the number of reports for a specific organization
	 * @return
	 */
	public int getReportsCount(String orgId){
		int res = 0;
		int id = Integer.parseInt(orgId);
		BasicDBObject q = new BasicDBObject();
		q.put("projectName", this.projectName);
		q.put("orgId", id);
		res = MongoDB.getDB().getCollection("reports").find(q).count();
		return res;
	}
	
	/**
	 * Get a filtered list of all the publications/reports for a specific organizations, based on the orgId
	 * @return
	 */
	public List<BasicDBObject> getReports(String orgId){
		ArrayList<BasicDBObject> res = new ArrayList<BasicDBObject>();
		int id = Integer.parseInt(orgId);
		BasicDBObject q = new BasicDBObject();
		BasicDBObject f = new BasicDBObject();
		q.put("projectName", this.projectName);
		q.put("orgId", id);
		
		f.put("projectName", 1);
		f.put("orgId", 1);
		f.put("datestamp", 1);
		f.put("insertedRecords", 1);
		f.put("conflictedRecords", 1);
		f.put("_id", 0);
		
		DBCursor cur = MongoDB.getDB().getCollection("reports").find(q, f);
		while(cur.hasNext()){
			res.add( (BasicDBObject) cur.next());
		}
		
		return res;
	}
}
