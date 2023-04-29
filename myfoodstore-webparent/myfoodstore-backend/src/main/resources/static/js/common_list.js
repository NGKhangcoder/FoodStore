


function showDeleteConfirmModal(link,entityName){
	entityId = link.attr("entityId");

	$("#yesButton").attr("href",link.attr("href"));
	$("#confirmText").text("Are you sure want to delete this " + entityName + " with Id: " + entityId);
	$("#confirmModal").modal();
}

function clearFilter() {
	window.location = moduleURL;
}
