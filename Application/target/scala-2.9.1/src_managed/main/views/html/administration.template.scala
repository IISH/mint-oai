
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
object administration extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template0[play.api.templates.Html] {

    /**/
    def apply():play.api.templates.Html = {
        _display_ {

Seq[Any](format.raw/*1.1*/("""<!DOCTYPE html>

<html>
<head>
    <link rel="stylesheet" media="screen" href='"""),_display_(Seq[Any](/*5.50*/routes/*5.56*/.Assets.at("css/bootstrap.min.css"))),format.raw/*5.91*/("""'></link>
    <link rel="stylesheet" media="screen" href='"""),_display_(Seq[Any](/*6.50*/routes/*6.56*/.Assets.at("css/main.css"))),format.raw/*6.82*/("""'></link>
    <link rel="shortcut icon" type="image/png" href='"""),_display_(Seq[Any](/*7.55*/routes/*7.61*/.Assets.at("images/favicon.png"))),format.raw/*7.93*/("""'></link>
    <script src='"""),_display_(Seq[Any](/*8.19*/routes/*8.25*/.Assets.at("js/jquery-1.7.1.min.js"))),format.raw/*8.61*/("""' type="text/javascript"></script>
    <script src='"""),_display_(Seq[Any](/*9.19*/routes/*9.25*/.Assets.at("js/bootstrap.js"))),format.raw/*9.54*/("""' type="text/javascript"></script>
    <script src='"""),_display_(Seq[Any](/*10.19*/routes/*10.25*/.Assets.at("js/oaimanager.js"))),format.raw/*10.55*/("""' type="text/javascript"></script>
    <script src='"""),_display_(Seq[Any](/*11.19*/routes/*11.25*/.Assets.at("js/administration.js"))),format.raw/*11.59*/("""' type="text/javascript"></script>

	<style>
		.list > dt """),format.raw("""{"""),format.raw/*14.15*/("""
			width: 100px;
			margin-right: 10px;
		"""),format.raw("""}"""),format.raw/*17.4*/("""
		
		.list > dd """),format.raw("""{"""),format.raw/*19.15*/("""
			margin-left: 120px;
			margin-bottom: 5px;
		"""),format.raw("""}"""),format.raw/*22.4*/("""
		
		input, textarea, .uneditable-input """),format.raw("""{"""),format.raw/*24.39*/("""
			margin-left: 0;
			width: 400px;
		"""),format.raw("""}"""),format.raw/*27.4*/("""
		
		textarea """),format.raw("""{"""),format.raw/*29.13*/("""
			height: 80px;
		"""),format.raw("""}"""),format.raw/*31.4*/("""
		
		button """),format.raw("""{"""),format.raw/*33.11*/("""
			margin-right: 5px;
		"""),format.raw("""}"""),format.raw/*35.4*/("""
	</style>
	<script type="text/javascript">
	  		$(document).ready(function () """),format.raw("""{"""),format.raw/*38.37*/("""
	  			administration = new MintOAIAdministration("projects", "overall");
	  		"""),format.raw("""}"""),format.raw/*40.7*/(""");
	</script>
</head>

<body class="gradient">
	<div class="wrapper">
		<div class="navbar">
			<div class="navbar-inner">
				<a class="brand" href="#">Mint OAI Repository: Administration</a>
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
                    DATE: Fri Mar 11 21:52:33 EET 2016
                    SOURCE: /home/costas/git/mintoai/Application/app/views/administration.scala.html
                    HASH: ef599f94666922c5bd21073499086d76825228a9
                    MATRIX: 828->0|947->84|961->90|1017->125|1112->185|1126->191|1173->217|1273->282|1287->288|1340->320|1404->349|1418->355|1475->391|1564->445|1578->451|1628->480|1718->534|1733->540|1785->570|1875->624|1890->630|1946->664|2055->726|2148->773|2215->793|2314->846|2405->890|2494->933|2559->951|2628->974|2691->990|2765->1018|2895->1101|3023->1183
                    LINES: 30->1|34->5|34->5|34->5|35->6|35->6|35->6|36->7|36->7|36->7|37->8|37->8|37->8|38->9|38->9|38->9|39->10|39->10|39->10|40->11|40->11|40->11|43->14|46->17|48->19|51->22|53->24|56->27|58->29|60->31|62->33|64->35|67->38|69->40
                    -- GENERATED --
                */
            