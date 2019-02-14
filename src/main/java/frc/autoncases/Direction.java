package frc.autoncases;

public enum Direction {
	FORWARD(0), RIGHT(90), REVERSE(180), LEFT(-90),
	FORWARD_LEFT(-45), FORWARD_RIGHT(45);

	public int degrees;

	private Direction(int degrees) {
		this.degrees = degrees;
	}
}