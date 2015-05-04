package model.entities;

import org.newdawn.slick.geom.Circle;

import math.Vector2D;
import model.World;
import model.managers.ConsoleLog;
import model.managers.EntityManager;
import model.managers.ExplosionManager;

public class Bullet extends MovingEntity{
	double aoe;
	int damage;
	Entity target;
	
	public Bullet(double initX, double initY, double r, double speed,
			World myWorld, double aoe, int damage, String entity_class, Entity target) {
		super(initX, initY, r, speed, myWorld);
		// TODO Auto-generated constructor stub
		this.aoe = aoe;
		this.entity_class = entity_class;
		this.damage = damage;
		this.target = target;
	}

	@Override
	public void update(int delta) {
		if(isCollided(target)){
			if(aoe > 0){// aoe or single target
				Circle splash = new Circle((float)loc.getX(), (float)loc.getY(), (float)aoe);
				ExplosionManager.getInstance().addExplosion(splash);
				for (Entity e : EntityManager.getInstance() ){
					if(e instanceof Agent && splash.contains((float)e.getX(), (float)e.getY())){
						e.hit(damage);
					}
				}
			}else{
				target.hit(damage);
			}
			ConsoleLog.getInstance().log("Hit Entity: " + target.getId() + "for " + damage + " damage");
			die();
		}else{
			Vector2D move_vect = getStraightToPoint(target.getX(), target.getY());
			move(move_vect, delta);
		}
	}

}
