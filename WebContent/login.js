$(document).on("click", "#userlogin", function() {
	var email = $('#txtEmail').val();
	var pwd = $('#txtPwd').val();
	$.ajax({
		url : 'http://localhost:8080/Ecommerce/Ecommerce?serviceId=login',
		type: 'POST',
		data : {
			email : email,password:pwd
		},
		success : function(responseText) {
			var obj = jQuery.parseJSON(responseText);
			console.log(obj);
			//alert(obj[0].data[0].success);
			var userid = obj[0].data[0].userid;
			if(obj[0].success[0].success == "success"){window.location.href = "http://localhost:8080/Ecommerce/home.html?userid="+userid;}
				
				else{alert("Wrong Login Credientials");$('#txtEmail').val("");$('#txtPwd').val("");}
					
		}
	});
});