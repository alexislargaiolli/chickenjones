package fr.alex.games.entity;

import com.badlogic.gdx.physics.box2d.Body;

public class Destroyable implements UserData{
	int life;
	
	public Destroyable(int lifeCount){
		this.life = lifeCount;
	}
	
	public void hit(Body body){
		life--;
	}	

	@Override
	public boolean isDead() {
		return life == 0;
	}

	@Override
	public void playerContact(Body body, Player p) {
		
	}
}
