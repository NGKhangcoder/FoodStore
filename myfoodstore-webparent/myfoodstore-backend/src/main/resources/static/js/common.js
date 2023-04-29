$(document).ready(function() {
	$("#logoutLink").on("click", function(e) {
		e.preventDefault();
		document.logoutForm.submit();//<form th:action="@{/logout}" method="post" name="logoutForm"/>
	});

	  
            searchInput = $(".search-box input");
            inputGroup = $(".search-box .input-group");
            boxWidth = inputGroup.width();
            searchInput.focus(function () {
                inputGroup.animate({
                    width: "300"
                });
            }).blur(function () {
                inputGroup.animate({
                    width: boxWidth
                });
            });

	$(".passwordShowOffIcon").on("click",function(e){
		
		if($(this).hasClass("fa-eye")){
			$(".passwordInput").attr("type","text");
			$(this).removeClass("fa-eye");
			$(this).addClass("fa-eye-slash");
			
		}else{
			$(".passwordInput").attr("type","password");
			$(this).addClass("fa-eye");
			$(this).removeClass("fa-eye-slash");
		}
	});
	
	customizeDropDownMenu();

});

function customizeDropDownMenu() {
	$(".navbar .dropdown").hover(//lấy ra tất cả thẻ có class .dropdown nằm trong thẻ có class .navbar
		function() {
			$(this).find('.dropdown-menu').first().stop(true, true).delay(250).slideDown();//tìm thẻ con đầu tiên bên trong có class là dropdown-menu và kéo xuống
		},
		function() {
			$(this).find('.dropdown-menu').first().stop(true, true).delay(100).slideUp();//tìm thẻ con đầu tiên bên trong có class là dropdown-menu và kéo lên
		}
	);
	
	$(".dropdown > a").click(function() {
		location.href = this.href;
	});
}