package model.entities;

import math.Point2D;
import math.Vector2D;
import model.World;
import model.managers.EntityManager;
import model.pathfinding.NavGraph;

public class Player extends NavigatingEntity {
	private Point2D startLoc;
	private int points;
	private int deaths;
	
	public Player(double initX, double initY, double r, double velocity, World myWorld,
			NavGraph graph) {
		super(initX, initY,r, velocity, myWorld, graph);
		startLoc = new Point2D(initX, initY);
	}

	public boolean moveLeft;
	public boolean moveRight;
	public boolean moveUp;
	public boolean moveDown;
	
	@Override
	public void update(int delta) {
		Vector2D v = new Vector2D(0,0);
		if(moveLeft){
			v.plusEquals(new Vector2D(-1,0));
		}
		if(moveRight){
			v.plusEquals(new Vector2D(1,0));
		}
		if(moveUp){
			v.plusEquals(new Vector2D(0,1));
		}
		if(moveDown){
			v.plusEquals(new Vector2D(0,-1));
		}
		move(v, delta);
		
		//make it so the player wraps
		if(loc.getX() > myWorld.getWorldW())
			loc.setX(0);
		if(loc.getY() > myWorld.getWorldW())
			loc.setY(0);
		if(loc.getX() < 0)
			loc.setX(myWorld.getWorldW());
		if(loc.getY() < 0)
			loc.setY(myWorld.getWorldW());
		
		checkCollisons();
	}
	private void checkCollisons(){
		//for now the player is the only one that collides with anyone, so let it handle collisons
		for(Entity e : EntityManager.getInstance() ){
			//don't collide with self
			if(e != this){
				if(this.isCollided(e)){
					if(e instanceof Agent){
						loc.set(startLoc);
						e.resetToStart();
						deaths++;
					}
					if(e instanceof Prize){
						((Prize) e).catchIt();
						points++;
					}
				}
			}
		}
	}

	
	public int getPoints() {
		return points;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setPoints(int i) {
		points = i;	
	}
	public void setDeaths(int i) {
		deaths = i;		
	}
}
