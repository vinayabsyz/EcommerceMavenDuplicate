package main.java.com.absyz.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.java.com.absyz.core.DbConnection;

public class Orders {
	
	public static String new_order(HttpServletRequest request) throws JSONException
	{
		Connection conn =null;
		PreparedStatement psInsert = null;
		ResultSet rsOrderMaxId = null;
		Statement stSelectMaxId = null;
		String strOutput="";
		String strQuery="";
		int intOrderId = 0;
		try {
		String strJson = request.getParameter("data");
		JSONArray jsonarray = new JSONArray(strJson);
		conn = DbConnection.getConnection();
		strQuery = "Select max(id) id from salesforce.Order__c	";
		stSelectMaxId = conn.createStatement();
		rsOrderMaxId = stSelectMaxId.executeQuery(strQuery);
		if(rsOrderMaxId.next())
		{
			intOrderId = rsOrderMaxId.getInt("id");
		}
		else
		{
			intOrderId = 100;
		}
		for (int i = 0; i < jsonarray.length(); i++) {
		    JSONObject jsonobject = jsonarray.getJSONObject(i);
		    String intCartId = jsonobject.getString("cartid");
		    Timestamp timestamp = new Timestamp(System.currentTimeMillis()+(330*60*1000));
			
			intOrderId=intOrderId+1;
			String intUserId = jsonobject.getString("userid");
			String intProductId = jsonobject.getString("productid");
			int intQuantity = Integer.parseInt(jsonobject.getString("quantity"));
			//int intPrice = Integer.parseInt(jsonobject.getString("price"));
			double dblAmount = Double.parseDouble(jsonobject.getString("totalamount"));
			int intShippingId = Integer.parseInt(jsonobject.getString("shippingid"));
			String status=jsonobject.getString("status");
			psInsert = conn.prepareStatement("Insert into salesforce.Order__c(id,contactid__c,productid__c,productquantity__c,totalamount__c,status__c,Order_Date__c)values(?,?,?,?,?,?,?)");
			psInsert.setInt(1, intOrderId);
			psInsert.setString(2, intUserId);
			psInsert.setString(3, intProductId);
		
			psInsert.setInt(4, intQuantity);
			psInsert.setDouble(5, dblAmount);
			psInsert.setString(6, status);
			//psInsert.setInt(7, intPrice);
			psInsert.setTimestamp(7, timestamp);
			psInsert.executeUpdate();
			String strDeleteCart = Carts.remove_cart_data(intCartId);
			Products.update_product(intProductId, intQuantity);
		}
			strOutput = "success";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			strOutput = "failure";
			e.printStackTrace();
		}
		System.out.println(strOutput);
		return strOutput;
	}
	
	public static String my_order_list(HttpServletRequest request)
	{
		String strOutput="";
		String intUserId = request.getParameter("userid");
		Connection conn = null;
		Statement stSelectOrders = null;
		ResultSet rsSelectOrders = null;
			Statement stSelectMaxId = null;
		ResultSet rsOrderMaxId = null;
		JSONArray json = new JSONArray();
		JSONObject obj=null;
		
		try {
			conn = DbConnection.getConnection();
				
			//String strQuery = "Select * from orders where userid = "+intUserId;
			String strQuery="";
			
			
			
			
					 strQuery = "Select o.id,o.sfid,o.contactid__c,o.productid__c,o.order_date__c,o.totalamount__c,o.status__c,o.productquantity__c,o.totalamount__c,p.name,p.price__c from Salesforce.Order__c o "
					+ "join salesforce.product2 p on o.productid__c  = p.sfid where o.contactid__c = '"+intUserId+"'";
		
			
			
				conn = DbConnection.getConnection();
			stSelectOrders = conn.createStatement();
			rsSelectOrders = stSelectOrders.executeQuery(strQuery);
				obj = new JSONObject();      //extends HashMap
		    obj.put("success",JsonObjects.json_objects("success","Orders data available"));
		    obj.put("data",JsonObjects.convertResultSetToJson(rsSelectOrders));
		    json.put(obj);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	public static String changestatus(HttpServletRequest request)
	{
		String strOutput="";
		int orderid = Integer.parseInt(request.getParameter("orderid"));
		//int intOrderId = Integer.parseInt(request.getParameter("orderid"));
		Connection conn = null;
		Statement stSelectOrders = null;
		ResultSet rsSelectOrders = null;
		JSONArray json = new JSONArray();
		JSONObject obj=null;
		try {
			//String strQuery = "Select * from orders where userid = "+intUserId;
			
			String strQuery = "select * from salesforce.order where id = "+orderid;
			conn = DbConnection.getConnection();
			
			stSelectOrders = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                  ResultSet.CONCUR_UPDATABLE);
			rsSelectOrders = stSelectOrders.executeQuery(strQuery);
			//strOutput = convertResultSetToJson(rsSelectOrders);
			  while (rsSelectOrders.next()) {
            //String f = rsSelectOrders.etString("o.status");
            rsSelectOrders.updateString( "status", "Order Delivered");
            rsSelectOrders.updateRow();
        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

}
