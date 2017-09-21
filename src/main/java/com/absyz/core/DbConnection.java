package main.java.com.absyz.core;

import java.sql.*;
import java.net.*;

public class DbConnection {
	
	public static Connection getConnection()
	{
		Connection conn = null;
		try {
	        URI dbUri = new URI(System.getenv("DATABASE_URL"));

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

   
	         conn = DriverManager.getConnection(dbUrl, username, password);
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
	      System.out.println("Opened database successfully");
		return conn;
	}

}
