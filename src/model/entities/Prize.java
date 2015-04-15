package model.entities;

import model.World;
import model.pathfinding.NavGraph;

public class Prize extends Entity {
	
	public boolean caught;
	
	public Prize(double initX, double initY, double r, World myWorld) {
		super(initX, initY, r, myWorld);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(int delta) {
		// do nothing
	}

	public void catchIt() {
		caught = true;
		//when caught give new random location
		//loc.setX(myWorld.getWorldW() * Math.random());
		//loc.setY(myWorld.getWorldH() * Math.random());
	}
	
	
}
