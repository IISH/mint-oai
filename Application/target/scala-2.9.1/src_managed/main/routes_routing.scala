// @SOURCE:/home/costas/git/mintoai/Application/conf/routes
// @HASH:5e38ce4afab862c8cf9ddc9e2b261558a1c90ee2
// @DATE:Fri Jun 06 13:36:48 EEST 2014

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString

object Routes extends Router.Routes {


// @LINE:6
val controllers_Manager_index0 = Route("GET", PathPattern(List(StaticPart("/"))))
                    

// @LINE:9
val controllers_Assets_at1 = Route("GET", PathPattern(List(StaticPart("/assets/"),DynamicPart("file", """.+"""))))
                    

// @LINE:13
val controllers_Manager_landingPage2 = Route("GET", PathPattern(List(StaticPart("/manager"))))
                    

// @LINE:14
val controllers_Manager_landingPage3 = Route("GET", PathPattern(List(StaticPart("/manager/"))))
                    

// @LINE:15
val controllers_Projects_landingPage4 = Route("GET", PathPattern(List(StaticPart("/manager/projects/"),DynamicPart("proj", """[^/]+"""))))
                    

// @LINE:16
val controllers_Organizations_landingPage5 = Route("GET", PathPattern(List(StaticPart("/manager/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/organizations/"),DynamicPart("org", """[^/]+"""))))
                    

// @LINE:20
val controllers_Manager_getOverall6 = Route("GET", PathPattern(List(StaticPart("/manager/overall"))))
                    

// @LINE:21
val controllers_Manager_getRecordsPerProject7 = Route("GET", PathPattern(List(StaticPart("/manager/recordsPerProject"))))
                    

// @LINE:23
val controllers_Projects_getProjects8 = Route("GET", PathPattern(List(StaticPart("/manager/projects"))))
                    

// @LINE:24
val controllers_Projects_getOverall9 = Route("GET", PathPattern(List(StaticPart("/manager/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/overall"))))
                    

// @LINE:25
val controllers_Projects_getRecordsPerOrganization10 = Route("GET", PathPattern(List(StaticPart("/manager/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/recordsPerOrganization/"),DynamicPart("ns", """[^/]+"""))))
                    

// @LINE:27
val controllers_Organizations_getOrganizations11 = Route("GET", PathPattern(List(StaticPart("/manager/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/organizations"))))
                    

// @LINE:28
val controllers_Organizations_getMetadata12 = Route("GET", PathPattern(List(StaticPart("/manager/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/organizations/"),DynamicPart("org", """[^/]+"""),StaticPart("/metadata"))))
                    

// @LINE:29
val controllers_Organizations_getReports13 = Route("GET", PathPattern(List(StaticPart("/manager/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/organizations/"),DynamicPart("org", """[^/]+"""),StaticPart("/reports"))))
                    

// @LINE:30
val controllers_Organizations_getOrgTimeLine14 = Route("GET", PathPattern(List(StaticPart("/manager/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/organizations/"),DynamicPart("org", """[^/]+"""),StaticPart("/timeline"))))
                    

// @LINE:34
val controllers_OAI_show15 = Route("GET", PathPattern(List(StaticPart("/"),DynamicPart("collection", """[^/]+"""),StaticPart("/oai"))))
                    

// @LINE:38
val controllers_Administration_landingPage16 = Route("GET", PathPattern(List(StaticPart("/admin"))))
                    

// @LINE:39
val controllers_Administration_landingPage17 = Route("GET", PathPattern(List(StaticPart("/admin/"))))
                    

// @LINE:40
val controllers_Administration_projects18 = Route("GET", PathPattern(List(StaticPart("/admin/projects"))))
                    

// @LINE:41
val controllers_Administration_organizations19 = Route("GET", PathPattern(List(StaticPart("/admin/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/organizations"))))
                    

// @LINE:45
val controllers_Administration_updateDetails20 = Route("POST", PathPattern(List(StaticPart("/admin/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/update"))))
                    

// @LINE:46
val controllers_Administration_organizations21 = Route("POST", PathPattern(List(StaticPart("/admin/projects/"),DynamicPart("proj", """[^/]+"""),StaticPart("/organizations"))))
                    
def documentation = List(("""GET""","""/""","""controllers.Manager.index()"""),("""GET""","""/assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)"""),("""GET""","""/manager""","""controllers.Manager.landingPage()"""),("""GET""","""/manager/""","""controllers.Manager.landingPage()"""),("""GET""","""/manager/projects/$proj<[^/]+>""","""controllers.Projects.landingPage(proj:String)"""),("""GET""","""/manager/projects/$proj<[^/]+>/organizations/$org<[^/]+>""","""controllers.Organizations.landingPage(proj:String, org:String)"""),("""GET""","""/manager/overall""","""controllers.Manager.getOverall()"""),("""GET""","""/manager/recordsPerProject""","""controllers.Manager.getRecordsPerProject()"""),("""GET""","""/manager/projects""","""controllers.Projects.getProjects()"""),("""GET""","""/manager/projects/$proj<[^/]+>/overall""","""controllers.Projects.getOverall(proj:String)"""),("""GET""","""/manager/projects/$proj<[^/]+>/recordsPerOrganization/$ns<[^/]+>""","""controllers.Projects.getRecordsPerOrganization(proj:String, ns:String)"""),("""GET""","""/manager/projects/$proj<[^/]+>/organizations""","""controllers.Organizations.getOrganizations(proj:String)"""),("""GET""","""/manager/projects/$proj<[^/]+>/organizations/$org<[^/]+>/metadata""","""controllers.Organizations.getMetadata(proj:String, org:String)"""),("""GET""","""/manager/projects/$proj<[^/]+>/organizations/$org<[^/]+>/reports""","""controllers.Organizations.getReports(proj:String, org:String)"""),("""GET""","""/manager/projects/$proj<[^/]+>/organizations/$org<[^/]+>/timeline""","""controllers.Organizations.getOrgTimeLine(proj:String, org:String)"""),("""GET""","""/$collection<[^/]+>/oai""","""controllers.OAI.show(collection:String)"""),("""GET""","""/admin""","""controllers.Administration.landingPage()"""),("""GET""","""/admin/""","""controllers.Administration.landingPage()"""),("""GET""","""/admin/projects""","""controllers.Administration.projects()"""),("""GET""","""/admin/projects/$proj<[^/]+>/organizations""","""controllers.Administration.organizations(proj:String)"""),("""POST""","""/admin/projects/$proj<[^/]+>/update""","""controllers.Administration.updateDetails(proj:String)"""),("""POST""","""/admin/projects/$proj<[^/]+>/organizations""","""controllers.Administration.organizations(proj:String)"""))
             
    
def routes:PartialFunction[RequestHeader,Handler] = {        

// @LINE:6
case controllers_Manager_index0(params) => {
   call { 
        invokeHandler(_root_.controllers.Manager.index(), HandlerDef(this, "controllers.Manager", "index", Nil))
   }
}
                    

// @LINE:9
case controllers_Assets_at1(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(_root_.controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String])))
   }
}
                    

