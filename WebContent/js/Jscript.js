

$(document).ready(function (){
	
	// $('#changepwd').hide();
	
	setTimeout(function() {
    $('#changepwd').stop(true, true).hide(); 
}, 1);

	
	

}); 


//for user reg
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
//product for admin
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


//Add to cart tab
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
//Place Order Button
$(document).on("click", "#placeorder", function() {
	//alert("mani");
	var shippingid="1";
	var searchParams = new URLSearchParams(window.location.search); //?anything=123
	var userid = searchParams.get("userid");
	var data="";
	jsonObj=[];
	var selected = [];
	alert("hi");
	$('#tbl_cart input:checked').each(function() {
		selected.push($(this).attr('id'));
	});
	$.each(selected, function (index, value) {
		alert(index);

		  console.log(value);
		var product_cart_id = value;
		//alert(product_cart_id);
		alert(product_cart_id);
		var split_ids = product_cart_id.split("_");
		alert(split_ids);
		var product_id = split_ids[0];
		alert(product_id);
		var cart_id = split_ids[1];
		alert(cart_id);
		var qty_id = "#qty_"+cart_id;
		alert(qty_id);
		var amt_id = "#amt_"+product_id;
		alert(amt_id);
		var price =$(amt_id).text();
		alert(price);
		var quantity = $(qty_id).val();
		alert(quantity);	
		var totalamount =  $('#txtTotal').val();
		var label= ["Order Received", "Order Processing", "Order dispatched", "Order Shipped", "Order Delivered"];
		var counter = 0;
		var elem = document.getElementById("label"); 
		setInterval(change, 1000); 
		function change() { 
			elem.innerHTML = text[counter]; 
			counter++; 
			if (counter >= text.length) {
				counter = 0; }
		}
		data={};
		data["productid"]= product_id;
		data["cartid"]= cart_id;
		data["quantity"]= quantity;
		data["price"]= price;
		data["totalamount"]= totalamount;
		data["shippingid"]= shippingid;
		data["userid"]= userid;
		data["status"]=label; 
		 jsonObj.push(data);
	});
		var jsonString = JSON.stringify(jsonObj);
		console.log(jsonString);
	alert(jsonString);
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

//order tab
$(document).on("click", "#td_myorders", function() {
	$('#divhome').hide();
	$('#product_list').hide();
	$('#my_carts').hide();
	$('#my_orders').show();
	$('#changepwd').hide();
	var searchParams = new URLSearchParams(window.location.search); //?anything=123
	var userid = searchParams.get("userid");
	//var userid = $('#hidid').val(); 
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
			var orderTable="<table width='100%'><tr class='tile'><td><strong>Order No</strong></td><td><strong>Product Name</strong></td><td><strong>Price</strong></td><td><strong>Date</strong></td><td><strong>Quantity</strong></td><td><strong>Amount</strong></td><td><strong>Status</strong></td>";
			for(var i=0;i<obj[0].data.length;i++)
			{
				if(i%2 == 0)
				{
					orderTable = orderTable + "<tr class='tile'><td>"+obj[0].data[i].orderid+"</td><td>"+obj[0].data[i].productname+"</td>" +
					"<td>"+obj[0].data[i].price+"</td><td>"+obj[0].data[i].orderdate+"</td><td>"+obj[0].data[i].productquantity+"</td><td>"+obj[0].data[i].totalamount+"</td><td>"+obj[0].data[i].label+"</td";
					}
				else
				{
					orderTable = orderTable + "<tr class='tile'><td>"+obj[0].data[i].orderid+"</td><td>"+obj[0].data[i].productname+"</td>" +
					"<td>"+obj[0].data[i].price+"</td><td>"+obj[0].data[i].orderdate+"</td><td>"+obj[0].data[i].productquantity+"</td><td>"+obj[0].data[i].totalamount+"</td></tr>";
					}
				
				}
			orderTable = orderTable + "</table>";
			$('#my_orders').empty();
			$('#my_orders').append(orderTable);
								
		}
	});
});

