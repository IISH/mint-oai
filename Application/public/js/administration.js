function MintOAIAdministration(projects, main) {
	var self = this;
	this.projectsContainer = $("#" + projects);
	this.mainContainer = $("#" + main);
	this.mainContainer.empty();
	
	this.tabsContainer = $("<div>").appendTo(this.mainContainer);
	this.tabsContainer.empty();
	this.legend = $("<legend>").appendTo(this.tabsContainer);
	var ul = $("<ul>").appendTo(this.tabsContainer).addClass("nav nav-tabs");
	$("<li>").appendTo(ul).append($("<a>").attr("data-toggle", "tab").attr("href", "#administration-details").text("Details"));
	$("<li>").appendTo(ul).append($("<a>").attr("data-toggle", "tab").attr("href", "#administration-organizations").text("Organizations"));
	
	this.tabsContainer.find("a").click(function (e) {
		console.log(this);
		e.preventDefault();
		$(this).tab('show');
	});
	
	var tabs = $("<div>").addClass("tab-content").appendTo(this.tabsContainer);
	this.detailsContainer = $("<div>").addClass("tab-pane active").attr("id", "administration-details").appendTo(tabs);
	this.organizationsContainer = $("<div>").addClass("tab-pane").attr("id", "administration-organizations").appendTo(tabs).text("ORgs");
	this.tabsContainer.find("a:first").tab('show');
	this.tabsContainer.hide();
	
	this.projects = new MintOAIProjects(projects, {
		showStatistics: false,
		click: function(project) {
			self.legend.text("Project Id: " + project.projectName);
			self.tabsContainer.show();
			self.showProjectEditForm(project, self.detailsContainer);
		}
	});
}

MintOAIAdministration.prototype.showProjectEditForm = function(project, container) {
	var self = this;
	container.empty();
	
	project = $.extend({}, {
		title: "",
		description: "",
		mintVersion: "mint1",
		mintURL: "",
	}, project);
	
	var dl = $("<dl>").addClass("dl-horizontal list").appendTo(container);
	var title = $("<input>").attr("id", "project-title").val(project.title);
	var description = $("<textarea>").attr("id", "project-description").text(project.description);
	var version = $("<select>").attr("id", "project-mint-version");
	$("<option>").appendTo(version).attr("value", "mint1").text("Mint 1");
	$("<option>").appendTo(version).attr("value", "mint2").text("Mint 2");
	version.val(project.mintVersion);
	var url = $("<input>").attr("id", "project-mint-url").val(project.mintURL);

	dl.append($("<dt>").text("title"));
	dl.append($("<dd>").append(title));
	dl.append($("<dt>").text("description"));
	dl.append($("<dd>").append(description));
	dl.append($("<dt>").text("mint version"));
	dl.append($("<dd>").append(version));
	dl.append($("<dt>").text("mint URL"));
	dl.append($("<dd>").append(url));
	
	var buttons = $("<div>").appendTo(container);
	$("<button>").addClass("btn").text("Submit").appendTo(buttons).click(function() {
		var sender = $(this);
		sender.text("Submitting...");
		$.post("/admin/projects/" + project.projectName + "/update",
		{
			title: title.val(),
			description: description.val(),
			mintVersion: version.val(),
			mintURL: url.val(),
		},
		function(project) {
			self.showProjectEditForm(project, container);
			self.projects.refresh();
		}); 
	});
	$("<button>").addClass("btn").text("Reset").appendTo(buttons).click(function () {
		showProjectEditForm(project, container);
	});
}
	  		