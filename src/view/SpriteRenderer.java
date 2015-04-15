package view;

import java.awt.Point;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;




import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;

import model.entities.*;


public class SpriteRenderer {
	
	private CoordinateConverter convert;
	private Animation playerImg;
	private Image agentImg;
	private Animation prizeImg;
	
	public SpriteRenderer(CoordinateConverter convert){
		this.convert = convert;
		try {
			playerImg = new Animation(new SpriteSheet("/res/player.png",32,32), 100);
			agentImg = new Image("/res/agent.png");
			prizeImg = new Animation(new SpriteSheet("/res/prize.png",32,32),100);
			
			playerImg.setAutoUpdate(false);
			prizeImg.setAutoUpdate(false);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	public void updateAnimations(int delta){
		playerImg.update(delta);
		prizeImg.update(delta);
	}
	
	public void render(Entity e, GameContainer gc, Graphics g){
		if(e instanceof Player){
			renderH((Player)e, gc, g);
		}else if(e instanceof Agent){
			renderH((Agent)e,gc,g);
		}else if(e instanceof Prize){
			renderH((Prize)e, gc, g);
		}else if(e instanceof BasicTower){
			renderH((BasicTower)e,gc,g);
		}
	}

	private void drawImage(Image i, Entity e){
		Point pc = convert.worldToScreen(e.getX(), e.getY());
		
		float rotation = (float) Math.toDegrees(e.getDirection());
		i.setRotation(- rotation);//slick2D does rotation the opposite way
		i.drawCentered((float) pc.x, (float) pc.y);
	}
	
	private void drawMovingAnimatedSprite(Animation a, MovingEntity e){
		Point pc = convert.worldToScreen(e.getX(), e.getY());
		//rotate before drawing
		float rotation = (float) Math.toDegrees(e.getDirection());
		a.getCurrentFrame().setRotation(-rotation + 90);
		
		a.getCurrentFrame().drawCentered(pc.x, pc.y);
		if(e.getVelocity().magnitudeSq() > 0){
			a.start();
		}else{
			a.stop();
			a.restart();
		}
	}
	
	private void drawStationaryAnimatedSprite(Animation a, Entity e){
		Point pc = convert.worldToScreen(e.getX(), e.getY());
		//a.draw(pc.x,pc.y);
		a.getCurrentFrame().drawCentered(pc.x, pc.y);
	}
	
	private void renderH(Agent e, GameContainer gc, Graphics g) {
		drawImage(agentImg,e);
	}
	private void renderH(Player e, GameContainer gc, Graphics g) {
		drawMovingAnimatedSprite(playerImg, e);
	}
	private void renderH(Prize e, GameContainer gc, Graphics g) {
		drawStationaryAnimatedSprite(prizeImg, e);
	}
	private void renderH(BasicTower e, GameContainer gc, Graphics g){
		Point pc = convert.worldToScreen(e.getX(), e.getY());
		Point oval = convert.worldToScreen(e.getRadius(), e.getRadius());
		Point sight = convert.worldToScreen(e.sightRange, e.sightRange);
		g.drawOval(pc.x-oval.x/2,pc.y-oval.x/2, oval.x, oval.x);
		g.draw(new Circle(pc.x, pc.y, sight.x));
	}
	
}
