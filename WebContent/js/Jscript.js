$(document).ready(function (){
	
	//showuserinfo();
	//loadProducts();
	
//	if($('#hidid').val() != "")
//	{
//		showuserinfo();
//		loadProducts();
//		}
//	else
//	{
//		window.location.href = "https://localhost:8080/Ecommerce/userlogin.html";
//		}
//	
});

$(document).on("click", "#userreg",function userReg(){
	var fname=$('#txtFname').val();
	var lname=$('#txtlname').val();
	var email=$('#txtEmail').val();
	var mobile=$('#txtPwd').val();
	var password=$('#txtMobile').val();
	var address1=$('#txtAdd1').val();
	var address2=$('#txtAdd2').val();
	var city=$('#txtCity').val();
	var state=$('#txtState').val();
	var country=$('#txtCountry').val();
	var zipcode=$('#txtZipcode').val();
	var landmark=$('#txtLadmark').val();
	var pwd = $('#txtPwd').val();
	var cfrmpwd = $('#txtCfrmPwd').val();
	var image = $('#photo').val();
	if($('#rdMale').is(':checked'))
	 {
	 	var gender = "Male";
	 }
	 else
	 {
	 	var gender = "Female";
	 }
	if(pwd == cfrmpwd)
	{
		$.ajax({
			url : '/Ecommerce?serviceId=userreg',
			type: 'POST',
			data : {
				fname:fname,lname:lname,email:email,password:pwd,mobile:mobile,address1:address1,
				address2:address2,city:city,state:state,country:country,zipcode:zipcode,landmark:landmark,
				gender:gender,image:image
			},
			success : function(responseText) {
				console.log(responseText);
				var obj = jQuery.parseJSON(responseText);
				console.log(obj);
				if(obj[0].success == "success"){window.location.href = "/userlogin.html";}
				
					else{alert(obj.message);}
						
			}
		});
	}
	else
	 {
		 alert("Password Not Matching");
	 }
	
});

$(document).on("click", "#addproduct",function userReg(){
	var pname=$('#txtPname').val();
	var pcount=$('#txtPcount').val();
	var bname=$('#txtBname').val();
	var price=$('#txtPrice').val();
		$.ajax({
		url : '/Ecommerce?serviceId=addproduct',
		type: 'POST',
		data : {
			productname:pname,stock:pcount,brand:bname,price:price
		},
		success : function(responseText) {
			var obj = jQuery.parseJSON(responseText);
			console.log(obj);
			if(obj == "success"){alert("Product Added");}
			
				else{alert("Try Again");}
					
		}
	});
});



$(document).on("click", "#btn_addtocart", function() {
	//var productid = $('input[name=product]:checked').attr('id'); 
	var productid = $('#hidPrdId').val();
	var quantity = $('#txtgetQty').val();
	var price = $('#showPrdPrice').text();
	var amount = quantity * price;
	var userid = $('#hidid').val();//alert(productid);
	$.ajax({
		url : '/Ecommerce?serviceId=addtocart',
		type: 'POST',
		data : {
			userid :userid,productid:productid,quantity:quantity,amount:amount,
		},
		success : function(responseText) {
			var obj = jQuery.parseJSON(responseText);
			alert(obj[0].success[0].message);
			$('#divcontent').show();
			$('#userdiv').hide();
			$('#changepwd').hide();
			$('#prdndesc').hide();
					
		}
	});
});

