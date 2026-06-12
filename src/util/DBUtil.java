package util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class DBUtil {

	private static final String URL = "jdbc:oracle:thin:@192.168.0.3:1521:xe";

	private static final String USER = "dteam";

	private static final String PASSWORD = "dteam1234";

	// JDBC Driver Load
	static {

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");			

		} catch (Exception e) {

			System.out.println("Oracle Driver Load Fail");

			e.printStackTrace();
		}
	}

	// Connection 반환
	public static Connection getConnection() throws Exception {

		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	/*
	 * 프로그램 종료용 DriverManager 해제
	 */
	public static void closeDataSource() {

		try {

			Enumeration<Driver> drivers = DriverManager.getDrivers();
			while (drivers.hasMoreElements()) {
				try {
					java.sql.DriverManager.deregisterDriver(drivers.nextElement());
				} catch (java.sql.SQLException e) {

				}
			}

		} catch (Exception e) {

			System.out.println("JDBC Driver 해제 실패");

			e.printStackTrace();
		}
	}

}
