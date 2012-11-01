// OAI Project list

function MintOAIProjects(id) {
	var self = this;
	
	if(id instanceof jQuery) {
		this.id = "overall-statistics";
		this.container = id;
	} else {
		this.id = id;
		this.container = $("#" + id);		
	}

	this.container.empty();

	// header
	
	var legend = $("<legend>").text("Projects").appendTo(this.container);
	$("<i>").addClass("icon-signal").css({ float: "right", cursor: "pointer" }).appendTo(legend).click(function () {	
		new MintOAIOverallStatistics("overall");
	}).attr("title", "Overall statistics").tooltip();
	
	// loading page
	this.loading = $("<div>").attr("id", this.id + "-loading").appendTo(this.container);
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	var projectsContainer = $("<div>").addClass("row").appendTo(this.contents);
	var projects = $("<div>").addClass("span3").appendTo(projectsContainer);
	this.projects = $("<div>").attr("id", id + "-projects").addClass("project-list").appendTo(projects);
	
	this.refresh();
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
	$.get("/manager/projects", function(projects) {
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
		self.projectStatistics = new MintOAIProjectStatistics(project, $("#overall"));
	}).attr("title", "Statistics for " + title).tooltip();

	$("<div>").append($("<a>").text(title).attr("target", "_blank").attr("href", "/manager/projects/" + project.projectName)).appendTo(div);
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
	
	this.refresh();
}

MintOAIOverallStatistics.prototype.refresh = function() {
	var self = this;
	
	this.loading.show();
	this.contents.hide();
	
	$.get("/manager/overall", function(data) {
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
	$.get("/manager/recordsPerProject", function(data) {
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
	if(this.project.title == undefined) this.project.title = this.project.projectName;
	
	this.publications = $("<span>").addClass("stat-value");
	this.organizations = $("<span>").addClass("stat-value");
	this.duplicates = $("<span>").addClass("stat-value");
	this.unique = $("<span>").addClass("stat-value");

	// header
	this.container.empty();
	var legend = $("<legend>").text("Project Statistics for " + this.project.title)
		.append($("<span>").css("color", "silver").text(" (" + this.project.projectName + ")"))
		.appendTo(this.container);
	
	// loading page
	this.loading = $("<div>").attr("id", id + "-loading").appendTo(this.container);
//	$("<div>").addClass("progress progress-striped active").appendTo(this.loading).append($("<div>").addClass("bar").css("width", "100%"));
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	this.statistics = $("<div>").attr("id", this.id).addClass("row").css("margin-bottom", "10px").appendTo(this.contents);
	statistic("Organizations", this.organizations).appendTo(this.statistics);
	statistic("Publications", this.publications).appendTo(this.statistics);
	statistic("Duplicates", this.duplicates).appendTo(this.statistics);
	statistic("Unique Records", this.unique).appendTo(this.statistics);
	
	this.latestPublicationDate = $("<strong>");
	var info = $("<div>").addClass("alert alert-info span7").append($("<span>").text("Date of last publication: ")).append(this.latestPublicationDate);
	$("<div>").addClass("row").append(info).appendTo(this.contents);
	
	this.visualizations = $("<div>").addClass("span7").appendTo($("<div>").addClass("row").appendTo(this.contents));
	
	this.refresh();
}

MintOAIProjectStatistics.prototype.refresh = function() {
	var self = this;
	
	this.loading.show();
	this.contents.hide();
	
	$.get("/manager/projects/" + this.project.projectName + "/overall", function(data) {
		self.loading.hide();
		self.contents.show();

		self.publications.text(addCommas(data.publications));
		self.organizations.text(addCommas(data.organizations));
		self.duplicates.text(addCommas(data.duplicates));
		self.unique.text(addCommas(data.unique));
		
		self.latestPublicationDate.text(data.latestPublicationDate);
		
		self.visualizations.empty();
		
		for(var i in data.namespaces) {
			var namespace = data.namespaces[i];
			
			$("<legend>").text("Namespace: " + namespace).appendTo(self.visualizations);
			var container = $("<div>").addClass("span7").css({
				width: "500px",
				height: "350px",
				margin: "0 auto"
			}).appendTo(self.visualizations);
			
			self.drawVisualization(container, namespace);
		}		
	});
}

MintOAIProjectStatistics.prototype.drawVisualization = function(container, namespace) {
	container.empty();
	container.append($("<div>").addClass("spinner"));

	$.get("/manager/projects/" + this.project.projectName + "/recordsPerOrganization/" + namespace, function(data) {
		toChart(container, data, ["Organization", "Records"], {
			title: 	"Records per organization",
			is3D: 	true
		});
	});
}

//OAI Project Organization list

function MintOAIOrganizations(project, id) {
	var self = this;
	
	if(id instanceof jQuery) {
		this.id = "overall-organizations";
		this.container = id;
	} else {
		this.id = id;
		this.container = $("#" + id);		
	}

	this.project = project;
	this.container.empty();

	// header
	
	var legend = $("<legend>").text("Organizations").appendTo(this.container);
	$("<i>").addClass("icon-signal").css({ float: "right", cursor: "pointer" }).appendTo(legend).click(function () {	
		new MintOAIProjectStatistics(project, "overall");
	}).attr("title", "Overall statistics").tooltip();
	
	// loading page
	this.loading = $("<div>").attr("id", this.id + "-loading").appendTo(this.container);
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	var organizationsContainer = $("<div>").addClass("row").appendTo(this.contents);
	var organizations = $("<div>").addClass("span3").appendTo(organizationsContainer);
	this.organizations = $("<div>").attr("id", id + "-organizations").addClass("organization-list").appendTo(organizations);
	
	this.refresh();
}

MintOAIOrganizations.prototype.refresh = function() {
	this.loadOrganizations();
}

MintOAIOrganizations.prototype.loadOrganizations = function() {
	var self = this;
	
	this.organizations.empty();
	this.organizations.append($("<div>").addClass("spinner"));
	
	this.loading.show();
	this.contents.hide();
	
	// Create and populate the data table.
	$.get("/manager/projects/" + self.project.projectName + "/organizations", function(organizations) {
		self.organizations.empty();
		
		console.log(organizations);

		for(var i in organizations) {
			var organization = organizations[i];
			console.log(organization);
			self.organizations.append(self.organizationDiv(organization));
		}

		self.loading.hide();
		self.contents.show();		
	});    
}

MintOAIOrganizations.prototype.organizationDiv = function(organization) {
	var self = this;
	
	var div = $("<div>").addClass("organization");
	
	var title = organization.name;
	if(organization.name != undefined) title = organization.id;
	
	$("<i>").addClass("icon-signal").css({ float: "right", cursor: "pointer" }).appendTo(div).click(function () {	
		self.projectStatistics = new MintOAIProjectStatistics(this.project, $("#overall"));
	}).attr("title", "Statistics for " + title).tooltip();

	$("<div>").append($("<a>").text(title).attr("target", "_blank")).appendTo(div);
	
	return div;
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

function toChart(container, data, headers, options) {
	container.empty();
	
	if(headers == undefined) headers = [ "Key", "Value"];
	
	var values = [];
	values.push(headers);
	
	for(var key in data) {
		var value = data[key];
		values.push([key, value]);
	}
	
	var dataTable = google.visualization.arrayToDataTable(values);
	var pieChart = new google.visualization.PieChart(container[0]);
	pieChart.draw(dataTable, options);
	
	return container;
}