package dao;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import entity.Music;
import entity.collection.Beatmap;
import entity.collection.Pair;
import entity.decoder.MP3AudioFileReader;
import javafx.scene.image.Image;
import utils.EnumUtils.Types;

public class MusicDAO extends DAO {

	final private String[] tableModeSuffixes = { "easy", "normal", "hard" };

	public int insertMusic(String title, int previewStart, int previewEnd, InputStream audioStream,
			InputStream thumbnailStream) throws SQLException {
		int musicId = -1;

		if (!connectToDatabase())
			return musicId;

		String query = "INSERT INTO music(title, favorite, preview_start, preview_end, audio, thumbnail) VALUES (?, ?, ?, ?, ?, ?);";

		pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		pst.setString(1, title);
		pst.setBoolean(2, false);
		pst.setInt(3, previewStart);
		pst.setInt(4, previewEnd);
		pst.setBinaryStream(5, audioStream);
		pst.setBinaryStream(6, thumbnailStream);

		pst.execute();

		try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				musicId = generatedKeys.getInt(1);
			} else {
				throw new SQLException("No ID obtained.");
			}

			generatedKeys.close();
		}

		pst.close();
		con.close();

		return musicId;
	}

	public void insertHighscore(int musicId, int mode) throws SQLException {
		if (!connectToDatabase())
			return;

		String query = "INSERT INTO highscore_" + tableModeSuffixes[mode] + "(id_music, score) VALUES (?, ?);";

		pst = con.prepareStatement(query);

		pst.setInt(1, musicId);
		pst.setInt(2, 0);

		pst.execute();

		pst.close();
		con.close();
	}

	public void updateFavorite(int musicId, boolean favorite) throws SQLException {
		if (!connectToDatabase())
			return;

		String query = "UPDATE music SET favorite = ? WHERE id = " + musicId + ";";

		pst = con.prepareStatement(query);

		pst.setBoolean(1, favorite);

		pst.execute();

		pst.close();
		con.close();
	}

	public void updateHighscore(int musicId, int mode, int score) throws SQLException {
		if (!connectToDatabase())
			return;

		String query = "UPDATE highscore_" + tableModeSuffixes[mode] + " SET score = " + score + " WHERE id_music = " + musicId + ";";

		pst = con.prepareStatement(query);
		pst.executeUpdate();

		pst.close();
		con.close();
	}

	public void insertBeatmap(int musicId, int mode, Beatmap beatmap) throws SQLException {
		if (!connectToDatabase())
			return;

		List<List<Pair<Integer, Types>>> beatmapData = beatmap.getData();

		String query = "INSERT INTO beatmap_" + tableModeSuffixes[mode]
				+ "(id_music, note_index, note_track, note_type) VALUES (?, ?, ?, ?);";

		con.setAutoCommit(false);

		pst = con.prepareStatement(query);

		for (int i = 0; i < beatmapData.size(); i++) {
			List<Pair<Integer, Types>> noteData = beatmapData.get(i);

			if (noteData == null)
				continue;

			for (Pair<Integer, Types> note : noteData) {
				pst.setInt(1, musicId);
				pst.setInt(2, i);
				pst.setInt(3, note.getL());
				pst.setInt(4, note.getR().getValue());

				pst.addBatch();
			}
		}

		pst.executeBatch();
		con.commit();

		con.setAutoCommit(true);

		pst.close();
		con.close();
	}

	public void deleteMusic(int musicId) throws SQLException {
		if (!connectToDatabase())
			return;

		String query = "DELETE FROM music WHERE id = " + musicId + ";";

		pst = con.prepareStatement(query);
		pst.execute();

		pst.close();
		con.close();
	}

	public List<Music> loadMusicLibrary()
			throws SQLException, IOException, LineUnavailableException, UnsupportedAudioFileException {
		List<Music> library = new ArrayList<>();

		if (!connectToDatabase())
			return library;

		String query = "SELECT * FROM music;";

		st = con.createStatement();
		rs = st.executeQuery(query);

		while (rs.next()) {
			int id = rs.getInt("id");
			String title = rs.getString("title");
			boolean favorite = rs.getBoolean("favorite");
			int previewStart = rs.getInt("preview_start");
			int previewEnd = rs.getInt("preview_end");
			InputStream audio = rs.getBinaryStream("audio");
			InputStream thumbnail = rs.getBinaryStream("thumbnail");

			InputStream in = new BufferedInputStream(audio, 1024 * 1024);
			AudioInputStream stream = new MP3AudioFileReader().getAudioInputStream(in);

			AudioFormat baseFormat = stream.getFormat();
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
					baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
			stream = AudioSystem.getAudioInputStream(format, stream);

			Clip clip = AudioSystem.getClip();
			clip.open(stream);

			library.add(new Music(id, title, favorite, clip, previewStart, previewEnd, new Image(thumbnail)));
		}

		st.close();
		rs.close();

		for (Music music : library) {
			String beatmapQuery = "SELECT * FROM beatmap_%s WHERE id_music = " + music.getId()
					+ " ORDER BY note_index;";
			String highscoreQuery = "SELECT * FROM highscore_%s WHERE id_music = " + music.getId() + ";";
			
			for (int i = 0; i < tableModeSuffixes.length; i++) {
				Beatmap beatmap = new Beatmap();

				String suffix = tableModeSuffixes[i];

				pst = con.prepareStatement(String.format(beatmapQuery, suffix));
				rs = pst.executeQuery();

				while (rs.next()) {
					int index = rs.getInt("note_index");
					int noteTrack = rs.getInt("note_track");
					int noteType = rs.getInt("note_type");

					while (index >= beatmap.size())
						beatmap.add(index > beatmap.size() ? null : new ArrayList<>());

					beatmap.get(index).add(new Pair<>(noteTrack, Types.values()[noteType]));
				}

				pst.close();
				rs.close();

				pst = con.prepareStatement(String.format(highscoreQuery, suffix));
				rs = pst.executeQuery();

				int highscore = 0;
				if (rs.next())
					highscore = rs.getInt("score");

				music.addBeatmap(beatmap);
				music.addHighscore(highscore);
				
				pst.close();
				rs.close();
			}
			
		}

		con.close();

		return library;
	}
}
