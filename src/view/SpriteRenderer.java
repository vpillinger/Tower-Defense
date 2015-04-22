package view;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;




import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.yaml.snakeyaml.Yaml;

import model.entities.*;


public class SpriteRenderer {
	
	private CoordinateConverter convert;
	private Map<String, Image> images;

	public SpriteRenderer(CoordinateConverter convert, String config) throws FileNotFoundException, SlickException{
		this.convert = convert;
		
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(new File(config));
		Map<String, String> image_files = (Map<String, String>) yaml.load(input);
		images = new LinkedHashMap<String, Image>(); 
		for(String key : image_files.keySet()){
			images.put(key, new Image(image_files.get(key)));
		}
		System.out.println(images);
	}
	/*public void updateAnimations(int delta){
		playerImg.update(delta);
		prizeImg.update(delta);
	}*/
	
	public void render(Entity e, GameContainer gc, Graphics g){
		if(images.containsKey(e.getType())){
			drawImage(images.get(e.getType()), e);
		}else{
			Point pc = convert.worldToScreen(e.getX(), e.getY());
			Point oval = convert.worldToScreen(e.getRadius(), e.getRadius());
			g.drawOval(pc.x-oval.x/2,pc.y-oval.x/2, oval.x, oval.x);
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
	
	private void renderH(BasicTower e, GameContainer gc, Graphics g){
		Point pc = convert.worldToScreen(e.getX(), e.getY());
		Point oval = convert.worldToScreen(e.getRadius(), e.getRadius());
		Point sight = convert.worldToScreen(e.sightRange, e.sightRange);
		g.drawOval(pc.x-oval.x/2,pc.y-oval.x/2, oval.x, oval.x);
		g.draw(new Circle(pc.x, pc.y, sight.x));
	}
	public Map<String, Image> getImages() {
		return images;
	}
	public void setImages(Map<String, Image> images) {
		this.images = images;
	}
	
}
