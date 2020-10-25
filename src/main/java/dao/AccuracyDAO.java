package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.BooleanWrapper;
import utils.GameUtils;

public class AccuracyDAO extends DAO {
	public void insertAccuracy(double accuracy) throws SQLException {
		insertAccuracy(accuracy, new BooleanWrapper());
	}

	public void insertAccuracy(double accuracy, BooleanWrapper clearOldestEntry) throws SQLException {
		if (!connectToDatabase())
			return;

		String deleteQuery = "DELETE FROM accuracy ORDER BY id LIMIT 1;";
		String insertQuery = "INSERT INTO accuracy(value) VALUES (?);";

		if (clearOldestEntry.getWrappedBoolean()) {
			pst = con.prepareStatement(deleteQuery);
			pst.execute();
			pst.close();
		}

		pst = con.prepareStatement(insertQuery);
		pst.setDouble(1, accuracy);
		pst.execute();
		pst.close();

		con.close();
	}

	public List<Double> loadAccuracyList() throws SQLException {
		List<Double> accuracy = new ArrayList<>(GameUtils.ACCURACY_GAME_AMOUNT);
		
		if (!connectToDatabase())
			return accuracy;

		String query = "SELECT * FROM accuracy;";

		st = con.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			accuracy.add(rs.getDouble("value"));
		}

		st.close();
		rs.close();
		con.close();

		return accuracy;
	}
}
