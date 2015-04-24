package view;

import model.managers.PlayerManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class PlayerView {
	
	public void render(Graphics g, GameContainer gc){
		int x = gc.getWidth()/2 - 50;
		int y = gc.getHeight() - 50;
		int width = 100;
		int height = 50;
		// Draw The box
		g.setColor(new Color(.25f,.25f,.25f,.5f));
		g.fill(new Rectangle(x, y, width, height));
		g.setColor(Color.white); 
		//Draw text
		g.drawString("Gold: " + PlayerManager.getInstance().gold, x + 1, y);
		g.drawString("Lives: " + PlayerManager.getInstance().lives, x + 1, y + 15);
	}
}
