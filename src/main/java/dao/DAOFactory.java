package dao;

import dao.impl.AccuracyDAO;
import dao.impl.MusicDAO;

public class DAOFactory {
	
	private DAOFactory() {}
	
	public static AccuracyDAO createAccuracyDAO() {
		return new AccuracyDAO();
	}
	
	public static MusicDAO createMusicDAO() {
		return new MusicDAO();
	}
	
}
