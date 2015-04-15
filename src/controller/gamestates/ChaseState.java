package controller.gamestates;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Scanner;

import math.Point2D;
import model.World;
import model.entities.Agent;
import model.entities.BasicTower;
import model.entities.Entity;
import model.entities.Player;
import model.entities.Prize;
import model.entities.agentstates.WanderState;
import model.entities.factories.EnemyFactory;
import model.managers.ConsoleLog;
import model.managers.EntityManager;
import model.managers.PlayerManager;
import model.managers.SoundManager;
import model.managers.WaveManager;
import model.pathfinding.NavGraph;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import view.ConsoleView;
import view.CoordinateConverter;
import view.MiniMap;
import view.NavGraphView;
import view.SpriteRenderer;

public class ChaseState extends BasicGameState {

	private TiledMap map;
	private Camera camera;
	private SpriteRenderer spriteRender;
	
	private MiniMap minimap;
	private ConsoleView consoleView;
	private NavGraphView navView;
	private boolean showNavGrid;
	private boolean showAgentPaths;
	private boolean showLog;
	
	private CoordinateConverter translator;
	
	private World world;
	private Player player;
	private Prize prize;
	private NavGraph nav;
	private SoundManager sound_manager;
	private boolean showAgentCons;

	

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		//Load Sound
		//sound_manager = new SoundManager();
		//Load tilemap 
		try{
			Scanner s = new Scanner(new File("res/config.txt"));
			String map_file = s.nextLine();
			map = new TiledMap(map_file);
			ConsoleLog.getInstance().log("Map Loaded");
			//Load Camera
			camera = new Camera(gc, map);
			ConsoleLog.getInstance().log("Camera Loaded");
			//Load World stuff
			world = new World(100, 100, new Point2D(50,50));
			translator = new CoordinateConverter(world.getWorldW(),
					world.getWorldH(), camera.mapWidth, camera.mapHeight);
			nav = new NavGraph(map, world);
			
			PlayerManager player = new PlayerManager("res/configs/player.txt");
			EnemyFactory factory = new EnemyFactory("res/configs/enemies.txt");
			WaveManager waveManager = new WaveManager("res/configs/waves.txt", factory, world, nav);
			waveManager.spawnWave();
		}catch(Exception e){
			// well, we are in trouble now...
			e.printStackTrace();
		}
		
		
		//Load Entities
		
		//Load UI Components
		//NavGraphView
		navView = new NavGraphView(nav, map);
		//Minimap
		Image miniMapImage = new Image("/res/minimap.png");
		minimap = new MiniMap(miniMapImage, world);
		//Console
		consoleView = new ConsoleView();
		//Sprites
		spriteRender = new SpriteRenderer(translator);
		
		ConsoleLog.getInstance().log("Entities Loaded");
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		// first center camera on player
		camera.centerOn(map.getWidth()*map.getTileWidth()/2, map.getHeight()*map.getTileHeight()/2);
		camera.drawMap();
		
		//Translate graphics for everything not in UI
		camera.translateGraphics();
		// then draw navGraph
		if(showNavGrid){
			navView.render(gc, g);
		}
		// then draw all entities
		Color[] agent_colors = {Color.blue, Color.green, Color.orange};//terrible
		Color[] con_colors = {Color.red, Color.pink, Color.black};//terrible
		int cur = 0;
		for (Entity e : EntityManager.getInstance()) {
			spriteRender.render(e, gc, g);
			if(e instanceof Agent){
				if(showAgentPaths){
					navView.renderAgentPath((Agent)e, agent_colors[cur], gc, g);
				}
				if(showAgentCons){
					navView.renderAgentConsidered((Agent)e, con_colors[cur], gc, g);
				}
				cur++;
			}
		}
		camera.untranslateGraphics();
		
		// now draw all UI elements
		
		//draw MiniMap
		//minimap.render(gc, g, camera, null);
		
		//draw Console
		if(showLog)
		consoleView.render(ConsoleLog.getInstance(), gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int t)
			throws SlickException {
		EntityManager.getInstance().updateEntities(t);
		
		// we have to tell the animations to update themselves
		spriteRender.updateAnimations(t);
		
		// If player out of lives, he dead
		if(PlayerManager.getInstance().lives <= 0){
			game.enterState(2);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
	
	}

	@Override
	public void keyReleased(int key, char c) {
	
	}

}