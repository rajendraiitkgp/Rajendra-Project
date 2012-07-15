package com.connectionUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.jdbc.PreparedStatement;

public class ConnectionUtil {
	public ConnectionUtil()
	{
		System.out.print("ConnectionUtil class instantiated");
	}
	private static Properties properties=new Properties();
	Statement s=null;
	PreparedStatement ps=null;
	
	static
	{
		try
		{
	ClassLoader cl=ConnectionUtil.class.getClassLoader();
	InputStream is=cl.getResourceAsStream("com/config/connection.properties");
	properties.load(is);
	Class.forName(properties.getProperty("driver"));
	System.out.println(properties.getProperty("driver"));
		}
		catch (Exception e) {
			System.out.println(e);
		}
	
	}
	public static Connection getConnection()
	{
		Connection connection=null;

		try
		{
		DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("user"),properties.getProperty("password"));
		System.out.println(properties.getProperty("url"));
		System.out.println(properties.getProperty("user"));
		System.out.println(properties.getProperty("password"));
		
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return connection;
		
	}
	public static void closeConnection(Connection connection)
	{
		try
		{
		if(connection!=null)
			connection.close();
		}
		catch (Exception e) {
			System.out.println("some exception occured" +e);
		}
		
	}
	public static void closeConnection(Connection connection, Statement statement)
	{
		try
		{
		if(statement!=null)
		{
			statement.close();
			closeConnection(connection);
		}
		
		}
		catch (Exception e) {
			System.out.println("some exception occured" +e);
		}
		
	}
	public static void closeConnection(Connection connection, Statement statement, ResultSet rs)
	{
		try
		{
		if(rs!=null)
		{
			statement.close();
			closeConnection(connection,statement);
		}
		
		}
		catch (Exception e) {
			System.out.println("some exception occured" +e);
		}
		
	}
	
	
}
