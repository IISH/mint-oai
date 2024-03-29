// OAI Project list

function MintOAIProjects(id, options) {
	var self = this;
	var defaults = {
		statistics: null,
		click: null,
		showStatistics: true
	};
	
	this.options = $.extend({}, defaults, options);
	
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
	var stats = $("<i>").addClass("icon-signal").css({ float: "right", cursor: "pointer" }).appendTo(legend).click(function () {	
		if(self.options.statistics != null) {
			self.options.statistics();
		} else new MintOAIOverallStatistics("overall");
	}).attr("title", "Overall statistics").tooltip();
	if(!this.options.showStatistics) stats.hide();
	
	// loading page
	this.loading = $("<div>").attr("id", this.id + "-loading").appendTo(this.container);
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	var projectsContainer = $("<div>").addClass("row").appendTo(this.contents);
	var projects = $("<div>").addClass("span3").appendTo(projectsContainer);
	this.projects = $("<div>").attr("id", id + "-projects").addClass("list").appendTo(projects);
	
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
	
	if(self.options.statistics != null) {
		var stats = $("<i>").addClass("icon-signal").css({ float: "right", cursor: "pointer" }).appendTo(div).click(function () {
			if(self.options.statistics != null) {
				self.options.statistics(project);
			}
		}).attr("title", "Statistics for " + title).tooltip();
	}

	$("<div>").append($("<a>").text(title).attr("target", "_blank").attr("href", "/manager/projects/" + project.projectName)).appendTo(div);
	$("<small>").css("color", "silver").text(project.projectName).appendTo(div);
	
	if(self.options.click != null) {
		div.click(function () {
			if(self.options.click != null) {
				self.options.click(project);
			}
		});
	}
	
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
	
	this.visualizations = $("<div>").addClass("span8").appendTo($("<div>").addClass("row").appendTo(this.contents));
	
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
			
			var legend = $("<legend>").text("Namespace: " + namespace.prefix).appendTo(self.visualizations);
			$("<a>").addClass("oai-action").css("float", "right").attr("title", "OAI url for " + namespace.prefix).attr("href", namespace.oai).attr("target", "_blank").appendTo(legend);
			var container = $("<div>").addClass("span8").css({
				width: "500px",
				height: "350px",
				margin: "0 auto"
			}).appendTo(self.visualizations);
			
			self.drawVisualization(container, namespace.prefix);
		}		
	});
}

