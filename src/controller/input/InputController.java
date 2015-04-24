package controller.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class InputController {
	private Map<String, String> towerBinds;
	private String selected;
	
	public InputController(String config) throws FileNotFoundException{
		Yaml yaml = new Yaml();
		InputStream input = new FileInputStream(new File(config));
		towerBinds = (Map<String ,String>) yaml.load(input);
		//System.out.println(towerBinds);
	}
	public void selectTower(String c){
		if(towerBinds.containsKey(c)){
			selected = towerBinds.get(c);
		}
	}
	public String getSelected(){
		return selected;
	}
}
