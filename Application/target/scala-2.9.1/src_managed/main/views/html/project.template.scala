
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
object project extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template1[gr.ntua.ivml.mint.oai.model.Project,play.api.templates.Html] {

    /**/
    def apply/*1.2*/(project: gr.ntua.ivml.mint.oai.model.Project):play.api.templates.Html = {
        _display_ {

Seq[Any](format.raw/*1.48*/("""

<!DOCTYPE html>

<html>

    <head>
        <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*8.54*/routes/*8.60*/.Assets.at("css/bootstrap.min.css"))),format.raw/*8.95*/("""">
        <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*9.54*/routes/*9.60*/.Assets.at("css/main.css"))),format.raw/*9.86*/("""">
        <link rel="shortcut icon" type="image/png" href=""""),_display_(Seq[Any](/*10.59*/routes/*10.65*/.Assets.at("images/favicon.png"))),format.raw/*10.97*/("""">
        <script src=""""),_display_(Seq[Any](/*11.23*/routes/*11.29*/.Assets.at("js/jquery-1.7.1.min.js"))),format.raw/*11.65*/("""" type="text/javascript"></script>
        <script src=""""),_display_(Seq[Any](/*12.23*/routes/*12.29*/.Assets.at("js/jquery.sortElements.js"))),format.raw/*12.68*/("""" type="text/javascript"></script>
        <script src=""""),_display_(Seq[Any](/*13.23*/routes/*13.29*/.Assets.at("js/bootstrap.js"))),format.raw/*13.58*/("""" type="text/javascript"></script>
        <script src=""""),_display_(Seq[Any](/*14.23*/routes/*14.29*/.Assets.at("js/date.js"))),format.raw/*14.53*/("""" type="text/javascript"></script>
		<script src=""""),_display_(Seq[Any](/*15.17*/routes/*15.23*/.Assets.at("js/oaimanager.js"))),format.raw/*15.53*/("""" type="text/javascript"></script>
        <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    	<script type="text/javascript">
	  		google.load('visualization', '1', """),format.raw("""{"""),format.raw/*18.41*/("""packages: ['corechart']"""),format.raw("""}"""),format.raw/*18.65*/(""");
      		google.load('visualization', '1', """),format.raw("""{"""),format.raw/*19.44*/("""packages:['table']"""),format.raw("""}"""),format.raw/*19.63*/(""");
	
	  		$(document).ready(function () """),format.raw("""{"""),format.raw/*21.37*/("""
	  			google.setOnLoadCallback(function() """),format.raw("""{"""),format.raw/*22.44*/("""
	  				overall = new MintOAIProjectStatistics("""),format.raw("""{"""),format.raw/*23.48*/(""" projectName: """"),_display_(Seq[Any](/*23.64*/project/*23.71*/.getId())),format.raw/*23.79*/("""" """),format.raw("""}"""),format.raw/*23.82*/(""", "overall");
	  				organizations = new MintOAIOrganizations("""),format.raw("""{"""),format.raw/*24.50*/(""" projectName: """"),_display_(Seq[Any](/*24.66*/project/*24.73*/.getId())),format.raw/*24.81*/("""" """),format.raw("""}"""),format.raw/*24.84*/(""", "organizations");
	  				
	  				$("#refresh").click(function() """),format.raw("""{"""),format.raw/*26.40*/("""
	  					organizations.refresh();
		  				overall = new MintOAIProjectStatistics("""),format.raw("""{"""),format.raw/*28.49*/(""" projectName: """"),_display_(Seq[Any](/*28.65*/project/*28.72*/.getId())),format.raw/*28.80*/("""" """),format.raw("""}"""),format.raw/*28.83*/(""", "overall");
	  				"""),format.raw("""}"""),format.raw/*29.9*/(""")
	  			"""),format.raw("""}"""),format.raw/*30.8*/(""");
	  		"""),format.raw("""}"""),format.raw/*31.7*/(""");

      		</script>
    </head>
    <body class="gradient">
		<div class="wrapper">
			<div class="navbar">
				<div class="navbar-inner">
					<a class="brand" href="#">Mint OAI Repository: """),_display_(Seq[Any](/*39.54*/project/*39.61*/.getTitle())),format.raw/*39.72*/("""</a>
					<button id="refresh" style="float: right" class="btn">Refresh</button>
				</div>
			</div>
			<div class="container">
				<div class="row">
					<div id="sidebar" class="well sidebar span3">
						<div class="row">
							<div id="project-info" class="span3">
								<legend>Information<a class="oai-action" target="_blank" style="float: right" title=""""),_display_(Seq[Any](/*48.95*/project/*48.102*/.getTitle())),format.raw/*48.113*/(""" OAI url for ese namespace" href='"""),_display_(Seq[Any](/*48.148*/project/*48.155*/.getOAIUrl("ese"))),format.raw/*48.172*/("""'></a></legend>
								<dl class="dl-horizontal list">
  									<dt>Title</dt>
									<dd>"""),_display_(Seq[Any](/*51.15*/project/*51.22*/.getTitle())),format.raw/*51.33*/("""</dd>
  									<dt>Description</dt>
									<dd>"""),_display_(Seq[Any](/*53.15*/project/*53.22*/.getDescription())),format.raw/*53.39*/("""</dd>
									<dt>Mint URL</dd>
									<dd><a target="_blank" href=""""),_display_(Seq[Any](/*55.40*/project/*55.47*/.getMintURL())),format.raw/*55.60*/("""">"""),_display_(Seq[Any](/*55.63*/project/*55.70*/.getId())),format.raw/*55.78*/("""</a></dd>
								</dl>
							</div>
						</div>
						<div class="row">
							<div id="organizations" class="span3"></div>
						</div>
					</div>
					<div id="overall" class="well span8"></div>
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
    
    def render(project:gr.ntua.ivml.mint.oai.model.Project) = apply(project)
    
    def f:((gr.ntua.ivml.mint.oai.model.Project) => play.api.templates.Html) = (project) => apply(project)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Fri Jun 06 13:36:49 EEST 2014
                    SOURCE: /home/costas/git/mintoai/Application/app/views/project.scala.html
                    HASH: 504e9fa9da043e05112d7bde5608708929334a71
                    MATRIX: 786->1|909->47|1042->145|1056->151|1112->186|1204->243|1218->249|1265->275|1363->337|1378->343|1432->375|1494->401|1509->407|1567->443|1661->501|1676->507|1737->546|1831->604|1846->610|1897->639|1991->697|2006->703|2052->727|2140->779|2155->785|2207->815|2452->1013|2523->1037|2617->1084|2683->1103|2773->1146|2865->1191|2961->1240|3013->1256|3029->1263|3059->1271|3109->1274|3220->1338|3272->1354|3288->1361|3318->1369|3368->1372|3484->1441|3615->1525|3667->1541|3683->1548|3713->1556|3763->1559|3832->1582|3888->1592|3944->1602|4182->1804|4198->1811|4231->1822|4641->2196|4658->2203|4692->2214|4764->2249|4781->2256|4821->2273|4956->2372|4972->2379|5005->2390|5095->2444|5111->2451|5150->2468|5260->2542|5276->2549|5311->2562|5350->2565|5366->2572|5396->2580
                    LINES: 27->1|30->1|37->8|37->8|37->8|38->9|38->9|38->9|39->10|39->10|39->10|40->11|40->11|40->11|41->12|41->12|41->12|42->13|42->13|42->13|43->14|43->14|43->14|44->15|44->15|44->15|47->18|47->18|48->19|48->19|50->21|51->22|52->23|52->23|52->23|52->23|52->23|53->24|53->24|53->24|53->24|53->24|55->26|57->28|57->28|57->28|57->28|57->28|58->29|59->30|60->31|68->39|68->39|68->39|77->48|77->48|77->48|77->48|77->48|77->48|80->51|80->51|80->51|82->53|82->53|82->53|84->55|84->55|84->55|84->55|84->55|84->55
                    -- GENERATED --
                */
            