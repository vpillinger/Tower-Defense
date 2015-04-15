package model.entities;

import java.awt.Point;

import math.Point2D;
import model.World;
import model.entities.agentstates.AgentStateMachine;
import model.entities.agentstates.RestState;
import model.managers.ConsoleLog;
import model.managers.PlayerManager;
import model.pathfinding.NavGraph;

public class Agent extends NavigatingEntity {
	private Entity target;
	private Point2D startLoc;
	private String agent_class;
	
	public double sight;
	
	
	public Agent(double initX, double initY, double r, double speed, double sight, World myWorld,
			NavGraph graph, String agentClass) {
		super(initX, initY, r, speed, myWorld, graph);
		
		startLoc = new Point2D(initX, initY);
		
		this.sight = sight;
		//it starts resting
		//my_state_machine.ChangeState(new RestState());

		// agentClass mainly going to be used for drawing
		this.agent_class = agentClass;
		
	}

	@Override
	public void update(int delta) {
		if(pathing.done()){
			//We have reached the goal
			ConsoleLog.getInstance().log("Goal Reached");
			PlayerManager.getInstance().lives -= 1;
			die();
		}else{
			pathing.walkPath(delta);
		}
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public Point2D getStartLoc() {
		return startLoc;
	}

}
