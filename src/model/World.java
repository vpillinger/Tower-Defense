package model;

import math.Point2D;

public class World {
	private double worldH,worldW;//world width and world height
	private Point2D goal;
	
	public World(double worldH, double worldW, Point2D goal) {
		super();
		this.worldH = worldH;
		this.worldW = worldW;
		
		this.goal = goal;
	}

	public double getWorldH() {
		return worldH;
	}

	public double getWorldW() {
		return worldW;
	}

	public Point2D getGoal() {
		return goal;
	}

	public void setGoal(Point2D goal) {
		this.goal = goal;
	}
}
