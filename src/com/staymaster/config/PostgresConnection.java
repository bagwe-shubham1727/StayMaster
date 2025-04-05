package com.stayease.config;
import java.sql.*;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.stayease.hibernate.SessionManager;

public class PostgresConnection {

//	public static Connection Connector() {
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/stayease", "postgres", "123123");
//            return conn;
//        } catch (Exception e) {
//            System.out.println(e);
//            return null;
//        }
//    }
//	
	
	public static void checkConnection() {
	    Session session = SessionManager.openSession();
	    try {
	        // Perform a simple database operation
	        String sql = "SELECT 1";
	        Integer result = session.createNativeQuery(sql, Integer.class).uniqueResult();
	        if (result != null && result.equals(1)) {
	            System.out.println("Hibernate connection successful!");
	        } else {
	            System.out.println("Hibernate connection failed!");
	        }
	    } catch (Exception e) {
	        System.out.println("Hibernate connection failed!");
	        e.printStackTrace();
	    } finally {
	        SessionManager.closeSession();
	    }
	}
	
}