// @LINE:13
case controllers_Manager_landingPage2(params) => {
   call { 
        invokeHandler(_root_.controllers.Manager.landingPage(), HandlerDef(this, "controllers.Manager", "landingPage", Nil))
   }
}
                    

// @LINE:14
case controllers_Manager_landingPage3(params) => {
   call { 
        invokeHandler(_root_.controllers.Manager.landingPage(), HandlerDef(this, "controllers.Manager", "landingPage", Nil))
   }
}
                    

// @LINE:15
case controllers_Projects_landingPage4(params) => {
   call(params.fromPath[String]("proj", None)) { (proj) =>
        invokeHandler(_root_.controllers.Projects.landingPage(proj), HandlerDef(this, "controllers.Projects", "landingPage", Seq(classOf[String])))
   }
}
                    

// @LINE:16
case controllers_Organizations_landingPage5(params) => {
   call(params.fromPath[String]("proj", None), params.fromPath[String]("org", None)) { (proj, org) =>
        invokeHandler(_root_.controllers.Organizations.landingPage(proj, org), HandlerDef(this, "controllers.Organizations", "landingPage", Seq(classOf[String], classOf[String])))
   }
}
                    

// @LINE:20
case controllers_Manager_getOverall6(params) => {
   call { 
        invokeHandler(_root_.controllers.Manager.getOverall(), HandlerDef(this, "controllers.Manager", "getOverall", Nil))
   }
}
                    

// @LINE:21
case controllers_Manager_getRecordsPerProject7(params) => {
   call { 
        invokeHandler(_root_.controllers.Manager.getRecordsPerProject(), HandlerDef(this, "controllers.Manager", "getRecordsPerProject", Nil))
   }
}
                    

