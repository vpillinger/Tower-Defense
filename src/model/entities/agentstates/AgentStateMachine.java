package model.entities.agentstates;

import model.entities.Agent;

public class AgentStateMachine {

	private AgentState cur_state;
	private Agent my_agent;

	public AgentStateMachine(Agent agent){
		my_agent = agent;
		cur_state = new AgentChaseTarget();
	}
	
	//initialization
	public void setCurrentState(AgentState new_state){
		cur_state = new_state;
	}
	
	public void update(int delta){
		if(cur_state != null){
			cur_state.execute(my_agent, delta);
		}
	}
	
	public void ChangeState(AgentState state){
		if(state == null){
			return;
		}
		
		//exit the previous state
		cur_state.exit(my_agent);
		
		//change to the new state
		cur_state = state;
		
		//enter the new state
		cur_state.enter(my_agent);
	}
	
}
