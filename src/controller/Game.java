package controller;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import controller.gamestates.ChaseState;
import controller.gamestates.LoseState;
import controller.gamestates.WinState;


public class Game extends StateBasedGame {
	
		public Game ()
	{
		super("Game");
	}

		@Override
		public void initStatesList(GameContainer gc) throws SlickException {
			// TODO Auto-generated method stub
			addState(new ChaseState());
			addState(new WinState());
			addState(new LoseState());
		}
}
