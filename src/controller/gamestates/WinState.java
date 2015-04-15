package controller.gamestates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class WinState extends BasicGameState {
	private boolean newGame;
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		newGame = false;

	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g)
			throws SlickException {
		g.drawString("You Won", gc.getWidth()/3, gc.getHeight()/3);
		g.drawString("Press Enter to Play again!", gc.getWidth()/3, gc.getHeight()/3 + 20);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame game, int arg2)
			throws SlickException {
		if(newGame){
			//game.getState(0).init(arg0, game);
			newGame = false;
			game.enterState(0);
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 1;
	}
	

	@Override
	public void keyPressed(int key, char c) {
		if (key ==  Input.KEY_RETURN) {
			newGame = true;
		}
	}
}
