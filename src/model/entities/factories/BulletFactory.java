package model.entities.factories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import math.Point2D;
import model.World;
import model.entities.Agent;
import model.entities.Bullet;
import model.entities.Entity;
import model.entities.NavigatingEntity;
import model.pathfinding.NavGraph;

import org.yaml.snakeyaml.Yaml;

public class BulletFactory {
	
	Map<String, Map<String,  Number>> bullets;
	public BulletFactory(String config) throws FileNotFoundException{
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(new File(config));
		bullets = (Map<String,Map<String, Number>>) yaml.load(input);
		System.out.println(bullets);
	}
	public Bullet makeBullet(String type, Point2D initLoc, World w, Entity target){
		Map<String, Number> bullet = bullets.get(type);
		//System.out.println(enemy);
		return new Bullet(initLoc.getX(),initLoc.getY(),.25, (double)bullet.get("speed"), w, (double) bullet.get("aoe"), 
				(int)bullet.get("damage"),
				type, target);
	}
}