MintOAIProjectStatistics.prototype.drawVisualization = function(container, namespace) {
	var self = this;
	
	container.empty();
	container.append($("<div>").addClass("spinner"));

	$.get("/manager/projects/" + self.project.projectName + "/organizations", function(organizations) {
		self.metadata = organizations;
		console.log(self.metadata);

		$.get("/manager/projects/" + self.project.projectName + "/recordsPerOrganization/" + namespace, function(data) {
			if(self.metadata != undefined) {
				var list = {};
				
				for(var key in data) {
					var value = data[key];
					if(self.metadata[key] != undefined) {
						list[self.metadata[key].metadata.name] = value;
					} else {
						list[key] = value;
					} 
				}
				
				data = list;
				console.log(list);
			}
		
			toChart(container, data, ["Organization", "Records"], {
				title: 	"Records per organization",
				is3D: 	true
			});		
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
	
	this.sortDirection = 1;

	var sortName = $("<a>").text("Name").click(function() { self.sortByName() });
	var sortRecords = $("<a>").text("Records").click(function() { self.sortByRecords() });
	var sortDate = $("<a>").text("Publication Date").click(function() { self.sortByDate() });
	
	var legend = $("<legend>").text("Organizations").appendTo(this.container);
	$("<div>").addClass("btn-group").css("float", "right")
	.append($("<button>").addClass("btn btn-small dropdown-toggle").attr("data-toggle", "dropdown").append($("<span>").text("Sort by ").append($("<span>").addClass("caret"))))
	.append($("<ul>").addClass("dropdown-menu")
			.append($("<li>").append(sortName))
			.append($("<li>").append(sortRecords))
			.append($("<li>").append(sortDate))
	).appendTo(legend);
	
	// loading page
	this.loading = $("<div>").attr("id", this.id + "-loading").appendTo(this.container);
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	var organizationsContainer = $("<div>").addClass("row").appendTo(this.contents);
	var organizations = $("<div>").addClass("span3").appendTo(organizationsContainer);
	this.organizations = $("<div>").attr("id", id + "-organizations").addClass("list").appendTo(organizations);
	
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
		self.metadata = organizations;
		
		for(var i in organizations) {
			var organization = organizations[i];
			self.organizations.append(self.organizationDiv(organization));
		}

		self.loading.hide();
		self.contents.show();		
	});    
}

MintOAIOrganizations.prototype.organizationDiv = function(organization) {
	var self = this;
	
	var div = $("<div>").addClass("organization");
	div.data("organization", organization);
	
	var title = (organization.metadata.name != undefined)?organization.metadata.name:organization.id;
	
	$("<div>").appendTo(div)
		.append($("<a>").text(title).attr("target", "_blank").attr("href", "/manager/projects/" + this.project.projectName + "/organizations/" + organization.id))
		.append($("<div>").append($("<small>").addClass("muted").css("float", "right").text("" + organization.records + " records"))
				.append($("<small>").addClass("muted").text(organization.latestPublicationDate)));
	
	return div;
}

MintOAIOrganizations.prototype.sortByName = function() {
	var self = this;
	this.organizations.find(".organization").sortElements(function(a, b) {
		console.log(a, b);
		return ($(a).data("organization").name < $(b).data("organization").name)?self.sortDirection:-1*self.sortDirection;
	});
	this.sortDirection *= -1;
}

MintOAIOrganizations.prototype.sortByRecords = function() {
	var self = this;
	this.organizations.find(".organization").sortElements(function(a, b) {
		return ($(a).data("organization").records < $(b).data("organization").records)?self.sortDirection:-1*self.sortDirection;
	});
	this.sortDirection *= -1;
}

MintOAIOrganizations.prototype.sortByDate = function() {
	var self = this;
	this.organizations.find(".organization").sortElements(function(a, b) {
		var dateA = Date.parseExact($(a).data("organization").latestPublicationDate, "dd/MM/yyyy HH:mm:ss");
		var dateB = Date.parseExact($(b).data("organization").latestPublicationDate, "dd/MM/yyyy HH:mm:ss");
		console.log(dateA, dateB, dateA < dateB);
		return (dateA < dateB)?self.sortDirection:-1 * self.sortDirection;
	});
	this.sortDirection *= -1;
}

//Organization statistics

function MintOAIOrganizationStatistics(projectId, organizationId, id) {
	var self = this;

	if(id instanceof jQuery) {
		this.id = "organization-statistics";
		this.container = id;
	} else {
		this.id = id;
		this.container = $("#" + id);		
	}
	
	this.projectId = projectId;
	this.organizationId = organizationId;
	
	// header
	this.container.empty();
	$("<legend>").text("Publication History").appendTo(this.container);

	
	// loading page
	this.loading = $("<div>").attr("id", id + "-loading").appendTo(this.container);
//	$("<div>").addClass("progress progress-striped active").appendTo(this.loading).append($("<div>").addClass("bar").css("width", "100%"));
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	
	this.refresh();
}


function MintOAIOrganizationTest(projectId, organizationId, id) {
	var self = this;

	if(id instanceof jQuery) {
		this.id = "organization-testing";
		this.container = id;
	} else {
		this.id = id;
		this.container = $("#" + id);		
	}
	
	this.projectId = projectId;
	this.organizationId = organizationId;
	
	// header
	this.container.empty();
	$("<legend>").text("Oai Status").appendTo(this.container);

	
	// loading page
	this.loading = $("<div>").attr("id", id + "-loading").appendTo(this.container);
//	$("<div>").addClass("progress progress-striped active").appendTo(this.loading).append($("<div>").addClass("bar").css("width", "100%"));
	$("<div>").addClass("spinner").appendTo(this.loading);
	
	// contents
	this.contents = $("<div>").appendTo(this.container);
	
	this.refresh();
}


MintOAIOrganizationTest.prototype.refresh = function() {
	var self = this;
	
	this.loading.show();
	this.contents.hide();
	
	var baseUrl = "/manager/projects/" + this.projectId + "/organizations/" + this.organizationId;
	var oaiUrl = "/"+this.projectId +"/oai?verb=ListRecords&set="  + this.organizationId;

	$.get(baseUrl + "/metadata", function(metadata) {
		self.organization = metadata;
		
	

		$.get(baseUrl + "/orgcounts", function(orgcounts) {
			self.loading.hide();
			self.contents.show();
			if(orgcounts.counts != undefined) {
				var rows = [];
				for(var i in orgcounts.counts) {
					var orgcount = orgcounts.counts[i];
					var row = [];
					var url = oaiUrl + "&metadataPrefix=" + orgcount.prefix; 
					var text = " "+orgcount.prefix+" ";
					var link = "<a href="+url+" target=_blank title='View Records'>"+text+"</a>";
					row.push(link);
				//	row.push(orgcount.prefix);
					row.push(orgcount.count);
					rows.push(row);
				}
				
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', 'Namespace');
		        data.addColumn('number', 'Published records');
		//	data.addColumn('string','Show records');
//		        var dateFormatter = new google.visualization.DateFormat({ pattern: "DD/MM/yyy hh:mm:ss" });
//		        dateFormatter.format(data, 0);
		        
		        data.addRows(rows);
		        var table = new google.visualization.Table(self.contents[0]);
		        table.draw(data, { showRowNumber: false, sortColumn: 0, sortAscending: false , allowHtml: true});
			}			
		});
	});
	
}

MintOAIOrganizationStatistics.prototype.refresh = function() {
	var self = this;
	
	this.loading.show();
	this.contents.hide();
	
	var baseUrl = "/manager/projects/" + this.projectId + "/organizations/" + this.organizationId;
	
	$.get(baseUrl + "/metadata", function(metadata) {
		self.organization = metadata;
		
		$.get(baseUrl + "/reports", function(reports) {
			self.loading.hide();
			self.contents.show();

			if(reports.reports != undefined) {
				var rows = [];
				
				for(var i in reports.reports) {
					var report = reports.reports[i];
					var row = [];
					var date = new Date();
					date.setTime(report.datestamp);
					row.push(date);
					row.push(report.insertedRecords);
					row.push(report.conflictedRecords);
					rows.push(row);
				}
				
		        var data = new google.visualization.DataTable();
		        
		        data.addColumn('datetime', 'Date');
		        data.addColumn('number', 'Published records');
		        data.addColumn('number', 'Conflicts');
		        
		        var dateFormatter = new google.visualization.DateFormat({ pattern: "DD/MM/yyy hh:mm:ss" });
		        dateFormatter.format(data, 0);
		        
		        data.addRows(rows);
	
		        var table = new google.visualization.Table(self.contents[0]);
		        table.draw(data, { showRowNumber: true, sortColumn: 0, sortAscending: false });
			}			
		});
	});
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
