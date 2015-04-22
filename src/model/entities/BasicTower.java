package model.entities;

import math.Point2D;
import model.World;
import model.managers.ConsoleLog;
import model.managers.EntityManager;

public class BasicTower extends Entity{
	public int fireRate;
	private int fireTimer;
	public double sightRange;
	
	public BasicTower(Point2D initLoc, double sightRange, double r, int fireRate,
			World myWorld, String type) {
		super(initLoc.getX(), initLoc.getY(), 0, r, myWorld);
		
		this.fireRate = fireRate;
		fireTimer = fireRate;
		
		this.sightRange = sightRange;
		this.entity_class = type;
	}

	// Find the closest target and shoot it.
	public void update(int delta) {
		if(fireTimer < 0){
			for(Entity e : EntityManager.getInstance() ){
				if(e instanceof Agent){
					if(this.inSight(e)){
						fire(e);
						fireTimer = fireRate;
						break;
					}
				}
			}
		}else{
			fireTimer -= delta;
		} 		
	}
	
	private boolean inSight(Entity e) {
		double a = loc.getX() - e.getX();
		double b = loc.getY() - e.getY();
		double r = sightRange;
		r = r*r;
		double d = a*a + b*b;
		if((r > d)){
			return true;
		}
		return false;
	}

	public void fire(Entity e){
		ConsoleLog.getInstance().log("Fired at id: " + e.getId());
		e.die();
	}
}
