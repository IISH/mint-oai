// OAI Project list

function MintOAIProjects(id) {
	var self = this;
	
	this.id = id;
	this.container = $("#" + id);

	// header
	this.container.empty();
	var legend = $("<legend>").text("Projects").appendTo(this.container);
	
	// loading page
	this.loading = $("<div>").attr("id", id + "-loading").appendTo(this.container);
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	var projectsContainer = $("<div>").addClass("row").appendTo(this.contents);
	var projects = $("<div>").addClass("span3").appendTo(projectsContainer);
	this.projects = $("<div>").attr("id", id + "-projects").addClass("project-list").appendTo(projects);
}

MintOAIProjects.prototype.refresh = function() {
	this.loadProjects();
}

MintOAIProjects.prototype.loadProjects = function() {
	var self = this;
	
	this.projects.empty();
	this.projects.append($("<div>").addClass("spinner"));
	
	this.loading.show();
	this.contents.hide();
	
	// Create and populate the data table.
	$.get("projects", function(projects) {
		self.projects.empty();

		for(var i in projects) {
			var project = projects[i];
			self.projects.append(self.projectDiv(project));
		}

		self.loading.hide();
		self.contents.show();		
	});    
}

MintOAIProjects.prototype.projectDiv = function(project) {
	var self = this;
	
	var div = $("<div>").addClass("project");
	
	var title = project.projectName;
	if(project.title != undefined) title = project.title;
	
	$("<i>").addClass("icon-signal").css({ float: "right", cursor: "pointer" }).appendTo(div).click(function () {
		var header = $("#projectStatistics").find(".modal-header > h3");
		var body = $("#projectStatistics").find(".modal-body");
	
		header.empty()
			.append($("<span>").text("Statistics for " + title))
			.append($("<span>").css("color", "silver").text(" (" + project.projectName + ")"));
		
		self.projectStatistics = new MintOAIProjectStatistics(project.projectName, body);

		$("#projectStatistics").modal("show").css({
		    width: 'auto',
		    'margin-left': function () {
		        return -($(this).width() / 2);
		    }
		});
	}).attr("title", "Statistics for " + title).tooltip();

	$("<div>").append($("<a>").text(title).attr("target", "_blank").attr("href", "http://localhost:9001/manager/statistics/" + project.projectName)).appendTo(div);
	$("<small>").css("color", "silver").text(project.projectName).appendTo(div);
	
	return div;
}

// Overall statistics

function MintOAIOverallStatistics(id) {
	var self = this;

	this.id = id;
	this.container = $("#" + id);
	this.repositories = $("<span>").addClass("stat-value");
	this.organizations = $("<span>").addClass("stat-value");
	this.duplicates = $("<span>").addClass("stat-value");
	this.unique = $("<span>").addClass("stat-value");

	// header
	this.container.empty();
	var legend = $("<legend>").text("Overall Statistics").appendTo(this.container);
	
	// loading page
	this.loading = $("<div>").attr("id", id + "-loading").appendTo(this.container);
//	$("<div>").addClass("progress progress-striped active").appendTo(this.loading).append($("<div>").addClass("bar").css("width", "100%"));
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	this.statistics = $("<div>").attr("id", id + "-statistics").addClass("row").css("margin-bottom", "10px").appendTo(this.contents);
	statistic("Repositories", this.repositories).appendTo(this.statistics);
	statistic("Organizations", this.organizations).appendTo(this.statistics);
	statistic("Duplicates", this.duplicates).appendTo(this.statistics);
	statistic("Unique Records", this.unique).appendTo(this.statistics);
	
	var visualizationContainer = $("<div>").addClass("row").appendTo(this.contents);
	this.visualization = $("<div>").addClass("span3").attr("id", id + "-visualization").css({
		"width": "500px",
		"height": "350px"
	}).appendTo(visualizationContainer);
}

