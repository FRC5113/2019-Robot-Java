package frc.autoncases;

public enum Direction {
	FORWARD(-90), RIGHT(0), REVERSE(90), LEFT(180),
	FORWARD_LEFT(135), FORWARD_RIGHT(45);

	public int degrees;

	private Direction(int degrees) {
		this.degrees = degrees;
	}
}