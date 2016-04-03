package gr.ntua.ivml.mint.oai.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import gr.ntua.ivml.mint.oai.util.Config;
import gr.ntua.ivml.mint.oai.util.JSONReader;
import gr.ntua.ivml.mint.oai.util.MongoDB;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class Project {
    public static final String METADATA_ID = "projectName";
    public static final String METADATA_TITLE = "title";
    public static final String METADATA_DESCRIPTION = "description";
    public static final String METADATA_ORGANIZATIONS = "organizations";
    public static final String METADATA_MINT_ID = "mintId";
    public static final String METADATA_MINT_URL = "mintURL";
    public static final String METADATA_MINT_VERSION = "mintVersion";
    public static final String METADATA_MINT_VERSION_1 = "mint1";
    public static final String METADATA_MINT_VERSION_2 = "mint2";

    private String projectId = null;
    private BasicDBObject metadata = null;

    public Project(String projectId) {
        this.projectId = projectId;
    }

    /**
     * Get json object with metadata for all projects in OAI.
     *
     * @return
     */
    public static BasicDBList getProjectsMetadata() {
        BasicDBObject q = new BasicDBObject();
        q.put("id", "projects");

        BasicDBObject metadata;

        if (MongoDB.getDB().getCollection("metadata").findOne(q) == null) {
            return new BasicDBList();
        } else {
            metadata = (BasicDBObject) MongoDB.getDB().getCollection("metadata").findOne(q);

            BasicDBList projects = (BasicDBList) metadata.get("projects");

            return projects;
        }
    }


    /**
     * Get json object with metadata for all projects in OAI.
     *
     * @return
     */
    public static BasicDBObject getProjects() {
        BasicDBObject results = new BasicDBObject();

        BasicDBList projects = Project.getProjectsMetadata();

        for (Object o : projects) {
            BasicDBObject project = (BasicDBObject) o;
            results.put(project.getString(Project.METADATA_ID), project);
        }

        return results;
    }

    /**
     * Get the project id which corresponds to the project mongo collection.
     *
     * @return
     */
    public String getId() {
        return this.projectId;
    }

    /**
     * Get project metadata. Caches result if not null.
     *
     * @return if no metadata exists, returns empty object to avoid null result and result is not cached.
     */
    public BasicDBObject getMetadata() {
        if (this.metadata == null) {
            BasicDBObject projects = Project.getProjects();
            this.metadata = (BasicDBObject) projects.get(this.projectId);
        }

        // return empty object to avoid null result;
        return (this.metadata != null) ? this.metadata : new BasicDBObject();
    }


    /**
     * Saves metadata information
     *
     * @param project
     */
    public static void saveProjectMetadata(BasicDBObject project) {
        DBCollection collection = MongoDB.getDB().getCollection("metadata");
        BasicDBList metadata = Project.getProjectsMetadata();

        boolean isNew = true;
        for (Object o : metadata) {
            BasicDBObject p = (BasicDBObject) o;
            if (p.containsField(Project.METADATA_ID) && p.getString(Project.METADATA_ID).equals(project.getString(Project.METADATA_ID))) {
                p.putAll(project.toMap());
                isNew = false;
                break;
            }
        }

        if (isNew) {
            metadata.add(project);
        }

        BasicDBObject update = new BasicDBObject("id", "projects");
        collection.update(update, new BasicDBObject("$set", new BasicDBObject("projects", metadata)));
    }

    /**
     * Get the project title or the project id if title is not set
     *
     * @return
     */
    public String getTitle() {
        String title = this.getMetadata().getString(Project.METADATA_TITLE);
        return (title != null) ? title : this.projectId;
    }

    /**
     * Get the project description.
     *
     * @return null if description is not set.
     */
    public String getDescription() {
        String decription = this.getMetadata().getString(Organization.METADATA_DESCRIPTION);
        return decription;
    }

    /**
     * Get the URL of the mint project.
     *
     * @return null if description is not set.
     */
    public String getMintURL() {
        String url = this.getMetadata().getString(Project.METADATA_MINT_URL);
        return url;
    }

    /**
     * Get list of namespaces related to this project.
     *
     * @return
     */
    public List<String> getNamespaces() {
        List<?> ns = MongoDB.getDB().getCollection(this.projectId).distinct("namespace.prefix");
        ArrayList<String> namespaces = new ArrayList<String>();
        for (Object n : ns) {
            String prefix = n.toString();
            namespaces.add(prefix);
        }

        return namespaces;
    }

    /**
     * Get a list of organization ids in OAI for this project.
     *
     * @return
     */
    public List<Integer> getOrganizationIds() {
        List<?> objects = MongoDB.getDB().getCollection(this.projectId).distinct("orgId");
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (Object o : objects) {
            ids.add(new Integer(o.toString()));
        }

        return ids;
    }

    /**
     * Get number of organizations in OAI for this project.
     *
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
     *
     * @return
     */
    public int getReportsCount() {
        BasicDBObject q = new BasicDBObject();
        q.put(Project.METADATA_ID, this.projectId);
        int publications = MongoDB.getDB().getCollection("reports").find(q).count(); //total number of reports

        return publications;
    }

    /**
     * Get number of conflicts for this project.
     *
     * @return
     */
    public int getConflicts() {
        int conflicts = 0;

        for (Integer id : this.getOrganizationIds()) {
            BasicDBObject q1 = new BasicDBObject();
            q1.put("orgId", id);
            DBCursor cur = MongoDB.getDB().getCollection("reports").find(q1);
            while (cur.hasNext()) {
                BasicDBObject tmpQ = (BasicDBObject) cur.next();
                if (tmpQ.get("conflictedRecords") != null) {
                    conflicts += tmpQ.getInt("conflictedRecords");
                }
            }

        }

        return conflicts;
    }

    /**
     * Get latest publication date for this project
     *
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
     *
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
     *
     * @return
     * @organizationId
     */
    public Date getLatestPublicationDate(Integer organizationId) {
        return this.getOrganization(organizationId).getLatestPublicationDate();
    }

    /**
     * Get number of records published by this organization that belong to the specified namespace.
     *
     * @param namespace
     * @param organizationId
     * @return
     */
    public int getRecordsCount(String namespace, Integer organizationId) {
        return this.getOrganization(organizationId).getRecordsCount(namespace);
    }

    /**
     * Get number of records of all namespaces published by this organization.
     *
     * @param organizationId
     * @return
     */
    public int getRecordsCount(Integer organizationId) {
        return this.getOrganization(organizationId).getRecordsCount();
    }

    /**
     * Get a map with organization id as the key and the number of records for the specified namespace as the value.
     *
     * @param namespace
     * @return
     */
    public Map<String, Integer> getRecordsCountPerOrganization(String namespace) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (Integer organizationId : this.getOrganizationIds()) {
            map.put(organizationId.toString(), new Integer(this.getRecordsCount(namespace, organizationId)));
        }

        return map;
    }

    /**
     * Get string representation of OAI url for this project
     */
    public String getOAIUrl(String namespace) {
        return "/" + this.projectId + "/oai?verb=ListIdentifiers&metadataPrefix=" + namespace;
    }

    /**
     * Retrieves organization metadata and stores in project metadata.
     * Uses mint.api.baseURL for mint1 projects, project url for mint2 projects.
     *
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public BasicDBObject fetchOrganizationsMetadata() throws JSONException, IOException {
        BasicDBObject result = new BasicDBObject();
        JSONObject organizations = new JSONObject();
        BasicDBObject metadata = this.getMetadata();
        //System.out.println("PRINT THIS ");
        if (!metadata.containsField(Project.METADATA_MINT_VERSION) || metadata.getString(Project.METADATA_MINT_VERSION).equals(Project.METADATA_MINT_VERSION_1)) {

            try {
                String mintid = this.projectId;
                if (metadata.containsField(Project.METADATA_MINT_ID))
                    mintid = metadata.getString(Project.METADATA_MINT_ID);
                //System.out.println(Config.get("mint.api.baseURL") + "/servlets/dbaseorgs?database=" + mintid);
                organizations = JSONReader.readJsonFromUrl(Config.get("mint.api.baseURL") + "/servlets/dbaseorgs?database=" + mintid);

                if (organizations.has("organizations")) {
                    JSONArray array = organizations.getJSONArray("organizations");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject organization = array.getJSONObject(i);
                        String id = organization.getString("id");
                        BasicDBObject org = new BasicDBObject();
                        org.put(Organization.METADATA_NAME, organization.getString("organization"));
                        org.put(Organization.METADATA_ADDRESS, organization.getString("address"));
                        org.put(Organization.METADATA_COUNTRY, organization.getString("country"));
                        org.put(Organization.METADATA_DESCRIPTION, organization.getString("description"));

                        if (organization.has("Administrator List") && organization.getJSONArray("Administrator List").length() > 0) {
                            JSONObject administrator = (JSONObject) organization.getJSONArray("Administrator List").get(0);
                            org.put(Organization.METADATA_CONTACT, administrator.getString("name"));
                            org.put(Organization.METADATA_CONTACT_EMAIL, administrator.getString("email"));
                            org.put(Organization.METADATA_CONTACT_PHONE, administrator.getString("telephone"));
                        }

                        result.put(id, org);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                organizations = new JSONObject().put("error", e.getMessage());
            }
        } else if (metadata.getString(Project.METADATA_MINT_VERSION).equals(Project.METADATA_MINT_VERSION_2)) {
            String minturl = metadata.getString(Project.METADATA_MINT_URL);
            //System.out.println(minturl+"/UrlApi?isApi=true&type=Organization&action=list");
            organizations = JSONReader.readJsonFromUrl(minturl + "/UrlApi?isApi=true&type=Organization&action=list");


            JSONObject users = JSONReader.readJsonFromUrl(minturl + "/UrlApi?isApi=true&type=User&action=list");

            if (organizations.has("result")) {
                JSONArray array = organizations.getJSONArray("result");
                JSONArray usersArray = users.getJSONArray("result");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject organization = array.getJSONObject(i);
                    String id = organization.getString("dbID");
                    BasicDBObject org = new BasicDBObject();
                    String name = organization.has("englishName") ? organization.getString("englishName") : organization.getString("originalName");
                    org.put(Organization.METADATA_NAME, name);
                    if (organization.has("address")) {
                        org.put(Organization.METADATA_ADDRESS, organization.getString("address"));
                    } else {
                        org.put(Organization.METADATA_ADDRESS, "");
                    }
                    if (organization.has("country")) {
                        org.put(Organization.METADATA_COUNTRY, organization.getString("country"));
                    } else {
                        org.put(Organization.METADATA_COUNTRY, "");
                    }
                    if (organization.has("description")) {
                        org.put(Organization.METADATA_DESCRIPTION, organization.getString("description"));
                    } else {
                        org.put(Organization.METADATA_DESCRIPTION, "");
                    }
                    //	org.put(Organization.METADATA_COUNTRY, organization.getString("country"));
                    //	org.put(Organization.METADATA_DESCRIPTION, organization.getString("description"));

                    if (usersArray != null && organization.has("primaryContact") && organization.getJSONObject("primaryContact").has("dbID")) {
                        String contactId = organization.getJSONObject("primaryContact").getString("dbID");

                        for (int u = 0; u < usersArray.length(); u++) {
                            JSONObject user = (JSONObject) usersArray.get(u);
                            if (user.getString("dbID").equals(contactId)) {
                                String userName = null;

                                if (user.has("firstName")) userName = user.getString("firstName");
                                if (user.has("lastName")) {
                                    if (userName.length() > 0) userName += " ";
                                    userName += user.getString("lastName");
                                }

                                org.put(Organization.METADATA_CONTACT, userName);
                                if (user.has("email"))
                                    org.put(Organization.METADATA_CONTACT_EMAIL, user.getString("email"));
                                if (user.has("workTelephone"))
                                    org.put(Organization.METADATA_CONTACT_PHONE, user.getString("workTelephone"));
                                break;
                            }
                        }
                    }

                    result.put(id, org);
                }
            }
        }

        metadata.put(Project.METADATA_MINT_ID, this.projectId);
        metadata.put(Project.METADATA_ORGANIZATIONS, result);
        Project.saveProjectMetadata(metadata);

        return result;
    }

    /**
     * Get organizations metadata for this project.
     * Metadata is a map of orgnization json data using organization id as the key
     *
     * @return
     */
    public BasicDBObject getOrganizationsMetadata() {
        BasicDBObject metadata = this.getMetadata();
        if (metadata.containsField("organizations")) return (BasicDBObject) metadata.get("organizations");
        else return new BasicDBObject();
    }
}
