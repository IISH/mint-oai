package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gr.ntua.ivml.mint.oai.model.Organization;
import gr.ntua.ivml.mint.oai.model.Project;
import gr.ntua.ivml.mint.oai.model.Report;
import gr.ntua.ivml.mint.oai.util.MongoDB;
import gr.ntua.ivml.mint.oai.util.StringUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.organization;
import views.html.serverUnavailable;

public class Organizations extends Controller {
    // views

    public Result landingPage(String projectId, String organizationId) {
        Project p = new Project(projectId);
        Organization o = p.getOrganization(Integer.parseInt(organizationId));
        try {
            return ok(organization.render(p, o));
        } catch (Exception e) {
            return internalServerError(serverUnavailable.render(e));
        }
    }

    // json data

    public Result getOrganizations(String proj) {
        Project project = new Project(proj);

        List<Integer> organizationIds = project.getOrganizationIds();
        BasicDBObject result = new BasicDBObject();

        for (Integer organizationId : organizationIds) {
            BasicDBObject organization = new BasicDBObject();

            Organization org = project.getOrganization(organizationId);
            organization.put("id", organizationId.toString());
            organization.put("metadata", org.getMetadata());
            organization.put("latestPublicationDate", StringUtils.formatDate(project.getLatestPublicationDate(organizationId)));
            organization.put("records", project.getRecordsCount(organizationId));

            result.put(organizationId.toString(), organization);
        }


        return ok(com.mongodb.util.JSON.serialize(result)).as("application/json");
    }


    public Result getOrganizationCounts(String proj, String orgId) {
        Project project = new Project(proj);
        Organization org = project.getOrganization(Integer.parseInt(orgId));
        BasicDBObject result = new BasicDBObject();

        ArrayList<BasicDBObject> counts = new ArrayList<BasicDBObject>();

        List<String> prefixes = project.getNamespaces();
        Integer count = 0;
        for (String prefix : prefixes) {
            BasicDBObject namespace = new BasicDBObject();
            namespace.put("prefix", prefix);
            count = org.getRecordsCount(prefix);
            namespace.put("count", count);
            counts.add(namespace);
        }

        result.put("counts", counts);
        return ok(com.mongodb.util.JSON.serialize(result)).as("application/json");

    }

    public Result getMetadata(String projectId, String organizationId) {
        Project project = new Project(projectId);
        Organization organization = project.getOrganization(Integer.parseInt(organizationId));
        BasicDBObject result = organization.getMetadata();

        return ok(com.mongodb.util.JSON.serialize(result)).as("application/json");
    }

    public Result getReports(String project, String organizationId) {
        Report report = new Report(project);
        BasicDBObject result = new BasicDBObject();
        result.put("reports", report.getReports(organizationId));

        return ok(com.mongodb.util.JSON.serialize(result)).as("application/json");
    }

    public Result getOrgTimeLine(String proj, String org) {
        BasicDBObject q = new BasicDBObject();
        q.put("projectName", proj);
        q.put("orgId", Integer.parseInt(org));
        DBCursor cur = MongoDB.getDB().getCollection("reports").find(q);

        if (cur.count() == 0) {
            return notFound();
        }

        BasicDBObject res = new BasicDBObject();
        String headline = "Timeline of publications for project with ID " + org + " for project " + proj;
        res.put("headhline", headline);
        res.put("type", "default");
        ArrayList<BasicDBObject> events = new ArrayList<BasicDBObject>();
        DateFormat df = new SimpleDateFormat("yyyy,MM,dd");
        Calendar mydate = Calendar.getInstance();

        while (cur.hasNext()) {
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
        return ok(com.mongodb.util.JSON.serialize(tm)).as("application/json");
    }
}
