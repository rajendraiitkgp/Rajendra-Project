package com.connect;

import java.sql.Connection;
import java.sql.DriverManager;

import com.connectionUtil.ConnectionUtil;

public class MysqlConnection {
	 	public static void main(String[] args)throws Exception {
	 		Connection connection=ConnectionUtil.getConnection();
	 		if(connection!=null)
	 			System.out.println("connection established");
	 		else
	 			System.out.println("no connection established");
	 		ConnectionUtil.closeConnection(connection);

	 			
	 		
	 	
	 	}
	 		

}
