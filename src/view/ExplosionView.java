package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import model.managers.ExplosionManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class ExplosionView{
	HashMap<Circle, Integer> activeExplosions = new HashMap<Circle, Integer>();
	public void render(Graphics g, GameContainer c, CoordinateConverter t){
		Iterator i = ExplosionManager.getInstance().iterator();
		while(i.hasNext()){
			
			Circle cir = (Circle) i.next();
			float r = (t.worldToScreen(cir.radius, 0)).x;
			float x = (t.worldToScreen(cir.getCenterX(), 0)).x;
			float y =  (t.worldToScreen(0, cir.getCenterY())).y;
			System.out.println(x + " " + y + " " + r);
			activeExplosions.put(new Circle(x,y,r), 60);
			i.remove();
		}
		drawExplosions(1, g);
	}
	
	private void drawExplosions(int delta, Graphics g){
		g.setColor(Color.darkGray);
		for(Circle c : activeExplosions.keySet()){
			g.fill(c);
			activeExplosions.replace(c, activeExplosions.get(c) - delta);
			if(activeExplosions.get(c) < 0){
				activeExplosions.remove(c);
			}
		}
		g.setColor(Color.white);
	}
}
