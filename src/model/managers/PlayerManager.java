package model.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class PlayerManager {
	private static PlayerManager manager;
	public int lives;
	public int gold;
	public int gold_gain;
	
	public static PlayerManager getInstance(){
		return manager;
	}
	public PlayerManager(String config) throws FileNotFoundException{
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(new File(config));
		Map<String, Object> map = (Map<String, Object>) yaml.load(input);
		lives = (int) map.get("lives");
		gold = (int) map.get("gold");
		gold_gain = (int) map.get("gold_gain");
		manager = this;
	}
}
