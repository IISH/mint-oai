function MintOAIAdministration(projects, detail) {
	var self = this;
	this.projectsContainer = $("#" + projects);
	this.detailsContainer = $("#" + detail);
	
	this.projects = new MintOAIProjects(projects, {
		showStatistics: false,
		click: function(project) {
			self.showProjectEditForm(project, self.detailsContainer);
		}
	});
}

MintOAIAdministration.prototype.showProjectEditForm = function(project, container) {
	var self = this;
	
	container.empty();
	container.append($("<legend>").text("Project Id: " + project.projectName));
	var dl = $("<dl>").addClass("dl-horizontal list").appendTo(container);
	
	console.log(project);
	
	project = $.extend({}, {
		title: "",
		description: "",
		mintVersion: "mint1",
		mintURL: "",
	}, project);
	
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
	  		