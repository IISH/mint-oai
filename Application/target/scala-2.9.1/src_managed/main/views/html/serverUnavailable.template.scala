
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
object serverUnavailable extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template1[Exception,play.api.templates.Html] {

    /**/
    def apply/*1.2*/(e: Exception):play.api.templates.Html = {
        _display_ {

Seq[Any](format.raw/*1.16*/("""
<!DOCTYPE html>

<html>
<head>
    <link rel="stylesheet" media="screen" href='"""),_display_(Seq[Any](/*6.50*/routes/*6.56*/.Assets.at("css/bootstrap.min.css"))),format.raw/*6.91*/("""'></link>
    <link rel="stylesheet" media="screen" href='"""),_display_(Seq[Any](/*7.50*/routes/*7.56*/.Assets.at("css/main.css"))),format.raw/*7.82*/("""'></link>
    <link rel="shortcut icon" type="image/png" href='"""),_display_(Seq[Any](/*8.55*/routes/*8.61*/.Assets.at("images/favicon.png"))),format.raw/*8.93*/("""'></link>
    <script src='"""),_display_(Seq[Any](/*9.19*/routes/*9.25*/.Assets.at("js/jquery-1.7.1.min.js"))),format.raw/*9.61*/("""' type="text/javascript"></script>
    <script src='"""),_display_(Seq[Any](/*10.19*/routes/*10.25*/.Assets.at("js/bootstrap.js"))),format.raw/*10.54*/("""' type="text/javascript"></script>
</head>

<body class="gradient">
    <div class="wrapper">
    	<div class="container">
	 		<div class="row">
     	 		<div class="span12">
			 		<div id="overall" class="well">
			 			<h5>Server Unavailable</h5>
			 			<p>"""),_display_(Seq[Any](/*20.12*/e/*20.13*/.getMessage())),format.raw/*20.26*/("""</p>
			 		</div>
		 		</div>
	 		</div>
		</div>
	</div>
	<div class="push"><!--//--></div>

	<footer>
	     <div class="container-fluid">
	     	<div class="row">
        		<p class="span2">Powered By <a href="http://mint.image.ece.ntua.gr/" rel="author">Mint</a>.</p>
        	</div>
      	</div>
	</footer>
</body>

</html>"""))}
    }
    
    def render(e:Exception) = apply(e)
    
    def f:((Exception) => play.api.templates.Html) = (e) => apply(e)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Fri Mar 11 21:52:33 EET 2016
                    SOURCE: /home/costas/git/mintoai/Application/app/views/serverUnavailable.scala.html
                    HASH: 342259802cb2e1ca1137bfae3c95753836e4bcff
                    MATRIX: 770->1|861->15|977->96|991->102|1047->137|1141->196|1155->202|1202->228|1301->292|1315->298|1368->330|1431->358|1445->364|1502->400|1591->453|1606->459|1657->488|1952->747|1962->748|1997->761
                    LINES: 27->1|30->1|35->6|35->6|35->6|36->7|36->7|36->7|37->8|37->8|37->8|38->9|38->9|38->9|39->10|39->10|39->10|49->20|49->20|49->20
                    -- GENERATED --
                */
            