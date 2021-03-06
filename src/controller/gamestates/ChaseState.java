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
import model.entities.factories.BulletFactory;
import model.entities.factories.EnemyFactory;
import model.entities.factories.TowerFactory;
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
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import controller.input.InputController;
import view.ConsoleView;
import view.CoordinateConverter;
import view.ExplosionView;
import view.MiniMap;
import view.NavGraphView;
import view.PlayerView;
import view.SpriteRenderer;
import view.TowerSelectBox;

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
	private NavGraph nav;
	private SoundManager sound_manager;
	private boolean showAgentCons;

	InputController inputBinds;
	TowerFactory tfactory;
	WaveManager waveManager;
	private TowerSelectBox towerBox;
	

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
			
			//Sprites
			spriteRender = new SpriteRenderer(translator, "res/configs/images.txt");
			
			PlayerManager player = new PlayerManager("res/configs/player.txt");
			BulletFactory bullet_factory = new BulletFactory("res/configs/bullets.txt");
			tfactory = new TowerFactory(bullet_factory, "res/configs/towers.txt");
			inputBinds = new InputController("res/configs/inputs.txt");
			EnemyFactory factory = new EnemyFactory("res/configs/enemies.txt");
			waveManager = new WaveManager("res/configs/waves.txt", factory, world, nav);
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
		//Tower Select Box
		towerBox = new TowerSelectBox();
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
		if(false){//debug
			navView.render(gc, g);
		}
		// then draw all entities
		Color[] agent_colors = {Color.blue, Color.green, Color.orange};//terrible
		Color[] con_colors = {Color.red, Color.pink, Color.black};//terrible
		int cur = 0;
		for (Entity e : EntityManager.getInstance()) {
			spriteRender.render(e, gc, g);
			if(e instanceof Agent){
				if(false){//debug
					navView.renderAgentPath((Agent)e, Color.blue, gc, g);
				}
				if(showAgentCons){
					navView.renderAgentConsidered((Agent)e, con_colors[cur], gc, g);
				}
				cur++;
			}
		}
		// Now draw all explosions
		(new ExplosionView()).render(g, gc, translator);
		camera.untranslateGraphics();
		
		// now draw all UI elements
		// Draw Player stats bar 
		new PlayerView().render(g, gc);
		
		//draw MiniMap
		//minimap.render(gc, g, camera, null);
		
		//Draw selected tower box
		towerBox.render(g,gc, translator, camera);
		
		//tower place warning
		if (inputBinds.getSelected() != null){
			g.drawString("You are about to place a tower of type: " + inputBinds.getSelected(), gc.getWidth()/4 - 50, 15);
		}
		
		//draw Console
		if(showLog)
			consoleView.render(ConsoleLog.getInstance(), gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int t)
			throws SlickException {
		EntityManager.getInstance().updateEntities(t);
		
		
		waveManager.update(t);
		
		// we have to tell the animations to update themselves
		//spriteRender.updateAnimations(t);
		
		// If player out of lives, he dead
		if(PlayerManager.getInstance().lives <= 0){
			game.enterState(2);
		}
		//check if no agents are left, and no waves are left, player wins
	}

	@Override
	public void keyPressed(int key, char c) {
		if (c == '`'){
			if(showLog){
				showLog = false;
			}else{
			showLog = true;
			}
		}
		inputBinds.selectTower(String.valueOf(c));
	}

	@Override
	public void keyReleased(int key, char c) {
	
	}

	public void mouseReleased(int button, int x, int y){
		if(inputBinds.getSelected() != null){
			placeTower(x, y);
			inputBinds.selectTower(null);
		}else if (towerBox.isUpgradePressed(x, y)){
			if(PlayerManager.getInstance().gold >= 50){
				PlayerManager.getInstance().gold -= 50;
				towerBox.selectedTower.sightRange += 5;
			}
		}else{
			Point2D loc = translator.screenToWorld((int)(x + camera.cameraX), (int)(y +camera.cameraY));
			Point p = nav.worldToTile(loc);
			loc = nav.tileToWorld(p.x, p.y);
			
			towerBox.selectedTower = EntityManager.getInstance().getTowerAt(loc);
		}
		
	}
	
	private void placeTower(int x, int y){
		int cost = tfactory.getCost(inputBinds.getSelected());
		if(PlayerManager.getInstance().gold < cost){
			return;//not enough money to place tower
		}
		Point2D loc = translator.screenToWorld((int)(x + camera.cameraX), (int)(y +camera.cameraY));
		if(!nav.blocked(loc)){
			//place towers in center of the tile
			Point p = nav.worldToTile(loc);
			//We need to make sure that it is still possible to make it to goal.
			AStarPathFinder a = new AStarPathFinder(nav, 9999, false);
			Point dest = nav.worldToTile(new Point2D(50,50));
			
			nav.setTile(loc, true); //pretend to block that place
			if(a.findPath(null, 0, 0, dest.x, dest.y) != null){
				loc = nav.tileToWorld(p.x, p.y);
				EntityManager.getInstance().addEntity(tfactory.makeTower(inputBinds.getSelected(), loc, world));
				
				//Repath
				EntityManager.getInstance().repath = true;
				PlayerManager.getInstance().gold -= cost;
			}else{//unblock if it wasn't possible
				nav.setTile(loc, false);
			}
		}
	}
}
