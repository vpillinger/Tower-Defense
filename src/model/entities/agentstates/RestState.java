package model.entities.agentstates;



import java.util.ArrayList;

import org.newdawn.slick.util.pathfinding.Path;

import math.Point2D;
import math.Vector2D;
import model.entities.Agent;
import model.managers.ConsoleLog;

public class RestState extends AgentState {
	private long timer;
	@Override
	public void enter(Agent e) {
		//log
		ConsoleLog.getInstance().log("Agent: " + e.getId() + " is going to rest" );
		
		Point2D[] rest_locs = {new Point2D(16,90), new Point2D(36,50), new Point2D(80,10)} ;
		int min = Integer.MAX_VALUE;
		Path best_path = null;
		ArrayList con_nodes = null;
		for(Point2D rest_loc : rest_locs){
			e.getPathing().generatePath(rest_loc);
			ConsoleLog.getInstance().log("Distance to rest location: " + rest_loc + " " + 
					e.getPathing().getPath_finder().getSearchDistance());
			if(e.getPathing().getPath_finder().getSearchDistance() < min){
				min = e.getPathing().getPath_finder().getSearchDistance();
				best_path = e.getPathing().getCur_path();
				con_nodes = e.getPathing().getPath_finder().saveClosed();
			}
		}
		ConsoleLog.getInstance().log("Desired Rest Node Distance: " + min);
		e.getPathing().setCur_path(best_path);
		e.getPathing().getPath_finder().setClosed(con_nodes);
		
		
		//rest for 1.5 seconds
		timer = 1500;
	}

	@Override
	public void execute(Agent e, int delta) {
		if(e.getPathing().done()){
			timer -= delta;
			//go back to wandering if done eating
			if(timer <= 0){
				e.getMy_state_machine().ChangeState(new WanderState());
			}
		}else{//else move down path
			e.getPathing().walkPath(delta);
		}
	}

	@Override
	public Vector2D computeHeading(Agent e) {
		//do not move while resting
		return new Vector2D(0,0);
	}

	@Override
	public void exit(Agent e) {
		//tell the agent that it doesn't need to rest for a 20 seconds
		e.rest = 20000;
		
	}

	@Override
	public void resetToStart(Agent e) {
		Point2D[] rest_locs = {new Point2D(16,90), new Point2D(36,50), new Point2D(80,10)} ;
		int min = Integer.MAX_VALUE;
		Path best_path = null;
		for(Point2D rest_loc : rest_locs){
			e.getPathing().generatePath(rest_loc);
			ConsoleLog.getInstance().log("Distance to rest location: " + rest_loc + " " + 
					e.getPathing().getPath_finder().getSearchDistance());
			if(e.getPathing().getPath_finder().getSearchDistance() < min){
				min = e.getPathing().getPath_finder().getSearchDistance();
				best_path = e.getPathing().getCur_path();
			}
		}
		e.getPathing().setCur_path(best_path);
	}

}
