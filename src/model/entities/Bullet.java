package model.entities;

import model.World;

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
		// TODO Auto-generated method stub
		
	}

}
