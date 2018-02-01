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
		strQuery = "Select max(orderid) orderid from orders";
		stSelectMaxId = conn.createStatement();
		rsOrderMaxId = stSelectMaxId.executeQuery(strQuery);
		if(rsOrderMaxId.next())
		{
			intOrderId = rsOrderMaxId.getInt("orderid")+1;
		}
		else
		{
			intOrderId = 100;
		}
		for (int i = 0; i < jsonarray.length(); i++) {
		    JSONObject jsonobject = jsonarray.getJSONObject(i);
		    int intCartId = Integer.parseInt(jsonobject.getString("cartid"));
		    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			int intUserId = Integer.parseInt(jsonobject.getString("userid"));
			int intProductId = Integer.parseInt(jsonobject.getString("productid"));
			int intQuantity = Integer.parseInt(jsonobject.getString("quantity"));
			double dblAmount = Double.parseDouble(jsonobject.getString("totalamount"));
			int intShippingId = Integer.parseInt(jsonobject.getString("shippingid"));
			String status=jsonobject.getString("status");
			psInsert = conn.prepareStatement("Insert into orders(orderid,userid,productid,shippingid,productquantity,totalamount,orderdate,status)values(?,?,?,?,?,?,?,?)");
			psInsert.setInt(1, intOrderId);
			psInsert.setInt(2, intUserId);
			psInsert.setInt(3, intProductId);
			psInsert.setInt(4, intShippingId);
			psInsert.setInt(5, intQuantity);
			psInsert.setDouble(6, dblAmount);
			psInsert.setTimestamp(7, timestamp);
			psInsert.setString(8, status);
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
		int intUserId = Integer.parseInt(request.getParameter("userid"));
		Connection conn = null;
		Statement stSelectOrders = null;
		ResultSet rsSelectOrders = null;
		JSONArray json = new JSONArray();
		JSONObject obj=null;
		try {
			//String strQuery = "Select * from orders where userid = "+intUserId;
			String strQuery = "Select o.orderid,o.userid,o.productid,o.orderdate,o.status,o.productquantity,o.totalamount,p.productname,p.price from orders o "
					+ "join products p on o.productid = p.productid where o.userid = "+intUserId+" order by o.orderid asc";
			conn = DbConnection.getConnection();
			stSelectOrders = conn.createStatement();
			rsSelectOrders = stSelectOrders.executeQuery(strQuery);
			//strOutput = convertResultSetToJson(rsSelectOrders);
			obj = new JSONObject();      //extends HashMap
		    obj.put("success",JsonObjects.json_objects("success","products data available"));
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

	
}
