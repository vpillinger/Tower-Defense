package model.pathfinding;

import org.newdawn.slick.util.pathfinding.AStarHeuristic;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class ClosestWrappingHeuristic implements AStarHeuristic{

	@Override
	public float getCost(TileBasedMap map, Mover mov, int cx, int cy,
			int tx, int ty) {
		int mapx = map.getWidthInTiles();
		int dx = Math.min( Math.abs(tx - cx), mapx - Math.abs(tx-cx));
		
		int mapy = map.getHeightInTiles();
		int dy = Math.min( Math.abs(ty - cy), mapy - Math.abs(ty-cy));
		
		return dx*dx + dy*dy;
	}

}
