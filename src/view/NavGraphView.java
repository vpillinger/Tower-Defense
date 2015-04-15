package view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import model.entities.Agent;
import model.pathfinding.NavGraph;

public class NavGraphView {
	private NavGraph nav;
	private TiledMap map;
	
	public NavGraphView(NavGraph nav, TiledMap map) {
		super();
		this.nav = nav;
		this.map = map;
	}
	public void render(GameContainer gc, Graphics g){
		for(int ty = 0; ty < map.getHeight(); ty++){
			for(int tx = 0; tx < map.getWidth(); tx++){
				int cur_x = tx * map.getTileWidth() + map.getTileWidth()/2;
				int cur_y = ty * map.getTileHeight() + map.getTileHeight()/2;
				if(!nav.blocked(null,tx,ty)){
					for(Point p: getNeighbors(tx, ty)){
						int nx = p.x * map.getTileWidth() + map.getTileWidth()/2;
						int ny = p.y * map.getTileHeight() + map.getTileHeight()/2;
						g.drawLine(cur_x, cur_y, nx, ny);
						//System.out.println("Line: " + cur_x + " " + cur_y+  " " + nx + " " + ny);
					}
				}
			}
		}	
	}
	public void renderAgentPath(Agent e, Color c, GameContainer gc, Graphics g){
		Path p = e.getPathing().getCur_path();
		int w = nav.getWidthInTiles();
		int h = nav.getHeightInTiles();
		if(p != null){
			Color last = g.getColor();
			g.setColor(c);
			for(int i=0; i<p.getLength()-1;i++){
				Step s = p.getStep(i);
				int cur_x = s.getX() * map.getTileWidth() + map.getTileWidth()/2;
				int cur_y = s.getY() * map.getTileHeight() + map.getTileHeight()/2;
				
				Step s2 = p.getStep(i+1);
				
				//Handle the edge cases //TODO -Draw the lines
				if(s.getX() == w -1 && s2.getX() == 0){
					
				}else if (s.getX() == 0 && s2.getX() == w -1){
					
				}else if(s.getY() == h -1 && s2.getY() == 0){
					
				}else if (s.getY() == 0 && s2.getY() == h -1){
					
				}else{
					int nex_x = s2.getX() * map.getTileWidth() + map.getTileWidth()/2;
					int nex_y = s2.getY() * map.getTileHeight() + map.getTileHeight()/2;
					g.drawLine(cur_x, cur_y, nex_x, nex_y);
				}
			}
			g.setColor(last);
		}
	}
	public void renderAgentConsidered(Agent e, Color c, GameContainer gc, Graphics g){
		Path p = e.getPathing().getCur_path();
		if(p != null){
			Color last = g.getColor();
			g.setColor(c);
			boolean[][] blocked = nav.getBlocked();
			boolean[][] matrix = new boolean[blocked.length][blocked[0].length];
			for(int i=0; i<matrix.length; i++){
				Arrays.fill(matrix[i], true);
			}
			for(Point x : e.getPathing().getPath_finder().getClosed()){
				matrix[x.y][x.x] = false;
			}
			nav.setBlocked(matrix);
			render(gc, g);
			nav.setBlocked(blocked);
			g.setColor(last);
		}else{
			//System.out.println("no path");
		}
	}
	// Returns list starting at NW and going seq across each row
	public ArrayList<Point> getNeighbors(int x, int y){
		//System.out.println("Origin:" + x + " " + y);
		ArrayList<Point> neighbors = new ArrayList<Point>();
		for(int ny=1; ny > -2; ny--){
			for(int nx=-1; nx < 2; nx++){
				//make sure stay in bounds
				Point p = wrapTiles(nx+x,ny+y);
				//System.out.println((p.x) + " " + (p.y));
				if(y == 49){
					System.out.println((p.x) + " " + (p.y));
					System.out.println(nav.blocked(x, y, p.x, p.y));
				}
				if(!nav.blocked(x, y, p.x, p.y)){
					neighbors.add(new Point(nx+x, ny+y));
				}
			}
		}
		return neighbors;
	}
	private Point wrapTiles(int x, int y){
		//--- Add in wrapping around edges ---//
		if(x < 0){
			x = map.getWidth() - 1;
		}
		if(x > map.getWidth() - 1){
			x = 0;
		}
		if(y < 0){
			y = map.getHeight() - 1;
		}
		if(y > map.getHeight() - 1){
			y = 0;
		}
		return new Point(x,y);
	}
}
