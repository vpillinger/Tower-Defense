package model.pathfinding;

import java.awt.Point;

import math.Point2D;
import math.Vector2D;
import model.entities.Agent;
import model.entities.MovingEntity;
import model.entities.NavigatingEntity;
import model.pathfinding.AStarPathFinder;

import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

public class PathFinderHolder {
	private AStarPathFinder path_finder;
	private Path cur_path;
	private int cur_step;
	private NavigatingEntity e;
	
	public PathFinderHolder(AStarPathFinder path_finder, NavigatingEntity e){
		this.path_finder = path_finder;
		this.e = e;
	}
	public void generatePath(Point2D dest){
		Point cur_tile = e.getNav().worldToTile(e.getLoc());
		Point dest_tile = e.getNav().worldToTile(dest);
		cur_path = path_finder.findPath(e, cur_tile.x, cur_tile.y, dest_tile.x, dest_tile.y);
		//attempt to smooth path
		//cur_path = smoothPath(cur_path);
		cur_step = 0;
	}
	public void generatePath(Point p) {
		Point cur_tile = e.getNav().worldToTile(e.getLoc());
		cur_path = path_finder.findPath(e, cur_tile.x, cur_tile.y, p.x, p.y);
		//attempt to smooth path
		//cur_path = smoothPath(cur_path);
		cur_step = 0;
	}
	
	public Path smoothPath(Path p){
		Path smoothPath = new Path();
		if(p == null || p.getLength() == 0){
			return smoothPath;
		}
		Step cur_step = p.getStep(0);
		smoothPath.appendStep(cur_step.getX(), cur_step.getY());
		Path lastSP = null;
		Path strP = null;
		for(int i=1; i < p.getLength(); i++){
			Step next_step = p.getStep(i);
			//if there is open space between the two lines
			strP = getOpenStraightPath(new Point(cur_step.getX(),cur_step.getY()), new Point(next_step.getX(),next_step.getY()));
			
			if(strP == null){
				//System.out.println(i);
				for(int j=0; j< lastSP.getLength(); j++){
					smoothPath.appendStep(lastSP.getX(j), lastSP.getY(j));
				}
				cur_step = next_step; 
			}else{
				lastSP = strP;
			}
		}
		if(strP != null){
			for(int j=0; j< strP.getLength(); j++){
				smoothPath.appendStep(strP.getX(j), strP.getY(j));
			}
		}
		return smoothPath;
	}
	// TODO -- This need to special case for wrapping
	public Path getOpenStraightPath(Point tail, Point head){
		//System.out.println("tail:" + tail + " head:" + head );
		Path strP = new Path();
		strP.appendStep(tail.x, tail.y);
		int x_dif = tail.x - head.x;
		int y_dif = tail.y - head.y;
		//System.out.println(x_dif + " " + y_dif);
		while(x_dif != 0 || y_dif != 0){
			//System.out.println("x: " + tail.x + " y: " + tail.y);
			int cx = tail.x;
			int cy = tail.y;
			//Handle the edges ?????????
			
			//get one tile closer
			tail.x -= Integer.signum(x_dif);
			tail.y -= Integer.signum(y_dif);
			if(e.getNav().blocked(cx, cy, tail.x, tail.y)){
				return null;
			}
			strP.appendStep(tail.x, tail.y);
			x_dif -= Integer.signum(x_dif);
			y_dif -= Integer.signum(y_dif);
		}
		return strP;
	}
	public void walkPath(int delta){
		if(cur_path == null || cur_step >= cur_path.getLength()){
			return;
		}
		Step curStep = cur_path.getStep(cur_step);
		Point2D curDest = e.getNav().tileToWorld(curStep.getX(), curStep.getY());
		//System.out.println(curStep.getX() + " " + curStep.getY());
		
		// Move to next location
		Vector2D move_vect = e.getStraightToPoint(curDest.getX(), curDest.getY());
		//System.out.println(move_vect);
		//move_vect.scalePlusEquals(-1, new Vector2D(e.getLoc().getX(), e.getLoc().getY()));
		//System.out.println(move_vect);
		e.move(move_vect, delta);
		
		// If at location, move to next step
		if(e.contains(curDest)){
			//System.out.println("next step");
			cur_step++;
		}
	}
	//Start Getters/Setters -------------
	public Path getCur_path() {
		return cur_path;
	}

	public void setCur_path(Path cur_path) {
		this.cur_path = cur_path;
	}

	public AStarPathFinder getPath_finder() {
		return path_finder;
	}

	public void setPath_finder(AStarPathFinder path_finder) {
		this.path_finder = path_finder;
	}
	public boolean done() {
		if(cur_path == null){
			return true;
		}
		return (cur_step >= cur_path.getLength());
	}
}
