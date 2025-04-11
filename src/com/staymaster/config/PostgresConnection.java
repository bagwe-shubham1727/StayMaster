package com.staymaster.config;

import java.sql.*;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.staymaster.hibernate.SessionManager;

public class PostgresConnection {


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
