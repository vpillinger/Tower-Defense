package model.entities.agentstates;

import math.Vector2D;
import model.entities.Agent;
import model.entities.Entity;

public abstract class AgentState extends EntityState {

	public abstract void enter(Agent e);

	public abstract void execute(Agent e, int delta);

	public abstract Vector2D computeHeading(Agent e);

	public abstract void exit(Agent e);
	
	public abstract void resetToStart(Agent e);

	
	//these should point these to the Agent versions,
	//if the Entity is an Agent
	@Override
	public void enter(Entity e) {
	}
	@Override
	public void execute(Entity e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void computeHeading(Entity e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exit(Entity e) {
		// TODO Auto-generated method stub
		
	}
	


}
