package fr.alex.games.entity;

import com.badlogic.gdx.physics.box2d.Body;

public class Mortal implements UserData{	
	
	public Mortal(){
	}
	
	public void hit(Body body){
		
	}	

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public void playerContact(Body body, Player p) {
		p.setDead(true);
	}
}