$(document).on("click", "#placeorder", function() {
	//alert("mani");
	var shippingid="1";
	var userid = $('#hidid').val();
	var data="";
	jsonObj=[];
	var selected = [];
	$('#mycartdata input:checked').each(function() {
		selected.push($(this).attr('id'));
	});
	$.each(selected, function (index, value) {
		  console.log(value);
		var product_cart_id = value;
		//alert(product_cart_id);
		var split_ids = product_cart_id.split("_");
		var product_id = split_ids[0];
		var cart_id = split_ids[1];
		var qty_id = "#qty_"+product_id;
		var amt_id = "#amt_"+product_id;
		var price =$(amt_id).text();
		var quantity = $(qty_id).text();
		var totalamount =  $('#txtTotal').val();
		data={};
		data["productid"]= product_id;
		data["cartid"]= cart_id;
		data["quantity"]= quantity;
		data["price"]= price;
		data["totalamount"]= totalamount;
		data["shippingid"]= shippingid;
		data["userid"]= userid;
		 jsonObj.push(data);
	});
		var jsonString = JSON.stringify(jsonObj);
		console.log(jsonString);
		console.log(selected);
		
	
	$.ajax({
		url : '/Ecommerce?serviceId=orders',
		type: 'POST',
		data : {
			data:jsonString
		},
		success : function(responseText) {
			if(responseText == "success"){
				alert("order placed");
				
				}
				
				else{alert("try again");}
					
		}
	});
});
$(document).on("click", "#td_myorders", function() {
	$('#divhome').hide();
	$('#product_list').hide();
	$('#my_carts').hide();
	$('#my_orders').show();
	var userid = $('#hidid').val(); 
	$.ajax({
		url : '/Ecommerce?serviceId=myorders',
		type: 'POST',
		data : {
			userid:userid
		},
		success : function(responseText) {
			console.log(responseText);
			var obj = jQuery.parseJSON(responseText);
			//alert(obj.length);
			//alert(obj[0].data.length);
			//alert(obj[0].data[0].productname);
			var orderTable="<table width='100%'><tr class='tbl_header'><td>Order No</td><td>Product Name</td><td>Price</td><td>Date</td><td>Quantity</td><td>Amount</td>";
			for(var i=0;i<obj[0].data.length;i++)
			{
				if(i%2 == 0)
				{
					orderTable = orderTable + "<tr class='tbl_even_row'><td>"+obj[0].data[i].orderid+"</td><td>"+obj[0].data[i].productname+"</td>" +
					"<td>"+obj[0].data[i].price+"</td><td>"+obj[0].data[i].orderdate+"</td><td>"+obj[0].data[i].productquantity+"</td><td>"+obj[0].data[i].totalamount+"</td></tr>";
					}
				else
				{
					orderTable = orderTable + "<tr class='tbl_odd_row'><td>"+obj[0].data[i].orderid+"</td><td>"+obj[0].data[i].productname+"</td>" +
					"<td>"+obj[0].data[i].price+"</td><td>"+obj[0].data[i].orderdate+"</td><td>"+obj[0].data[i].productquantity+"</td><td>"+obj[0].data[i].totalamount+"</td></tr>";
					}
				
				}
			orderTable = orderTable + "</table>";
			$('#my_orders').empty();
			$('#my_orders').append(orderTable);
								
		}
	});
});

$(document).on("click", "#btnSave", function() {
	var pwd = $('#txtPwd').val();
	var cfrmpwd = $('#txtCfrmPwd').val();
	var userid = $('#hidid').val();
	$.ajax({
		url : '/Ecommerce?serviceId=changepwd',
		type: 'POST',
		data : {
			userid : userid,pwd:pwd
		},
		success : function(responseText) {
			var obj = jQuery.parseJSON(responseText);
			
			if(obj[0].success == "success"){
				alert("Password Changed Successfully");
				$('#txtPwd').val("");
				$('#txtCfrmPwd').val("");
				$('#divcontent').hide();
				$('#userdiv').show();
				$('#changepwd').hide();
				}
				else{alert("try again");}
					
		}
	});
});

$(document).on("click", "#td_products", function() {
	loadProducts();
});
$(document).on("click","#username",function(){
	$('#divcontent').hide();
	$('#userdiv').show();
	$('#changepwd').hide();
	$('#mycartdata').hide();
})
$(document).on("click","#btnBack",function(){
	$('#divcontent').show();
	$('#userdiv').hide();
	$('#changepwd').hide();
	$('#mycartdata').hide();
})
$(document).on("click","#btnChngPwd",function(){
	$('#divcontent').hide();
	$('#userdiv').hide();
	$('#changepwd').show();
	$('#mycartdata').hide();
})

$(document).on("click","#btnUser",function(){
	$('#divcontent').hide();
	$('#userdiv').show();
	$('#changepwd').hide();
	$('#mycartdata').hide();
})
$(document).on("click","#td_home",function(){
	$('#divhome').show();
	$('#product_list').hide();
	$('#my_orders').hide();
})
$(document).on("click","#divlogout",function(){
	
	$('#hidid').val("");
	window.location.href = "/userlogin.html";
})

$(document).on("click","#btn_backpd",function(){
	$('#divcontent').show();
	$('#userdiv').hide();
	$('#changepwd').hide();
	$('#prdndesc').hide();
})
$(document).on("click","#btn_backcart",function(){
	$('#divcontent').show();
	$('#userdiv').hide();
	$('#changepwd').hide();
	$('#prdndesc').hide();
	$('#mycartdata').hide();
	$('#product_list').show();
})

