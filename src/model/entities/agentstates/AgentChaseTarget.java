package model.entities.agentstates;

import math.Vector2D;
import model.entities.Agent;

public class AgentChaseTarget extends AgentState {
	
	@Override
	public void enter(Agent e) {
		// clear the agents path, as it doesn't exist anymore
		e.getPathing().setCur_path(null);
	}

	@Override
	public void execute(Agent e, int delta) {
		// get hungrier and more tired
		e.rest -= delta;
		e.food -= delta;
		
		//if rest timer out then rest
		if(e.rest < 0){
			e.getMy_state_machine().ChangeState(new RestState());
		}
		
		//if out of food, eat
		if(e.food < 0){
			e.getMy_state_machine().ChangeState(new EatState());
		}	
		//move in the direction
		e.move(computeHeading(e), delta);
	}

	@Override
	public Vector2D computeHeading(Agent e) {
		return  new Vector2D(
				e.getTarget().getX() - e.getLoc().getX(), //x
				e.getTarget().getY() - e.getLoc().getY()); //y
	}

	@Override
	public void exit(Agent e) {
		// nothing special on exit
		
	}

	@Override
	public void resetToStart(Agent e) {
		// Do nothing in this state
		
	}

}
