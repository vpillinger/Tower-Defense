package model.managers;

import java.util.ArrayList;
import java.util.Iterator;

import math.Point2D;
import model.entities.Bullet;
import model.entities.Entity;
import model.entities.NavigatingEntity;


public class EntityManager implements Iterable<Entity>{
	private static EntityManager em;
	private ArrayList<Entity> entities;
	private ArrayList<Entity> to_add;
	public boolean repath;
	
	public static EntityManager getInstance(){
		if(em == null){
			em = new EntityManager();
		}
		return em;
	}

	private EntityManager() {
		super();
		this.entities = new ArrayList<Entity>();
		this.to_add = new ArrayList<Entity>();
	}
	
	public void addEntity( Entity e){
		to_add.add(e);
	}
	public void addEntity(int pos, Entity e){
		if(pos < 0){
			entities.add(e);
		}else{
			entities.add(pos, e);
		}
	}
	
	public void updateEntities(int delta){
		//first add new entities
		entities.addAll(to_add);
		to_add.clear();
		//now iterate through entities
		Iterator<Entity> i = EntityManager.getInstance().iterator();
		while(i.hasNext()){
			Entity e = i.next();
			if (repath && !e.toDie && e instanceof NavigatingEntity){
				((NavigatingEntity) e).pathing.generatePath(new Point2D(50,50));
			}
			if(e.toDie){
				i.remove();
			}else{
				e.update(delta);
			}
		}
		repath = false;
	}
	
	@Override
	public Iterator<Entity> iterator() {
		return entities.iterator();
	}

}
