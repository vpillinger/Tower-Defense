package model.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import math.Point2D;
import model.World;
import model.entities.NavigatingEntity;
import model.entities.factories.EnemyFactory;
import model.pathfinding.NavGraph;

import org.yaml.snakeyaml.Yaml;

public class WaveManager {
	private Point2D goal;
	private int waveNum;
	private ArrayList<Map<String, Integer>> waves;
	private EnemyFactory factory;
	private World w;
	private NavGraph nav;
	
	public WaveManager(String config, EnemyFactory factory, World w, NavGraph nav) throws FileNotFoundException{
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(new File(config));
		Map<String, Object> map = (Map<String, Object>) yaml.load(input);
		
		goal = new Point2D((double)map.get("goal_x"), (double)map.get("goal_y"));
		waves = (ArrayList<Map<String,Integer>>)map.get("waves");
		
		this.factory = factory;
		this.w = w;
		this.nav = nav;
		waveNum = 0;
	}
	
	public void spawnWave(){
		Map<String, Integer> wave = waves.get(waveNum);
		System.out.println(wave);
		for(String type: wave.keySet()){
			for(int i=0; i < wave.get(type); i++){
				NavigatingEntity e = factory.makeEnemy(type, new Point2D(0,Math.random() * w.getWorldH()), w, nav);
				e.pathing.generatePath(w.getGoal());
				EntityManager.getInstance().addEntity(e);
			}
		}
		waveNum++;
	}
}
