
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
object manager extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template0[play.api.templates.Html] {

    /**/
    def apply():play.api.templates.Html = {
        _display_ {

Seq[Any](format.raw/*1.1*/("""<!DOCTYPE html>

<html>
<head>
    <link rel="stylesheet" media="screen" href='"""),_display_(Seq[Any](/*5.50*/routes/*5.56*/.Assets.at("css/bootstrap.min.css"))),format.raw/*5.91*/("""'></link>
    <link rel="stylesheet" media="screen" href='"""),_display_(Seq[Any](/*6.50*/routes/*6.56*/.Assets.at("css/main.css"))),format.raw/*6.82*/("""'></link>
    <link rel="shortcut icon" type="image/png" href='"""),_display_(Seq[Any](/*7.55*/routes/*7.61*/.Assets.at("images/favicon.png"))),format.raw/*7.93*/("""'></link>
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <script src='"""),_display_(Seq[Any](/*9.19*/routes/*9.25*/.Assets.at("js/jquery-1.7.1.min.js"))),format.raw/*9.61*/("""' type="text/javascript"></script>
    <script src='"""),_display_(Seq[Any](/*10.19*/routes/*10.25*/.Assets.at("js/bootstrap.js"))),format.raw/*10.54*/("""' type="text/javascript"></script>
    <script src='"""),_display_(Seq[Any](/*11.19*/routes/*11.25*/.Assets.at("js/oaimanager.js"))),format.raw/*11.55*/("""' type="text/javascript"></script>
	<script type="text/javascript">
	  		google.load('visualization', '1', """),format.raw("""{"""),format.raw/*13.41*/("""packages: ['corechart']"""),format.raw("""}"""),format.raw/*13.65*/(""");

	  		$(document).ready(function () """),format.raw("""{"""),format.raw/*15.37*/("""
	  			google.setOnLoadCallback(function() """),format.raw("""{"""),format.raw/*16.44*/("""
	  				overall = new MintOAIOverallStatistics("overall");
	  				projects = new MintOAIProjects("projects");
	  				
	  				$("#refresh").click(function() """),format.raw("""{"""),format.raw/*20.40*/("""
		  				projects.refresh();
		  				overall = new MintOAIOverallStatistics("overall");
	  				"""),format.raw("""}"""),format.raw/*23.9*/(""")
	  			"""),format.raw("""}"""),format.raw/*24.8*/(""");
	  		"""),format.raw("""}"""),format.raw/*25.7*/(""");
	</script>
</head>

<body class="gradient">
	<div class="wrapper">
		<div class="navbar">
			<div class="navbar-inner">
				<a class="brand" href="#">Mint OAI Repository</a>
				<button id="refresh" style="float: right" class="btn">Refresh</button>
			</div>
		</div>
		<div class="container">
			<div class="row">
				<div id="projects" class="well sidebar span3"></div>
				<div id="overall" class="well content span8"></div>
			</div>
		</div>
	</div>

	<div class="push">
		<!--//-->
	</div>

	<footer>
		<div class="container-fluid">
			<div class="row">
				<p class="span2">
					trying to refresd
					Powered By <a href="http://mint.image.ece.ntua.gr/" rel="author">Mint</a>.
				</p>
			</div>
		</div>
	</footer>
</body>

</html>
"""))}
    }
    
    def render() = apply()
    
    def f:(() => play.api.templates.Html) = () => apply()
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Mon Mar 16 10:01:04 EET 2015
                    SOURCE: /home/costas/toplay/mintoai/Application/app/views/manager.scala.html
                    HASH: e46f2c89826794aba8cfefd259de77bbd35a1cf5
                    MATRIX: 821->0|940->84|954->90|1010->125|1105->185|1119->191|1166->217|1266->282|1280->288|1333->320|1477->429|1491->435|1548->471|1638->525|1653->531|1704->560|1794->614|1809->620|1861->650|2018->760|2089->784|2178->826|2270->871|2478->1032|2623->1131|2679->1141|2735->1151
                    LINES: 30->1|34->5|34->5|34->5|35->6|35->6|35->6|36->7|36->7|36->7|38->9|38->9|38->9|39->10|39->10|39->10|40->11|40->11|40->11|42->13|42->13|44->15|45->16|49->20|52->23|53->24|54->25
                    -- GENERATED --
                */
            