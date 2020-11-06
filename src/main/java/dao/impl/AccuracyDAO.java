package dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.DAO;
import utils.GameUtils;

public class AccuracyDAO extends DAO {

	public AccuracyDAO() {
		super();
	}
	
	public void insertAccuracy(double accuracy) throws SQLException {
		insertAccuracy(accuracy, false);
	}

	public void insertAccuracy(double accuracy, boolean clearOldestEntry) throws SQLException {
		String deleteQuery = "DELETE FROM accuracy ORDER BY id LIMIT 1;";
		String insertQuery = "INSERT INTO accuracy(value) VALUES (?);";

		if (clearOldestEntry) {
			pst = con.prepareStatement(deleteQuery);
			pst.execute();
			pst.close();
		}

		pst = con.prepareStatement(insertQuery);
		pst.setDouble(1, accuracy);
		pst.execute();
		pst.close();
	}

	public List<Double> loadAccuracyList() throws SQLException {
		List<Double> accuracy = new ArrayList<>(GameUtils.ACCURACY_GAME_AMOUNT);

		String query = "SELECT * FROM accuracy;";

		st = con.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			accuracy.add(rs.getDouble("value"));
		}

		st.close();
		rs.close();

		return accuracy;
	}
}
