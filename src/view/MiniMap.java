package view;

import java.awt.Point;

import model.World;
import model.entities.Entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import controller.gamestates.Camera;

public class MiniMap {

   private Image miniMap;
   private int x;
   private int y;
   private CoordinateConverter convertWtoM;
   
   public MiniMap(Image map, World w) {
	   miniMap = map;
	   convertWtoM = new CoordinateConverter(w.getWorldW(), w.getWorldH(),128, 128);
	  
   }


   public void render(GameContainer gc, Graphics g, Camera c, Entity e) {
	  x = gc.getWidth() - 129;
	  y = gc.getHeight() - 129;
	  //Draw MiniMap image
	  miniMap.draw(x, y);
	  //Draw player dot
	  Point p = convertWtoM.worldToScreen(e.getLoc());
	  g.drawOval(x + p.x, y + p.y, 1, 1);
	  
	  //Draw Screen rect
	  int leftOfMini = (int) (c.cameraX * 128.0/c.mapWidth);
	  int topOfMini = (int) (c.cameraY * 128.0/c.mapHeight);
	  p = new Point(leftOfMini, topOfMini);
	  Color col = g.getColor();
	  g.setColor(Color.white);
	  g.drawRect(x + p.x, y + p.y, 128 / ((float)(c.mapWidth)/gc.getWidth()), 128 / ((float)(c.mapHeight)/gc.getHeight()));
	  g.setColor(col);
   }
}