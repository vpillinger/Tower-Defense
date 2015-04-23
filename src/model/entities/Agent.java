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
	private Point2D target;
	private Point2D startLoc;
	
	public double sight;
	
	
	public Agent(double initX, double initY, double r, double speed, int hp, World myWorld,
			NavGraph graph, String agentClass) {
		super(initX, initY, r, speed, myWorld, graph);
		
		startLoc = new Point2D(initX, initY);
		target = new Point2D(50,50);
		this.hp = hp;
		//it starts resting
		//my_state_machine.ChangeState(new RestState());

		// agentClass mainly going to be used for drawing
		this.entity_class = agentClass;
		
	}

	@Override
	public void update(int delta) {
		if(pathing.done()){
			//Make sure we contain the goal
			if(this.contains(target)){
				//We have reached the goal
				ConsoleLog.getInstance().log("Goal Reached");
				PlayerManager.getInstance().lives -= 1;
				die();
			}else{//else walk in straight line towards target
				//if collides with tower, kill tower
				//if kills tower, we should repath too.
				
			}
		}else{
			pathing.walkPath(delta);
		}
	}

	public Point2D getTarget() {
		return target;
	}

	public void setTarget(Point2D target) {
		this.target = target;
	}

	public Point2D getStartLoc() {
		return startLoc;
	}

}
