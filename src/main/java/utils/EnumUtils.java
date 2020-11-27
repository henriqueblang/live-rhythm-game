package utils;

public class EnumUtils {
	
	private EnumUtils() {}
	
	public static enum Grades {
		C, B, A, S, SS
	}
	
	public static enum Types {
		NORMAL, LONG_START, LONG_MIDDLE, LONG_END, FLICK;
		
		private final int value;

		Types() {
			this.value = ordinal();
		}

		public int getValue() {
			return value;
		}
	}

	public static enum Scores {
		MISS(-1), NEUTRAL(0), BAD(25), GOOD(50), GREAT(100), PERFECT(150);

		private final int score;

		Scores(int score) {
			this.score = score;
		}

		public int getValue() {
			return score;
		}
	}
}
