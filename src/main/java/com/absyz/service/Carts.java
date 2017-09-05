package main.java.com.absyz.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.java.com.absyz.core.DbConnection;

public class Carts {
	
	public static String add_to_cart(HttpServletRequest request)
	{
		Connection conn =null;
		PreparedStatement psInsert = null;
		ResultSet rsCartsMaxId = null;
		Statement stSelectMaxId = null;
		String strOutput="";
		String strQuery="";
		int intCartId = 0;
		try {
			conn = DbConnection.getConnection();
			strQuery = "Select max(cartid) cartid from carts";
			int intUserId = Integer.parseInt(request.getParameter("userid"));
			int intProductId = Integer.parseInt(request.getParameter("productid"));
			int intQuantity = Integer.parseInt(request.getParameter("quantity"));
			double dblAmount = Double.parseDouble(request.getParameter("amount"));
			stSelectMaxId = conn.createStatement();
			rsCartsMaxId = stSelectMaxId.executeQuery(strQuery);
			if(rsCartsMaxId.next())
			{
				intCartId = rsCartsMaxId.getInt("cartid")+1;
			}
			else
			{
				intCartId = 100;
			}
			psInsert = conn.prepareStatement("Insert into carts(cartid,userid,productid,quantity,amount)values(?,?,?,?,?)");
			psInsert.setInt(1, intCartId);
			psInsert.setInt(2, intUserId);
			psInsert.setInt(3, intProductId);
			psInsert.setInt(4, intQuantity);
			psInsert.setDouble(5, dblAmount);
			psInsert.executeUpdate();
			strOutput = "success";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			strOutput = "failure";
			e.printStackTrace();
		}
		System.out.println(strOutput);
		return strOutput;
	}
	
	public static String my_cart_list(HttpServletRequest request)
	{
		String strOutput="";
		int intUserId = Integer.parseInt(request.getParameter("userid"));
		Connection conn = null;
		Statement stSelectCarts = null;
		ResultSet rsSelectCarts = null;
		JSONArray json = new JSONArray();
		JSONObject obj=null;
		try {
			//String strQuery = "Select * from carts where userid = "+intUserId;
			String strQuery = "Select c.cartid,c.userid,c.productid,c.quantity,c.amount,p.productname,p.price from carts c "
					+ "join products p on c.productid = p.productid where c.userid = "+intUserId;
			conn = DbConnection.getConnection();
			stSelectCarts = conn.createStatement();
			rsSelectCarts = stSelectCarts.executeQuery(strQuery);
			obj = new JSONObject();      //extends HashMap
		    obj.put("success",JsonObjects.json_objects("success","products data available"));
		    obj.put("data",JsonObjects.convertResultSetToJson(rsSelectCarts));
		    json.put(obj);
			//strOutput = Orders.convertResultSetToJson(rsSelectCarts);
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
