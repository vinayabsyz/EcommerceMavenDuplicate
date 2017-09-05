package main.java.com.absyz.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjects {
	 public static JSONArray json_objects(String str,String msg)
	   {
		  
		   JSONArray json = new JSONArray();
		   try {
				    JSONObject obj = new JSONObject();      //extends HashMap
				    obj.put("success",str);
				    obj.put("message",msg);
				    json.put(obj);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return json;
		   
	   }@SuppressWarnings("unchecked")
		public static JSONArray convertResultSetToJson(ResultSet resultSet) throws SQLException
		{
		    JSONArray json = new JSONArray();
		    ResultSetMetaData metadata = resultSet.getMetaData();
		    int numColumns = metadata.getColumnCount();

		    try {
				while(resultSet.next())             //iterate rows
				{
				    JSONObject obj = new JSONObject();      //extends HashMap
				    for (int i = 1; i <= numColumns; ++i)           //iterate columns
				    {
				        String column_name = metadata.getColumnName(i);
				        obj.put(column_name, resultSet.getObject(column_name));
				    }
				    //obj.put("success","success");
				    json.put(obj);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return json;
		}

}
