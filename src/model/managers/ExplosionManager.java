package model.managers;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.geom.Circle;

import model.entities.Entity;

public class ExplosionManager implements Iterable<Circle>{
	private static ExplosionManager em;
	private ArrayList<Circle> explosions;
	public boolean repath;
	
	public static ExplosionManager getInstance(){
		if(em == null){
			em = new ExplosionManager();
		}
		return em;
	}

	private ExplosionManager() {
		super();
		this.explosions = new ArrayList<Circle>();
	}
	
	public void addExplosion(Circle c){
		explosions.add(c);
	}

	@Override
	public Iterator<Circle> iterator() {
		return explosions.iterator();
	}
}
