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
	
	public static String add_products_withimage(String strPname,String strBname, String strFilename,int intCount,int intPrice)
	{
		Connection conn =null;
		PreparedStatement psInsert = null;
		ResultSet rsProducts = null;
		ResultSet rsProductsMaxId = null;
		Statement stSelectQuery = null;
		Statement stSelectMaxId = null;
		String strQuery = "Select * from salesforce.product2 where productname = '"+strPname+"'";
		System.out.println(strQuery);
		String strOutput="";
		int intProductId = 0;
		try {
			conn = DbConnection.getConnection();
			stSelectQuery = conn.createStatement();
			rsProducts = stSelectQuery.executeQuery(strQuery);
//			if(rsProducts.next())
//			{
				strQuery = "Select max(id) id from products";
				stSelectMaxId = conn.createStatement();
				rsProductsMaxId = stSelectMaxId.executeQuery(strQuery);
				if(rsProductsMaxId.next())
				{
					intProductId = rsProductsMaxId.getInt("id")+1;
				}
				else
				{
					intProductId = 100;
				}
				psInsert = conn.prepareStatement("Insert into salesforce.product2(id,productname,stock__c,brand_name__c,price__c,filename__c)values(?,?,?,?,?,?)");
				psInsert.setInt(1, intProductId);
				psInsert.setString(2, strPname);
				psInsert.setInt(3, intCount);
				psInsert.setString(4, strBname);
				psInsert.setDouble(5, intPrice);
				psInsert.setString(6, strFilename);
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
	
	public static String add_products(HttpServletRequest request)
	{
		Connection conn =null;
		PreparedStatement psInsert = null;
		ResultSet rsProducts = null;
		ResultSet rsProductsMaxId = null;
		Statement stSelectQuery = null;
		Statement stSelectMaxId = null;
		String strProduct = request.getParameter("productname");
		String strQuery = "Select * from salesforce.product2 where productname = '"+strProduct+"'";
		System.out.println(strQuery);
		String strOutput="";
		int intProductId = 0;
		try {
			conn = DbConnection.getConnection();
			stSelectQuery = conn.createStatement();
			rsProducts = stSelectQuery.executeQuery(strQuery);
//			if(rsProducts.next())
//			{
				strQuery = "Select max(id) id from salesforce.product2";
				stSelectMaxId = conn.createStatement();
				rsProductsMaxId = stSelectMaxId.executeQuery(strQuery);
				if(rsProductsMaxId.next())
				{
					intProductId = rsProductsMaxId.getInt("id")+1;
				}
				else
				{
					intProductId = 100;
				}
				String strBrand = request.getParameter("brand");
				int intStock = Integer.parseInt(request.getParameter("stock"));
				double dblPrice = Double.parseDouble(request.getParameter("price"));
				psInsert = conn.prepareStatement("Insert into salesforce.product2(id,productname__c,stock__c,brand_name__c,price__c)values(?,?,?,?,?)");
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
		String strQuery = "Select * from salesforce.product2 where stock >= 1";
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
		String strQuery = "Select * from salesforce.product2 where id = "+intProductid;
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
	
	public static String update_product(int intProductId,int intQuantity)
	{
		Connection conn=null;
		ResultSet rsGetPrdQty = null;
		Statement stGetPrdQty = null;
		PreparedStatement psUpdatePrdQty = null;
		String strGetQuery = "";
		int intProdQuantity = 0;
		int intUpdatePrdQty = 0;
		strGetQuery = "Select stock from salesforce.product2 where id = "+intProductId;
		try {
			conn = DbConnection.getConnection();
			stGetPrdQty = conn.createStatement();
			rsGetPrdQty = stGetPrdQty.executeQuery(strGetQuery);
			if(rsGetPrdQty.next())
			{
				intProdQuantity = rsGetPrdQty.getInt("stock");
				intUpdatePrdQty = intProdQuantity - intQuantity;
			}
			psUpdatePrdQty = conn.prepareStatement("Update salesforce.product2 set stock__c = ? where id = ?");
			psUpdatePrdQty.setInt(1, intUpdatePrdQty);
			psUpdatePrdQty.setInt(2, intProductId);
			psUpdatePrdQty.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "Mani";
	}

}