function loadProducts()
{
	$('#divcontent').show();
	$('#divhome').hide();
	$('#product_list').show();
	$('#my_carts').hide();
	$('#my_orders').hide();
	$('#prdndesc').hide();
	$('#mycartdata').hide();
	$.ajax({
		url : '/Ecommerce?serviceId=show_products',
		type: 'POST',
		data : {
			
		},
		success : function(responseText) {
			console.log(responseText);
			var obj = jQuery.parseJSON(responseText);
			alert('hi');
			alert(responseText);
			alert(obj[0].data.length);
			alert(obj[0].data[0].productname);
			var productTable="<table class='prdtable' width='100%'><tr class='tbl_header' style='height:30px;'><td>Select</td><td>Brand</td><td>Product Name</td><td> Price</td></tr>";
			for(var i=0;i<obj[0].data.length;i++)
			{
				var pr_id = "pr_"+obj[0].data[i].productid;
//<<<<<<< HEAD
				//var filename = "images/"+obj[0].data[i].filename;

				//var filename = "WebContent/images/"+obj[0].data[i].filename;
				//var filename = " https://git.heroku.com/absyzecommerceportal.git/WebContent/images/"+obj[0].data[i].filename;
				var filename = "images/"+obj[0].data[i].filename;;
				console.log(filename);
				alert(filename);
//>>>>>>> 3936b108a5b59962890db28e92fa04e5f4ac63a7
				//alert(amt_id);
				//productTable = productTable + "<tr style='height:100px;'><td><input name='product' type ='radio' id="+obj[0].data[i].productid+" onclick='showproduct(this.id);'/></td><td>"+obj[0].data[i].brandname+"</td><td>"+obj[0].data[i].productname+"</td><td id="+pr_id+">"+obj[0].data[i].price+"</td>" +
				if(i%2 == 0)
				{
					productTable = productTable + "<tr class='tbl_even_row' style='height:100px;'><td><img id="+obj[0].data[i].productid+" src="+filename+" height='100' width='100' onclick='showproduct(this.id);'/></td><td>"+obj[0].data[i].brandname+"</td><td>"+obj[0].data[i].productname+"</td><td id="+pr_id+">"+obj[0].data[i].price+"</td>" +
					"</tr>";
					}
				else
				{
					productTable = productTable + "<tr class='tbl_odd_row' style='height:100px;'><td><img id="+obj[0].data[i].productid+" src="+filename+" height='100' width='100' onclick='showproduct(this.id);'/></td><td>"+obj[0].data[i].brandname+"</td><td>"+obj[0].data[i].productname+"</td><td id="+pr_id+">"+obj[0].data[i].price+"</td>" +
					"</tr>";
					}
				
				}
			productTable = productTable + "</table>";
			alert(productTable);
			$('#product_list').empty();
			$('#product_list').append(productTable);
		}
	});
}
function showproduct(prdid)
{
	alert("showproduct");
	alert(prdid);
	alert("hi");
	$('#divcontent').hide();
	$('#userdiv').hide();
	$('#changepwd').hide();
	$('#prdndesc').show();
	
	$.ajax({
		url : '/Ecommerce?serviceId=show_productinfo',
		type: 'POST',
		data : {
			prodid:prdid,
		},
		success : function(responseText) {
			console.log(responseText);
			var obj = jQuery.parseJSON(responseText);
			alert(responseText);
			alert(obj[0].data.length);
			alert(obj[0].data[0].productname);
			//	$('#menu1').show();
				$('#product_list').show();
					
			$('#showPrdName').text(obj[0].data[0].productname);
			$('#showBrand').text(obj[0].data[0].brandname);
			$('#showPrdPrice').text(obj[0].data[0].price);
			$('#hidPrdId').val("");
			$('#hidPrdId').val(obj[0].data[0].productid);
			//$('#imgid').html("<img  alt='product' src='2017-10-06.jpg' width='250px' height='250px'>");
		}
	});
}
function showuserinfo()
{
	var searchParams = new URLSearchParams(window.location.search); //?anything=123
	var userid = searchParams.get("userid");
	console.log(userid);
	$('#divcontent').show();
	$('#userdiv').hide();
	$('#changepwd').hide();
	$('#prdndesc').hide();
	$.ajax({
		url : '/Ecommerce?serviceId=showuser',
		type: 'POST',
		data : {
			userid:userid,
		},
		success : function(responseText) {
			
			console.log(responseText);
			console.log('Showuserinfo'+responseText);
			var obj = jQuery.parseJSON(responseText);
			//alert(obj.length);
			console.log(obj);
			//alert(obj[0].data[0].zipcode);
			if(obj[0].success[0].success == "success"){
				$('#username').text('Welcome '+obj[0].data[0].firstname);
				var usertable="<table border='1' width='100%'><tr class='tbl_header'><td colspan='4'>User Info</td></tr>";
				usertable = usertable + "<tr><td class='tbl_header'>profilepic</td><td><img src="WebContent/images/dell_laptop.jpg" alt="Girl in a jacket" width="500" height="600"></td>";
				usertable = usertable + "<tr><td class='tbl_header'>Firstname</td><td>"+obj[0].data[0].firstname+"</td><td class='tbl_header'>Lastname</td><td>"+obj[0].data[0].lastname+"</td></tr>";
				usertable = usertable + "<tr><td class='tbl_header'>Email</td><td>"+obj[0].data[0].email+"</td><td class='tbl_header'>Mobile</td><td>"+obj[0].data[0].mobile+"</td></tr>";		
				usertable = usertable + "<tr><td class='tbl_header'>Address1</td><td>"+obj[0].data[0].address1+"</td><td class='tbl_header'>Address2</td><td>"+obj[0].data[0].address2+"</td></tr>";
				usertable = usertable + "<tr><td class='tbl_header'>City</td><td>"+obj[0].data[0].city+"</td><td class='tbl_header'>State</td><td>"+obj[0].data[0].state+"</td></tr>";
				usertable = usertable + "<tr><td class='tbl_header'>Country</td><td>"+obj[0].data[0].country+"</td><td class='tbl_header'>Zipcode</td><td>"+obj[0].data[0].zipcode+"</td></tr>";
								
				usertable = usertable + "<tr><td colspan='4' align='center'><input type='button' id='btnBack' value='Back' /><input type='button' id='btnChngPwd' value='Change Password' /></td></tr></table>";
				
				$('#menu5').empty();
				$('#menu5').append(usertable);
				$('#hidid').val(userid);
			}
			else{
				alert("Wrong Login Credientials");$('#txtEmail').val("");$('#txtPwd').val("");
			}
			
		}
	});
	}

