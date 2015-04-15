package model.entities;

import org.newdawn.slick.util.pathfinding.heuristics.ClosestHeuristic;

import math.Point2D;
import math.Vector2D;
import model.World;
import model.pathfinding.AStarPathFinder;
import model.pathfinding.NavGraph;
import model.pathfinding.PathFinderHolder;

public abstract class NavigatingEntity extends MovingEntity {
	
	protected NavGraph my_nav;
	public PathFinderHolder pathing;
	
	public NavigatingEntity(double initX, double initY, double r, double speed,
			World myWorld, NavGraph graph) {
		super(initX, initY, r, speed, myWorld);
		my_nav = graph;
		setPathing(new PathFinderHolder(new AStarPathFinder(my_nav, Integer.MAX_VALUE, true, new ClosestHeuristic()), this));
	}
	
	public void move(Vector2D v, int delta){
		velocity = v;
		if(v.magnitude() > Vector2D.TOL){
			v.normalize();
			velocity = v.times(speed * delta);
			Point2D newLoc = new Point2D((loc.getX() + velocity.getX()),
					(loc.getY() + velocity.getY()));
			wrap(newLoc);
			if(!my_nav.blocked(newLoc)){
				this.setLoc(newLoc);
			}
			direction = velocity.angle();
		}
	}
	
	// Getters And Setters ---------------------------
	public PathFinderHolder getPathing() {
		return pathing;
	}

	public void setPathing(PathFinderHolder pathing) {
		this.pathing = pathing;
	}

	public NavGraph getNav() {
		return my_nav;
	}
}
