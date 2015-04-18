package model.entities.factories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import math.Point2D;
import model.World;
import model.entities.Agent;
import model.entities.BasicTower;
import model.entities.NavigatingEntity;
import model.pathfinding.NavGraph;

import org.yaml.snakeyaml.Yaml;

public class TowerFactory {
	Map<String, Map<String, Number>> towers;
	public TowerFactory(String config) throws FileNotFoundException{
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(new File(config));
		towers = (Map<String,Map<String, Number>>) yaml.load(input);
		System.out.println(towers);
	}
	public BasicTower makeTower(String type, Point2D initLoc, World w){
		Map<String, Number> tower = towers.get(type);
		//System.out.println(enemy);
		return new BasicTower(initLoc,(double)tower.get("sightRange"),0.5,(int)tower.get("fireRate"), w, type);
	}
}