$(document).on("click", "#td_mycarts", function() {
	
	var userid=$('#hidid').val();
	$.ajax({
		url : '/Ecommerce?serviceId=mycarts',
		type: 'POST',
		data : {
			userid:userid,
		},
		success : function(responseText) {
			console.log(responseText);
			var obj = jQuery.parseJSON(responseText);
			if(obj[0].success[0].success == "success")
			{
				//alert(obj[0].success[0].success);
				//alert(obj.length);
				//alert(obj[0].data.length);
				//alert(obj[0].data[0].productname);
				var cartTable="<table width='100%' border='1'><tr class='tbl_header'><td colspan='5'>My Carts Info</td></tr><tr class='tbl_header'><td>Select</td><td>Product Name</td><td>Quantity</td><td>Amount</td><td>Remove Item</td></tr>";
				for(var i=0;i<obj[0].data.length;i++)
				{
					var qty_id = "qty_"+ obj[0].data[i].productid;
					var amt_id = "amt_"+ obj[0].data[i].productid;
					var rm_id = "rm_"+ obj[0].data[i].cartid;
					var chk_id = obj[0].data[i].productid + "_" + obj[0].data[i].cartid;
					if(i%2 == 0)
					{
						cartTable = cartTable + "<tr class='tbl_even_row'><td><input name='cart' type ='checkbox' id="+chk_id+" onclick='add_totalamount(this.id)' /></td><td>"+obj[0].data[i].productname+"</td>" +
						"<td id="+qty_id+">"+obj[0].data[i].quantity+"</td><td id="+amt_id+">"+obj[0].data[i].amount+"</td><td id="+rm_id+" onclick='delete_cartitem(this.id)'>Remove</td>" +
						"</tr>";
						}
					else
					{
						cartTable = cartTable + "<tr class='tbl_odd_row'><td><input name='cart' type ='checkbox' id="+chk_id+" onclick='add_totalamount(this.id)' /></td><td>"+obj[0].data[i].productname+"</td>" +
						"<td id="+qty_id+">"+obj[0].data[i].quantity+"</td><td id="+amt_id+">"+obj[0].data[i].amount+"</td><td id="+rm_id+" onclick='delete_cartitem(this.id)'>Remove</td>" +
						"</tr>";
						}
					
					}
				cartTable = cartTable + "<tr class='tbl_even_row'><td colspan='2'>Total Amount</td><td colspan='3'><input type='text' id='txtTotal' value ='0' disabled height='40'/></td><tr><td colspan='5' align='center'><input type='button' id='placeorder' value='Place Order' ><input type='button' id='btn_backcart' value='Back' ></td></tr></table>";
				$('#menu3').empty();
				$('#menu3').append(cartTable);
				
			}
			else
			{
				alert(obj[0].success[0].message);
				loadProducts();
			}
			
								
		}
	});
});
function delete_cartitem(cartid)
{
	$('#divcontent').hide();
	$('#divhome').hide();
	$('#product_list').hide();
	$('#my_orders').hide();
	$('#prdndesc').hide();
	$('#userdiv').hide();
	$('#mycartdata').show();
	var cart_id = cartid.split("_");
	//alert(cartid);alert(cart_id[1]);
	var userid=$('#hidid').val();
	$.ajax({
		url : '/Ecommerce?serviceId=deletecart',
		type: 'POST',
		data : {
			userid:userid,cartid:cart_id[1]
		},
		success : function(responseText) {
			console.log(responseText);
			if(obj[0].success[0].success == "success")
			{
			var obj = jQuery.parseJSON(responseText);
			//alert(obj.length);
			//alert(obj[0].data.length);
			//alert(obj[0].data[0].productname);
			var cartTable="<table width='100%' border='1'><tr class='tbl_header'><td colspan='5'>My Carts Info</td></tr><tr class='tbl_header'><td>Select</td><td>Product Name</td><td>Quantity</td><td>Amount</td><td>Remove Item</td></tr>";
			for(var i=0;i<obj[0].data.length;i++)
			{
				var qty_id = "qty_"+ obj[0].data[i].productid;
				var amt_id = "amt_"+ obj[0].data[i].productid;
				var rm_id = "rm_"+ obj[0].data[i].cartid;
				var chk_id = obj[0].data[i].productid + "_" + obj[0].data[i].cartid;
				if(i%2 == 0)
				{
					cartTable = cartTable + "<tr class='tbl_even_row'><td><input name='cart' type ='checkbox' id="+chk_id+" onclick='add_totalamount(this.id)' /></td><td>"+obj[0].data[i].productname+"</td>" +
					"<td id="+qty_id+">"+obj[0].data[i].quantity+"</td><td id="+amt_id+">"+obj[0].data[i].amount+"</td><td id="+rm_id+" onclick='delete_cartitem(this.id)'>Remove</td>" +
					"</tr>";
					}
				else
				{
					cartTable = cartTable + "<tr class='tbl_odd_row'><td><input name='cart' type ='checkbox' id="+chk_id+" onclick='add_totalamount(this.id)' /></td><td>"+obj[0].data[i].productname+"</td>" +
					"<td id="+qty_id+">"+obj[0].data[i].quantity+"</td><td id="+amt_id+">"+obj[0].data[i].amount+"</td><td id="+rm_id+" onclick='delete_cartitem(this.id)'>Remove</td>" +
					"</tr>";
					}
				
				}
			cartTable = cartTable + "<tr class='tbl_even_row'><td colspan='2'>Total Amount</td><td colspan='3'><input type='text' id='txtTotal' value ='0' disabled height='40'/></td><tr><td colspan='5' align='center'><input type='button' id='placeorder' value='Place Order' ><input type='button' id='btn_backcart' value='Back' ></td></tr></table>";
			$('#menu3').empty();
			$('#menu3').append(cartTable);
			
		}
		else
		{
			//alert(obj[0].success[0].message);
			loadProducts();
		}
								
		}
	});
}
function showrow()
{
	$('.products').hide();
}
function add_totalamount(id)
{
	//alert(id);
	//alert($("#"+id).is(':checked'));
	var product_ids = id.split("_");
	var product_id = product_ids[0];
	var amt_id = "#amt_"+product_id;
	var qty_id = "#qty_"+product_id;
	var total_amount=0;
	var prod_amount = 0;
	if($("#"+id).is(':checked')){
		
		prod_amount = parseInt($(amt_id).html());
		total_amount = parseInt($('#txtTotal').val()) + prod_amount;
	}
	else
		{
			prod_amount = parseInt($(amt_id).html()) * parseInt($(qty_id).html());
			total_amount = parseInt($('#txtTotal').val()) - prod_amount;
		}
	
	$('#txtTotal').val("");
	$('#txtTotal').val(total_amount);
}


