function statistic(label, dataContainer) {
	var stat = $("<div>").addClass("span2");

	$("<div>").addClass("stat").appendTo(stat).append(dataContainer).append($("<span>").text(label));
	
	return stat;
}

function MintOAIOverallStatistics(selector) {
	var self = this;

	this.container = $(selector);
	this.repositories = $("<span>").addClass("stat-value");
	this.organizations = $("<span>").addClass("stat-value");
	this.duplicates = $("<span>").addClass("stat-value");
	this.unique = $("<span>").addClass("stat-value");

	// header
	this.container.empty();
	var legend = $("<legend>").text("Mint OAI-PMH Repository Overall Statistics").appendTo(this.container);
	this.btnRefresh = $("<button>").addClass("btn disabled").css("float", "right").appendTo(legend);
	this.btnRefresh.append($("<span>").text("Refresh"));
	this.btnRefresh.click(function () {
		if(!$(this).hasClass("disabled")) {
			self.refresh();
		} else {
			alert("This is disabled!"); 
		}
	});
	
	// loading page
	this.loading = $("<div>").attr("id", "overall-loading").appendTo(this.container);
//	$("<div>").addClass("progress progress-striped active").appendTo(this.loading).append($("<div>").addClass("bar").css("width", "100%"));
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	this.statistics = $("<div>").attr("id", "overall-statistics").addClass("row").appendTo(this.contents);
	statistic("Repositories", this.repositories).appendTo(this.statistics).addClass("offset2");
	statistic("Organizations", this.organizations).appendTo(this.statistics);
	statistic("Duplicates", this.duplicates).appendTo(this.statistics);
	statistic("Unique Records", this.unique).appendTo(this.statistics);

	visualizationContainer = $("<div>").addClass("row").appendTo(this.contents);
	this.visualization = $("<div>").addClass("span2 offset3").attr("id", "overall-visualization").css({
		"width": "500px",
		"height": "350px"
	}).appendTo(visualizationContainer);
}

MintOAIOverallStatistics.prototype.refresh = function() {
	var self = this;
	
	this.btnRefresh.addClass("disabled");
	this.loading.show();
	this.contents.hide();
	
	$.get("overall", function(data) {
		self.loading.hide();
		self.contents.show();

		self.repositories.text(data.repositories);
		self.organizations.text(data.organizations);
		self.duplicates.text(data.duplicates);
		self.unique.text(data.unique);

		self.drawVisualization();

		self.btnRefresh.removeClass("disabled");
	});
	
}

// previous methods

function initializeModal(projectName){
	if(projectName == 'conflicts'){
		$('#conflictsModal').modal('show');
		return;
	}
	$.get("quickStats/"+projectName,function(data){
		$('#spanOrgsStats').text(data.OrgsNumber);
		$('#spanRecsStats').text(data.RecordsNumber);
		$('#spanPubsStats').text(data.ReportsNumber);
		$('#spanDupsStats').text(data.ConflictsNumber);
		$('#latestDateHero').text(data.latestDate);
		$('#pieCharts').empty();
		for(var i=0;i<data.orgsRecordsCount.length;i++){
			var tmpPieData = data.orgsRecordsCount[i];
			var values = tmpPieData.values;
			var pref = tmpPieData.namespace;
			var data2 = google.visualization.arrayToDataTable(values);
			d=document.createElement('div');
			pd = document.createElement('div');
			a = document.createElement('a');
			$(a).text(pref).attr("href", "http://panic.image.ntua.gr:9000/"+projectName+"/oai?verb=ListIdentifiers&metadataPrefix="+pref)
			.attr("target","_blank");
			l=document.createElement('legend');
			$(l).text("Distribution of Records among Organizations for NameSpace ").append(a);
			$(d).addClass("well span9").append([l,pd]);
			
			var pieChart = new google.visualization.PieChart(pd);
			pieChart.draw(data2, {title:"Records per Organization ID", width:600, height:500});

			$('#pieCharts').append(d);
		}
		//var values = data.orgsRecordsCount;
		//var data2 = google.visualization.arrayToDataTable(values);
		//var pieChart = new google.visualization.PieChart(document.getElementById('orgViz'));
		//pieChart.draw(data2, {title:"Records per Organization ID", width:600, height:500,backgroundColor: '#B6C2C6'});
	});
	$("#modalHeaderText").html('Quick Overview for Project ' + projectName);
	$('#statsModal').modal('show');	
}

MintOAIOverallStatistics.prototype.drawVisualization = function() {
	var self = this;
	this	.visualization.empty();
	this.visualization.append($("<div>").addClass("spinner"));
	
	// Create and populate the data table.
	$.get("getAggregatedValues", function(data) {
		self.visualization.empty();

		var data1 = google.visualization.arrayToDataTable(data.vals);
		
		var pie = new google.visualization.PieChart(self.visualization[0]);
		pie.draw(data1, {title: "Records per Project", is3D: true});
    
		google.visualization.events.addListener(pie, 'select', function() {
			//alert(data1.getValue(pie.getSelection()[0].row, 0));
			//$('#statsModal').modal('show');
        	initializeModal(data1.getValue(pie.getSelection()[0].row, 0));
      	});
	});    
}

$(document).ready(function () {
	overall = new MintOAIOverallStatistics("#overall");

	google.setOnLoadCallback(function() {
		console.log(overall);
		overall.refresh();
	});
	
	
	$.get("projects.json", function(data){
		var inner = "";
		for(var i = 0; i < data.projects.length;i++){
			if(i == 0){
	             inner += "<div class=\"item active\">"
	            	 inner +=   "<img src=\"/assets/images/" + data.projects[i].image + "\" " + "alt=\"\">"
	            		 inner +=  "<div class=\"carousel-caption\">"
	            			 inner +=   "<h4>"+data.projects[i].title+"</h4>"
	            			 inner +=   "<p>"+data.projects[i].description+"</p>"
	            			 inner += "<span class=\"label label-important\">" + "<a href="+ "\"/manager/statistics/"+data.projects[i].projectName +"\">" +"Load" +"</a>"+"</span>"
	            			 inner +=  "</div>"
	            				 inner +=  " </div>"
			}else{
				inner +=   "<div class=\"item\">"
					inner +=    " <img src=\"/assets/images/"+ data.projects[i].image +  "\" " + "alt=\"\">"
						inner +=  "<div class=\"carousel-caption\">"
							inner +=   "<h4>"+data.projects[i].title+"</h4>"
							inner +=  "<p>"+data.projects[i].description+"</p>"
							inner += "<span class=\"label label-important\">" + "<a href="+ "\"/manager/statistics/"+data.projects[i].projectName +"\">" +"Load" +"</a>"+"</span>"
							inner +=  "</div>"
								inner +=  "</div>"
			}
		}
	  $('#inner-car').append(inner);
	});
});

