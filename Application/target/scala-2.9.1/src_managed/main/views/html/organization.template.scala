
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.api.i18n._
import play.api.templates.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import com.avaje.ebean._
import play.mvc.Http.Context.Implicit._
import views.html._
/**/
object organization extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template2[gr.ntua.ivml.mint.oai.model.Project,gr.ntua.ivml.mint.oai.model.Organization,play.api.templates.Html] {

    /**/
    def apply/*1.2*/(project: gr.ntua.ivml.mint.oai.model.Project, organization: gr.ntua.ivml.mint.oai.model.Organization):play.api.templates.Html = {
        _display_ {import gr.ntua.ivml.mint.oai.model.Organization


Seq[Any](format.raw/*1.104*/("""
"""),format.raw/*3.1*/("""
<!DOCTYPE html>

<html>

    <head>
        <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*9.54*/routes/*9.60*/.Assets.at("css/bootstrap.min.css"))),format.raw/*9.95*/("""">
        <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*10.54*/routes/*10.60*/.Assets.at("css/main.css"))),format.raw/*10.86*/("""">
        <link rel="shortcut icon" type="image/png" href=""""),_display_(Seq[Any](/*11.59*/routes/*11.65*/.Assets.at("images/favicon.png"))),format.raw/*11.97*/("""">
        <script src=""""),_display_(Seq[Any](/*12.23*/routes/*12.29*/.Assets.at("js/jquery-1.7.1.min.js"))),format.raw/*12.65*/("""" type="text/javascript"></script>
        <script src=""""),_display_(Seq[Any](/*13.23*/routes/*13.29*/.Assets.at("js/jquery.sortElements.js"))),format.raw/*13.68*/("""" type="text/javascript"></script>
        <script src=""""),_display_(Seq[Any](/*14.23*/routes/*14.29*/.Assets.at("js/bootstrap.js"))),format.raw/*14.58*/("""" type="text/javascript"></script>
        <script src=""""),_display_(Seq[Any](/*15.23*/routes/*15.29*/.Assets.at("js/date.js"))),format.raw/*15.53*/("""" type="text/javascript"></script>
		<script src=""""),_display_(Seq[Any](/*16.17*/routes/*16.23*/.Assets.at("js/oaimanager.js"))),format.raw/*16.53*/("""" type="text/javascript"></script>
        <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    	<script type="text/javascript">
      		google.load('visualization', '1', """),format.raw("""{"""),format.raw/*19.44*/("""packages:['table']"""),format.raw("""}"""),format.raw/*19.63*/(""");
	
	  		$(document).ready(function () """),format.raw("""{"""),format.raw/*21.37*/("""
	  			google.setOnLoadCallback(function() """),format.raw("""{"""),format.raw/*22.44*/("""
	  				overall = new MintOAIOrganizationStatistics(""""),_display_(Seq[Any](/*23.54*/project/*23.61*/.getId())),format.raw/*23.69*/("""", """"),_display_(Seq[Any](/*23.74*/organization/*23.86*/.getId())),format.raw/*23.94*/("""", "organization");
	  				
					simple_overall = new MintOAIOrganizationTest(""""),_display_(Seq[Any](/*25.53*/project/*25.60*/.getId())),format.raw/*25.68*/("""", """"),_display_(Seq[Any](/*25.73*/organization/*25.85*/.getId())),format.raw/*25.93*/("""", "organization-testing");
	  				$("#refresh").click(function() """),format.raw("""{"""),format.raw/*26.40*/("""
		  				overall.refresh();
						simple_overall.refresh();
	  				"""),format.raw("""}"""),format.raw/*29.9*/(""")
					
					
				
	  			"""),format.raw("""}"""),format.raw/*33.8*/(""");
	  		"""),format.raw("""}"""),format.raw/*34.7*/(""");

      		</script>
    </head>
    <body class="gradient">
		<div class="wrapper">
			<div class="navbar">
				<div class="navbar-inner">
					<a class="brand" href="#">Mint OAI Repository: """),_display_(Seq[Any](/*42.54*/project/*42.61*/.getTitle())),format.raw/*42.72*/(""": """),_display_(Seq[Any](/*42.75*/organization/*42.87*/.getName())),format.raw/*42.97*/("""</a>
					<button id="refresh" style="float: right" class="btn">Refresh</button>
				</div>
			</div>
			<div class="container">
				<div class="row">
					<div id="sidebar" class="well sidebar span3">
						<div class="row">
							<div id="organization-info" class="span3">
								<legend>Information<a class="oai-action" target="_blank" style="float: right" title=""""),_display_(Seq[Any](/*51.95*/project/*51.102*/.getTitle())),format.raw/*51.113*/(""" OAI url for rdf namespace" href='"""),_display_(Seq[Any](/*51.148*/project/*51.155*/.getOAIUrl("rdf"))),format.raw/*51.172*/("""'></a></legend>
								<dl class="dl-horizontal list">
  									<dt>Name</dt>
									<dd>"""),_display_(Seq[Any](/*54.15*/organization/*54.27*/.getName())),format.raw/*54.37*/("""</dd>
  									<dt>Description</dt>
									<dd>"""),_display_(Seq[Any](/*56.15*/{val data = organization.getMetadata(Organization.METADATA_DESCRIPTION); if(data) data else "- not set -" })),format.raw/*56.122*/("""</dd>
  									<dt>Country</dt>
									<dd>"""),_display_(Seq[Any](/*58.15*/{val data = organization.getMetadata(Organization.METADATA_COUNTRY); if(data) data else "- not set -" })),format.raw/*58.118*/("""</dd>
  									<dt>Address</dt>
									<dd>"""),_display_(Seq[Any](/*60.15*/{val data = organization.getMetadata(Organization.METADATA_ADDRESS); if(data) data else "- not set -" })),format.raw/*60.118*/("""</dd>
  									<hr/>
  									<dt>Contact</dt>
									<dd>"""),_display_(Seq[Any](/*63.15*/{val data = organization.getMetadata(Organization.METADATA_CONTACT); if(data) data else "- not set -" })),format.raw/*63.118*/("""</dd>
  									<dt>Email</dt>
									<dd>"""),_display_(Seq[Any](/*65.15*/{val data = organization.getMetadata(Organization.METADATA_CONTACT_EMAIL); if(data) data else "- not set -" })),format.raw/*65.124*/("""</dd>
  									<dt>Telephone</dt>
									<dd>"""),_display_(Seq[Any](/*67.15*/{val data = organization.getMetadata(Organization.METADATA_CONTACT_PHONE); if(data) data else "- not set -" })),format.raw/*67.124*/("""</dd>
								</dl>
							</div>
						</div>

					</div>

					<div id="organization-testing" class="well span8">
					</div>
					<div id="organization" class="well span8"></div>

				</div>

	    	</div>
    	</div>
    </body>
    <div class="push"><!--//--></div>
	<footer>
	     <div class="container">
	     	<div class="row">
        		<p class="span2">Powered By <a href="http://mint.image.ece.ntua.gr/" rel="author">Mint</a>.</p>
        	</div>
      </div>
	</footer>
</html>
"""))}
    }
    
    def render(project:gr.ntua.ivml.mint.oai.model.Project,organization:gr.ntua.ivml.mint.oai.model.Organization) = apply(project,organization)
    
    def f:((gr.ntua.ivml.mint.oai.model.Project,gr.ntua.ivml.mint.oai.model.Organization) => play.api.templates.Html) = (project,organization) => apply(project,organization)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Fri Mar 11 21:52:34 EET 2016
                    SOURCE: /home/costas/git/mintoai/Application/app/views/organization.scala.html
                    HASH: 2207251da8ecc44b907dc84f74c068b186c0ace6
                    MATRIX: 832->1|1061->103|1089->155|1220->251|1234->257|1290->292|1383->349|1398->355|1446->381|1544->443|1559->449|1613->481|1675->507|1690->513|1748->549|1842->607|1857->613|1918->652|2012->710|2027->716|2078->745|2172->803|2187->809|2233->833|2321->885|2336->891|2388->921|2636->1122|2702->1141|2792->1184|2884->1229|2975->1284|2991->1291|3021->1299|3062->1304|3083->1316|3113->1324|3231->1406|3247->1413|3277->1421|3318->1426|3339->1438|3369->1446|3484->1514|3601->1585|3677->1615|3733->1625|3971->1827|3987->1834|4020->1845|4059->1848|4080->1860|4112->1870|4527->2249|4544->2256|4578->2267|4650->2302|4667->2309|4707->2326|4841->2424|4862->2436|4894->2446|4984->2500|5114->2607|5200->2657|5326->2760|5412->2810|5538->2913|5642->2981|5768->3084|5852->3132|5984->3241|6072->3293|6204->3402
                    LINES: 27->1|31->1|32->3|38->9|38->9|38->9|39->10|39->10|39->10|40->11|40->11|40->11|41->12|41->12|41->12|42->13|42->13|42->13|43->14|43->14|43->14|44->15|44->15|44->15|45->16|45->16|45->16|48->19|48->19|50->21|51->22|52->23|52->23|52->23|52->23|52->23|52->23|54->25|54->25|54->25|54->25|54->25|54->25|55->26|58->29|62->33|63->34|71->42|71->42|71->42|71->42|71->42|71->42|80->51|80->51|80->51|80->51|80->51|80->51|83->54|83->54|83->54|85->56|85->56|87->58|87->58|89->60|89->60|92->63|92->63|94->65|94->65|96->67|96->67
                    -- GENERATED --
                */
            