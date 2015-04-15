package model.entities;

import math.Point2D;
import math.Vector2D;
import model.World;
import model.pathfinding.NavGraph;

public abstract class MovingEntity extends Entity {
	
	protected double speed;
	protected Vector2D velocity;//change in speed at last update
	
	
	
	public MovingEntity(double initX, double initY, double r, double speed,World myWorld) {
		super(initX, initY,r, myWorld);
		this.speed = speed;
		this.velocity = new Vector2D(0,0);
	}
	
	public void move(Vector2D v, int delta){
		velocity = v;
		if(v.magnitude() > Vector2D.TOL){
			v.normalize();
			velocity = v.times(speed * delta);
			Point2D newLoc = new Point2D((loc.getX() + velocity.getX()),
					(loc.getY() + velocity.getY()));
			wrap(newLoc);
			this.setLoc(newLoc);
			direction = velocity.angle();
		}
	}
	
	public Vector2D getVelocity() {
		return velocity;
	}
	public double getSpeed(){
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public Vector2D getStraightToPoint(double tx, double ty){
		double cx = this.getX();
		double cy = this.getY();
		double mapx = this.getWorld().getWorldW();
		double dx = 0;
		if(Math.abs(tx - cx) < mapx - Math.abs(tx-cx)){
			dx = (tx-cx);
		}else{
			dx = -(tx-cx);
		}
		
		double mapy = this.getWorld().getWorldH();
		double dy = 0;
		if(Math.abs(ty - cy) < mapy - Math.abs(ty-cy)){
			dy = (ty-cy);
		}else{
			dy = -(ty-cy);
		}

		return new Vector2D(dx,dy);
	}
	
}
