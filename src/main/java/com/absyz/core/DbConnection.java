package main.java.com.absyz.core;

import java.sql.*;

public class DbConnection {
	
	public static Connection getConnection()
	{
		Connection conn = null;
		try {
	         Class.forName("org.postgresql.Driver");
	         conn = DriverManager
	            .getConnection("jdbc:postgresql://localhost:5432/ecommerce",
	            "ecommerce", "ecommerce");
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
	      System.out.println("Opened database successfully");
		return conn;
	}

}
