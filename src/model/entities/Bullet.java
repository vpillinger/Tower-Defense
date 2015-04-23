package model.entities;

import math.Vector2D;
import model.World;
import model.managers.ConsoleLog;

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
			target.hit(damage);
			if(aoe > 0){
				// TODO AOE
			}
			ConsoleLog.getInstance().log("Hit Entity: " + target.getId() + "for " + damage + " damage");
			die();
		}else{
			Vector2D move_vect = getStraightToPoint(target.getX(), target.getY());
			move(move_vect, delta);
		}
	}

}
