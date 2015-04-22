package model.pathfinding;

import java.awt.Point;

import math.Point2D;
import model.World;
import model.entities.Entity;
import model.pathfinding.AStarPathFinder;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class NavGraph implements TileBasedMap {
	private static final float sq2 = (float) Math.sqrt(2.0);
	private TiledMap map;
	private World world;
	private boolean[][] blocked;
	
	public NavGraph(TiledMap map, World world) {
		super();
		this.map = map;
		this.world = world;
		
		setBlocked(new boolean[map.getWidth()][map.getHeight()]);

		int layer = 1; 

		for(int i = 0; i < map.getHeight(); i++) {
		    for(int j = 0; j < map.getWidth(); j++) {
		        int tileID = map.getTileId(j, i, layer);
		        getBlocked()[i][j] = (tileID != 0);
		    }
		}
	}

	public Point worldToTile(Point2D cur) {
		int x = (int) ((map.getWidth() * map.getTileWidth())/world.getWorldW() * cur.getX());
		int y = (int) ((map.getHeight() * map.getTileHeight())/world.getWorldH() * cur.getY());
		//System.out.println(x/map.getTileWidth() + " " + (map.getHeight() - y/map.getTileHeight()));
		// we have to do the extra -1 on y to account for rounding error from integer division
		return wrapTiles(x/map.getTileWidth(), map.getHeight() - y/map.getTileHeight() - 1);
	}
	public Point2D tileToWorld(int x, int y) {
		double wx = ((x + 0.5)/map.getWidth())*world.getWorldW();
		double wy = world.getWorldH() - ((y + 0.5)/map.getHeight())*world.getWorldH();
		//System.out.println(wx + " " + wy);
		return new Point2D(wx,wy);
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
	public Point getRandomOpenTile(){
		boolean block = true;
		int x = 0;
		int y = 0;
		while(block){
			 x = (int) (Math.random() * map.getWidth());
			 y = (int) (Math.random() * map.getHeight());
			block = getBlocked()[y][x];
		}
		return new Point(x,y);
	}
	public boolean blocked(Point2D cur){
		Point mapCord = worldToTile(cur);
		return getBlocked()[mapCord.y][mapCord.x];
	}

	@Override
	public boolean blocked(PathFindingContext c, int tx, int ty) {
		// Only handles AStar for now
		if(c instanceof AStarPathFinder){
			AStarPathFinder finder = (AStarPathFinder) c;
			// first case
			if(finder.getCurrentX() == -1 && finder.getCurrentY() == -1){
				return blocked[ty][tx];
			}
			// If both x and y are different, then it must be diagonal
			return blocked(finder.getCurrentX(),finder.getCurrentY(), tx, ty);
		}
		return blocked[ty][tx];
	}

	@Override
	public float getCost(PathFindingContext c, int tx, int ty) {
		// We have to weight the diagonal lines more than the straight lines
		if(c instanceof AStarPathFinder){
			AStarPathFinder finder = (AStarPathFinder) c;
			// If both x and y are different, then it must be diagonal
			if(finder.getCurrentX() != tx && finder.getCurrentY() != ty){
				return sq2;
			}
		}
		return 1;
	}

	@Override
	public int getHeightInTiles() {
		return map.getHeight();
	}

	@Override
	public int getWidthInTiles() {
		return map.getWidth();
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
		//System.out.println("Not Implemented");
	}

	public boolean blocked(int cx, int cy, int tx, int ty) {
		if(getBlocked()[ty][tx]){
			return true;
		}
		// Don't allow 'corner cutting'
		return checkCornerCut(cx,cy,tx,ty);
	}
	public boolean blocked(int x, int y){
		// If out of bounds, same as blocked
		if(x < 0 || x > blocked[0].length - 1 || y < 0 || y > blocked.length -1){
			return true;
		}
		//Point p = wrapTiles(x,y);
		return blocked[y][x];
	}
	private boolean checkCornerCut(int cx, int cy, int tx, int ty){
		
		boolean cornerBlocked = false;
		if(cx != tx && cy != ty){
			//Edge Cases --literally
			if(cx == map.getWidth() - 1 && tx == 0){
				if(blocked(0, ty)){
					cornerBlocked = true;
				}
			}else if(cx == 0 && tx == map.getWidth() - 1){
				if(blocked(map.getWidth() - 1, ty)){
					cornerBlocked = true;
				}
			}else{//General Cases
				if(cx < tx){
					if(blocked(tx - 1,ty)){
						cornerBlocked = true;
					}
				}else{
					if(blocked(tx + 1, ty)){
						cornerBlocked = true;
					}
				}
			}
			// Edge cases --literally
			if(cy == map.getHeight() - 1 && ty == 0){
				if(blocked(tx, 0)){
					cornerBlocked = true;
				}
			}else if(cy == 0 && ty == map.getHeight() - 1){
				if(blocked(tx, map.getHeight() - 1)){
					cornerBlocked = true;
				}
			}else{//General Cases
				if(cy < ty){
					if(blocked(tx,ty - 1 )){
						cornerBlocked = true;
					}
				}else{
					if(blocked(tx,ty + 1)){
						cornerBlocked = true;
					}
				}
			}
		}
		return cornerBlocked;
	}
	public boolean[][] getBlocked() {
		return blocked;
	}

	public void setBlocked(boolean[][] blocked) {
		this.blocked = blocked;
	}
	public void setTile(Point2D loc, boolean val){
		Point p = worldToTile(loc);
		blocked[p.y][p.x] = val;
		System.out.println(blocked[p.y][p.x] + " " +  p.x + " " + p.y);
	}
	
}