// @LINE:23
case controllers_Projects_getProjects8(params) => {
   call { 
        invokeHandler(_root_.controllers.Projects.getProjects(), HandlerDef(this, "controllers.Projects", "getProjects", Nil))
   }
}
                    

// @LINE:24
case controllers_Projects_getOverall9(params) => {
   call(params.fromPath[String]("proj", None)) { (proj) =>
        invokeHandler(_root_.controllers.Projects.getOverall(proj), HandlerDef(this, "controllers.Projects", "getOverall", Seq(classOf[String])))
   }
}
                    

// @LINE:25
case controllers_Projects_getRecordsPerOrganization10(params) => {
   call(params.fromPath[String]("proj", None), params.fromPath[String]("ns", None)) { (proj, ns) =>
        invokeHandler(_root_.controllers.Projects.getRecordsPerOrganization(proj, ns), HandlerDef(this, "controllers.Projects", "getRecordsPerOrganization", Seq(classOf[String], classOf[String])))
   }
}
                    

// @LINE:27
case controllers_Organizations_getOrganizations11(params) => {
   call(params.fromPath[String]("proj", None)) { (proj) =>
        invokeHandler(_root_.controllers.Organizations.getOrganizations(proj), HandlerDef(this, "controllers.Organizations", "getOrganizations", Seq(classOf[String])))
   }
}
                    

// @LINE:28
case controllers_Organizations_getMetadata12(params) => {
   call(params.fromPath[String]("proj", None), params.fromPath[String]("org", None)) { (proj, org) =>
        invokeHandler(_root_.controllers.Organizations.getMetadata(proj, org), HandlerDef(this, "controllers.Organizations", "getMetadata", Seq(classOf[String], classOf[String])))
   }
}
                    

// @LINE:29
case controllers_Organizations_getReports13(params) => {
   call(params.fromPath[String]("proj", None), params.fromPath[String]("org", None)) { (proj, org) =>
        invokeHandler(_root_.controllers.Organizations.getReports(proj, org), HandlerDef(this, "controllers.Organizations", "getReports", Seq(classOf[String], classOf[String])))
   }
}
                    

// @LINE:30
case controllers_Organizations_getOrgTimeLine14(params) => {
   call(params.fromPath[String]("proj", None), params.fromPath[String]("org", None)) { (proj, org) =>
        invokeHandler(_root_.controllers.Organizations.getOrgTimeLine(proj, org), HandlerDef(this, "controllers.Organizations", "getOrgTimeLine", Seq(classOf[String], classOf[String])))
   }
}
                    

// @LINE:34
case controllers_OAI_show15(params) => {
   call(params.fromPath[String]("collection", None)) { (collection) =>
        invokeHandler(_root_.controllers.OAI.show(collection), HandlerDef(this, "controllers.OAI", "show", Seq(classOf[String])))
   }
}
                    

// @LINE:38
case controllers_Administration_landingPage16(params) => {
   call { 
        invokeHandler(_root_.controllers.Administration.landingPage(), HandlerDef(this, "controllers.Administration", "landingPage", Nil))
   }
}
                    

// @LINE:39
case controllers_Administration_landingPage17(params) => {
   call { 
        invokeHandler(_root_.controllers.Administration.landingPage(), HandlerDef(this, "controllers.Administration", "landingPage", Nil))
   }
}
                    

// @LINE:40
case controllers_Administration_projects18(params) => {
   call { 
        invokeHandler(_root_.controllers.Administration.projects(), HandlerDef(this, "controllers.Administration", "projects", Nil))
   }
}
                    

// @LINE:41
case controllers_Administration_organizations19(params) => {
   call(params.fromPath[String]("proj", None)) { (proj) =>
        invokeHandler(_root_.controllers.Administration.organizations(proj), HandlerDef(this, "controllers.Administration", "organizations", Seq(classOf[String])))
   }
}
                    

// @LINE:45
case controllers_Administration_updateDetails20(params) => {
   call(params.fromPath[String]("proj", None)) { (proj) =>
        invokeHandler(_root_.controllers.Administration.updateDetails(proj), HandlerDef(this, "controllers.Administration", "updateDetails", Seq(classOf[String])))
   }
}
                    

// @LINE:46
case controllers_Administration_organizations21(params) => {
   call(params.fromPath[String]("proj", None)) { (proj) =>
        invokeHandler(_root_.controllers.Administration.organizations(proj), HandlerDef(this, "controllers.Administration", "organizations", Seq(classOf[String])))
   }
}
                    
}
    
}
                