package entity.ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import states.UIStates;
import utils.EnumUtils.Types;
import utils.GameUtils;

public class Note extends Rectangle {
	private int track;
	private double endY;

	private Types type;

	public Note(int track, Types type) {
		this(track, type, (1080 - (GameUtils.NOTE_WIDTH * GameUtils.TRACK_AMOUNT)) / 2, GameUtils.NOTE_WIDTH,
				GameUtils.DEFAULT_NOTE_HEIGHT, 650 + (GameUtils.DEFAULT_NOTE_HEIGHT / 2),
				UIStates.getInstance().getRoot());
	}

	public Note(int track, Types type, double firstTrackStartX, double width, double height, double endY, Pane root) {
		this.setWidth(width);
		this.setHeight(height);

		root.getChildren().add(this);

		this.track = track;
		this.type = type;
		this.endY = endY;

		double startX = 0;
		if (this.track == 0)
			startX = firstTrackStartX;
		else if (this.track == 1)
			startX = firstTrackStartX + (width * 1);
		else if (this.track == 2)
			startX = firstTrackStartX + (width * 2);
		else if (this.track == 3)
			startX = firstTrackStartX + (width * 3);

		this.setX(startX);
		this.setClip(new Rectangle(startX, 0, GameUtils.NOTE_WIDTH, endY));

		if (this.type == Types.NORMAL)
			this.setFill(Color.rgb(117, 247, 255));
		else if (this.type == Types.LONG_START || this.type == Types.LONG_END)
			this.setFill(Color.rgb(18, 214, 16));
		else if (this.type == Types.FLICK)
			this.setFill(Color.rgb(235, 166, 210));
		else if (this.type == Types.LONG_MIDDLE) {
			this.setFill(Color.rgb(18, 214, 16));
			this.setOpacity(0.5);
		}

	}

	public int getTrack() {
		return track;
	}

	public double getEndY() {
		return endY;
	}

	public Types getType() {
		return type;
	}
}
