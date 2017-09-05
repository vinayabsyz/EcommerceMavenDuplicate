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

public class Products {
	
	public static String add_products(HttpServletRequest request)
	{
		Connection conn =null;
		PreparedStatement psInsert = null;
		ResultSet rsProducts = null;
		ResultSet rsProductsMaxId = null;
		Statement stSelectQuery = null;
		Statement stSelectMaxId = null;
		String strProduct = request.getParameter("productname");
		String strQuery = "Select * from products where productname = '"+strProduct+"'";
		System.out.println(strQuery);
		String strOutput="";
		int intProductId = 0;
		try {
			conn = DbConnection.getConnection();
			stSelectQuery = conn.createStatement();
			rsProducts = stSelectQuery.executeQuery(strQuery);
//			if(rsProducts.next())
//			{
				strQuery = "Select max(productid) productid from products";
				stSelectMaxId = conn.createStatement();
				rsProductsMaxId = stSelectMaxId.executeQuery(strQuery);
				if(rsProductsMaxId.next())
				{
					intProductId = rsProductsMaxId.getInt("productid")+1;
				}
				else
				{
					intProductId = 100;
				}
				String strBrand = request.getParameter("brand");
				int intStock = Integer.parseInt(request.getParameter("stock"));
				double dblPrice = Double.parseDouble(request.getParameter("price"));
				psInsert = conn.prepareStatement("Insert into products(productid,productname,stock,brandname,price)values(?,?,?,?,?)");
				psInsert.setInt(1, intProductId);
				psInsert.setString(2, strProduct);
				psInsert.setInt(3, intStock);
				psInsert.setString(4, strBrand);
				psInsert.setDouble(5, dblPrice);
				
				psInsert.executeUpdate();
				strOutput = "success";
				
//			}
//			else
//			{
//				strOutput = "User Already Exists";
//			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			strOutput = "failure";
			e.printStackTrace();
		}
		System.out.println(strOutput);
		return strOutput;
	}
	
	public static String show_products(HttpServletRequest request)
	{
		String strOutput="";
		Connection conn=null;
		Statement stGetProducts;
		ResultSet rsGetProducts;
		String strQuery = "Select * from products where stock >= 1";
		JSONArray json = new JSONArray();
		JSONObject obj=null;
		try {
			conn = DbConnection.getConnection();
			stGetProducts = conn.createStatement();
			rsGetProducts = stGetProducts.executeQuery(strQuery);
			//strOutput = Orders.convertResultSetToJson(rsGetProducts);
			obj = new JSONObject();      //extends HashMap
		    obj.put("success",JsonObjects.json_objects("success","products data available"));
		    obj.put("data",JsonObjects.convertResultSetToJson(rsGetProducts));
		    json.put(obj);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(obj);
		return json.toString();
	}
	
	public static String show_productinfo(HttpServletRequest request)
	{
		String strOutput="";
		Connection conn=null;
		Statement stGetProducts;
		ResultSet rsGetProducts;
		int intProductid = Integer.parseInt(request.getParameter("prodid"));
		String strQuery = "Select * from products where productid = "+intProductid;
		JSONArray json = new JSONArray();
		JSONObject obj=null;
		try {
			conn = DbConnection.getConnection();
			stGetProducts = conn.createStatement();
			rsGetProducts = stGetProducts.executeQuery(strQuery);
			obj = new JSONObject();      //extends HashMap
		    obj.put("success",JsonObjects.json_objects("success","products data available"));
		    obj.put("data",JsonObjects.convertResultSetToJson(rsGetProducts));
		    json.put(obj);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(obj);
		return json.toString();
	}

}
