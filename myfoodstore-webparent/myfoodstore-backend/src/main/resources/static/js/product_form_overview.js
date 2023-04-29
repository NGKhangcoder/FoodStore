
const dropdownCuisine = $("#cuisine");
const dropdownCategory = $("#category");


$(document).ready(function () {

    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownCuisine.change(function () {
        dropdownCategory.empty();
        getCategories();
    })
    getCategoriesForNewForm();
});

function getCategoriesForNewForm() {
    cateIdField = $("#categoryId");
    editMode = false;
    if (cateIdField.length) {
        editMode = true;
    }

    if (!editMode) getCategories();
}

function getCategories() {
    cuisineId = dropdownCuisine.val();
    url = cuisineModuleURL + "/" + cuisineId + "/categories";
    $.get(url, function (responseJson) {
        $.each(responseJson, function (index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategory);
        });
    });

}


function checkUnique(form) {
    productId = $("#id").val();
    productName = $("#name").val();

    csrfValue = $("input[name='_csrf']").val();

    params = { id: productId, name: productName, _csrf: csrfValue };

    $.post(checkUniqueUrl, params, function (response) {
        if (response == "OK") {
            form.submit();
        } else if (response == "Duplicate") {
            showWarningModal("There is another product having the name " + productName);
        } else {
            showErrorModal("Unknown response from server");
        }

    }).fail(function () {
        showErrorModal("Could not connect to the server");
    });

    return false;
}

function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
}