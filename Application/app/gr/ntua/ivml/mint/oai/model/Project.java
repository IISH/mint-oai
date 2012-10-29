package gr.ntua.ivml.mint.oai.model;

import gr.ntua.ivml.mint.oai.util.MongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;

public class Project {
	public static BasicDBList getAll() {
		BasicDBObject q = new BasicDBObject();
		q.put("id", "projects");
		BasicDBObject projects = (BasicDBObject) MongoDB.getDB().getCollection("metadata").findOne(q);

		return (BasicDBList) projects.get("projects");
	}
}
