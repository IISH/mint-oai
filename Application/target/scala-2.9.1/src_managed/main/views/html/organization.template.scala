
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
	  				
	  				$("#refresh").click(function() """),format.raw("""{"""),format.raw/*25.40*/("""
		  				overall.refresh();
	  				"""),format.raw("""}"""),format.raw/*27.9*/(""")
	  			"""),format.raw("""}"""),format.raw/*28.8*/(""");
	  		"""),format.raw("""}"""),format.raw/*29.7*/(""");

      		</script>
    </head>
    <body class="gradient">
		<div class="wrapper">
			<div class="navbar">
				<div class="navbar-inner">
					<a class="brand" href="#">Mint OAI Repository: """),_display_(Seq[Any](/*37.54*/project/*37.61*/.getTitle())),format.raw/*37.72*/(""": """),_display_(Seq[Any](/*37.75*/organization/*37.87*/.getName())),format.raw/*37.97*/("""</a>
					<button id="refresh" style="float: right" class="btn">Refresh</button>
				</div>
			</div>
			<div class="container">
				<div class="row">
					<div id="sidebar" class="well sidebar span3">
						<div class="row">
							<div id="organization-info" class="span3">
								<legend>Information<a class="oai-action" target="_blank" style="float: right" title=""""),_display_(Seq[Any](/*46.95*/project/*46.102*/.getTitle())),format.raw/*46.113*/(""" OAI url for ese namespace" href='"""),_display_(Seq[Any](/*46.148*/project/*46.155*/.getOAIUrl("ese"))),format.raw/*46.172*/("""'></a></legend>
								<dl class="dl-horizontal list">
  									<dt>Name</dt>
									<dd>"""),_display_(Seq[Any](/*49.15*/organization/*49.27*/.getName())),format.raw/*49.37*/("""</dd>
  									<dt>Description</dt>
									<dd>"""),_display_(Seq[Any](/*51.15*/{val data = organization.getMetadata(Organization.METADATA_DESCRIPTION); if(data) data else "- not set -" })),format.raw/*51.122*/("""</dd>
  									<dt>Country</dt>
									<dd>"""),_display_(Seq[Any](/*53.15*/{val data = organization.getMetadata(Organization.METADATA_COUNTRY); if(data) data else "- not set -" })),format.raw/*53.118*/("""</dd>
  									<dt>Address</dt>
									<dd>"""),_display_(Seq[Any](/*55.15*/{val data = organization.getMetadata(Organization.METADATA_ADDRESS); if(data) data else "- not set -" })),format.raw/*55.118*/("""</dd>
  									<hr/>
  									<dt>Contact</dt>
									<dd>"""),_display_(Seq[Any](/*58.15*/{val data = organization.getMetadata(Organization.METADATA_CONTACT); if(data) data else "- not set -" })),format.raw/*58.118*/("""</dd>
  									<dt>Email</dt>
									<dd>"""),_display_(Seq[Any](/*60.15*/{val data = organization.getMetadata(Organization.METADATA_CONTACT_EMAIL); if(data) data else "- not set -" })),format.raw/*60.124*/("""</dd>
  									<dt>Telephone</dt>
									<dd>"""),_display_(Seq[Any](/*62.15*/{val data = organization.getMetadata(Organization.METADATA_CONTACT_PHONE); if(data) data else "- not set -" })),format.raw/*62.124*/("""</dd>
								</dl>
							</div>
						</div>
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
</html>"""))}
    }
    
    def render(project:gr.ntua.ivml.mint.oai.model.Project,organization:gr.ntua.ivml.mint.oai.model.Organization) = apply(project,organization)
    
    def f:((gr.ntua.ivml.mint.oai.model.Project,gr.ntua.ivml.mint.oai.model.Organization) => play.api.templates.Html) = (project,organization) => apply(project,organization)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Fri Jun 06 13:36:49 EEST 2014
                    SOURCE: /home/costas/git/mintoai/Application/app/views/organization.scala.html
                    HASH: c3a787218816f9e886e71cbc0c4e804d9bb0c649
                    MATRIX: 832->1|1061->103|1089->155|1220->251|1234->257|1290->292|1383->349|1398->355|1446->381|1544->443|1559->449|1613->481|1675->507|1690->513|1748->549|1842->607|1857->613|1918->652|2012->710|2027->716|2078->745|2172->803|2187->809|2233->833|2321->885|2336->891|2388->921|2636->1122|2702->1141|2792->1184|2884->1229|2975->1284|2991->1291|3021->1299|3062->1304|3083->1316|3113->1324|3229->1393|3313->1431|3369->1441|3425->1451|3663->1653|3679->1660|3712->1671|3751->1674|3772->1686|3804->1696|4219->2075|4236->2082|4270->2093|4342->2128|4359->2135|4399->2152|4533->2250|4554->2262|4586->2272|4676->2326|4806->2433|4892->2483|5018->2586|5104->2636|5230->2739|5334->2807|5460->2910|5544->2958|5676->3067|5764->3119|5896->3228
                    LINES: 27->1|31->1|32->3|38->9|38->9|38->9|39->10|39->10|39->10|40->11|40->11|40->11|41->12|41->12|41->12|42->13|42->13|42->13|43->14|43->14|43->14|44->15|44->15|44->15|45->16|45->16|45->16|48->19|48->19|50->21|51->22|52->23|52->23|52->23|52->23|52->23|52->23|54->25|56->27|57->28|58->29|66->37|66->37|66->37|66->37|66->37|66->37|75->46|75->46|75->46|75->46|75->46|75->46|78->49|78->49|78->49|80->51|80->51|82->53|82->53|84->55|84->55|87->58|87->58|89->60|89->60|91->62|91->62
                    -- GENERATED --
                */
            