$(document).on("click", "#userlogin", function() {
	var email = $('#txtEmail').val();
	var pwd = $('#txtPwd').val();
	
	if(localStorage){
	localStorage.setItem("loggedin", "loginval");
	
	 }
	$.ajax({
		url : '/Ecommerce?serviceId=login',
		type: 'POST',
		data : {
			email : email,password:pwd
		},
		success : function(responseText) {
			console.log(responseText);
			
			var obj = jQuery.parseJSON(responseText);
			console.log(obj);
			//alert(obj[0].data[0].success);
			
			//alert(obj[0].success[0].success);
			//alert(userid);
			if(obj[0].success[0].success == "success")
			{
				var userid = obj[0].data[0].userid;
				//$('#hidid').val(userid);
				window.location.href = "/home.html?userid="+userid;
				}
				
				
				else{
					//alert(obj[0].success[0].message);$('#txtEmail').val("");$('#txtPwd').val("");
				window.prompt("sometext","defaultText");
				}
					
		}
		
	});
	
});

$(document).on("click", "#adminlogin", function() {
	var email = $('#txtEmail').val();
	var pwd = $('#txtPwd').val();
	$.ajax({
		url : '/Ecommerce?serviceId=adminlogin',
		type: 'POST',
		data : {
			email : email,password:pwd
		},
		success : function(responseText) {
			console.log(responseText);
			var obj = jQuery.parseJSON(responseText);
			console.log(obj);
			//alert(obj[0].data[0].success);
			var userid = obj[0].data[0].userid;
			//alert(userid);
			if(obj[0].success[0].success == "success")
			{
				//$('#hidid').val(userid);
				window.location.href = "/addproduct.html";
				}
				
				else{alert(obj[0].success[0].message);$('#txtEmail').val("");$('#txtPwd').val("");}
					
		}
	});
});
