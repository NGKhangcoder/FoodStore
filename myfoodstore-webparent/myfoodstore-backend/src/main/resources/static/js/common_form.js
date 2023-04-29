$(document).ready(function () {
	$("#buttonCancel").on("click", function () {
		window.location = moduleURL;//window.location dùng để gọi đến 1 @GetMapping bất kỳ
	});

	$("#fileImage").on("change", function () {
		if (!checkFileSize(this)) {

			return;
		}

		showImage(this);
	});
});

function checkFileSize(fileInput) {

	fileSize = fileInput.files[0].size;

	if (fileSize > MAX_FILE_SIZE) {
		fileInput.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + " bytes!");

		return false;
	} else {
		fileInput.setCustomValidity("");

		return true;
	}

}

function showImage(fileInput) {
	let file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function (e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}

function showErrorModal(message) {//modal hiển thị lỗi
	showModalDialog("Error", message);
}

function showWarningModal(message) {//modal hiển thị cảnh báo
	showModalDialog("Warning", message);
}