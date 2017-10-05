$(document).on("click", "#userlogin", function() {
	var email = $('#txtEmail').val();
	var pwd = $('#txtPwd').val();
	alert(email+","+pwd);
	$.ajax({
		url : '/userlogin.html/Ecommerce?serviceId=login',
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
			//alert(obj[0].success[0].success);
			//alert(userid);
			if(obj[0].success[0].success == "success")
			{
				//$('#hidid').val(userid);
				window.location.href = "https://absyz.herokuapp.com/home.html?userid="+userid;
				}
				
				
				else{
					alert(obj[0].success[0].message);$('#txtEmail').val("");$('#txtPwd').val("");}
					
		}
		
	});
	
});

$(document).on("click", "#adminlogin", function() {
	var email = $('#txtEmail').val();
	var pwd = $('#txtPwd').val();
	$.ajax({
		url : 'https://absyz.herokuapp.com?serviceId=adminlogin',
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
				window.location.href = "https://absyz.herokuapp.com/addproduct.html";
				}
				
				else{alert(obj[0].success[0].message);$('#txtEmail').val("");$('#txtPwd').val("");}
					
		}
	});
});
