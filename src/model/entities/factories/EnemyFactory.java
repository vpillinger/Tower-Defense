package model.entities.factories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import math.Point2D;
import model.World;
import model.entities.Agent;
import model.entities.NavigatingEntity;
import model.pathfinding.NavGraph;

import org.yaml.snakeyaml.Yaml;

public class EnemyFactory {
	Map<String, Map<String, Number>> agents;
	public EnemyFactory(String config) throws FileNotFoundException{
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(new File(config));
		agents = (Map<String,Map<String, Number>>) yaml.load(input);
		System.out.println(agents);
	}
	public NavigatingEntity makeEnemy(String type, Point2D initLoc, World w, NavGraph nav){
		Map<String, Number> enemy = agents.get(type);
		//System.out.println(enemy);
		Agent a = new Agent(initLoc.getX(),initLoc.getY(),.25,(double)enemy.get("speed"), 0,w,nav, type);
		a.hp = (int)enemy.get("hp");
		return a;
	}
}
