package model.entities;

import org.newdawn.slick.util.pathfinding.Mover;

import math.Point2D;
import math.Vector2D;
import model.World;
import model.managers.ConsoleLog;
import model.managers.EntityManager;
import model.pathfinding.NavGraph;

public abstract class Entity implements Mover {
	private int id;
	private static int nextId;
	
	public boolean toDie;
	
	protected Point2D loc;
	protected double direction;
	protected double radius;
	public World myWorld;
	
	protected String entity_class;
	
	public Entity(double initX, double initY, double direction,double r, World myWorld){
		setSelf(initX, initY, r, direction, myWorld);
	}
	public Entity(double initX, double initY, double r, World myWorld){
		setSelf(initX, initY, r, 0, myWorld);
	}
	private void setSelf(double initX, double initY, double r, double direction, World myWorld){
		loc = new Point2D(initX, initY);
		this.direction = direction;
		radius = r;
		this.myWorld = myWorld;
		//keep a unique id for every Entity
		id = (nextId++);
	}
	
	public abstract void update(int delta);
	
	//whether this entity has collided with another entity
	public boolean isCollided(Entity e){
		double a = loc.getX() - e.getX();
		double b = loc.getY() - e.getY();
		double r = radius + e.getRadius();
		r = r*r;
		double d = a*a + b*b;
		if((r > d)){
			ConsoleLog.getInstance().log("Collision Detected: D^2=" + d + "R^2=" + r );
			return true;
		}
		return false;
	}
	
	//whether the entity contains the point
	public boolean contains(Point2D p){
		double a = loc.getX() - p.getX();
		double b = loc.getY() - p.getY();
		double r = radius * radius;
		double d = a*a + b*b;
		
		return ( r > d );
	}
	
	public Point2D wrap(Point2D p){
		double w = this.getWorld().getWorldW();
		if(p.getX() < 0){
			p.setX( w - 0.001);
		}else if(p.getX() >= w){
			p.setX(0);
		}
		double h = this.getWorld().getWorldH();
		if(p.getY() < 0){
			p.setY(h - 0.001);
		}else if(p.getY() >= h){
			p.setY(0);
		}
		return p;
	}
	
	public void die(){
		toDie = true;
	}

	//Start Getters/Setters ---------------------------------------
	public double getX(){
		return loc.getX();
	}
	public double getY(){
		return loc.getY();
	}
	public void setX(double x){
		loc.setX(x);
	}
	public void setY(double y){
		loc.setY(y);
	}
	public Point2D getLoc() {
		return loc;
	}
	public void setLoc(Point2D loc) {
		this.loc.set(loc);
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public int getId() {
		return id;
	}
	public double getDirection(){
		return direction;
	}
	public World getWorld(){
		return myWorld;
	}

	public void resetToStart() {
		// TODO Auto-generated method stub
		
	}
	public String getType() {
		return entity_class;
	}
}
