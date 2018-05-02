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

public class LoginService {
	
	public static String adminLogin(HttpServletRequest request) throws JSONException
	{
		String strOutput="";
		Connection conn =null;
		ResultSet rsLogin = null;
		ResultSet rsLogData = null;
		Statement stSelectQuery = null;
		String strEmail = request.getParameter("email");
		String strPassword = request.getParameter("password");
		String strQuery = "Select userid from adminusers where email = '"+strEmail+"' and password = '"+strPassword+"'";
		System.out.println(strQuery);
		JSONArray json = new JSONArray();
		JSONObject obj=null;
		try {
			conn = DbConnection.getConnection();
			stSelectQuery = conn.createStatement();
			rsLogin = stSelectQuery.executeQuery(strQuery);
			//strOutput = Orders.convertResultSetToJson(rsLogin);
			if(rsLogin.next())
			{
				rsLogData = stSelectQuery.executeQuery(strQuery);
				//strOutput = Orders.convertResultSetToJson(rsLogData);
				System.out.println(strOutput);
				obj = new JSONObject();      //extends HashMap
			    obj.put("success",JsonObjects.json_objects("success","user data available"));
			    obj.put("data",JsonObjects.convertResultSetToJson(rsLogData));
			    json.put(obj);
				//strOutput = "[{+"'""+"success:"+JsonObjects.json_objects("success","user data available")+"}]"+",[{data:"+strOutput+"}]";
				
			}
			else
			{
				obj = new JSONObject();      //extends HashMap
			    obj.put("success",JsonObjects.json_objects("failure","user data not available"));
			    json.put(obj);
			}
		}
			 catch (SQLException e) {
					// TODO Auto-generated catch block
				 	obj = new JSONObject();      //extends HashMap
				    obj.put("success",JsonObjects.json_objects("failure","Data Connection Lost. Please Try again after sometime"));
				    json.put(obj);
					strOutput = "Data Connection Lost. Please Try again after sometime";
					e.printStackTrace();
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				System.out.println(json);
			
		return json.toString();
	}
	
	public static String userLogin(HttpServletRequest request) throws JSONException
	{
		String strOutput="";
		Connection conn =null;
		ResultSet rsLogin = null;
		ResultSet rsLogData = null;
		Statement stSelectQuery = null;
		String strEmail = request.getParameter("email");
		String strPassword = request.getParameter("password");
		String strQuery = "Select id from salesforce.contact where email = '"+strEmail+"' and password__c = '"+strPassword+"'";
		System.out.println(strQuery);
		JSONArray json = new JSONArray();
		JSONObject obj=null;
		try {
			conn = DbConnection.getConnection();
			stSelectQuery = conn.createStatement();
			rsLogin = stSelectQuery.executeQuery(strQuery);
			//strOutput = Orders.convertResultSetToJson(rsLogin);
			if(rsLogin.next())
			{
				rsLogData = stSelectQuery.executeQuery(strQuery);
				//strOutput = Orders.convertResultSetToJson(rsLogData);
				System.out.println(strOutput);
				obj = new JSONObject();      //extends HashMap
			    obj.put("success",JsonObjects.json_objects("success","user data available"));
			    obj.put("data",JsonObjects.convertResultSetToJson(rsLogData));
			    json.put(obj);
				//strOutput = "[{+"'""+"success:"+JsonObjects.json_objects("success","user data available")+"}]"+",[{data:"+strOutput+"}]";
				
			}
			else
			{
				obj = new JSONObject();      //extends HashMap
			    obj.put("success",JsonObjects.json_objects("failure","user data not available"));
			    json.put(obj);
			}
		}
			 catch (SQLException e) {
					// TODO Auto-generated catch block
				 	obj = new JSONObject();      //extends HashMap
				    obj.put("success",JsonObjects.json_objects("failure","Data Connection Lost. Please Try again after sometime"));
				    json.put(obj);
					strOutput = "Data Connection Lost. Please Try again after sometime";
					e.printStackTrace();
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				System.out.println(json);
			
		return json.toString();
	}
	public static String show_user(HttpServletRequest request) throws JSONException
	{
		Connection conn =null;
		ResultSet rsLogData = null;
		ResultSet rsLogin = null;
		Statement stSelectQuery = null;
		int intUserid  = Integer.parseInt(request.getParameter("userid"));
		String strQuery = "Select id,firstname,lastname,email,mobilephone,Address1__c,Address_2__c,mailingcity,mailingstate,mailingpostalcode,mailingcountry from salesforce.contact where id = "+intUserid;
		System.out.println(strQuery);
		JSONArray json = new JSONArray();
		JSONObject obj=null;
		try {
			conn = DbConnection.getConnection();
			stSelectQuery = conn.createStatement();
			rsLogData = stSelectQuery.executeQuery(strQuery);
			if(rsLogData.next())
			{
				rsLogin = stSelectQuery.executeQuery(strQuery);
				obj = new JSONObject();      //extends HashMap
			    obj.put("success",JsonObjects.json_objects("success","user data available"));
			    obj.put("data",JsonObjects.convertResultSetToJson(rsLogin));
			    json.put(obj);				
			}
			else
			{
				obj = new JSONObject();      //extends HashMap
			    obj.put("success",JsonObjects.json_objects("failure","user data not available"));
			    json.put(obj);
			}
		}
			 catch (SQLException e) {
					// TODO Auto-generated catch block
				 	obj = new JSONObject();      //extends HashMap
				    obj.put("success",JsonObjects.json_objects("failure","Data Connection Lost. Please Try again after sometime"));
				    json.put(obj);
					e.printStackTrace();
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				System.out.println(json);
			
		return json.toString();
	}

}