MintOAIOverallStatistics.prototype.refresh = function() {
	var self = this;
	
	this.loading.show();
	this.contents.hide();
	
	$.get("overall", function(data) {
		self.loading.hide();
		self.contents.show();

		self.repositories.text(addCommas(data.repositories));
		self.organizations.text(addCommas(data.organizations));
		self.duplicates.text(addCommas(data.duplicates));
		self.unique.text(addCommas(data.unique));

		self.drawVisualization();
	});
}

MintOAIOverallStatistics.prototype.drawVisualization = function() {
	var self = this;
	this.visualization.empty();
	this.visualization.append($("<div>").addClass("spinner"));
	
	// Create and populate the data table.
	$.get("recordsPerProject", function(data) {
		self.visualization.empty();

		var data1 = google.visualization.arrayToDataTable(data.vals);		
		var pie = new google.visualization.PieChart(self.visualization[0]);
		pie.draw(data1, {title: "Records per Project", is3D: true});    
	});    
}

// Project statistics

function MintOAIProjectStatistics(project, id) {
	var self = this;

	if(id instanceof jQuery) {
		this.id = "project-statistics";
		this.container = id;
	} else {
		this.id = id;
		this.container = $("#" + id);		
	}
	
	this.project = project;
	
	this.publications = $("<span>").addClass("stat-value");
	this.organizations = $("<span>").addClass("stat-value");
	this.duplicates = $("<span>").addClass("stat-value");
	this.unique = $("<span>").addClass("stat-value");

	// header
	this.container.empty();
	var legend = $("<legend>").text("Project Statistics").appendTo(this.container);
	
	// loading page
	this.loading = $("<div>").attr("id", id + "-loading").appendTo(this.container);
//	$("<div>").addClass("progress progress-striped active").appendTo(this.loading).append($("<div>").addClass("bar").css("width", "100%"));
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	this.statistics = $("<div>").attr("id", id + "-statistics").addClass("row").css("margin-bottom", "10px").appendTo(this.contents);
	statistic("Organizations", this.organizations).appendTo(this.statistics).addClass("offset1");
	statistic("Publications", this.publications).appendTo(this.statistics);
	statistic("Duplicates", this.duplicates).appendTo(this.statistics);
	statistic("Unique Records", this.unique).appendTo(this.statistics);
	
	var visualizationContainer = $("<div>").addClass("row").appendTo(this.contents);
	this.visualization = $("<div>").addClass("span3").attr("id", id + "-visualization").css({
		"width": "500px",
		"height": "350px"
	}).appendTo(visualizationContainer);
	
	this.refresh();
}

MintOAIProjectStatistics.prototype.refresh = function(project) {
	var self = this;
	
	if(project != undefined) this.project = project;
	
	this.loading.show();
	this.contents.hide();
	
	$.get("quickStats/" + this.project, function(data) {
		self.loading.hide();
		self.contents.show();

		self.publications.text(addCommas(data.publications));
		self.organizations.text(addCommas(data.organizations));
		self.duplicates.text(addCommas(data.duplicates));
		self.unique.text(addCommas(data.unique));

		self.drawVisualization(data.orgsRecordsCount[0]);
	});
}

MintOAIProjectStatistics.prototype.drawVisualization = function(data) {
	this.visualization.empty();

	var values = google.visualization.arrayToDataTable(data.values);
	var container = $("<div>").appendTo(this.visualization);
	var chart = new google.visualization.PieChart(container);
	chart.draw(values, {title:"Records per Organization ID", width:600, height:500});
}


// previous methods

function initializeModal(projectName){
	$.get("quickStats/"+projectName,function(data){
		$("#modalHeaderText").html('Quick Overview for Project ' + projectName);

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

	$('#statsModal').modal('show');	
}

// utility functions

function addCommas(nStr)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

function statistic(label, dataContainer) {
	var stat = $("<div>").addClass("span2");

	$("<div>").addClass("stat").appendTo(stat).append(dataContainer).append($("<span>").text(label));
	
	return stat;
}