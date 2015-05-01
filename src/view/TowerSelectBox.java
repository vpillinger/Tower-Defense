package view;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

import controller.gamestates.Camera;
import model.entities.BasicTower;

public class TowerSelectBox {
	public BasicTower selectedTower;
	int ty;
	int tx;
	
	public void render(Graphics g, GameContainer gc, CoordinateConverter c, Camera camera){
		if (selectedTower == null){
			return;
		}
		tx = gc.getWidth()/4 - 100;
		ty = gc.getHeight() - 50;
		int width = 150;
		int height = 50;
		// Draw The box
		g.setColor(new Color(.25f,.25f,.25f,.5f));
		g.fill(new Rectangle(tx, ty, width, height));
		g.setColor(Color.white); 
		//Draw text
		g.drawString("id: " + selectedTower.getId(), tx + 1, ty);
		g.drawString("Range: " + selectedTower.sightRange, tx + 1, ty + 15);
		g.drawString("FireRate: " + selectedTower.fireRate, tx + 1, ty + 30);
		g.setColor(new Color(.5f,.25f,.25f, .8f));
		g.fill(new Rectangle(tx,ty-20, 150, 20));
		g.setColor(Color.white);
		g.drawString("Upgrade: 50 gold" , tx, ty-15);
		
		//also draw tower range
		java.awt.Point p = c.worldToScreen(selectedTower.getLoc());
		int r = c.worldToScreen(selectedTower.sightRange, 0).x;
		camera.translateGraphics();
		g.draw(new Circle((float)p.x, (float)p.y, r));
		camera.untranslateGraphics();
	}
	
	public boolean isUpgradePressed(int x, int y){
		if(selectedTower == null){
			return false;
		}
		Rectangle rect = new Rectangle(tx,ty-20, 150, 20);
		if( rect.contains(x, y)){
			return true;
		}
		return false;
	}
}
