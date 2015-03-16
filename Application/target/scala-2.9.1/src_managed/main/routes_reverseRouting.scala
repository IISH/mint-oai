// @SOURCE:/home/costas/toplay/mintoai/Application/conf/routes
// @HASH:5a31fed53dee2869a6cd49374ba46017df3dee76
// @DATE:Mon Mar 16 13:12:28 EET 2015

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString


// @LINE:47
// @LINE:46
// @LINE:42
// @LINE:41
// @LINE:40
// @LINE:39
// @LINE:35
// @LINE:32
// @LINE:30
// @LINE:29
// @LINE:28
// @LINE:27
// @LINE:25
// @LINE:24
// @LINE:23
// @LINE:21
// @LINE:20
// @LINE:16
// @LINE:15
// @LINE:14
// @LINE:13
// @LINE:9
// @LINE:6
package controllers {

// @LINE:9
class ReverseAssets {
    


 
// @LINE:9
def at(file:String) = {
   Call("GET", "/assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                        

                      
    
}
                            

// @LINE:47
// @LINE:46
// @LINE:42
// @LINE:41
// @LINE:40
// @LINE:39
class ReverseAdministration {
    


 
// @LINE:47
// @LINE:42
def organizations(proj:String) = {
   (proj) match {
// @LINE:42
case (proj) if true => Call("GET", "/admin/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/organizations")
                                                                
// @LINE:47
case (proj) if true => Call("POST", "/admin/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/organizations")
                                                                    
   }
}
                                                        
 
// @LINE:40
// @LINE:39
def landingPage() = {
   () match {
// @LINE:39
case () if true => Call("GET", "/admin")
                                                                
// @LINE:40
case () if true => Call("GET", "/admin/")
                                                                    
   }
}
                                                        
 
// @LINE:41
def projects() = {
   Call("GET", "/admin/projects")
}
                                                        
 
// @LINE:46
def updateDetails(proj:String) = {
   Call("POST", "/admin/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/update")
}
                                                        

                      
    
}
                            

// @LINE:32
// @LINE:30
// @LINE:29
// @LINE:28
// @LINE:27
// @LINE:16
class ReverseOrganizations {
    


 
// @LINE:28
def getMetadata(proj:String, org:String) = {
   Call("GET", "/manager/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/organizations/" + implicitly[PathBindable[String]].unbind("org", org) + "/metadata")
}
                                                        
 
// @LINE:29
def getReports(proj:String, org:String) = {
   Call("GET", "/manager/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/organizations/" + implicitly[PathBindable[String]].unbind("org", org) + "/reports")
}
                                                        
 
// @LINE:27
def getOrganizations(proj:String) = {
   Call("GET", "/manager/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/organizations")
}
                                                        
 
// @LINE:16
def landingPage(proj:String, org:String) = {
   Call("GET", "/manager/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/organizations/" + implicitly[PathBindable[String]].unbind("org", org))
}
                                                        
 
// @LINE:32
def getOrganizationCounts(proj:String, org:String) = {
   Call("GET", "/manager/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/organizations/" + implicitly[PathBindable[String]].unbind("org", org) + "/orgcounts")
}
                                                        
 
// @LINE:30
def getOrgTimeLine(proj:String, org:String) = {
   Call("GET", "/manager/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/organizations/" + implicitly[PathBindable[String]].unbind("org", org) + "/timeline")
}
                                                        

                      
    
}
                            

// @LINE:21
// @LINE:20
// @LINE:14
// @LINE:13
// @LINE:6
class ReverseManager {
    


 
// @LINE:21
def getRecordsPerProject() = {
   Call("GET", "/manager/recordsPerProject")
}
                                                        
 
// @LINE:20
def getOverall() = {
   Call("GET", "/manager/overall")
}
                                                        
 
// @LINE:6
def index() = {
   Call("GET", "/")
}
                                                        
 
// @LINE:14
// @LINE:13
def landingPage() = {
   () match {
// @LINE:13
case () if true => Call("GET", "/manager")
                                                                
// @LINE:14
case () if true => Call("GET", "/manager/")
                                                                    
   }
}
                                                        

                      
    
}
                            

// @LINE:35
class ReverseOAI {
    


 
// @LINE:35
def show(collection:String) = {
   Call("GET", "/" + implicitly[PathBindable[String]].unbind("collection", collection) + "/oai")
}
                                                        

                      
    
}
                            

// @LINE:25
// @LINE:24
// @LINE:23
// @LINE:15
class ReverseProjects {
    


 
// @LINE:23
def getProjects() = {
   Call("GET", "/manager/projects")
}
                                                        
 
// @LINE:25
def getRecordsPerOrganization(proj:String, ns:String) = {
   Call("GET", "/manager/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/recordsPerOrganization/" + implicitly[PathBindable[String]].unbind("ns", ns))
}
                                                        
 
// @LINE:24
def getOverall(proj:String) = {
   Call("GET", "/manager/projects/" + implicitly[PathBindable[String]].unbind("proj", proj) + "/overall")
}
                                                        
 
// @LINE:15
def landingPage(proj:String) = {
   Call("GET", "/manager/projects/" + implicitly[PathBindable[String]].unbind("proj", proj))
}
                                                        

                      
    
}
                            
}
                    


// @LINE:47
// @LINE:46
// @LINE:42
// @LINE:41
// @LINE:40
// @LINE:39
// @LINE:35
// @LINE:32
// @LINE:30
// @LINE:29
// @LINE:28
// @LINE:27
// @LINE:25
// @LINE:24
// @LINE:23
// @LINE:21
// @LINE:20
// @LINE:16
// @LINE:15
// @LINE:14
// @LINE:13
// @LINE:9
// @LINE:6
package controllers.javascript {

// @LINE:9
class ReverseAssets {
    


 
// @LINE:9
def at = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"/assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:47
// @LINE:46
// @LINE:42
// @LINE:41
// @LINE:40
// @LINE:39
class ReverseAdministration {
    


 
// @LINE:47
// @LINE:42
def organizations = JavascriptReverseRoute(
   "controllers.Administration.organizations",
   """
      function(proj) {
      if (true) {
      return _wA({method:"GET", url:"/admin/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/organizations"})
      }
      if (true) {
      return _wA({method:"POST", url:"/admin/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/organizations"})
      }
      }
   """
)
                                                        
 
// @LINE:40
// @LINE:39
def landingPage = JavascriptReverseRoute(
   "controllers.Administration.landingPage",
   """
      function() {
      if (true) {
      return _wA({method:"GET", url:"/admin"})
      }
      if (true) {
      return _wA({method:"GET", url:"/admin/"})
      }
      }
   """
)
                                                        
 
// @LINE:41
def projects = JavascriptReverseRoute(
   "controllers.Administration.projects",
   """
      function() {
      return _wA({method:"GET", url:"/admin/projects"})
      }
   """
)
                                                        
 
// @LINE:46
def updateDetails = JavascriptReverseRoute(
   "controllers.Administration.updateDetails",
   """
      function(proj) {
      return _wA({method:"POST", url:"/admin/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/update"})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:32
// @LINE:30
// @LINE:29
// @LINE:28
// @LINE:27
// @LINE:16
class ReverseOrganizations {
    


 
// @LINE:28
def getMetadata = JavascriptReverseRoute(
   "controllers.Organizations.getMetadata",
   """
      function(proj,org) {
      return _wA({method:"GET", url:"/manager/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/organizations/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("org", org) + "/metadata"})
      }
   """
)
                                                        
 
// @LINE:29
def getReports = JavascriptReverseRoute(
   "controllers.Organizations.getReports",
   """
      function(proj,org) {
      return _wA({method:"GET", url:"/manager/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/organizations/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("org", org) + "/reports"})
      }
   """
)
                                                        
 
// @LINE:27
def getOrganizations = JavascriptReverseRoute(
   "controllers.Organizations.getOrganizations",
   """
      function(proj) {
      return _wA({method:"GET", url:"/manager/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/organizations"})
      }
   """
)
                                                        
 
// @LINE:16
def landingPage = JavascriptReverseRoute(
   "controllers.Organizations.landingPage",
   """
      function(proj,org) {
      return _wA({method:"GET", url:"/manager/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/organizations/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("org", org)})
      }
   """
)
                                                        
 
// @LINE:32
def getOrganizationCounts = JavascriptReverseRoute(
   "controllers.Organizations.getOrganizationCounts",
   """
      function(proj,org) {
      return _wA({method:"GET", url:"/manager/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/organizations/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("org", org) + "/orgcounts"})
      }
   """
)
                                                        
 
// @LINE:30
def getOrgTimeLine = JavascriptReverseRoute(
   "controllers.Organizations.getOrgTimeLine",
   """
      function(proj,org) {
      return _wA({method:"GET", url:"/manager/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/organizations/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("org", org) + "/timeline"})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:21
// @LINE:20
// @LINE:14
// @LINE:13
// @LINE:6
class ReverseManager {
    


 
// @LINE:21
def getRecordsPerProject = JavascriptReverseRoute(
   "controllers.Manager.getRecordsPerProject",
   """
      function() {
      return _wA({method:"GET", url:"/manager/recordsPerProject"})
      }
   """
)
                                                        
 
// @LINE:20
def getOverall = JavascriptReverseRoute(
   "controllers.Manager.getOverall",
   """
      function() {
      return _wA({method:"GET", url:"/manager/overall"})
      }
   """
)
                                                        
 
// @LINE:6
def index = JavascriptReverseRoute(
   "controllers.Manager.index",
   """
      function() {
      return _wA({method:"GET", url:"/"})
      }
   """
)
                                                        
 
// @LINE:14
// @LINE:13
def landingPage = JavascriptReverseRoute(
   "controllers.Manager.landingPage",
   """
      function() {
      if (true) {
      return _wA({method:"GET", url:"/manager"})
      }
      if (true) {
      return _wA({method:"GET", url:"/manager/"})
      }
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:35
class ReverseOAI {
    


 
// @LINE:35
def show = JavascriptReverseRoute(
   "controllers.OAI.show",
   """
      function(collection) {
      return _wA({method:"GET", url:"/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("collection", collection) + "/oai"})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:25
// @LINE:24
// @LINE:23
// @LINE:15
class ReverseProjects {
    


 
// @LINE:23
def getProjects = JavascriptReverseRoute(
   "controllers.Projects.getProjects",
   """
      function() {
      return _wA({method:"GET", url:"/manager/projects"})
      }
   """
)
                                                        
 
// @LINE:25
def getRecordsPerOrganization = JavascriptReverseRoute(
   "controllers.Projects.getRecordsPerOrganization",
   """
      function(proj,ns) {
      return _wA({method:"GET", url:"/manager/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/recordsPerOrganization/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("ns", ns)})
      }
   """
)
                                                        
 
// @LINE:24
def getOverall = JavascriptReverseRoute(
   "controllers.Projects.getOverall",
   """
      function(proj) {
      return _wA({method:"GET", url:"/manager/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj) + "/overall"})
      }
   """
)
                                                        
 
// @LINE:15
def landingPage = JavascriptReverseRoute(
   "controllers.Projects.landingPage",
   """
      function(proj) {
      return _wA({method:"GET", url:"/manager/projects/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("proj", proj)})
      }
   """
)
                                                        

                      
    
}
                            
}
                    


// @LINE:47
// @LINE:46
// @LINE:42
// @LINE:41
// @LINE:40
// @LINE:39
// @LINE:35
// @LINE:32
// @LINE:30
// @LINE:29
// @LINE:28
// @LINE:27
// @LINE:25
// @LINE:24
// @LINE:23
// @LINE:21
// @LINE:20
// @LINE:16
// @LINE:15
// @LINE:14
// @LINE:13
// @LINE:9
// @LINE:6
package controllers.ref {

// @LINE:9
class ReverseAssets {
    


 
// @LINE:9
def at(path:String, file:String) = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]))
)
                              

                      
    
}
                            

// @LINE:47
// @LINE:46
// @LINE:42
// @LINE:41
// @LINE:40
// @LINE:39
class ReverseAdministration {
    


 
// @LINE:42
def organizations(proj:String) = new play.api.mvc.HandlerRef(
   controllers.Administration.organizations(proj), HandlerDef(this, "controllers.Administration", "organizations", Seq(classOf[String]))
)
                              
 
// @LINE:39
def landingPage() = new play.api.mvc.HandlerRef(
   controllers.Administration.landingPage(), HandlerDef(this, "controllers.Administration", "landingPage", Seq())
)
                              
 
// @LINE:41
def projects() = new play.api.mvc.HandlerRef(
   controllers.Administration.projects(), HandlerDef(this, "controllers.Administration", "projects", Seq())
)
                              
 
// @LINE:46
def updateDetails(proj:String) = new play.api.mvc.HandlerRef(
   controllers.Administration.updateDetails(proj), HandlerDef(this, "controllers.Administration", "updateDetails", Seq(classOf[String]))
)
                              

                      
    
}
                            

// @LINE:32
// @LINE:30
// @LINE:29
// @LINE:28
// @LINE:27
// @LINE:16
class ReverseOrganizations {
    


 
// @LINE:28
def getMetadata(proj:String, org:String) = new play.api.mvc.HandlerRef(
   controllers.Organizations.getMetadata(proj, org), HandlerDef(this, "controllers.Organizations", "getMetadata", Seq(classOf[String], classOf[String]))
)
                              
 
// @LINE:29
def getReports(proj:String, org:String) = new play.api.mvc.HandlerRef(
   controllers.Organizations.getReports(proj, org), HandlerDef(this, "controllers.Organizations", "getReports", Seq(classOf[String], classOf[String]))
)
                              
 
// @LINE:27
def getOrganizations(proj:String) = new play.api.mvc.HandlerRef(
   controllers.Organizations.getOrganizations(proj), HandlerDef(this, "controllers.Organizations", "getOrganizations", Seq(classOf[String]))
)
                              
 
// @LINE:16
def landingPage(proj:String, org:String) = new play.api.mvc.HandlerRef(
   controllers.Organizations.landingPage(proj, org), HandlerDef(this, "controllers.Organizations", "landingPage", Seq(classOf[String], classOf[String]))
)
                              
 
// @LINE:32
def getOrganizationCounts(proj:String, org:String) = new play.api.mvc.HandlerRef(
   controllers.Organizations.getOrganizationCounts(proj, org), HandlerDef(this, "controllers.Organizations", "getOrganizationCounts", Seq(classOf[String], classOf[String]))
)
                              
 
// @LINE:30
def getOrgTimeLine(proj:String, org:String) = new play.api.mvc.HandlerRef(
   controllers.Organizations.getOrgTimeLine(proj, org), HandlerDef(this, "controllers.Organizations", "getOrgTimeLine", Seq(classOf[String], classOf[String]))
)
                              

                      
    
}
                            

// @LINE:21
// @LINE:20
// @LINE:14
// @LINE:13
// @LINE:6
class ReverseManager {
    


 
// @LINE:21
def getRecordsPerProject() = new play.api.mvc.HandlerRef(
   controllers.Manager.getRecordsPerProject(), HandlerDef(this, "controllers.Manager", "getRecordsPerProject", Seq())
)
                              
 
// @LINE:20
def getOverall() = new play.api.mvc.HandlerRef(
   controllers.Manager.getOverall(), HandlerDef(this, "controllers.Manager", "getOverall", Seq())
)
                              
 
// @LINE:6
def index() = new play.api.mvc.HandlerRef(
   controllers.Manager.index(), HandlerDef(this, "controllers.Manager", "index", Seq())
)
                              
 
// @LINE:13
def landingPage() = new play.api.mvc.HandlerRef(
   controllers.Manager.landingPage(), HandlerDef(this, "controllers.Manager", "landingPage", Seq())
)
                              

                      
    
}
                            

// @LINE:35
class ReverseOAI {
    


 
// @LINE:35
def show(collection:String) = new play.api.mvc.HandlerRef(
   controllers.OAI.show(collection), HandlerDef(this, "controllers.OAI", "show", Seq(classOf[String]))
)
                              

                      
    
}
                            

// @LINE:25
// @LINE:24
// @LINE:23
// @LINE:15
class ReverseProjects {
    


 
// @LINE:23
def getProjects() = new play.api.mvc.HandlerRef(
   controllers.Projects.getProjects(), HandlerDef(this, "controllers.Projects", "getProjects", Seq())
)
                              
 
// @LINE:25
def getRecordsPerOrganization(proj:String, ns:String) = new play.api.mvc.HandlerRef(
   controllers.Projects.getRecordsPerOrganization(proj, ns), HandlerDef(this, "controllers.Projects", "getRecordsPerOrganization", Seq(classOf[String], classOf[String]))
)
                              
 
// @LINE:24
def getOverall(proj:String) = new play.api.mvc.HandlerRef(
   controllers.Projects.getOverall(proj), HandlerDef(this, "controllers.Projects", "getOverall", Seq(classOf[String]))
)
                              
 
// @LINE:15
def landingPage(proj:String) = new play.api.mvc.HandlerRef(
   controllers.Projects.landingPage(proj), HandlerDef(this, "controllers.Projects", "landingPage", Seq(classOf[String]))
)
                              

                      
    
}
                            
}
                    
                