//Save the changed password
$(document).on("click", "#btnSave", function() {
	var pwd = $('#txtPwd').val();
	var cfrmpwd = $('#txtCfrmPwd').val();
	var searchParams = new URLSearchParams(window.location.search); //?anything=123
	var userid = searchParams.get("userid");
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
//product tab div
$(document).on("click", "#product_list", function() {
	$('#changepwd').hide();
	loadProducts();
});
// user info tab
$(document).on("click","#username",function(){
	$('#divcontent').hide();
	$('#userdiv').show();
	$('#changepwd').hide();
	$('#mycartdata').hide();
})
//Back button on change password
$(document).on("click","#btnBack",function(){
	$('#divcontent').show();
	$('#userdiv').hide();
	$('#changepwd').hide();
	$('#mycartdata').hide();
})
//Button to change password
$(document).on("click","#btnChngPwd",function(){
	
	/*var changePwdTable = "<table class="login-screen"><tr class='tbl_header'><td colspan='2'> <h2>Change Password</h2> </td>
				</tr>";
				
				changePwdTable = changePwdTable + "<tr>
					<td>Password</td><td><input type="text" name="txtPwd" id="txtPwd" /></td>
				</tr>";
				
				changePwdTable= changePwdTable+ "<tr>
					<td>Confirm Password</td><td><input type="password" name="txtCfrmPwd" id="txtCfrmPwd" /></td>
				</tr>";
				
				changePwdTable = changePwdTable+ "<tr>
					<td><input type="button" id="btnSave" value="Save" /><input type="button" id="btnUser" value="Back" /></td>
				</tr>
			</table>"; 
	$('#menu5').append(changePwdTable);*/
	
	alert('vv');
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

	

//new codee
/*$(document).on("click","#td_home",function(){
	
	$('#divhome').show();
	$('#product_list').hide();
	$('#my_orders').hide();
	$('#changepwd').hide();
	
}) */

//new2



$(document).on("click","#td_home",function(){
	if ($("#divhome").length > 0){
alert('panel id existing');

	
	$('#divhome').show();
	$('#product_list').hide();
	$('#my_orders').hide();
	$('#changepwd').hide(); }
	
})
//logout
$(document).on("click","#divlogout",function(){
	
	$('#hidid').val("");
	window.location.href = "/userlogin.html";
}) 
//
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



function hidechangepwdH(){
	alert('home clicked');
$('#changepwd').hide();

}

function hidechangepwdCr(){
alert('carts clicked');
	$('#changepwd').hide();
}
function loadProducts()
{
	$('#divcontent').show();
	$('#divhome').hide();
	$('#product_list').show();
	$('#my_carts').hide();
	$('#my_orders').hide();
	$('#prdndesc').hide();
	$('#mycartdata').hide();
	$('#changepwd').hide();
	$.ajax({
		url : '/Ecommerce?serviceId=show_products',
		type: 'POST',
		data : {
			
		},
		success : function(responseText) {
			console.log(responseText);
			var obj = jQuery.parseJSON(responseText);
			//alert(responseText);
			//alert(obj);
			
  
			//alert(obj[0].data.length);
			//alert(obj[0].data[0].productname);
			var productTable="<table><tr class='tile' style='height:30px;'><td></td><td><strong>Brand</strong></td><td><b>Product Name</b></td></td><td><b> Price</b></td></td><td><b>select</b></td></tr>";
			for(var i=0;i<obj[0].data.length;i++)
			{
				var pr_id = "pr_"+obj[0].data[i].productid;
				var filename = "images/"+obj[0].data[i].filename;
				var pid=obj[0].data[i].productid;
				var pquantity="2";
				//var quant;
				var pprice=obj[0].data[i].price;
				//alert(amt_id);
				//productTable = productTable + "<tr style='height:100px;'><td><input name='product' type ='radio' id="+obj[0].data[i].productid+" onclick='showproduct(this.id);'/></td><td>"+obj[0].data[i].brandname+"</td><td>"+obj[0].data[i].productname+"</td><td id="+pr_id+">"+obj[0].data[i].price+"</td>" +
				if(i%2 == 0)
				{
					productTable = productTable + "<tr class='tile' style='height:100px;'><td><img id="+obj[0].data[i].productid+" src="+filename+" height='100' width='100'/></td><td>"+obj[0].data[i].brandname+"</td><td>"+obj[0].data[i].productname+"</td><td id="+pr_id+">"+obj[0].data[i].price+"</td><td><button type='button' onclick='Addtocart("+pid+","+pquantity+","+pprice+")'>Add to Cart</button></td>" +
					"</tr>";
					}
				else
				{
					productTable = productTable + "<tr class='tile' style='height:100px;'><td><img id="+obj[0].data[i].productid+" src="+filename+" height='100' width='100'/></td><td>"+obj[0].data[i].brandname+"</td><td>"+obj[0].data[i].productname+"</td><td id="+pr_id+">"+obj[0].data[i].price+"</td><td><button type='button' onclick='Addtocart("+pid+","+pquantity+","+pprice+")'>Add to Cart</button></td>" +
					"</tr>";
					}
				//alert(document.getElementById("quantity").value);
				}
				
			productTable = productTable + "</table>";
			$('#product_list').empty();
			$('#product_list').append(productTable);
		}
	});
	/*$('#divcontent').show();
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
			//alert(responseText);
			//alert(obj);
			
  
			//alert(obj[0].data.length);
			//alert(obj[0].data[0].productname);
			var productTable="<table class='table table-bordered' width='100%'><tr class='tbl_header' style='height:30px;'><td>Select</td><td>Brand</td><td>Product Name</td><td> Price</td><td>Cart</td></tr>";
			//var productTable=" <div class="tile tile-grid-item tile-column-3">
   // <div class="tile-inner-padded tile-grid-item-inner">";
			for(var i=0;i<obj[0].data.length;i++)
			{
				var pr_id = "pr_"+obj[0].data[i].productid;
				var filename = "images/"+obj[0].data[i].filename;
				var pid=obj[0].data[i].productid;
				//var pquantity="2";
				//var quant;
				var pprice=obj[0].data[i].price;
				//alert(amt_id);
				productTable = productTable + "<tr style='height:100px;'><td><input name='product' type ='radio' id="+obj[0].data[i].productid+" onclick='showproduct(this.id);'/></td><td>"+obj[0].data[i].brandname+"</td><td>"+obj[0].data[i].productname+"</td><td id="+pr_id+">"+obj[0].data[i].price+"</td>" +
				if(i%2 == 0)
				{
					productTable = productTable + "<tr class='tbl_even_row' style='height:100px;'><td><img id="+obj[0].data[i].productid+" src="+filename+" height='100' width='100'/></td><td>"+obj[0].data[i].brandname+"</td><td>"+obj[0].data[i].productname+"</td><td id="+pr_id+">"+obj[0].data[i].price+"</td><td><button type='button' onclick='Addtocart("+pid+","+pprice+")'>Add to Cart</button></td>" +
					"</tr>";
					//productTable = productTable + "<img id="+obj[0].data[i].productid+" src="+filename+" height='100' width='100'/>"+obj[0].data[i].brandname+obj[0].data[i].productname+obj[0].data[i].price+"<button type='button' onclick='Addtocart("+pid+","+pprice+")'>Add to Cart</button>" +
				
					}
				else
				{
					productTable = productTable + "<tr class='tbl_odd_row' style='height:100px;'><td><img id="+obj[0].data[i].productid+" src="+filename+" height='100' width='100'/></td><td>"+obj[0].data[i].brandname+"</td><td>"+obj[0].data[i].productname+"</td><td id="+pr_id+">"+obj[0].data[i].price+"</td><td><button type='button' onclick='Addtocart("+pid+","+pprice+")'>Add to Cart</button></td>" +
					"</tr>";
					//productTable = productTable + "<img id="+obj[0].data[i].productid+" src="+filename+" height='100' width='100'/>"+obj[0].data[i].brandname+obj[0].data[i].productname+obj[0].data[i].price+"<button type='button' onclick='Addtocart("+pid+","+pprice+")'>Add to Cart</button>" +
				
				
					}
				//alert(document.getElementById("quantity").value);
				}
				
			productTable = productTable + "</table>";
			//productTable = productTable + "</div>"+"</div>";
			$('#product_list').empty();
			$('#product_list').append(productTable);
		}
	});*/
}
function Addtocart(productid,price)
{
	alert(productid);
    var quantity = 1;
	//alert(i);
	alert(price);
var amount = 1 * price;
	var searchParams = new URLSearchParams(window.location.search); //?anything=123
	var userid = searchParams.get("userid");
	$.ajax({
		url : '/Ecommerce?serviceId=addtocart',
		type: 'POST',
		data : {
			userid :userid,productid:productid,quantity:quantity,amount:amount,
		},
		success : function(responseText) {
			var obj = jQuery.parseJSON(responseText);
			alert("added to cart");
			alert(obj[0].success[0].message);
			$('#divcontent').show();
			$('#userdiv').hide();
			$('#changepwd').hide();
			$('#prdndesc').hide();
					
		}
	});
}
function showproduct(prdid)
{
	
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
	var Cl=localStorage.getItem("loggedin")
	alert("Cl, " + Cl);
	
	if(Cl==null){
	window.location.href = "/userlogin.html";}
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
			//console.log(obj);
			//alert(obj[0].data[0].zipcode);
			if(obj[0].success[0].success == "success"){
				$('#username').text('Welcome '+obj[0].data[0].firstname);
				var usertable="<table border='1' width='100%'><tr class='tbl_header'><td colspan='4' class='Bold1'><strong>User Info</strong></td></tr>";
				//usertable = usertable + "<tr><td class='tbl_header'>profilepic</td><td><img src="WebContent/images/dell_laptop.jpg" alt="Girl in a jacket" width="500" height="600"></td>";
			
				usertable = usertable + "<tr class='tile'><td class='tbl_header'><strong>Firstname</strong></td><td>"+obj[0].data[0].firstname+"</td><td class='tbl_header'><strong>Lastname</strong></td><td>"+obj[0].data[0].lastname+"</td></tr>";
				usertable = usertable + "<tr class='tile'><td class='tbl_header'><strong>Email</strong></td><td>"+obj[0].data[0].email+"</td><td class='tbl_header'><strong>Mobile</strong></td><td>"+obj[0].data[0].mobile+"</td></tr>";		
				usertable = usertable + "<tr class='tile'><td class='tbl_header'><strong>Address1</strong></td><td>"+obj[0].data[0].address1+"</td><td class='tbl_header'><strong>Address2</strong></td><td>"+obj[0].data[0].address2+"</td></tr>";
				usertable = usertable + "<tr class='tile'><td class='tbl_header'><strong>City</strong></td><td>"+obj[0].data[0].city+"</td><td class='tbl_header'><strong>State</strong></td><td>"+obj[0].data[0].state+"</td></tr>";
				usertable = usertable + "<tr class='tile'><td class='tbl_header'><strong>Country</strong></td><td>"+obj[0].data[0].country+"</td><td class='tbl_header'><strong>Zipcode</strong></td><td>"+obj[0].data[0].zipcode+"</td></tr></table>";
								
				usertable = usertable + "<tr><td colspan='4' align='center'><input type='button' id='btnChngPwd' value='Change Password' /></td></tr></table>";
				//usertable = usertable + "<tr><td colspan='4' align='center'><img  alt='product' src='images/download.jpg' width='250px' height='250px'/></td></tr>
			 $("#btnChngPwd").click(function(){
        alert("button");
    }); 
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
var proarray = [];
var cartarray = [];
$(document).on("click", "#td_mycarts", function() {
	var searchParams = new URLSearchParams(window.location.search); //?anything=123
	var userid = searchParams.get("userid");
	//var userid=$('#hidid').val();
	$.ajax({
		url : '/Ecommerce?serviceId=mycarts',
		type: 'POST',
		data : {
			userid:userid,
		},
		success : function(responseText) {
			//console.log(responseText);
			var obj = jQuery.parseJSON(responseText);
			if(obj[0].success[0].success == "success")
			{
				//alert(obj[0].success[0].success);
				//alert(obj.length);
				//alert(obj[0].data.length);
				//alert(obj[0].data[0].productname);
				var cartTable="<table width='100%' border='1' class='tile' id='tbl_cart'><tr class='tbl_header'><td colspan='5'>My Carts Info</td></tr><tr class='tbl_header'><td>Select</td><td>Product Name</td><td>Quantity</td><td>Amount</td><td>Remove Item</td></tr>";
				var chkVal = 2;
				var chk_idarray=[];
				for(var i=0;i<obj[0].data.length;i++)
				{
					var qty_id = "qty_"+ obj[0].data[i].cartid;
					var amt_id = "amt_"+ chkVal;
					var rm_id = "rm_"+ obj[0].data[i].cartid;
					var chk_id = obj[0].data[i].productid + "_" + obj[0].data[i].cartid;
					proarray.push(obj[0].data[i].productid);
					cartarray.push(obj[0].data[i].cartid);
					var obj1=obj;
					//var chk_id = "chk_"+chkVal;
					chkVal = parseInt(chkVal)+1;
					if(i%2 == 0)
					{
						cartTable = cartTable + "<tr class='tile'><td><input name='cart' type ='checkbox' id="+chk_id+" onclick='add_totalamount(this.id)' /></td><td>"+obj[0].data[i].productname+"</td>" +
						"<td ><input id="+qty_id+" type='number' name='inputcell' value='0'/></td><td id="+amt_id+">"+obj[0].data[i].price+"</td><td><button type='button' id="+rm_id+" onclick='delete_cartitem(this.id)'>remove</button></td>" +
						"</tr>";
						}
					else
					{
						cartTable = cartTable + "<tr class='tile'><td><input name='cart' type ='checkbox' id="+chk_id+" onclick='add_totalamount(this.id)' /></td><td>"+obj[0].data[i].productname+"</td>" +
						"<td ><input type='number' id="+qty_id+" name='inputcell' value='0'/></td><td id="+amt_id+">"+obj[0].data[i].price+"</td><td><button type='button' id="+rm_id+" onclick='delete_cartitem(this.id)'>remove</button></td>" +
						"</tr>";
						}
					
					}
				cartTable = cartTable + "<tr class='tile'><td colspan='2'>Total Amount</td><td colspan='3'><input type='text' id='txtTotal' value ='0' disabled height='40'/></td><tr><td colspan='5' align='center'><input type='button' id='placeorder' value='Place Order' ></td></tr></table>";
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
	//var userid=$('#hidid').val();
	var searchParams = new URLSearchParams(window.location.search); //?anything=123
	var userid = searchParams.get("userid");
	
	$.ajax({
		url : '/Ecommerce?serviceId=deletecart',
		type: 'POST',
		data : {
			userid:userid,cartid:cart_id[1]
		},
		success : function(responseText) {
			alert("Removed");
			
			console.log(responseText);
			var obj = jQuery.parseJSON(responseText);
			alert("obj[0].success[0].success"+obj[0].success[0].success);
			if(obj[0].success[0].success == "success")
			{
			var obj = jQuery.parseJSON(responseText);
			//alert(obj.length);
			//alert(obj[0].data.length);
			//alert(obj[0].data[0].productname);
		var cartTable="<table width='100%' border='1' class='tile' id='tbl_cart'><tr class='tbl_header'><td colspan='5'><strong>My Carts Info</strong></td></tr><tr class='tbl_header'><td>Select</td><td><strong>Product Name</strong></td><td><strong>Quantity</strong></td><td><strong>Amount</storng></td><td><strong>Remove Item</strong></td></tr>";
				for(var i=0;i<obj[0].data.length;i++)
			{
				var qty_id = "qty_"+ obj[0].data[i].productid;
				var amt_id = "amt_"+ obj[0].data[i].productid;
				var rm_id = "rm_"+ obj[0].data[i].cartid;
				var chk_id = obj[0].data[i].productid + "_" + obj[0].data[i].cartid;
				if(i%2 == 0)
				{
					cartTable = cartTable + "<tr class='tile'><td><input name='cart' type ='checkbox' id="+chk_id+" onclick='add_totalamount(this.id)' /></td><td>"+obj[0].data[i].productname+"</td>" +
						"<td ><input id="+qty_id+" type='number' name='inputcell'/></td><td id="+amt_id+">"+obj[0].data[i].price+"</td><td><button type='button' id="+rm_id+" onclick='delete_cartitem(this.id)'>remove</button></td>" +
						"</tr>";
					}
				else
				{
					cartTable = cartTable + "<tr class='tile'><td><input name='cart' type ='checkbox' id="+chk_id+" onclick='add_totalamount(this.id)' /></td><td>"+obj[0].data[i].productname+"</td>" +
						"<td ><input type='number' id="+qty_id+" name='inputcell'/></td><td id="+amt_id+">"+obj[0].data[i].price+"</td><td><button type='button' id="+rm_id+" onclick='delete_cartitem(this.id)'>remove</button></td>" +
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
			$('#menu3').empty();
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
	var total_amount=0;
	var prod_amount = 0;
	
	var rowCount = $('#tbl_cart tr').length;
	var MyRows = $('table#tbl_cart').find('tbody').find('tr');
		for(var id=2;id<rowCount-2;id++){
			alert("#"+proarray [id-2]+"_"+cartarray[id-2]);
		  if($("#"+proarray [id-2]+"_"+cartarray[id-2]).is(':checked')){
			prod_amount = 0;
			var amt =  $(MyRows[id]).find('td:eq(3)').html();
			var qty =  $('#qty_'+cartarray[id-2]).val();
			  if(qty==""){
			  qty=0;
			  }
			  
			//prod_amount = $(MyRows[id]).find('td:eq(3)').html()*parseInt(qty);
			  prod_amount = amt*parseInt(qty);
			total_amount = total_amount + prod_amount;
			  
			 
		}	
		else
			{
				
			}		
	}	
	
	$('#txtTotal').val("");
	//qty = isNaN(qty) ? '0.00' : qty;
	$('#txtTotal').val(total_amount);
}
