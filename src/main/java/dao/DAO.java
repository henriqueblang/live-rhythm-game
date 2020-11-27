package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utils.UIUtils;

public abstract class DAO {
	// Object for database connection.
	protected Connection con;

	// Object for dynamic SQL commands manipulation.
	protected PreparedStatement pst;

	// Object for static SQL commands manipulation.
	protected Statement st;

	// Object that references the table resulted from a search (SELECT).
	protected ResultSet rs;

	private String database = "live";
	private String url = "jdbc:mysql://localhost:3306/" + database
			+ "?useTimezone=true&serverTimezone=UTC&autoReconnect=true&useSSL=false";
	private String user = "root";
	private String password = "admin";
	
	public DAO() {
		connectToDatabase();
	}

	public void connectToDatabase() {
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			UIUtils.showError(e.getMessage());
		}
	}
	
	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			UIUtils.showError(e.getMessage());
		}
	}
	
}
